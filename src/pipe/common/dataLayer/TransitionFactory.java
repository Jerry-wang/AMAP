package pipe.common.dataLayer;

public class TransitionFactory {

	public static Transition createTransition(double positionXInput,
			double positionYInput, String idInput, String nameInput,
			double nameOffsetXInput, double nameOffsetYInput, double rateInput,
			boolean timedTransition, boolean infServer, int angleInput,
			int priority) {
		return new Transition(positionXInput, positionYInput, idInput,
				nameInput, nameOffsetXInput, nameOffsetYInput, rateInput,
				timedTransition, infServer, angleInput, priority);
	}

	public static Transition createTransition(double positionXInput,
			double positionYInput) {
		return new Transition(positionXInput, positionYInput);
	}

}
