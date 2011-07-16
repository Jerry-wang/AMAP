package pipe.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import pipe.common.dataLayer.Transition;
import pipe.gui.CreateGui;
import pipe.gui.Constants;
import pipe.gui.ZoomController;
import pipe.gui.action.ShowHideInfoAction;
import pipe.gui.undo.UndoableEdit;

/**
 * Class used to implement methods corresponding to mouse events on transitions.
 */
public class TransitionHandler 
        extends PlaceTransitionObjectHandler {
        //implements java.awt.event.MouseWheelListener {  //NOU-PERE
  
   
   public TransitionHandler(Container contentpane, Transition obj) {
      super(contentpane, obj);
   }

   
   public void mouseWheelMoved (MouseWheelEvent e) {

      if (CreateGui.getApp().isEditionAllowed() == false || 
              e.isControlDown()) {
         return;
      }
      
      if (e.isShiftDown()) {
         CreateGui.getView().getUndoManager().addNewEdit(
                 ((Transition)myObject).setTimed(
                 !((Transition)myObject).isTimed()));
      } else {
         int rotation = 0;
         if (e.getWheelRotation() < 0) {
            rotation = -e.getWheelRotation() * 135;
         } else {
            rotation = e.getWheelRotation() * 45;
         }
         CreateGui.getView().getUndoManager().addNewEdit(
                 ((Transition)myObject).rotate(rotation));
      }
   }   
   
   
   /** 
    * Creates the popup menu that the user will see when they right click on a 
    * component 
    */
   public JPopupMenu getPopup(MouseEvent e) {
      int index = 0;
      JPopupMenu popup = super.getPopup(e);
      
//      JMenuItem menuItem = new JMenuItem("Edit Transition");
      JMenuItem menuItem = new JMenuItem("编辑变迁");
      menuItem.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e) {
            ((Transition)myObject).showEditor();
         }
      });       
      popup.insert(menuItem, index++);             
      
      menuItem = new JMenuItem(new ShowHideInfoAction((Transition)myObject));
      if (((Transition)myObject).getAttributesVisible() == true){
//         menuItem.setText("Hide Attributes");
         menuItem.setText("隐藏属性");
      } else {
//    	  menuItem.setText("Show Attributes");
    	  menuItem.setText("显示属性");
      }
      popup.insert(menuItem, index++);     
      
      popup.insert(new JPopupMenu.Separator(), index);
//      menuItem = new JMenuItem("Group Transitions");   
      menuItem = new JMenuItem("合并变迁 ");  
      menuItem.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e) {
        	 UndoableEdit edit = ((Transition)myObject).groupTransitions();
        	 if(edit != null){
        		 CreateGui.getView().getUndoManager().addNewEdit(edit);
        	 }
         }
      });       
      popup.insert(menuItem, index++);           
            
      return popup;
   }
   
   
   public void mouseClicked(MouseEvent e) {   
      if (SwingUtilities.isLeftMouseButton(e)){    
         if (e.getClickCount() == 2 &&
                 CreateGui.getApp().isEditionAllowed() && 
                 (CreateGui.getApp().getMode() == Constants.TIMEDTRANS || 
                 CreateGui.getApp().getMode() == Constants.IMMTRANS ||
                 CreateGui.getApp().getMode() == Constants.SELECT)) {
            ((Transition)myObject).showEditor();
         } 
      }  else if (SwingUtilities.isRightMouseButton(e)){
         if (CreateGui.getApp().isEditionAllowed() && enablePopup) { 
            JPopupMenu m = getPopup(e);
            if (m != null) {           
               int x = ZoomController.getZoomedValue(
                       ((Transition)myObject).getNameOffsetXObject().intValue(),
                       myObject.getZoom());
               int y = ZoomController.getZoomedValue(
                       ((Transition)myObject).getNameOffsetYObject().intValue(),
                       myObject.getZoom());
               m.show(myObject, x, y);
            }
         }
      }
   }
   
}
