/*
 * PlaceMarkingEdit.java
 */

package pipe.gui.undo;

import java.util.LinkedList;

import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.Place;
import pipe.gui.CreateGui;

/**
 * 
 * @author corveau
 */
public class PlaceMarkingEdit extends UndoableEdit {

	Place place;
	LinkedList<Marking> newMarking;
	LinkedList<Marking> oldMarking;

	public PlaceMarkingEdit(Place _place, LinkedList<Marking> _oldMarking,
			LinkedList<Marking> _newMarking) {
		place = _place;
		oldMarking = _oldMarking;
		newMarking = _newMarking;
	}

	public void undo() {
		// Restore references to tokenClasses so that updates are reflected
		// in marking.
		for (Marking m : oldMarking) {
			m.setTokenClass(CreateGui.getModel().getTokenClassFromID(
					m.getTokenClass().getID()));
		}
		place.setCurrentMarking(oldMarking);
	}

	public void redo() {
		// Restore references to tokenClasses so that updates are reflected
		// in marking.
		for (Marking m : newMarking) {
			m.setTokenClass(CreateGui.getModel().getTokenClassFromID(
					m.getTokenClass().getID()));
		}
		place.setCurrentMarking(newMarking);
	}

}
