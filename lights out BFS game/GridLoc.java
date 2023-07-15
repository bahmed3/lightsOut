
/**
 * A Grid location in the Lights Out world.
 * A combination of row and column.
 */
public class GridLoc {

	public static final int MAXIND = 16;
	
	// NB: Only final immutable/primitive field can be public.
	public final int row, col;
	
	private GridLoc(int r, int c) {
		row = r;
		col = c;
	}
	
	private static GridLoc[][] locations;
	
	static {
		locations = new GridLoc[MAXIND][MAXIND];
		for (int r = 0; r < MAXIND; ++r) {
			for (int c = 0; c < MAXIND; ++c) {
				locations[r][c] = new GridLoc(r,c);
			}
		}
	}
	
	/**
	 * Get the grid location specified by the given row and column (0 based).
	 * @param r 0-based row, must be non-negative and <= MAXIND
	 * @param c 0-based col, must be non-negative and <= MAXIND
	 * @return Grid location for the given row and column
	 * @throws IndexOutOfBoundsException if r or c is negative or too big.
	 */
	public static GridLoc get(int r, int c) {
		return locations[r][c];
	}
	
	@Override
	public String toString() {
		return "("+row+","+col+")";
	}
	
	@Override
	public int hashCode() {
		return row * MAXIND + col;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GridLoc)) return false;
		GridLoc l = (GridLoc)obj;
		return row == l.row && col == l.col;
	}

	/**
	 * Convert a (r,c) representation into a grid location
	 * @param s string to convert, must be a valid grid location string
	 * @return grid location (never null) for string
	 * @throws NumberFormatException if not a valid grid location string
	 */
	public static GridLoc fromString(String s) throws NumberFormatException {
		int c = s.indexOf(',');
		if (!s.startsWith("(") || !s.endsWith(")")) {
			throw new NumberFormatException("missing parens");
		}
		if (c < 0) throw new NumberFormatException("No comma found");
		return get(Integer.parseInt(s.substring(1,c)),
				Integer.parseInt(s.substring(c+1,s.length()-1)));
	}
}
