package pipe.gui.handler;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.ArcFactory;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.InhibitorArc;
import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.NormalArc;
import pipe.common.dataLayer.ObjectDeepCopier;
import pipe.common.dataLayer.Place;
import pipe.common.dataLayer.PlaceTransitionObject;
import pipe.common.dataLayer.TokenClass;
import pipe.common.dataLayer.Transition;
import pipe.gui.CreateGui;
import pipe.gui.GuiFrame;
import pipe.gui.GuiView;
import pipe.gui.Constants;
import pipe.gui.undo.UndoManager;
import pipe.gui.undo.AddPetriNetObjectEdit;

/**
 * Class used to implement methods corresponding to mouse events on places.
 *
 * @author Pere Bonet - changed the mousePressed method to only allow the
 * creation of an arc by left-clicking
 * @author Matthew Worthington - modified the handler which was causing the
 * null pointer exceptions and incorrect petri nets xml representation.
 */
public class PlaceTransitionObjectHandler 
        extends PetriNetObjectHandler {
   
   ArcKeyboardEventHandler keyHandler = null;
   
   // constructor passing in all required objects
   public PlaceTransitionObjectHandler(Container contentpane,
           PlaceTransitionObject obj) {
      super(contentpane, obj);
      enablePopup = true;
   }
   
   
   private void createArc(Arc newArc, PlaceTransitionObject currentObject){
	   TokenClass tc = CreateGui.getModel().getActiveTokenClass();
	   Marking m = new Marking(tc, 1);
	   LinkedList<Marking> markings= new LinkedList<Marking>();
	   markings.add(m);
	   newArc.setWeight(markings);
      newArc.setZoom(CreateGui.getView().getZoom());
      contentPane.add(newArc);
      currentObject.addConnectFrom(newArc);
      CreateGui.getView().createArc = (Arc)newArc;
      // addPetriNetObject a handler for shift & esc actions drawing arc
      // this is removed when the arc is finished drawing:
      keyHandler = new ArcKeyboardEventHandler((Arc)newArc);
      newArc.addKeyListener(keyHandler);
      newArc.requestFocusInWindow();
      newArc.setSelectable(false);
   }
   
   
   public void mousePressed(MouseEvent e) {
      super.mousePressed(e);
      // Prevent creating arcs with a right-click or a middle-click
      if (e.getButton() != MouseEvent.BUTTON1) {
         return;
      }
      
      PlaceTransitionObject currentObject = (PlaceTransitionObject)myObject;
      switch (CreateGui.getApp().getMode()) {
         case Constants.ARC:
            if (e.isControlDown()) {
               // user is holding Ctrl key; switch to fast mode
               if (this.myObject instanceof Place) {
                  CreateGui.getApp().enterFastMode(Constants.FAST_TRANSITION);
               } else if (this.myObject instanceof Transition) {
                  CreateGui.getApp().enterFastMode(Constants.FAST_PLACE);
               }
            }
         case Constants.INHIBARC:
         case Constants.FAST_PLACE:
         case Constants.FAST_TRANSITION:
            if (CreateGui.getView().createArc == null) {
               if (CreateGui.getApp().getMode() == Constants.INHIBARC){
                  if (currentObject instanceof Place) {
                     createArc(ArcFactory.createInhibitorArc(currentObject), currentObject);
                  }
               } else {
                  createArc(ArcFactory.createNormalArc(currentObject), currentObject);
               }
            }
            break;
            
         default:
            break;
      }
   }
   
   
   public void mouseReleased(MouseEvent e) {
      boolean isNewArc = true; // true if we have to add a new arc to the Petri Net
      boolean fastMode = false;
      
      GuiView view = CreateGui.getView();
      DataLayerInterface model = CreateGui.getModel();
      UndoManager undoManager = view.getUndoManager();
      GuiFrame app = CreateGui.getApp();
      
      super.mouseReleased(e);
      
      PlaceTransitionObject currentObject = (PlaceTransitionObject)myObject;
      
      switch (app.getMode()) {
         case Constants.INHIBARC:
            InhibitorArc createInhibitorArc = (InhibitorArc) view.createArc;
            if (createInhibitorArc != null) {
               if (!currentObject.getClass().equals(
                       createInhibitorArc.getSource().getClass())) {
                  
                  Iterator arcsFrom =
                          createInhibitorArc.getSource().getConnectFromIterator();
                  // search for pre-existent arcs from createInhibitorArc's 
                  // source to createInhibitorArc's target
                  while(arcsFrom.hasNext()) {
                     Arc someArc = ((Arc)arcsFrom.next());
                     if (someArc == createInhibitorArc) {
                        break;
                     } else if (someArc.getTarget() == currentObject &&
                             someArc.getSource() == createInhibitorArc.getSource()) {
                        isNewArc = false;
                        if (someArc instanceof NormalArc){
                           // user has drawn an inhibitor arc where there is 
                           // a normal arc already - nothing to do
                        } else if (someArc instanceof InhibitorArc) {
                           // user has drawn an inhibitor arc where there is 
                           // an inhibitor arc already - we increment arc's 
                           // weight
                           LinkedList<Marking> weight = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(someArc.getWeight());
                           for(Marking m:weight){
                        	   m.setCurrentMarking(m.getCurrentMarking()+1);
                           }
                           undoManager.addNewEdit(someArc.setWeight(someArc.getWeight()));
                        } else {
                           // This is not supposed to happen
                        }
                        createInhibitorArc.delete();
                        someArc.getTransition().removeArcCompareObject(
                                createInhibitorArc);
                        someArc.getTransition().updateConnected();
                        break;
                     }
                  }
                  
                  if (isNewArc == true) {
                     createInhibitorArc.setSelectable(true);
                     createInhibitorArc.setTarget(currentObject);
                     currentObject.addConnectTo(createInhibitorArc);
                     // Evil hack to prevent the arc being added to GuiView twice
                     contentPane.remove(createInhibitorArc);
                     model.addArc(createInhibitorArc);
                     view.addNewPetriNetObject(createInhibitorArc);
                     undoManager.addNewEdit(
                             new AddPetriNetObjectEdit(createInhibitorArc,
                             view, model));
                  }
                  
                  // arc is drawn, remove handler:
                  createInhibitorArc.removeKeyListener(keyHandler);
                  keyHandler = null;
                  view.createArc = null;
               }
            }
            break;
            
         case Constants.FAST_TRANSITION:
         case Constants.FAST_PLACE:
            fastMode = true;
         case Constants.ARC:
            Arc createArc = (NormalArc) view.createArc;
            if (createArc != null) {
               if (currentObject != createArc.getSource()) {
                  createArc.setSelectable(true);
                  Iterator arcsFrom = createArc.getSource().getConnectFromIterator();
                  // search for pre-existent arcs from createArc's source to 
                  // createArc's target                  
                  while(arcsFrom.hasNext()) {
                     Arc someArc = ((Arc)arcsFrom.next());
                     if (someArc == createArc) {
                        break;
                     } else if (someArc.getSource() == createArc.getSource() &&
                             someArc.getTarget() == currentObject) {
                        isNewArc = false;
                        if (someArc instanceof NormalArc) {
                           // user has drawn a normal arc where there is 
                           // a normal arc already - we increment arc's weight
                        	
                            LinkedList<Marking> weight = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(someArc.getWeight());
                            for(Marking m:weight){
                         	   m.setCurrentMarking(m.getCurrentMarking()+1);
                            }
                            undoManager.addNewEdit(someArc.setWeight(weight));
                        } else{
                           // user has drawn a normal arc where there is 
                           // an inhibitor arc already - nothing to do
                           //System.out.println("DEBUG: arc normal i arc inhibidor!");
                        }
                        createArc.delete();
                        someArc.getTransition().removeArcCompareObject(createArc);
                        someArc.getTransition().updateConnected();
                        break; 
                     }
                  }
                  
                  NormalArc inverse = null;
                  if (isNewArc == true) {
                     createArc.setTarget(currentObject);
                     
                     //check if there is an inverse arc
                     Iterator arcsFromTarget =
                             createArc.getTarget().getConnectFromIterator();
                     while (arcsFromTarget.hasNext()) {
                        Arc anArc = (Arc)arcsFromTarget.next();
                        if (anArc.getTarget() == createArc.getSource()) {
                           if (anArc instanceof NormalArc) {
                              inverse = (NormalArc)anArc;
                              // inverse arc found
                              if (inverse.hasInverse()){
                                 // if inverse arc has an inverse arc, it means
                                 // that createArc is equal to inverse's inverse
                                 // arc so we only have to increment its weight
                                 isNewArc = false;
                                 
                                 LinkedList<Marking> weightInverse = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(inverse.getInverse().getWeight());
                                 for(Marking m:weightInverse){
                              	   m.setCurrentMarking(m.getCurrentMarking()+1);
                                 }
                                 undoManager.addNewEdit( inverse.getInverse().setWeight(weightInverse));
                                 
                                 createArc.delete();
                                 inverse.getTransition().removeArcCompareObject(
                                         createArc);
                                 inverse.getTransition().updateConnected();
                              }
                              break;
                           }
                        }
                     }
                  }
                  
                  if (isNewArc == true) {
                     currentObject.addConnectTo(createArc);
                     
                     // Evil hack to prevent the arc being added to GuiView twice
                     contentPane.remove(createArc);
                     
                     model.addArc((NormalArc)createArc);
                     view.addNewPetriNetObject(createArc);
                     if (!fastMode) {
                        // we are not in fast mode so we have to set a new edit
                        // in undoManager for adding the new arc
                        undoManager.newEdit(); // new "transaction""
                     }
                     undoManager.addEdit(
                             new AddPetriNetObjectEdit(createArc, view, model));
                     if (inverse != null) {
                        undoManager.addEdit(
                                inverse.setInverse((NormalArc)createArc,
                                Constants.JOIN_ARCS));
                     }
                  }
                  
                  // arc is drawn, remove handler:
                  createArc.removeKeyListener(keyHandler);
                  keyHandler = null;
                  /**/
                  if (isNewArc == false){
                     view.remove(createArc);
                  }
                  /* */
                  view.createArc = null;
               }
            }
            
            if (app.getMode() == Constants.FAST_PLACE ||
                    app.getMode() == Constants.FAST_TRANSITION) {
               if (view.newPNO == true) {
                  // a new PNO has been created 
                  view.newPNO = false;

                  if (currentObject instanceof Transition) {
                     app.setMode(Constants.FAST_PLACE);
                  } else if (currentObject instanceof Place) {
                     app.setMode(Constants.FAST_TRANSITION);
                  }
               } else {
                  if (view.createArc == null) {
                     // user has clicked on an existent PNO
                     app.resetMode();
                  } else {
                     if (currentObject instanceof Transition) {
                        app.setMode(Constants.FAST_PLACE);
                     } else if (currentObject instanceof Place) {
                        app.setMode(Constants.FAST_TRANSITION);
                     }
                  }
               }
            }
            break;
            
         default:
            break;
      }
   }
   
}
