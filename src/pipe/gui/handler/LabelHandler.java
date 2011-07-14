package pipe.gui.handler;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;

import pipe.common.dataLayer.NameLabel;
import pipe.common.dataLayer.PlaceTransitionObject;


public class LabelHandler 
        extends javax.swing.event.MouseInputAdapter {
        //implements java.awt.event.MouseWheelListener { NOU-PERE, i rename aquesta classe a NameLabelHand
   
   private PlaceTransitionObject obj;
   
   private NameLabel nl;
   
   protected Point dragInit = new Point();
   
   
   public LabelHandler(NameLabel _nl, PlaceTransitionObject _obj) {
      obj = (PlaceTransitionObject)_obj;
      nl = _nl;
   }
   
   
   public void mouseClicked(MouseEvent e) {
      obj.dispatchEvent(e);
   }
   
   
   public void mousePressed(MouseEvent e) {
      dragInit = e.getPoint(); //
      dragInit = javax.swing.SwingUtilities.convertPoint(nl, dragInit, obj);
   }
  

   public void mouseDragged(MouseEvent e){
      // 
      if (!SwingUtilities.isLeftMouseButton(e)){
         return;
      }
      
      Point p = javax.swing.SwingUtilities.convertPoint(nl, e.getPoint(), obj);
      //obj.setNameOffsetX((e.getXOnScreen() - dragInit.x)); //causes exception in Windows!
      //obj.setNameOffsetY((e.getYOnScreen() - dragInit.y)); //causes exception in Windows!
      //dragInit = e.getLocationOnScreen(); //causes exception in Windows!
      obj.setNameOffsetX((p.x - dragInit.x));
      obj.setNameOffsetY((p.y - dragInit.y));
      dragInit = p;
      obj.update();
   }   
   
   public void mouseWheelMoved(MouseWheelEvent e) {
      obj.dispatchEvent(e);
   }
   
}
