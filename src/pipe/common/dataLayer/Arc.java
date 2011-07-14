package pipe.common.dataLayer;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import javax.swing.JLayeredPane;

import pipe.gui.CreateGui;
import pipe.gui.Constants;
import pipe.gui.GuiView;
import pipe.gui.undo.UndoableEdit;
import pipe.gui.undo.AddArcPathPointEdit;
import pipe.gui.undo.ArcWeightEdit;
import pipe.gui.widgets.ArcWeightEditorPanel;
import pipe.gui.widgets.EscapableDialog;

/**
 * <b>Arc</b> - Petri-Net Arc Class
 * 
 * @see <p>
 *      <a href="..\PNMLSchema\index.html">PNML - Petri-Net XMLSchema
 *      (stNet.xsd)</a>
 * @see </p>
 *      <p>
 *      <a href="..\..\..\UML\dataLayer.html">UML - PNML Package </a>
 *      </p>
 * @version 1.0
 * @author James D Bloom
 * 
 * @author Pere Bonet modifed the delete method so that the weight label of an
 *         arc is deleted when the associated arc is deleted
 * 
 * @author Edwin Chung 16 Mar 2007: modified the constructor and several other
 *         functions so that DataLayer objects can be created outside the GUI
 * 
 * @author Nick Dingle 18 Oct 2007: added the ability for an arc to be "tagged"
 *         (permit the passage of tagged tokens).
 */
