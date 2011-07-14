package pipe.common.dataLayer;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import pipe.gui.Constants;
import pipe.gui.ZoomController;


/**
 * @version 1.0
 * @author Pere Bonet
 */
public class InhibitorArc 
        extends Arc {
   
   public final static String type = "inhibitor";
   private final static int OVAL_X = -4;
   private final static int OVAL_Y = -8;
   private final static int OVAL_WIDTH = 8;
   private final static int OVAL_HEIGHT = 8;
   //NOU-PERE
   
   
   /**
    * Create Petri-Net Arc object
    *
    * @param startPositionXInput Start X-axis Position
    * @param startPositionYInput Start Y-axis Position
    * @param endPositionXInput End X-axis Position
    * @param endPositionYInput End Y-axis Position
    * @param sourceInput Arc source
    * @param targetInput Arc target
    * @param idInput Arc id
    */
   InhibitorArc(double startPositionXInput, double startPositionYInput,                        double endPositionXInput, double endPositionYInput, 
                       PlaceTransitionObject sourceInput, 
                       PlaceTransitionObject targetInput,
                       LinkedList<Marking> weightInput, 
                       String idInput) {
      super(startPositionXInput, startPositionYInput,
            endPositionXInput, endPositionYInput,
            sourceInput,
            targetInput,
            weightInput,
            idInput);
   }
   
   
   /**
    * Create Petri-Net Arc object
    */
   InhibitorArc(PlaceTransitionObject newSource) {
      super(newSource);
   }
   
   
   
   InhibitorArc(InhibitorArc arc) {
	   weightLabel = new LinkedList<NameLabel>();
	   for(int i = 0; i < 100; i++){
			weightLabel.add(new NameLabel(zoom));
	   }
      
      for (int i = 0; i <= arc.myPath.getEndIndex(); i++){
         this.myPath.addPoint(arc.myPath.getPoint(i).getX(),
                              arc.myPath.getPoint(i).getY(),
                              arc.myPath.getPointType(i));         
      }      
      this.myPath.createPath();
      this.updateBounds();  
      this.id = arc.id;
      this.setSource(arc.getSource());
      this.setTarget(arc.getTarget());
      this.setWeight((LinkedList<Marking>)ObjectDeepCopier.mediumCopy(arc.getWeight()));
   }
   
   
   public InhibitorArc paste(double despX, double despY, boolean toAnotherView, DataLayerInterface model){
      PlaceTransitionObject source = this.getSource().getLastCopy();
      PlaceTransitionObject target = this.getTarget().getLastCopy();
      
      if (source == null && target == null) {
         // don't paste an arc with neither source nor target
         return null;
      }
      
      if (source == null){
         if (toAnotherView) {
            // if the source belongs to another Petri Net, the arc can't be 
            // pasted
            return null;
         } else {
            source = this.getSource();
         }
      }
      
      if (target == null){
         if (toAnotherView) {
            // if the target belongs to another Petri Net, the arc can't be 
            // pasted
            return null;
         } else {
            target = this.getTarget();
         }
      }

      InhibitorArc copy =
              ArcFactory.createInhibitorArc(0, 0, 0, 0, source, target, this.getWeight(),
			source.getId() + " to " + target.getId());      

      copy.myPath.delete();
      for (int i = 0; i <= this.myPath.getEndIndex(); i++){
         copy.myPath.addPoint(this.myPath.getPoint(i).getX() + despX,
                              this.myPath.getPoint(i).getY() + despY,
                              this.myPath.getPointType(i));         
         //copy.myPath.selectPoint(i);
      }

      source.addConnectFrom(copy);
      target.addConnectTo(copy);
      return copy;
   }
   
   
   public InhibitorArc copy(){
      return ArcFactory.createInhibitorArc(this);
   }
    
   
   public String getType(){
      return this.type;
   }   

   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;   
      
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                          RenderingHints.VALUE_ANTIALIAS_ON);
      
      g2.translate(getComponentDrawOffset() + zoomGrow - myPath.getBounds().getX(),
               getComponentDrawOffset() + zoomGrow - myPath.getBounds().getY());
      
      if (selected && !ignoreSelection){
         g2.setPaint(Constants.SELECTION_LINE_COLOUR);
      } else{
         g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
      }
     
      g2.setStroke(new BasicStroke(0.01f * zoom));
      g2.draw(myPath);
      
      g2.translate(myPath.getPoint(myPath.getEndIndex()).getX(),
               myPath.getPoint(myPath.getEndIndex()).getY());
        
      g2.rotate(myPath.getEndAngle()+Math.PI);
      g2.setColor(java.awt.Color.WHITE);
            
      AffineTransform reset = g2.getTransform();
      g2.transform(ZoomController.getTransform(zoom));   
  
      g2.setStroke(new BasicStroke(0.8f));      
      g2.fillOval(OVAL_X, OVAL_Y, OVAL_WIDTH, OVAL_HEIGHT);
  
      if (selected && !ignoreSelection){
         g2.setPaint(Constants.SELECTION_LINE_COLOUR);
      } else{
         g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
      }
      g2.drawOval(OVAL_X, OVAL_Y, OVAL_WIDTH, OVAL_HEIGHT);
      
      g2.setTransform(reset);
   }   

}
