package pipe.gui;

import java.awt.geom.AffineTransform;


/**
 * @author Tim Kimber
 * @author Pere Bonet - Minor changes
 */
public class ZoomController {

   private int percent;
   private AffineTransform transform = new AffineTransform();
   private GuiFrame app;
   
   public ZoomController(GuiFrame _app){
      this(100, _app);
   }
   
   
   public ZoomController(int pct, GuiFrame _app){
      percent = pct;
      app = _app;
   }
   
   
   public boolean zoomOut() {
      percent -= Constants.ZOOM_DELTA;
      if (percent < Constants.ZOOM_MIN) {
         percent += Constants.ZOOM_DELTA;
         return false;
      } else {
         return true;
      }
   }
   
   
   public boolean zoomIn(){
      percent += Constants.ZOOM_DELTA;
      if (percent > Constants.ZOOM_MAX) {
         percent -= Constants.ZOOM_DELTA;
         return false;
      } else {
         return true;
      }
   }

   
   public int getPercent() {
      return percent;
   }

   
   private void setPercent(int newPercent) {
      if ((newPercent >= Constants.ZOOM_MIN) && (newPercent <= Constants.ZOOM_MAX)) {
         percent=newPercent;
      }
   }

   
   public void setZoom(int newPercent) {
      setPercent(newPercent);
   }


   public static int getZoomedValue(int x, int zoom) {
      return (int)(x * zoom * 0.01);
   }
   
   
   public static float getZoomedValue(float x, int zoom) {
      return (float)(x * zoom * 0.01);
   }
   
   
   public static double getZoomedValue(double x, int zoom) {
      return (x * zoom * 0.01);
   }   
   
   
   public static AffineTransform getTransform(int zoom){
      return AffineTransform.getScaleInstance(zoom * 0.01, zoom * 0.01);
   }
   
   
   public static double getScaleFactor(int zoom) {
      return zoom * 0.01;
   }
   
   
   public static int getUnzoomedValue (int x, int zoom) {
      return (int)(x / (zoom * 0.01));
   }

   
   public static double getUnzoomedValue(double x, int zoom) {
      return (x / (zoom * 0.01));
   }
   
	public AffineTransform getTransform() {
		return transform;
	}

}