public abstract class Arc extends PetriNetObject implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// public final static String TYPE = "arc";
	/** Current Marking */
	protected LinkedList<Marking> weight = new LinkedList<Marking>();

	protected LinkedList<NameLabel> weightLabel = new LinkedList<NameLabel>(); 

	private static Point2D.Double point;

	/** references to the objects this arc connects */
	private PlaceTransitionObject source = null;
	private PlaceTransitionObject target = null;
	// private boolean deleted = false; // Used for cleanup purposes
	
	/**
	 * Whether or not the Arc is capable of carrying tagged tokens By default it
	 * is not
	 */
	private Boolean					tagged				= false;

	protected ArcPath myPath = new ArcPath(this);

	// true if arc is not hidden when a bidirectional arc is used
	protected boolean inView = true;

	// bounds of arc need to be grown in order to avoid clipping problems
	protected int zoomGrow = 10;

	/**
	 * Create Petri-Net Arc object
	 * 
	 * @param startPositionXInput
	 *            Start X-axis Position
	 * @param startPositionYInput
	 *            Start Y-axis Position
	 * @param endPositionXInput
	 *            End X-axis Position
	 * @param endPositionYInput
	 *            End Y-axis Position
	 * @param sourceInput
	 *            Arc source
	 * @param targetInput
	 *            Arc target
	 * @param idInput
	 *            Arc id
	 * @param inputTagged
	 *            TODO
	 */
	public Arc(double startPositionXInput, double startPositionYInput,
			double endPositionXInput, double endPositionYInput,
			PlaceTransitionObject sourceInput,
			PlaceTransitionObject targetInput, LinkedList<Marking> weightInput,
			String idInput) {
		myPath.addPoint((float) startPositionXInput,
				(float) startPositionYInput, ArcPathPoint.STRAIGHT);
		myPath.addPoint((float) endPositionXInput, (float) endPositionYInput,
				ArcPathPoint.STRAIGHT);
		myPath.createPath();
		updateBounds();
		id = idInput;
		setSource(sourceInput);
		setTarget(targetInput);
		setWeight((LinkedList<Marking>) ObjectDeepCopier.mediumCopy(weightInput));
		//Debug - need to work out how the marking linked list works for weighting arcs.
		//System.out.println(ObjectDeepCopier.mediumCopy(weightInput));
		//System.out.println(ObjectDeepCopier.mediumCopy(weightInput).getFirst().getCurrentMarking());
	}

	/**
	 * Create Petri-Net Arc object
	 */
	public Arc(PlaceTransitionObject newSource) {
		source = newSource;
		myPath.addPoint();
		myPath.addPoint();
		myPath.createPath();
	}

	public Arc() {
		super();
	}

	/**
	 * Set source
	 * 
	 * @param sourceInput
	 *            PlaceTransitionObject value for Arc source;
	 */
	public void setSource(PlaceTransitionObject sourceInput) {
		source = sourceInput;
	}

	/**
	 * Set target
	 * 
	 * @param targetInput
	 *            PlaceTransitionObject value for Arc target;
	 */
	public void setTarget(PlaceTransitionObject targetInput) {
		target = targetInput;
	}

	/**
	 * Set weight
	 * 
	 * @param weightInput
	 *            String value for Arc weight;
	 */
	public UndoableEdit setWeight(LinkedList<Marking> weightInput) {
		LinkedList<Marking> oldWeight = (LinkedList<Marking>) ObjectDeepCopier
				.mediumCopy(weight);
		return new ArcWeightEdit(this, oldWeight, weight);
	}
	
	// Sets arc weight label position. Each weight for each class goes one under 
	// the other until the tokenClass weights exceed 4, in which case
	// a new column is started on the right of this column.
	public void setWeightLabelPosition() {
		int originalX = (int) (myPath.midPoint.x);
		int originalY = (int) (myPath.midPoint.y) - 10;
		int x = originalX;
		int y = originalY;
		int yCount = 0;
		for (NameLabel label : weightLabel) {
			if(yCount >= 4){
				y = originalY;
				x += 17;
				yCount = 0;
			}
			// we must trim here as the removeLabelsFromArc adds blank spaces
			if( !label.getText().trim().equals("") && Integer.valueOf(label.getText()) >0){
				label.setPosition(x + label.getWidth() / 2 - 4, y);
				y += 10;
				yCount++;
			}
		}
	}
	// Removes any current labels from the arc
	public void removeLabelsFromArc(){
			int x = (int) (myPath.midPoint.x);
			int y = (int) (myPath.midPoint.y) - 10;
			for (NameLabel label : weightLabel) {
				if( !label.getText().trim().equals("") && Integer.valueOf(label.getText()) >0){
					// Put blank spaces over previous label
				//	NameLabel newLabel = new NameLabel(zoom);
					int currentLength = label.getText().length();
					label.setText("");
					for(int i = 0; i < currentLength; i++){
						label.setText(label.getText() + " ");
					}
					label.setPosition(x + label.getWidth() / 2 - 4, y);
					y += 10;
				}
				getParent().remove(label);
			}
			weightLabel.clear();
	}

	/**
	 * Get id
	 * 
	 * @return String value for Arc id;
	 */
	public String getId() {
		if (id != null) {
			return id;
		} else {
			if (source != null && target != null) {
				return source.getId() + " to " + target.getId();
			}
		}
		return "";
	}

	public String getName() {
		return getId();
	}

	/**
	 * Get source returns null if value not yet entered
	 * 
	 * @return String value for Arc source;
	 */
	public PlaceTransitionObject getSource() {
		return source;
	}

	/**
	 * Get target returns null if value not yet entered
	 * 
	 * @return String value for Arc target;
	 */
	public PlaceTransitionObject getTarget() {
		return target;
	}

	/**
	 * Get X-axis value of start position
	 * 
	 * @return Double value for X-axis of start position
	 */
	public double getStartPositionX() {
		return myPath.getPoint(0).getX();
	}

	/**
	 * Get Y-axis value of start position
	 * 
	 * @return Double value for Y-axis of start position
	 */
	public double getStartPositionY() {
		return myPath.getPoint(0).getY();
	}

	/**
	 * Get weight
	 * 
	 * @return Integer value for Arc weight;
	 */
	public LinkedList<Marking> getWeight() {
		return weight;
	}
	
	public int getSimpleWeight(){
		return 1;
	}

	/**
	 * Updates the start position of the arc, resets the arrowhead and updates
	 * the bounds
	 */
	public void updateArcPosition() {
		if (source != null) {
			source.updateEndPoint(this);
		}
		if (target != null) {
			target.updateEndPoint(this);
		}
		myPath.createPath();
	}

	public void setEndPoint(double x, double y, boolean type) {
		myPath.setPointLocation(myPath.getEndIndex(), x, y);
		myPath.setPointType(myPath.getEndIndex(), type);
		updateArcPosition();
	}

	public void setTargetLocation(double x, double y) {
		myPath.setPointLocation(myPath.getEndIndex(), x, y);
		myPath.createPath();
		updateBounds();
		repaint();
	}

	public void setSourceLocation(double x, double y) {
		myPath.setPointLocation(0, x, y);
		myPath.createPath();
		updateBounds();
		repaint();
	}

	/** Updates the bounding box of the arc component based on the arcs bounds */
	public void updateBounds() {
		bounds = myPath.getBounds();
		bounds.grow(getComponentDrawOffset() + zoomGrow, getComponentDrawOffset()
				+ zoomGrow);
		setBounds(bounds);
	}

	public ArcPath getArcPath() {
		return myPath;
	}

	public boolean contains(int x, int y) {
		point = new Point2D.Double(x + myPath.getBounds().getX()
				- getComponentDrawOffset() - zoomGrow, y
				+ myPath.getBounds().getY() - getComponentDrawOffset() - zoomGrow);
		if (!CreateGui.getView().isInAnimationMode()) {
			if (myPath.proximityContains(point) || selected) {
				// show also if Arc itself selected
				myPath.showPoints();
			} else {
				myPath.hidePoints();
			}
		}

		return myPath.contains(point);
	}

	public void addedToGui() {
		// called by GuiView / State viewer when adding component.
		deleted = false;
		markedAsDeleted = false;

		if (getParent() instanceof GuiView) {
			myPath.addPointsToGui((GuiView) getParent());
		} else {
			myPath.addPointsToGui((JLayeredPane) getParent());
		}
		updateArcPosition();
		Container parent = getParent();
		if (parent != null){
			int i = 0;
			for (NameLabel label : weightLabel) {
				if (label.getParent() == null) {
					i++;
					parent.add(label);
				}
			}
		}
	}

	public void delete() {
		if (!deleted) {
			for (NameLabel label : weightLabel) {
				getParent().remove(label);
			}
			myPath.forceHidePoints();
			super.delete();
			deleted = true;
		}
	}

	public void setPathToTransitionAngle(int angle) {
		myPath.setTransitionAngle(angle);
	}

	public UndoableEdit split(Point2D.Float mouseposition) {
		ArcPathPoint newPoint = myPath.splitSegment(mouseposition);
		return new AddArcPathPointEdit(this, newPoint);
	}

	public abstract String getType();

	public boolean inView() {
		return inView;
	}

	public Transition getTransition() {
		if (getTarget() instanceof Transition) {
			return (Transition) getTarget();
		} else {
			return (Transition) getSource();
		}
	}

	public void removeFromView() {
		if (getParent() != null) {
			for (NameLabel label : weightLabel) {
				getParent().remove(label);
			}
		}
		myPath.forceHidePoints();
		removeFromContainer();
	}

	public void addToView(GuiView view) {
		if (getParent() != null) {
			for (NameLabel label : weightLabel) {
				getParent().add(label);
			}
		}
		myPath.showPoints();
	    view.add(this);
	}
	
	public boolean getsSelected(Rectangle selectionRectangle) {
		if (selectable) {
			ArcPath arcPath = getArcPath();
			if (arcPath.proximityIntersects(selectionRectangle)) {
				arcPath.showPoints();
			} else {
				arcPath.hidePoints();
			}
			if (arcPath.intersects(selectionRectangle)) {
				select();
				return true;
			}
		}
		return false;
	}

	public int getLayerOffset() {
		return Constants.ARC_LAYER_OFFSET;
	}

	public void translate(int x, int y) {
		// We don't translate an arc, we translate each selected arc point
	}

	public void zoomUpdate(int percent) {
		zoom = percent;
		this.updateArcPosition();
		for (NameLabel label : weightLabel) {
			label.zoomUpdate(percent);
			label.updateSize();
		}
	}

	public void setZoom(int percent) {
		zoom = percent;
	}

	public void undelete(DataLayerInterface model, GuiView view) {
		if (this.isDeleted()) {
			model.addPetriNetObject(this);
			view.add(this);
			getSource().addConnectFrom(this);
			getTarget().addConnectTo(this);
		}
	}

	/**
	 * Method to clone an Arc object
	 */
	public PetriNetObject clone() {
		return (Arc) super.clone();
	}

	public int getWeightOfTokenClass(String id) {
		if (weight != null) {
			for (Marking m : weight) {
				if (m.getTokenClass().getID().equals(id)) {
					return m.getCurrentMarking();
				}
			}
		}
		return 0;
	}

	public void showEditor() {
		// Build interface
		EscapableDialog guiDialog = new EscapableDialog(CreateGui.getApp(),
				"PIPE2", true);

		ArcWeightEditorPanel arcWeightEditor = new ArcWeightEditorPanel(
				guiDialog.getRootPane(), this, CreateGui.getModel(), CreateGui
						.getView());

		guiDialog.add(arcWeightEditor);

		guiDialog.getRootPane().setDefaultButton(null);

		guiDialog.setResizable(false);

		// Make window fit contents' preferred size
		guiDialog.pack();

		// Move window to the middle of the screen
		guiDialog.setLocationRelativeTo(null);

		guiDialog.setVisible(true);

		guiDialog.dispose();
	}

	// Accessor function to check whether or not the Arc is tagged
	public boolean isTagged()
	{
		return this.tagged;
	}
}
