
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LightsOut {

	private final int rows, columns;
	private final int bits, max;
	private final GridLoc[] lights;
	
	/**
	 * Create a "lights Out" game.
	 * The total number of spots (lights) on the board must
	 * not be more than 30 (to keep the number of possibilities down).
	 * @param r number of rows, must be positive.
	 * @param c number of columns, must be positive.
	 */
	public LightsOut(int r, int c) {
		if (r <= 0 || c <= 0) throw new IllegalArgumentException("Rows and columns must ne positive");
		rows = r;
		columns = c;
		bits = rows * columns;
		if (bits < 0 || bits > 30) throw new IllegalArgumentException("Can't handle more than 30 bits");
		max = (1 << bits);
		lights = new GridLoc[bits];
		for (int i =0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				lights[i*columns+j] = GridLoc.get(i, j);
			}
		}
	}
	
	public int rows() {
		return rows;
	}
	
	public int columns() {
		return columns;
	}
	
	/**
	 * Return the lights for this version of the game.
	 * The returned list is unmodifiable.
	 * @return list of lights.
	 */
	public List<GridLoc> getLights() {
		return Collections.unmodifiableList(Arrays.asList(lights));
	}
	
	private int toBit(int i, int j) {
		if (i < 0 || i >= rows) return 0;
		if (j < 0 || j >= columns) return 0;
		int k = i*columns + j;
		return 1 << k;
	}
	
	private int toBitChecked(int i, int j) {
		int result = toBit(i,j);
		if (result == 0) {
			throw new IndexOutOfBoundsException("(" + i + "," + j + ") not in range (" + rows + "," + columns + ")");
		}
		return result;
	}
	
	/**
	 * Return whether the light is on at a given point.
	 * @param state integer representing the state of the lights.
	 * @param i row (0-based), must not be negative or more than or equal to rows
	 * @param j column (0-based), must not be negative or >= columns
	 * @return whether the light at this point is on
	 */
	public boolean isOn(int state, int i, int j) {
		int mask = toBitChecked(i,j);
		return ((state & mask) != 0);
	}
	
	/**
	 * Return whether the light is on at a given location.
	 * @param state integer representing the state of the lights.
	 * @param loc location of light to check, must not be null
	 * @return whether the light at this point is on
	 */
	public boolean isOn(int state, GridLoc loc) {
		return isOn(state,loc.row,loc.col);
	}
	
	/**
	 * Change whether the light is on at a given point.
	 * @param state integer representing the state of the lights.
	 * @param i row (0-based), must not be negative or more than or equal to rows
	 * @param j column (0-based), must not be negative or >= columns
	 * @return new state with light switched.
	 */
	public int toggle(int state, int i, int j) {
		int mask = toBitChecked(i,j);
		return (state ^ mask);
	}
	
	/**
	 * Change whether the light is on at a given point.
	 * @param state integer representing the state of the lights.
	 * @param loc location of light to toggle, must not be null
	 * @return new state with light switched.
	 */
	public int toggle(int state, GridLoc loc) {
		int mask = toBitChecked(loc.row,loc.col);
		return (state ^ mask);
	}
	
	
	private int getMask(int i, int j) {
		int result = toBit(i,j) | toBit(i-1,j) | toBit(i+1,j) | toBit(i,j-1) | toBit(i,j+1);
		return result;
	}

	/**
	 * Get the next state of the system after pressing the button at (i,j).
	 * @param from state of the system before pressing button
	 * @param i row (0-based), must not be negative or more than or equal to rows
	 * @param j column (0-based), must not be negative or >= columns
	 * @return new state after button is pushed.
	 */
	public int next(int from, int i, int j) {
		// for side-effect:
		toBitChecked(i,j);
		return from ^ getMask(i,j);
	}

	/**
	 * Get the next state of the system after pressing the button at (i,j).
	 * @param from state of the system before pressing button
	 * @param loc location of button to press, must not be null
	 * @return new state after button is pushed.
	 */
	public int next(int from, GridLoc loc) {
		// for side-effect:
		toBitChecked(loc.row,loc.col);
		return from ^ getMask(loc.row,loc.col);
	}

	/**
	 * Return state of system with all lights off.
	 * @return integer representing situation where all lights are off.
	 */
	public int allOff() {
		return 0;
	}
	
	/**
	 * Return the integer representing the state that all lights are on.
	 * @return
	 */
	public int allOn() {
		return max - 1;
	}
	
	/**
	 * Convert the state of the system into ASCII graphics: using X for lights
	 * that are on, and O for those off. 
	 * @param lo state of the system
	 * @return non-null string giving the current state.
	 */
	public String toString(int lo) {
		StringBuilder sb = new StringBuilder();
		int b = 1;
		for (int i=0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				if ((b & lo) == 0) sb.append("O");
				else sb.append("X");
				b <<= 1;
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Convert an ASCII representation using X and O into a state representation.
	 * @param s string to convert, must not be null, or have
	 * characters other than X, O or newline, return or space.
	 * @return integer representation of state
	 */
	public int fromString(String s) {
		int state = 0;
		int k = 0;
		for (int i=0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				boolean done = false;
				do {
					done = true;
					switch (s.charAt(k)) {
					case ' ':
					case '\n': 
					case '\r': done = false; break;
					case 'O': case '0': break;
					case 'X': case '1':
						state |= toBit(i,j);
						break;
					default:
						throw new IllegalArgumentException("Light status ? " + s.charAt(k));
					}
					++k;
				} while (!done);
			}
		}
		return state;
	}

	/**
	 * Return the size of the state space (2 ^ rows*columns).
	 * This can be up to 2^30 (for the maximum size).
	 * @return number of possible states of lights.
	 */
	public int getSize() {
		return max;
	}
}
