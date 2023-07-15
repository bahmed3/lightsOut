
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Queue;

public class BreadthFirstLightsOut {
	private LightsOut lo;
	private int initialState;
	private boolean onlyLit;
	private BitSet enqueued;

	private BreadthFirstLightsOut(LightsOut lo, int initial, boolean onlyLit) {
		this.lo = lo;
		this.initialState = initial;
		this.onlyLit = onlyLit;
		this.enqueued = new BitSet(lo.getSize());
	}

	private Plan solve() {
		Queue<Plan> queue = new ArrayDeque<>();
		Plan initialPlan = Plan.EMPTY_PLAN;

		queue.add(initialPlan);
		enqueued.set(initialState);

		while (!queue.isEmpty()) {
			Plan currentPlan = queue.poll();
			int currentState = currentPlan.apply(lo, initialState);

			if (currentState == lo.allOff()) {
				return currentPlan;
			}

			for (GridLoc button : lo.getLights()) {
				if (!onlyLit || lo.isOn(currentState, button)) {
					int nextState = lo.next(currentState, button);
					if (!enqueued.get(nextState)) {
						enqueued.set(nextState);
						queue.add(currentPlan.add(button));
					}
				}
			}
		}

		return null;
	}

	/**
	 * Find a solution (sequences of lights to press so that the state is all off)
	 * using depth first search starting from the given state.
	 * 
	 * @param lo      lights out instance
	 * @param from    state to start from
	 * @param onlyLit if a button can only be pressed if it is lit.
	 * @return plan, or null if no such sequence exists.
	 */
	public static Plan findSolution(LightsOut lo, int from, boolean onlyLit) {
		BreadthFirstLightsOut bfs = new BreadthFirstLightsOut(lo, from, onlyLit);
		return bfs.solve();
	}
}
