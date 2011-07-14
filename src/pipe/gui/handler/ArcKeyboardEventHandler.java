package pipe.gui.handler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import pipe.common.dataLayer.Arc;
import pipe.gui.CreateGui;
import pipe.gui.GuiView;
import pipe.gui.Constants;



/**
 * @authors Michael Camacho and Tom Barnwell
 *
 */
public class ArcKeyboardEventHandler
        extends KeyAdapter {
   
   private Arc arcBeingDrawn;
   
   
   public ArcKeyboardEventHandler(Arc anArc) {
      arcBeingDrawn = anArc;
   }
   

   public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
         case KeyEvent.VK_META:
         case KeyEvent.VK_WINDOWS:
            // I don't know if it's a java's bug or if I have a configuration 
            // problem with my linux box, but there is an issue with the 
            // Windows key under linux, so the space key is used as a provisional
            // solution
         case KeyEvent.VK_SPACE: //provisional
            ((GuiView)arcBeingDrawn.getParent()).setMetaDown(true);
            break;
            
         case KeyEvent.VK_ESCAPE:
         case KeyEvent.VK_DELETE:
            GuiView aView = ((GuiView)arcBeingDrawn.getParent());
            aView.createArc = null;
            arcBeingDrawn.delete();
            if ((CreateGui.getApp().getMode() == Constants.FAST_PLACE) ||
                    (CreateGui.getApp().getMode() == Constants.FAST_TRANSITION)) {
               CreateGui.getApp().resetMode();
            }
            aView.repaint();
            break;
            
         default:
            break;
      }
   }
   
   
   public void keyReleased(KeyEvent e) {   
      switch (e.getKeyCode()) {
         case KeyEvent.VK_META:
         case KeyEvent.VK_WINDOWS:
         case KeyEvent.VK_SPACE: //provisional
            ((GuiView)arcBeingDrawn.getParent()).setMetaDown(false);
            break;
            
         default:
            break;
      }
      e.consume();
   }
   
}
