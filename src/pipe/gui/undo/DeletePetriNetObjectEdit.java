/*
 * DeletePetriNetObjectEdit.java
 */
package pipe.gui.undo;

import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.Parameter;
import pipe.common.dataLayer.PetriNetObject;
import pipe.common.dataLayer.RateParameter;
import pipe.common.dataLayer.Transition;
import pipe.gui.GuiView;


/**
 *
 * @author Pere Bonet
 */
public class DeletePetriNetObjectEdit 
        extends UndoableEdit {
   
   PetriNetObject pnObject;
   DataLayerInterface model;
   GuiView view;
   Object[] objects;
   Parameter param;
   
   
   /** Creates a new instance of placeWeightEdit */
   public DeletePetriNetObjectEdit(PetriNetObject _pnObject,
            GuiView _view, DataLayerInterface _model) {
      pnObject = _pnObject;
      view = _view;
      model = _model;

      if (pnObject instanceof RateParameter) {
         objects = ((RateParameter)pnObject).getTransitions();
      } else if (pnObject instanceof Transition) {
         RateParameter rParam = ((Transition)pnObject).getRateParameter();
         if (rParam != null) {
            param = rParam;
         }
      }
      pnObject.markAsDeleted();      
   }

     
   /** */
   public void redo() {
      pnObject.delete();
   }

   
   /** */
   public void undo() {
      pnObject.undelete(model,view);
   }
   
   
   public String toString(){
      return super.toString() + " " + pnObject.getClass().getSimpleName() 
             + " [" +  pnObject.getId() + "]";
   }   
   
}
