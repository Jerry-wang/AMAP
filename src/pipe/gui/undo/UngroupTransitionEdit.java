package pipe.gui.undo;

import java.util.ArrayList;

import pipe.common.dataLayer.GroupTransition;
import pipe.common.dataLayer.Transition;

/**
 * 
 * @author Alex Charalambous
 */
public class UngroupTransitionEdit extends UndoableEdit {

	GroupTransition groupTransition;

	public UngroupTransitionEdit(GroupTransition _groupTransition) {
		groupTransition = _groupTransition;
	}

	/** */
	public void undo() {
		Transition foldedInto = groupTransition.getFoldedInto();
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		for(Transition t:groupTransition.getTransitions()){
			transitions.add(t);
		}
		groupTransition.getTransitions().clear();
		// Make the transition "foldedInto" group the transitions 
		// "transitions" into the group transition: "groupTransition"
		foldedInto.groupTransitionsHelper(
				transitions, groupTransition);
	}

	/** */
	public void redo() {
		groupTransition.ungroupTransitionsHelper();
	}
}
