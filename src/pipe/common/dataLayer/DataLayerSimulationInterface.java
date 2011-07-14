package pipe.common.dataLayer;

import java.util.LinkedList;

public interface DataLayerSimulationInterface {

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#hasPlaceTransitionObjects()
	 */
	public abstract boolean hasPlaceTransitionObjects();

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#storeState()
	 */
	public abstract void storeState();

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#restoreState()
	 */
	public abstract void restoreState();

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#fireTransition(pipe.common.dataLayer.Transition)
	 */
	public abstract void fireTransition(Transition transition);

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getRandomTransition()
	 */
	public abstract Transition getRandomTransition();

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlace(int)
	 */
	public abstract Place getPlace(int placeNo);

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getInitialMarkingVector()
	 */
	public abstract LinkedList<Marking>[] getInitialMarkingVector();

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getCurrentMarkingVector()
	 */
	public abstract LinkedList<Marking>[] getCurrentMarkingVector();

}