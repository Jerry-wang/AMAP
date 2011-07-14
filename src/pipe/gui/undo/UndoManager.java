/*
 * UndoManager.java
 */
package pipe.gui.undo;

import java.util.ArrayList;
import java.util.Iterator;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.ArcPathPoint;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.NormalArc;
import pipe.common.dataLayer.PetriNetObject;
import pipe.common.dataLayer.PlaceTransitionObject;
import pipe.gui.GuiFrame;
import pipe.gui.GuiView;
import pipe.gui.Constants;


/**
 * Class to handle undo & redo functionality
 * @author pere
 */
public class UndoManager {

   private static int UNDO_BUFFER_CAPACITY = Constants.DEFAULT_BUFFER_SIZE;
   
   private int freePosition  = 0; //index for new edits
   private int fillCount     = 0; //
   private int startOfBuffer = 0; // index of the eldest element
   private int undoneEdits   = 0;

   private ArrayList<ArrayList> edits = new ArrayList(UNDO_BUFFER_CAPACITY);
   
   private GuiView view;
   private DataLayerInterface model;
   private GuiFrame app;

   
   /**
    * Creates a new instance of UndoManager
    */
   public UndoManager(GuiView _view, DataLayerInterface _model, GuiFrame _app) {
      view = _view;
      model = _model;
      app = _app;
      app.setUndoActionEnabled(false);
      app.setRedoActionEnabled(false);
      for (int i=0; i < UNDO_BUFFER_CAPACITY; i++){
         edits.add(null);
      }
   }
  
   
   public void doRedo() {

      if (undoneEdits > 0) {
         checkArcBeingDrawn();
         checkMode();

         // The currentEdit to redo
         Iterator<UndoableEdit> currentEdit = edits.get(freePosition).iterator();         
         while (currentEdit.hasNext()){
            currentEdit.next().redo();
         }
         freePosition = (freePosition + 1) % UNDO_BUFFER_CAPACITY;
         fillCount++; 
         undoneEdits--;
         if (undoneEdits==0){
            app.setRedoActionEnabled(false);
         }
         app.setUndoActionEnabled(true);
      }
   }
   
   
   public void doUndo() {

      if (fillCount > 0) {
         checkArcBeingDrawn();
         checkMode();

         if (--freePosition < 0){
            freePosition += UNDO_BUFFER_CAPACITY;
         }
         fillCount--;
         undoneEdits++;
         
         // The currentEdit to undo (reverse order)
         ArrayList<UndoableEdit> currentEdit = edits.get(freePosition);
         for (int i = currentEdit.size()-1; i >= 0; i--) {
            currentEdit.get(i).undo();
         }

         if (fillCount==0){
            app.setUndoActionEnabled(false);
         }
         app.setRedoActionEnabled(true);
      }
   }   
   
  
   public void clear() {
      freePosition = 0;
      fillCount   = 0;
      startOfBuffer  = 0;
      undoneEdits    = 0;
      app.setUndoActionEnabled(false);
      app.setRedoActionEnabled(false);
   }   
   
   
   public void newEdit(){
      ArrayList lastEdit = edits.get(currentIndex());
      if ((lastEdit != null) && (lastEdit.isEmpty())){
         return;
      }              
      
      undoneEdits = 0;
      app.setUndoActionEnabled(true);
      app.setRedoActionEnabled(false);
      view.setNetChanged(true);
      
      ArrayList<UndoableEdit> compoundEdit = new ArrayList();
      edits.set(freePosition, compoundEdit);
      freePosition = (freePosition + 1) % UNDO_BUFFER_CAPACITY;
      if (fillCount < UNDO_BUFFER_CAPACITY){
         fillCount++;
      } else {
         startOfBuffer = (startOfBuffer + 1) % UNDO_BUFFER_CAPACITY;
      }       
   }
   
   
   public void addEdit(UndoableEdit undoableEdit){
      ArrayList<UndoableEdit> compoundEdit = edits.get(currentIndex());      
      compoundEdit.add(undoableEdit);
      //debug();
   }      
   
   
   public void addNewEdit(UndoableEdit undoableEdit) {
       newEdit(); // mark for a new "transtaction""
       addEdit(undoableEdit);
    }
   
   
   public void deleteSelection(PetriNetObject pnObject) {
      deleteObject(pnObject);
   }
   
   
   public void deleteSelection(ArrayList<PetriNetObject> selection) {
      for (PetriNetObject pnObject : selection) {
         deleteObject(pnObject);
      }
   }   

     
   public void translateSelection(ArrayList objects, int transX, int transY) {
      newEdit(); // new "transaction""
      Iterator<PetriNetObject> iterator = objects.iterator();
      while (iterator.hasNext()){
         addEdit(new TranslatePetriNetObjectEdit(
                 iterator.next(), transX, transY));
      }
   }

   
   private int currentIndex() {
      int lastAdd = freePosition - 1;
      if (lastAdd < 0){
         lastAdd += UNDO_BUFFER_CAPACITY;
      }
      return lastAdd;
   }

   
   // removes the arc currently being drawn if any
   private void checkArcBeingDrawn(){
      Arc arcBeingDrawn= view.createArc;
      if (arcBeingDrawn != null){
         if (arcBeingDrawn.getParent() != null) {
            arcBeingDrawn.getParent().remove(arcBeingDrawn);
         }
         view.createArc = null;
      }      
   }
   
   
   private void checkMode(){
      if ((app.getMode() == Constants.FAST_PLACE) ||
              (app.getMode() == Constants.FAST_TRANSITION)) {
         app.resetMode();
      }      
   }  
   
   
   private void deleteObject(PetriNetObject pnObject) {
      if (pnObject instanceof ArcPathPoint) {
         if (!((ArcPathPoint)pnObject).getArcPath().getArc().isSelected()){
            addEdit(new DeleteArcPathPointEdit(
                    ((ArcPathPoint)pnObject).getArcPath().getArc(), 
                    (ArcPathPoint)pnObject, ((ArcPathPoint)pnObject).getIndex()));
         } 
      } else {
         if (pnObject instanceof PlaceTransitionObject) {
            //
            Iterator arcsTo = 
                    ((PlaceTransitionObject)pnObject).getConnectToIterator();
            while (arcsTo.hasNext()) {
               Arc anArc = (Arc)arcsTo.next();
               if (!anArc.isDeleted()){  
                  addEdit(new DeletePetriNetObjectEdit(anArc, view, model));
               }
            }            
            //
            Iterator arcsFrom = 
                    ((PlaceTransitionObject)pnObject).getConnectFromIterator();
            while (arcsFrom.hasNext()) {
               Arc anArc = (Arc)arcsFrom.next();
               if (!anArc.isDeleted()){
                  addEdit(new DeletePetriNetObjectEdit(anArc, view, model));
               }
            }

         } else if (pnObject instanceof NormalArc) {
            if (((NormalArc)pnObject).hasInverse()) {
               if (((NormalArc)pnObject).hasInvisibleInverse()) {
                  addEdit(((NormalArc)pnObject).split());
                  NormalArc inverse = ((NormalArc)pnObject).getInverse();
                  addEdit(((NormalArc)pnObject).clearInverse());
                  addEdit(new DeletePetriNetObjectEdit(inverse, view, model));
                  inverse.delete();
               } else {
                  addEdit(((NormalArc)pnObject).clearInverse());
               }
            }            
         }

         if (!pnObject.isDeleted()){
            addEdit(new DeletePetriNetObjectEdit(pnObject, view, model));
            pnObject.delete();
         }
      }
   }
   
   
   private void debug(){
      int i = startOfBuffer;
      System.out.println("");
      for (int k = 0; k < fillCount; k++) {
         Iterator<UndoableEdit> currentEdit = edits.get(i).iterator();
         while (currentEdit.hasNext()) {
            System.out.println("["+ i + "]" + currentEdit.next().toString());
         }
         i = (i + 1 ) % UNDO_BUFFER_CAPACITY;
      }
   }
   
}
