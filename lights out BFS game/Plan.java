
import java.util.NoSuchElementException;

/**
 * A sequence of button presses (noted by grid locations) in the LightsOut game.
 * This class is immutable.
 */
public class Plan {
	private final Plan previous;
	private final GridLoc light;

	private Plan() {
		previous = null;
		light = null;
	}

	/**
	 * The empty plan: nothing is pressed. The only plan with a null previous or
	 * light.
	 */
	public static final Plan EMPTY_PLAN = new Plan();

	/**
	 * Create a plan given an existing plan followed by a second press.
	 * 
	 * @param previous plan being extended, must not be null
	 * @param light    location of light next pressed, must not be null.
	 */
	public Plan(Plan previous, GridLoc light) {
		if (previous == null || light == null)
			throw new IllegalArgumentException("Invalid plan");
		this.previous = previous;
		this.light = light;
	}

	/**
	 * Given a state of the system, return the new state after executing the plan.
	 * 
	 * @param lo    lights out description, must not be null
	 * @param state current state
	 * @return new state.
	 */
	public int apply(LightsOut lo, int state) {
		if (this == EMPTY_PLAN)
			return state;
		int prevState = previous.apply(lo, state);
		return lo.next(prevState, light.row, light.col);
	}

	/**
	 * Return the plan that starts with this plan and adds the given button press.
	 * 
	 * @param loc location pressed, must not be null
	 * @return new plan (never null).
	 */
	public Plan add(GridLoc loc) {
		return new Plan(this, loc);
	}

	/**
	 * Return the location of the first light pressed.
	 * 
	 * @return the first grid location of the plan (never null).
	 * @throws NoSuchElementException if the plan is empty.
	 */
	public GridLoc first() {
		if (this == EMPTY_PLAN)
			throw new NoSuchElementException("Empty plan");
		return previous == EMPTY_PLAN ? light : previous.first();
	}

	/**
	 * Compute the length of this plan. This is the number of lights pressed.
	 * 
	 * @return length of this plan.
	 */
	public int length() {
		return previous == null ? 0 : 1 + previous.length();
	}

	//override equals and hashCode

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Plan other = (Plan) obj;
		return (this.previous == null ? other.previous == null : this.previous.equals(other.previous))
				&& (this.light == null ? other.light == null : this.light.equals(other.light));
	}

	@Override
	public int hashCode() {
		int result = (previous != null ? previous.hashCode() * 5 : 0);
		result += (light != null ? 1 + light.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		if (this == EMPTY_PLAN)
			return "[]";
		if (previous == EMPTY_PLAN)
			return "[" + light + "]";
		String ps = previous.toString();
		return ps.substring(0, ps.length() - 1) + "," + light + "]";
	}

	/**
	 * Create a plan from an array of button presses.
	 * 
	 * @param a array of locations of buttons to press, must not be null
	 * @return Plan (never null) that presses the buttons in order.
	 */
	public static Plan create(GridLoc... a) {
		Plan p = EMPTY_PLAN;
		for (GridLoc loc : a) {
			p = p.add(loc);
		}
		return p;
	}
}
