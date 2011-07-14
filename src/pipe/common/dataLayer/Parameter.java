/*
 * Created on 
 * Author is 
 */
package pipe.common.dataLayer;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import pipe.gui.Constants;
import pipe.gui.ZoomController;
import pipe.gui.undo.ParameterNameEdit;
import pipe.gui.undo.UndoableEdit;


public abstract class Parameter 
        extends Note {
         
   protected String name; 
   
   protected boolean valueChanged = false;
   
   
   protected Parameter(int x, int y){
      super (x, y);
      copyPasteable = false;
      note.setLineWrap(false);
      note.setWrapStyleWord(false);
   }
   

   public String getName() {
      return name;
   }

   
   public UndoableEdit setParameterName(String _name) {
      String oldName = name;
      name = _name;
      valueChanged = true;
      return new ParameterNameEdit(this, oldName, name);
   }
   
   
   public String toString(){
      return name;
   }
   
   
   public abstract void enableEditMode();

   
   public abstract void update();

   
   public void paintComponent(Graphics g) {
      //updateBounds();
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.transform(ZoomController.getTransform(zoom));   
      g2.setStroke(new BasicStroke(1.5f));
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                          RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 
                          RenderingHints.VALUE_STROKE_NORMALIZE);

      if (selected && !ignoreSelection) {
         g2.setPaint(Constants.SELECTION_FILL_COLOUR);
         g2.fill(noteRect);
         g2.setPaint(Constants.SELECTION_LINE_COLOUR);
      } else {
         g2.setPaint(Constants.ELEMENT_FILL_COLOUR);         
         g2.fill(noteRect);
         g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
      }
      g2.draw(noteRect);
   }   
   
}
