/*
 * AddPetriNetObjectEdit.java
 */

package pipe.gui.undo;

import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.PetriNetObject;

import pipe.gui.GuiView;

/**
 *
 * @author corveau
 */
public class AddPetriNetObjectEdit 
        extends UndoableEdit {
   
   PetriNetObject pnObject;
   DataLayerInterface model;
   GuiView view;
   
   
   /** Creates a new instance of placeWeightEdit */
   public AddPetriNetObjectEdit(PetriNetObject _pnObject, 
                                GuiView _view, DataLayerInterface _model) {
      pnObject = _pnObject;
      view = _view;
      model = _model;
   }

   
   /** */
   public void undo() {
      pnObject.delete();
   }

   
   /** */
   public void redo() {
      pnObject.undelete(model, view);
   }
   
   
   public String toString(){
      return super.toString() + " \"" + pnObject.getName() + "\"";
   }
   
}
