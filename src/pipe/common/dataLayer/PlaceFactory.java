package pipe.common.dataLayer;

import java.util.LinkedList;


public class PlaceFactory {

	public static Place createPlace(double positionXInput,
			double positionYInput, String idInput, String nameInput,
			double nameOffsetXInput, double nameOffsetYInput,
			LinkedList<Marking> initialMarkingInput,
			double markingOffsetXInput, double markingOffsetYInput,
			int capacityInput, boolean tagged) {
		return new Place(positionXInput, positionYInput, idInput, nameInput,
				nameOffsetXInput, nameOffsetYInput, initialMarkingInput,
				markingOffsetXInput, markingOffsetYInput, capacityInput, tagged);
	}

	public static Place createPlace(double positionXInput,
			double positionYInput, String idInput, String nameInput,
			double nameOffsetXInput, double nameOffsetYInput,
			LinkedList<Marking> initialMarkingInput,
			double markingOffsetXInput, double markingOffsetYInput,
			int capacityInput) {
		return new Place(positionXInput, positionYInput, idInput, nameInput,
				nameOffsetXInput, nameOffsetYInput, initialMarkingInput,
				markingOffsetXInput, markingOffsetYInput, capacityInput);
	}

	public static Place createPlace(double positionXInput, double positionYInput) {
		return new Place(positionXInput, positionYInput);
	}

}
