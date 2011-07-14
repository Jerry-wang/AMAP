package pipe.common.dataLayer;

import java.util.LinkedList;

public class ArcFactory {

	public static NormalArc createNormalArc(double startPositionXInput,
			double startPositionYInput, double endPositionXInput,
			double endPositionYInput, PlaceTransitionObject sourceInput,
			PlaceTransitionObject targetInput, LinkedList<Marking> weightInput,
			String idInput, boolean taggedInput) {
		return new NormalArc(startPositionXInput, startPositionYInput,
				endPositionXInput, endPositionYInput, sourceInput, targetInput,
				weightInput, idInput, taggedInput);
	}

	public static NormalArc createNormalArc(NormalArc arc) {
		return new NormalArc(arc);
	}

	public static NormalArc createNormalArc(PlaceTransitionObject newSource) {
		return new NormalArc(newSource);
	}

	public static InhibitorArc createInhibitorArc(double startPositionXInput,
			double startPositionYInput, double endPositionXInput,
			double endPositionYInput, PlaceTransitionObject sourceInput,
			PlaceTransitionObject targetInput, LinkedList<Marking> weightInput,
			String idInput) {
		return new InhibitorArc(startPositionXInput, startPositionYInput,
				endPositionXInput, endPositionYInput, sourceInput, targetInput,
				weightInput, idInput);
	}

	public static InhibitorArc createInhibitorArc(InhibitorArc arc) {
		return new InhibitorArc(arc);
	}

	public static InhibitorArc createInhibitorArc(
			PlaceTransitionObject newSource) {
		return new InhibitorArc(newSource);
	}

}
