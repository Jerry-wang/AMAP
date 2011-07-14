/*
 * Translatable.java
 */

package pipe.gui;

import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.PetriNetObject;


/**
 * This is the interface that a component must implement so that it can be 
 * copied and pasted.
 * @author Pere Bonet
 */
public interface CopyPasteable {

   /** 
    * copy()
    * @return a copy of the PetriNetObject 
    */
   public PetriNetObject copy();

   /** 
    * paste()
    * @param despX
    * @param despY
    * @param toAnotherView
    * @param model
    * @return a copy of the saved PetriNetObject that can be added to a GuiView
    * instance
    */
   public PetriNetObject paste(double despX, double despY, 
           boolean notInTheSameView, DataLayerInterface model);
   
   /**
    * isCopyPasteable();
    * @return true if this object can be copied and pasted
    */
   public boolean isCopyPasteable();
   
}
