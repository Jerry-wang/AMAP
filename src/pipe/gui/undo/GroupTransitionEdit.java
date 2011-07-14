package pipe.gui.undo;

import java.util.ArrayList;

import pipe.common.dataLayer.GroupTransition;
import pipe.common.dataLayer.Transition;

/**
 * 
 * @author Alex Charalambous
 */
public class GroupTransitionEdit extends UndoableEdit {

	GroupTransition groupTransition;

	public GroupTransitionEdit(GroupTransition _groupTransition) {
		groupTransition = _groupTransition;
	}

	/** */
	public void undo() {
		groupTransition.ungroupTransitionsHelper();
	}

	public void redo() {
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
}
