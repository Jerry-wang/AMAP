package pipe.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.Transition;

public class SimpleTransitions implements Serializable
{
	/**
	 * Simple Transitions is a simplified version of the
	 * pipe.dataLayer.Transition class Its purpose is to provide a simple
	 * serializable object for socket transmission to processing clusters. The
	 * necessary attributes from Places for building a '.mod' file are the place
	 * IDs with the respective IDs of all their To/From transition targets
	 */

	private static final long	serialVersionUID	= 1L;

	public String[]				ids, names;
	public boolean[]			timed;
	public double[]				rate;
	public ArrayList<LinkedList<SimpleArc>>	arcsTo, arcsFrom;
	public int								length;

	public SimpleTransitions(final DataLayerInterface pnmldata) {
		int i;
		Iterator arcsToIter;
		Iterator arcsFromIter;

		Transition[] transitions;
		transitions = pnmldata.getTransitions();

		// Declare the objects variables
		this.length = transitions.length;

		this.ids = new String[this.length];
		this.names = new String[this.length];
		this.timed = new boolean[this.length];
		this.rate = new double[this.length];
		this.arcsTo = new ArrayList<LinkedList<SimpleArc>>(this.length);
		this.arcsFrom = new ArrayList<LinkedList<SimpleArc>>(this.length);

		for (i = 0; i < this.length; i++)
		{
			this.ids[i] = transitions[i].getId();
			this.names[i] = transitions[i].getName();
			this.timed[i] = transitions[i].isTimed();
			this.rate[i] = transitions[i].getRate();

			this.arcsTo.add(i, new LinkedList<SimpleArc>());
			this.arcsFrom.add(i, new LinkedList<SimpleArc>());

			arcsToIter = transitions[i].getConnectToIterator();
			arcsFromIter = transitions[i].getConnectFromIterator();

			// Create list of all targets from current place
			while (arcsToIter.hasNext())
			{
				final Arc currentArc = (Arc) arcsToIter.next();
				
				// TODO: similar problem as SimplePlaces - constructor expects an int but we are passing an linked list.
				// Passing an unfolded net, so need a simple method that returns an int for each transition.
				// currentArc.getWeight().getFirst().getCurrentMarking() returns an int corresponding to the weight of the first arc class
				// in the simple arc. since the net is unfolded there should only be one class in the linked list, hence getFirst() is sufficient.
				final SimpleArc newTransArc = new SimpleArc(currentArc.getSource().getId(),currentArc.getWeight().getFirst().getCurrentMarking());
				this.arcsTo.get(i).add(newTransArc);
			}

			// Create list of source places to current
			while (arcsFromIter.hasNext())
			{
				final Arc currentArc = (Arc) arcsFromIter.next();
				
				// TODO: Same as above
				final SimpleArc newTransArc = new SimpleArc(currentArc.getTarget().getId(),currentArc.getWeight().getFirst().getCurrentMarking());
				this.arcsFrom.get(i).add(newTransArc);
			}
		}
	}

}