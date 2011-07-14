package pipe.gui.handler;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.SwingUtilities;

import pipe.common.dataLayer.GroupTransition;
import pipe.common.dataLayer.Transition;
import pipe.gui.CreateGui;

/**
 * This class handles mouse clicks by the user. 
 * 
 * @author unknown 
 * @author David Patterson
 * 
 * Change by David Patterson was to fire the selected 
 * transition in the DataLayer, and then record the firing
 * in the animator.
 * 
 * @author Pere Bonet reverted the above change.
 */
public class AnimationHandler 
        extends javax.swing.event.MouseInputAdapter {
   
	// This linked list stores the previously fired groupTransition.
	// Once the user clicks on a grouped transition on the GUI, this is not null
	// Once the user fires a transition within the group this is used to
	// recover the group transition.
	private LinkedList<GroupTransition> lastGroupTransition = new LinkedList<GroupTransition>();
   
   public void mouseClicked(MouseEvent e){      
      if (e.getComponent() instanceof Transition) {
         Transition transition = (Transition)e.getComponent();
         
         if (SwingUtilities.isLeftMouseButton(e)
                 && (transition.isEnabled(true))) {
            CreateGui.getAnimationHistory().clearStepsForward();
            CreateGui.getAnimator().fireTransition(transition);
            CreateGui.getApp().setRandomAnimationMode(false);
            if(!lastGroupTransition.isEmpty()){
            	for(GroupTransition groupTransition:lastGroupTransition){
	        		for (Transition t : groupTransition.getTransitions()) {
	        			t.hideFromCanvas();
	        			t.hideAssociatedArcs();
	        		}
	        		groupTransition.getNameLabel().setVisible(true);
	        		groupTransition.setVisible(true);
	        		groupTransition.showAssociatedArcs();
	            }
            }
         }
      }
      else if (e.getComponent() instanceof GroupTransition) {
          GroupTransition groupTransition = (GroupTransition)e.getComponent();

          if (SwingUtilities.isLeftMouseButton(e)
        		  && (groupTransition.isEnabled(true)) ) {
      		for(Transition t:groupTransition.getTransitions()){
    			t.unhideFromCanvas();
    			t.showAssociatedArcs();
    		}
    		groupTransition.hideAssociatedArcs();
    		groupTransition.setVisible(false);
    		groupTransition.getNameLabel().setVisible(false);
    		
    		lastGroupTransition.add(groupTransition);
      		/*DataLayer model = CreateGui.getModel();
      		model.removePetriNetObject(groupTransition);
      		GuiView view = CreateGui.getView();
      		view.remove(groupTransition);
	        */     /*CreateGui.getAnimationHistory().clearStepsForward();
	             CreateGui.getAnimator().fireTransition(enabledTransitions.get(transitionToFirePos));
	             CreateGui.getApp().setRandomAnimationMode(false);*/
          }
       }
   }
   
}
