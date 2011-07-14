/*
 * transitionPriorityEdit.java
 */
package pipe.gui.undo;

import pipe.common.dataLayer.GroupTransition;


/**
 *
 * @author Alex Charalambous
 */
public class GroupTransitionRotationEdit 
        extends UndoableEdit {
   
   GroupTransition groupTransition;
   Integer angle;
   
   
   /** Creates a new instance of placePriorityEdit */
   public GroupTransitionRotationEdit(GroupTransition _groupTransition, Integer _angle) {
      groupTransition = _groupTransition;
      angle = _angle;
   }

   
   /** */
   public void undo() {
      groupTransition.rotate(-angle);
   }

   
   /** */
   public void redo() {
      groupTransition.rotate(angle);
   }
   
}
