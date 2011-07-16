package pipe.common.dataLayer;

public class TransitionFactory {

	public static Transition createTransition(double positionXInput,
			double positionYInput, String idInput, String nameInput,
			double nameOffsetXInput, double nameOffsetYInput, double rateInput,
			boolean timedTransition, boolean deterministicTransition, boolean infServer, int angleInput,
			int priority) {System.out.println("----");
		return new Transition(positionXInput, positionYInput, idInput,
				nameInput, nameOffsetXInput, nameOffsetYInput, rateInput,
				timedTransition, deterministicTransition, infServer, angleInput, priority);
	}

	public static Transition createTransition(double positionXInput,
			double positionYInput, boolean isNarrow) {
		return new Transition(positionXInput, positionYInput, isNarrow);
	}

}
