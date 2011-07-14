/*
 * ArcWeightEdit.java
 */

package pipe.gui.undo;

import java.util.LinkedList;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.Marking;

/**
 *
 * @author Alex Charalambous
 */
public class ArcWeightEdit 
        extends UndoableEdit {
   
   Arc arc;
   LinkedList<Marking> newWeight;
   LinkedList<Marking> oldWeight;
   
   
   /** Creates a new instance of arcWeightEdit */
   public ArcWeightEdit(Arc _arc, LinkedList<Marking> _oldWeight, LinkedList<Marking> _newWeight) {
      arc = _arc;
      oldWeight = _oldWeight;      
      newWeight = _newWeight;
   }

   
   /** */
   public void undo() {
      arc.setWeight(oldWeight);
   }

   
   /** */
   public void redo() {
      arc.setWeight(newWeight);
   }
   
   
   public String toString(){
      return super.toString() + " " + arc.getName() + 
              "oldWeight: " + oldWeight + "newWeight: " + newWeight;
   }   
   
}
