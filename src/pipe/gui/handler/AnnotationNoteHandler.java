 /*
 * Created on 05-Mar-2004
 * Author is Michael Camacho
 *
 */
package pipe.gui.handler;

import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import pipe.common.dataLayer.AnnotationNote;
import pipe.gui.action.EditAnnotationBackgroundAction;
import pipe.gui.action.EditAnnotationBorderAction;
import pipe.gui.action.EditNoteAction;


public class AnnotationNoteHandler 
        extends NoteHandler {
   

   public AnnotationNoteHandler(Container contentpane, AnnotationNote note) {
      super(contentpane, note);
      enablePopup = true;
   }

   
   /** 
    * Creates the popup menu that the user will see when they right click on a 
    * component */
   public JPopupMenu getPopup(MouseEvent e) {
      int popupIndex = 0;
      JPopupMenu popup = super.getPopup(e);
      
      JMenuItem menuItem =
              new JMenuItem(new EditNoteAction((AnnotationNote)myObject));
//      menuItem.setText("Edit text");
      menuItem.setText("编辑注释");
      popup.insert(menuItem, popupIndex++);
      
      menuItem = new JMenuItem(
              new EditAnnotationBorderAction((AnnotationNote)myObject));
      if (((AnnotationNote)myObject).isShowingBorder()){
         menuItem.setText("去除边框");
//         menuItem.setText("Disable Border");
      } else{
//         menuItem.setText("Enable Border");
    	  menuItem.setText("显示边框");
      }
      popup.insert(menuItem, popupIndex++);
      
      menuItem = new JMenuItem(
              new EditAnnotationBackgroundAction((AnnotationNote)myObject));
      if (((AnnotationNote)myObject).isFilled()) {
//         menuItem.setText("Transparent");
         menuItem.setText("去除背景");
      } else {
//         menuItem.setText("Solid Background");
    	  menuItem.setText("显示背景");
      }
      popup.insert(new JPopupMenu.Separator(), popupIndex++);      
      popup.insert(menuItem, popupIndex);

      return popup;
   }

   
   public void mouseClicked(MouseEvent e) {
      if ((e.getComponent() == myObject || !e.getComponent().isEnabled()) && 
              (SwingUtilities.isLeftMouseButton(e))) { 
         if (e.getClickCount() == 2){
            ((AnnotationNote)myObject).enableEditMode();
         }
      }
   }
   
}
