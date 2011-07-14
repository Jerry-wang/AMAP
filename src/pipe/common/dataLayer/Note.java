/*
 * Note.java
 */
package pipe.common.dataLayer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;

import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

import pipe.gui.CreateGui;
import pipe.gui.Constants;
import pipe.gui.Translatable;
import pipe.gui.ZoomController;
import pipe.gui.undo.UndoableEdit;
import pipe.gui.undo.AnnotationBorderEdit;


/** 
 * This abstract class is the base class for AnnotationNote class and for 
 * Parameter class
 */
public abstract class Note 
        extends PetriNetObject 
        implements Translatable {

   protected JTextArea note = new JTextArea();
   protected boolean drawBorder = true;
   protected RectangularShape noteRect = new Rectangle();
   private int originalX;
   private int originalY;   

   
   public Note(int x, int y){
      originalX = ZoomController.getUnzoomedValue(x, zoom);
      originalY = ZoomController.getUnzoomedValue(y, zoom);

      note.setAlignmentX(Component.CENTER_ALIGNMENT);
      note.setAlignmentY(Component.CENTER_ALIGNMENT);
      note.setOpaque(false);
      note.setEditable(false);
      note.setEnabled(false);
      note.setLineWrap(true);
      note.setWrapStyleWord(true);
      
      // Set minimum size the preferred size for an empty string:
      note.setText("");
      note.setFont(new Font(Constants.ANNOTATION_DEFAULT_FONT, Font.PLAIN, 
                            Constants.ANNOTATION_DEFAULT_FONT_SIZE));
      note.setSize(note.getPreferredSize().width,note.getPreferredSize().height);
      note.setMinimumSize(note.getPreferredSize());
      note.setHighlighter(new DefaultHighlighter());
      note.setDisabledTextColor(Constants.NOTE_DISABLED_COLOUR);
      note.setForeground(Constants.NOTE_EDITING_COLOUR);
      add(note);
      setLocation(x - Constants.RESERVED_BORDER/2, y - Constants.RESERVED_BORDER/2);
   }
   
   
   public Note(String id, String text, int x, int y){
      this(x, y);
      this.id = id;
      note.setText(text);
      note.setSize(note.getPreferredSize().width,note.getPreferredSize().height);
      updateBounds();
   }

   
   public Note(String text, int x, int y, int w, int h, boolean border){
      this(x, y);      
      note.setText(text);
      drawBorder = border;
      note.setSize(w, h);
      updateBounds();
   }
   
   
   /** Calculates the BoundsOffsets used for setBounds() method */
   public void updateBounds(){  
      int newHeight = note.getPreferredSize().height;
      
      if ((note.getHeight() < newHeight) 
              && (newHeight >= note.getMinimumSize().height)){
         note.setSize(note.getWidth(), newHeight);
      }
      
      int rectWidth = note.getWidth() + Constants.RESERVED_BORDER;
      int rectHeight = note.getHeight()  + Constants.RESERVED_BORDER;

      noteRect.setFrame(Constants.RESERVED_BORDER/2, Constants.RESERVED_BORDER/2, 
                        rectWidth, rectHeight);
      setSize(rectWidth  + Constants.ANNOTATION_SIZE_OFFSET, 
              rectHeight + Constants.ANNOTATION_SIZE_OFFSET);
      
      note.setLocation((int)noteRect.getX() + (rectWidth - note.getWidth())/2,
                       (int)noteRect.getY() + (rectHeight - note.getHeight())/2);

      bounds.setBounds((int)ZoomController.getZoomedValue(originalX, zoom) - 20,
                       (int)ZoomController.getZoomedValue(originalY, zoom) - 20,
                       (int)((rectWidth + Constants.RESERVED_BORDER
                              + Constants.ANNOTATION_SIZE_OFFSET)
                              * ZoomController.getScaleFactor(zoom)) + 20,
                       (int)((rectHeight + Constants.RESERVED_BORDER + 
                              + Constants.ANNOTATION_SIZE_OFFSET)
                              * ZoomController.getScaleFactor(zoom)) + 20);              
      setBounds(bounds);
   }

   
   public abstract void enableEditMode();

   
   public boolean isShowingBorder(){
      return drawBorder;
   }

   
   public UndoableEdit showBorder(boolean show){
      drawBorder = show;
      repaint();
      return new AnnotationBorderEdit(this);
   }

   
   public JTextArea getNote(){
      return note;
   }

   
   public String getNoteText(){
      return note.getText();
   }

   
   public int getNoteWidth(){
      return note.getWidth();
   }

   
   public int getNoteHeight(){
      return note.getHeight();
   }

   
   /** Translates the component by x,y */
   public void translate(int x, int y) {
      setLocation(getX() + x, getY() + y);
      originalX += ZoomController.getUnzoomedValue(x, zoom);
      originalY += ZoomController.getUnzoomedValue(y, zoom);
      updateBounds();
   }
   

   protected void adjustTop(int dy){
      if (note.getPreferredSize().height <= (note.getHeight() - dy)) {
         note.setSize(new Dimension(note.getWidth(), note.getHeight() - dy));
         setLocation(getX(), getY() + dy);
         originalY += dy;
      }
   }

   protected void adjustBottom(int dy){
      if (note.getPreferredSize().height <= (note.getHeight() + dy)) {
         note.setSize(new Dimension(note.getWidth(), note.getHeight() + dy));
      }
   }

   
   protected void adjustLeft(int dx){
      if (Constants.ANNOTATION_MIN_WIDTH <= (note.getWidth() - dx)) {
         note.setSize(new Dimension(note.getWidth() - dx, note.getHeight()));
         setLocation(getX() + dx, getY());
         originalX += dx;
      }
   }

   
   protected void adjustRight(int dx){
      if (Constants.ANNOTATION_MIN_WIDTH <= (note.getWidth() + dx)) {
         note.setSize(new Dimension(note.getWidth() + dx, note.getHeight()));
      }
   }

   
   public boolean contains(int x, int y) {
      return noteRect.contains(x / ZoomController.getScaleFactor(zoom), 
                               y / ZoomController.getScaleFactor(zoom));
   }


   // 
   public void addedToGui(){
      if (CreateGui.getView() != null){
         markedAsDeleted = false;
         deleted = false;
         updateBounds();
//         CreateGui.getView().setNetChanged(true);
      }
    } 
   

   public void setText(String text){
      note.setText(text);
      note.setSize(note.getPreferredSize());
   }

   
   public String getText(){
      return note.getText();
   }   
   
   
   public void delete() {
      CreateGui.getModel().removePetriNetObject(this);
      CreateGui.getView().remove(this);
   }   
   
   
   public int getLayerOffset() {
      return Constants.NOTE_LAYER_OFFSET;
   }   
   
   
   public int getOriginalX() {
      return originalX;
   }
   
   
   public int getOriginalY() {
      return originalY;
   }
   
   
   public void zoomUpdate(int percent) {
      zoom = percent;
      updateBounds();
   }   

}
