/*
 * AddArcPathPointEdit.java
 */

package pipe.gui.undo;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.ArcPath;
import pipe.common.dataLayer.ArcPathPoint;


/**
 *
 * @author Pere Bonet
 */
public class AddArcPathPointEdit
        extends UndoableEdit {
   
   ArcPath arcPath;
   ArcPathPoint point;
   Integer index;

   /** Creates a new instance of AddArcPathPointEdit */
   public AddArcPathPointEdit(Arc _arc, ArcPathPoint  _point) {
      arcPath = _arc.getArcPath();
      point = _point;
      index = point.getIndex();
   }

   
   /**
    *
    */
   public void undo() {
      point.delete();
   }

   
   /** */
   public void redo() {
      arcPath.insertPoint(index, point);
      arcPath.updateArc();
   }
   
}
