package pipe.common.dataLayer;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import pipe.gui.Constants;
import pipe.gui.CreateGui;
import pipe.gui.Grid;
import pipe.gui.GuiView;
import pipe.gui.undo.ChangeRateParameterEdit;
import pipe.gui.undo.ClearRateParameterEdit;
import pipe.gui.undo.GroupTransitionEdit;
import pipe.gui.undo.SetRateParameterEdit;
import pipe.gui.undo.TransitionServerSemanticEdit;
import pipe.gui.undo.UndoableEdit;
import pipe.gui.undo.TransitionPriorityEdit;
import pipe.gui.undo.TransitionRateEdit;
import pipe.gui.undo.TransitionRotationEdit;
import pipe.gui.undo.TransitionTimingEdit;
import pipe.gui.widgets.TransitionEditorPanel;
import pipe.gui.ZoomController;
import pipe.gui.widgets.EscapableDialog;

/**
 * <b>Transition</b> - Petri-Net Transition Class
 * 
 * @see <p>
 *      <a href="..\PNMLSchema\index.html">PNML - Petri-Net XMLSchema
 *      (stNet.xsd)</a>
 * @see </p>
 *      <p>
 *      <a href="..\..\..\UML\dataLayer.html">UML - PNML Package </a>
 *      </p>
 * @version 1.0
 * @author James D Bloom *
 * @author Dave Patterson Add fields and methods to handle delay time for
 *         exponentially distributed timed transitions.
 * 
 *         Note: The exponential distribution is based on a single parameter,
 *         which can either be specified as a delay or a rate. The two values
 *         are inverses of each other, so no matter how the parameter is
 *         specified, the code after the constructor is the same. When a timed
 *         transition is found to be enabled, a projected delay is calculated
 *         based on its exponential distribution. As long as the transition does
 *         not get disabled before it can be fired, that delay governs the
 *         timing of the transition's firing. If other timed transitions are
 *         fired, the delay before they fire (that is, the progress of the
 *         virtual clock) is decremented from this transition's delay time to
 *         simulate the progress of time in the virtual space. Thus, at any
 *         time, the next timed transition to fire is the one with the lowest
 *         delay remaining. If a timed transition gets an expected delay and
 *         then later becomes disabled, its delay is no longer valid. When it
 *         next becomes enabled, it will get a new expected delay (an offset
 *         from the current virtual time). This explaination was included to
 *         clarify any confusion about fields and methods with "delay" in their
 *         names. They have nothing to do with whether the parameter of the
 *         exponential distribution is specified as a rate, or an expected
 *         delay. to the
 */
public class Transition extends PlaceTransitionObject {

	/** Transition is of Rectangle2D.Double */
	private GeneralPath transition;
	private Shape proximityTransition;
	/** Place Width */
	public static  final int TRANSITION_HEIGHT = Constants.PLACE_TRANSITION_HEIGHT;
	/** Place Width */
	public static final int TRANSITION_WIDTH = TRANSITION_HEIGHT / 3;
	
	/** Place Width */
	public static  int TRANSITION_WIDTH_NARROW = TRANSITION_HEIGHT / 5;

	private int angle;
	private boolean enabled = false;
	private boolean enabledBackwards = false;
	public boolean highlighted = false;

	private boolean infiniteServer = false;

	/**
	 * The delay before this transition fires.
	 */
	private double delay;

	/**
	 * A boolean to track whether the delay is valid or not.
	 */
	private boolean delayValid;

	/** Is this a timed transition or not? *//*
	private boolean timed = false;*/
	
	/** Is this a timed transition or deterministic? */
	/**
	 * 这里的规则是 timed false 则是瞬时变迁； timed true 并且 deterministic true是确定变迁
	 * 										timed true     deterministic false是随机变迁
	 */
	private boolean  timed = false;
	private boolean  deterministic = false;  

	private static final double rootThreeOverTwo = 0.5 * Math.sqrt(3);

	private ArrayList arcAngleList = new ArrayList();

	/** The transition rate */
	private double rate = 1;

	/** Rate X-axis Offset */
	private Double rateOffsetX = 0.0;

	/** Rate Y-axis Offset */
	private Double rateOffsetY = 24.0;

	/** The transition priority */
	private Integer priority = 1;

	/** Priority X-axis Offset */
	private Double priorityOffsetX = 0.0;

	/** Priority Y-axis Offset */
	private Double priorityOffsetY = 13.0;

	/** The transtion rate parameter */
	private RateParameter rateParameter = null;

	// If this transition belongs to a group
	private GroupTransition groupTransition = null;
	/**
	 * Create Petri-Net Transition object
	 * 
	 * @param positionXInput
	 *            X-axis Position
	 * @param positionYInput
	 *            Y-axis Position
	 * @param idInput
	 *            Transition id
	 * @param nameInput
	 *            Name
	 * @param nameOffsetXInput
	 *            Name X-axis Position
	 * @param nameOffsetYInput
	 *            Name Y-axis Position
	 * @param infServer
	 *            TODO
	 */
	Transition(double positionXInput, double positionYInput,
			String idInput, String nameInput, double nameOffsetXInput,
			double nameOffsetYInput, double rateInput, boolean timedTransition, boolean deterministicTransition,
			boolean infServer, int angleInput, int priority) {
		super(positionXInput, positionYInput, idInput, nameInput,
				nameOffsetXInput, nameOffsetYInput);
 		componentWidth = TRANSITION_HEIGHT; // sets width
		componentHeight = TRANSITION_HEIGHT;// sets height
		rate = rateInput;
		timed = timedTransition;
		deterministic = deterministicTransition; System.out.println(deterministicTransition);
		infiniteServer = infServer;
		constructTransition(!timedTransition);
		angle = 0;
		setCentre((int) positionX, (int) positionY);
		rotate(angleInput);
		updateBounds();
		// this.updateEndPoints();
		this.priority = priority;
		/*if(timedTransition)
		{
			Transition.TRANSITION_WIDTH = Transition.TRANSITION_HEIGHT/6;
		}
		if(!timedTransition)
		{
			Transition.TRANSITION_WIDTH = Transition.TRANSITION_HEIGHT/3;
		}*/
	}

	/**
	 * Create Petri-Net Transition object
	 * 
	 * @param positionXInput
	 *            X-axis Position
	 * @param positionYInput
	 *            Y-axis Position
	 */
	Transition(double positionXInput, double positionYInput, boolean isNarrow) {
		super(positionXInput, positionYInput);
		if(isNarrow)
			componentWidth = TRANSITION_WIDTH_NARROW; // sets width
		else
			componentWidth = TRANSITION_WIDTH;
		componentHeight = TRANSITION_HEIGHT;// sets height
		constructTransition(isNarrow);
		setCentre((int) positionX, (int) positionY);
		updateBounds();
		this.updateEndPoints();
		
	}
	 

	public Transition paste(double x, double y, boolean fromAnotherView,
			DataLayerInterface model) {
		Transition copy = TransitionFactory.createTransition(Grid.getModifiedX(x + this.getX()
				+ Constants.PLACE_TRANSITION_HEIGHT / 2), Grid.getModifiedY(y
		+ this.getY() + Constants.PLACE_TRANSITION_HEIGHT / 2),!timed&&!deterministic);

		String newName = this.pnName.getName() + "(" + this.getCopyNumber()
				+ ")";
		boolean properName = false;

		while (!properName) {
			if (model.checkTransitionIDAvailability(newName) == true) {
				copy.pnName.setName(newName);
				properName = true;
			} else {
				newName = newName + "'";
			}
		}

		this.newCopy(copy);

		copy.nameOffsetX = this.nameOffsetX;
		copy.nameOffsetY = this.nameOffsetY;

		copy.timed = this.timed;
		copy.deterministic = this.deterministic;System.out.println(deterministic);
		copy.rate = this.rate;
		copy.angle = this.angle;

		copy.attributesVisible = this.attributesVisible;
		copy.priority = this.priority;
		copy.transition.transform(AffineTransform.getRotateInstance(Math
				.toRadians(copy.angle), Transition.TRANSITION_HEIGHT / 2,
				Transition.TRANSITION_HEIGHT / 2));
		copy.rateParameter = null;// this.rateParameter;
		return copy;
	}

	public Transition copy() {
		Transition copy = TransitionFactory.createTransition(ZoomController.getUnzoomedValue(this.getX(),
				zoom), ZoomController.getUnzoomedValue(this.getY(), zoom),deterministic);
		copy.pnName.setName(this.getName());
		copy.nameOffsetX = this.nameOffsetX;
		copy.nameOffsetY = this.nameOffsetY;
		copy.timed = this.timed;
		copy.deterministic = this.deterministic;System.out.println(deterministic);
		copy.rate = this.rate;
		copy.angle = this.angle;
		copy.attributesVisible = this.attributesVisible;
		copy.priority = this.priority;
		copy.setOriginal(this);
		copy.rateParameter = this.rateParameter;
		return copy;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (selected && !ignoreSelection) {
			g2.setColor(Constants.SELECTION_FILL_COLOUR);
		} else {
			g2.setColor(Constants.ELEMENT_FILL_COLOUR);
		}

		if (timed) {System.out.println("Transition.java line 256");
			if (infiniteServer) {
				for (int i = 2; i >= 1; i--) {
					g2.translate(2 * i, -2 * i);
					g2.fill(transition);
					Paint pen = g2.getPaint();
					if (highlighted) {
						g2.setPaint(Constants.ENABLED_TRANSITION_COLOUR);
					} else if (selected && !ignoreSelection) {
						g2.setPaint(Constants.SELECTION_LINE_COLOUR);
					} else {
						g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
					}
					g2.draw(transition);
					g2.setPaint(pen);
					g2.translate(-2 * i, 2 * i);
				}
			}
			g2.fill(transition);
		}

		if (highlighted) {
			g2.setPaint(Constants.ENABLED_TRANSITION_COLOUR);
		} else if (selected && !ignoreSelection) {
			g2.setPaint(Constants.SELECTION_LINE_COLOUR);
		} else {
			g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
		}

		g2.draw(transition);
		if (!timed) {
			if (infiniteServer) {
				for (int i = 2; i >= 1; i--) {
					g2.translate(2 * i, -2 * i);
					Paint pen = g2.getPaint();
					g2.setPaint(Constants.ELEMENT_FILL_COLOUR);
					g2.fill(transition);
					g2.setPaint(pen);
					g2.draw(transition);
					g2.translate(-2 * i, 2 * i);
				}
			}
			g2.draw(transition);
			g2.fill(transition);
		}
		if (this.isTimed()) {
			setToolTipText("r = " + this.getRate());
		} else {
			setToolTipText("\u03c0 = " + this.getPriority() + "; w = "
					+ this.getRate());
		}
	}

	/**
	 * Rotates the Transition through the specified angle around the midpoint
	 */
	public UndoableEdit rotate(int angleInc) {
		angle = (angle + angleInc) % 360;
		transition.transform(AffineTransform.getRotateInstance(Math
				.toRadians(angleInc), componentWidth / 2, componentHeight / 2));
		outlineTransition();

		Iterator arcIterator = arcAngleList.iterator();
		while (arcIterator.hasNext()) {
			((ArcAngleCompare) arcIterator.next()).calcAngle();
		}
		Collections.sort(arcAngleList);

		updateEndPoints();
		repaint();

		return new TransitionRotationEdit(this, angleInc);
	}

	private void outlineTransition() {
		proximityTransition = (new BasicStroke(
				Constants.PLACE_TRANSITION_PROXIMITY_RADIUS))
				.createStrokedShape(transition);
	}

	/**
	 * Determines whether Transition is enabled
	 * 
	 * @param animationStatus
	 *            Animation status
	 * @return True if enabled
	 */
	public boolean isEnabled(boolean animationStatus) {
		if(groupTransition != null){
			groupTransition.isEnabled(animationStatus);
		}
		if (animationStatus == true) {
			if (enabled) {
				highlighted = true;
				return true;
			} else {
				highlighted = false;
			}
		}
		return false;
	}

	/**
	 * Determines whether Transition is enabled backwards
	 * 
	 * @return True if enabled
	 */
	public boolean isEnabledBackwards() {
		return enabledBackwards;
	}

	/**
	 * Determines whether Transition is enabled
	 * 
	 * @return True if enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	public void setHighlighted(boolean status) {
		if(groupTransition != null){
			groupTransition.setHighlighted(status);
		}
		highlighted = status;
	}

	public UndoableEdit setInfiniteServer(boolean status) {
		infiniteServer = status;
		repaint();
		return new TransitionServerSemanticEdit(this);
	}

	public boolean isInfiniteServer() {
		return infiniteServer;
	}

	/**
	 * Sets whether Transition is enabled
	 * 
	 * @return enabled if True
	 */
	public void setEnabled(boolean status) {
		if (enabled && !status) { // going from enabled to disabled
			delayValid = false; // mark that delay is not valid
		}
		if(groupTransition != null){
			groupTransition.setEnabled(status);
		}
		enabled = status;
		
	}

	/**
	 * Sets whether Transition is enabled
	 * 
	 * @return enabled if True
	 */
	public void setEnabledBackwards(boolean status) {
		enabledBackwards = status;
		if(groupTransition != null){
			groupTransition.setEnabledBackwards(status);
		}
	}

	/* Called at the end of animation to reset Transitions to false */
	public void setEnabledFalse() {
		enabled = false;
		highlighted = false;
		if(groupTransition != null){
			groupTransition.setEnabled(false);
		}
	}

	public UndoableEdit setRate(double _rate) {
		double oldRate = rate;

		rate = _rate;
		pnName.setText(getAttributes());
		repaint();
		return new TransitionRateEdit(this, oldRate, rate);
	}

	public double getRate() {
		return rate;
	}

	public int getAngle() {
		return angle;
	}

	public int getPriority() {
		return priority;
	}

	/**
	 * Set priority
	 * 
	 * @param newPriority
	 *            Integer value for ...
	 */
	public UndoableEdit setPriority(int newPriority) {
		int oldPriority = priority;

		priority = new Integer(newPriority);
		pnName.setText(getAttributes());
		repaint();
		return new TransitionPriorityEdit(this, oldPriority, priority);
	}

	/** Set the timed transition attribute (for GSPNs) */
	public UndoableEdit setTimed(boolean change) {
		timed = change;
		pnName.setText(getAttributes());
		repaint();
		 
		return new TransitionTimingEdit(this);
	}
	
	/** Set the timed transition attribute (for GSPNs) */
	public UndoableEdit setDeterministic(boolean change) {
		deterministic = change;
		pnName.setText(getAttributes());
		repaint();
		 
		return new TransitionTimingEdit(this);
	}

	/** Get the timed transition attribute (for GSPNs) */
	public boolean isTimed() {
		return timed;
	}
	
	/** Get the timed transition attribute (for GSPNs) */
	public boolean isDeterministic() {
		return deterministic;
	}

	/**
	 * This is a setter for the delay for this transition.
	 * 
	 * @author Dave Patterson as part of the Exponential Distribution support
	 *         for timed transitions.
	 * 
	 * @param _delay
	 *            the time until this transition will fire
	 */
	public void setDelay(double _delay) {
		delay = _delay;
		delayValid = true;
	}

	/**
	 * This is a getter for the delay for this transition.
	 * 
	 * @author Dave Patterson as part of the Exponential Distribution support
	 *         for timed transitions.
	 * 
	 * @return a double with the amount of delay
	 * 
	 */
	public double getDelay() {
		return delay;
	}

	/**
	 * This method is a getter for the boolean indicating if the delay is valid
	 * or not.
	 * 
	 * @author Dave Patterson as part of the Exponential Distribution support
	 *         for timed transitions.
	 * 
	 * @return the delayValid a boolean that is true if the delay is valid, and
	 *         false otherwise
	 */
	public boolean isDelayValid() {
		return delayValid;
	}

	/**
	 * This method is used to set a flag to indicate that the delay is valid or
	 * invalid. (Mainly it is used to invalidate the delay.)
	 * 
	 * @author Dave Patterson as part of the Exponential Distribution support
	 *         for timed transitions.
	 * 
	 * @param _delayValid
	 *            a boolean that is true if the delay is valid, false otherwise
	 * 
	 */
	public void setDelayValid(boolean _delayValid) {
		delayValid = _delayValid;
	}

	private void constructTransition(boolean isNarrow) {
		transition = new GeneralPath();
		if(isNarrow){ 
			transition.append(new Rectangle2D.Double(
					(componentWidth - TRANSITION_WIDTH_NARROW) / 2, 0, TRANSITION_WIDTH_NARROW,
					TRANSITION_HEIGHT), false);
		
		}else{System.out.println(2222);
			transition.append(new Rectangle2D.Double(
					(componentWidth - TRANSITION_WIDTH) / 2, 0, TRANSITION_WIDTH,
					TRANSITION_HEIGHT), false);
		}
		outlineTransition();
	}

	public boolean contains(int x, int y) {
		int zoomPercentage = zoom;

		double unZoomedX = (x - getComponentDrawOffset())
				/ (zoomPercentage / 100.0);
		double unZoomedY = (y - getComponentDrawOffset())
				/ (zoomPercentage / 100.0);

		someArc = CreateGui.getView().createArc;
		if (someArc != null) { // Must be drawing a new Arc if non-NULL.
			if ((proximityTransition.contains((int) unZoomedX, (int) unZoomedY) || transition
					.contains((int) unZoomedX, (int) unZoomedY))
					&& areNotSameType(someArc.getSource())) {
				// assume we are only snapping the target...
				if (someArc.getTarget() != this) {
					someArc.setTarget(this);
				}
				someArc.updateArcPosition();
				return true;
			} else {
				if (someArc.getTarget() == this) {
					someArc.setTarget(null);
					removeArcCompareObject(someArc);
					updateConnected();
				}
				return false;
			}
		} else {
			return transition.contains((int) unZoomedX, (int) unZoomedY);
		}
	}

	public void removeArcCompareObject(Arc a) {
		Iterator arcIterator = arcAngleList.iterator();
		while (arcIterator.hasNext()) {
			if (((ArcAngleCompare) arcIterator.next()).arc == a) {
				arcIterator.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pipe.dataLayer.PlaceTransitionObject#updateEndPoint(pipe.dataLayer.Arc)
	 */
	public void updateEndPoint(Arc arc) {
		boolean match = false;

		Iterator arcIterator = arcAngleList.iterator();
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			if (thisArc.arc == arc || !arc.inView()) {
				thisArc.calcAngle();
				match = true;
				break;
			}
		}

		if (!match) {
			arcAngleList.add(new ArcAngleCompare(arc, this));
		}

		Collections.sort(arcAngleList);
		updateEndPoints();
	}

	public void updateEndPoints() {
		ArrayList top = new ArrayList();
		ArrayList bottom = new ArrayList();
		ArrayList left = new ArrayList();
		ArrayList right = new ArrayList();

		Iterator arcIterator = arcAngleList.iterator();
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			double thisAngle = thisArc.angle - Math.toRadians(angle);
			if (Math.cos(thisAngle) > (rootThreeOverTwo)) {
				top.add(thisArc);
				thisArc.arc.setPathToTransitionAngle(angle + 90);
			} else if (Math.cos(thisAngle) < -rootThreeOverTwo) {
				bottom.add(thisArc);
				thisArc.arc.setPathToTransitionAngle(angle + 270);
			} else if (Math.sin(thisAngle) > 0) {
				left.add(thisArc);
				thisArc.arc.setPathToTransitionAngle(angle + 180);
			} else {
				right.add(thisArc);
				thisArc.arc.setPathToTransitionAngle(angle);
			}
		}

		AffineTransform transform = AffineTransform.getRotateInstance(Math
				.toRadians(angle + Math.PI));
		Point2D.Double transformed = new Point2D.Double();

		transform.concatenate(ZoomController.getTransform(zoom));

		arcIterator = top.iterator();
		transform.transform(new Point2D.Double(1, 0.5 * TRANSITION_HEIGHT),
				transformed); // +1 due to rounding making it off by 1
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			if (thisArc.sourceOrTarget()) {
				thisArc.arc.setTargetLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			} else {
				thisArc.arc.setSourceLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			}
		}

		arcIterator = bottom.iterator();
		transform.transform(new Point2D.Double(0, -0.5 * TRANSITION_HEIGHT),
				transformed);
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			if (thisArc.sourceOrTarget()) {
				thisArc.arc.setTargetLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			} else {
				thisArc.arc.setSourceLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			}
		}

		arcIterator = left.iterator();
		double inc = TRANSITION_HEIGHT / (left.size() + 1);
		double current = TRANSITION_HEIGHT / 2 - inc;
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			transform.transform(new Point2D.Double(-0.5 * TRANSITION_WIDTH,
					current + 1), transformed); // +1 due to rounding making it
			// off by 1
			if (thisArc.sourceOrTarget()) {
				thisArc.arc.setTargetLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			} else {
				thisArc.arc.setSourceLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			}
			current -= inc;
		}

		inc = TRANSITION_HEIGHT / (right.size() + 1);
		current = -TRANSITION_HEIGHT / 2 + inc;
		arcIterator = right.iterator();
		while (arcIterator.hasNext()) {
			ArcAngleCompare thisArc = (ArcAngleCompare) arcIterator.next();
			transform.transform(new Point2D.Double(+0.5 * TRANSITION_WIDTH,
					current), transformed);
			if (thisArc.sourceOrTarget()) {
				thisArc.arc.setTargetLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			} else {
				thisArc.arc.setSourceLocation(positionX + centreOffsetLeft()
						+ transformed.x, positionY + centreOffsetTop()
						+ transformed.y);
			}
			current += inc;
		}
	}

	public void addedToGui() {
		super.addedToGui();
		update();
	}

	private String getAttributes() { // NOU-PERE
		if (attributesVisible == true) {
			if (isTimed()) {
				if (rateParameter != null) {
					return "\nr=" + rateParameter.getName();
				} else {
					return "\nr=" + rate;
				}
			} else {
				if (rateParameter != null) {
					return "\n" + '\u03c0' + "=" + priority + "\nw="
							+ rateParameter.getName();
				} else {
					return "\n" + '\u03c0' + "=" + priority + "\nw=" + rate;
				}
			}
		}
		return "";
	}

	public void setCentre(double x, double y) {
		super.setCentre(x, y);
		update();
	}

	public void toggleAttributesVisible() {
		attributesVisible = !attributesVisible;
		pnName.setText(getAttributes());
	}

	public void showEditor() {
		// Build interface
		EscapableDialog guiDialog = new EscapableDialog(CreateGui.getApp(),
				"AMAP", true);

		TransitionEditorPanel te = new TransitionEditorPanel(guiDialog
				.getRootPane(), this, CreateGui.getModel(), CreateGui.getView());

		guiDialog.add(te);

		guiDialog.getRootPane().setDefaultButton(null);

		guiDialog.setResizable(false);

		// Make window fit contents' preferred size
		guiDialog.pack();

		// Move window to the middle of the screen
		guiDialog.setLocationRelativeTo(null);

		guiDialog.setVisible(true);

		guiDialog.dispose();
	}

	public RateParameter getRateParameter() {
		return rateParameter;
	}

	public UndoableEdit setRateParameter(RateParameter _rateParameter) {
		double oldRate = rate;
		rateParameter = _rateParameter;
		rateParameter.add(this);
		rate = rateParameter.getValue();
		update();
		return new SetRateParameterEdit(this, oldRate, rateParameter);
	}

	public UndoableEdit clearRateParameter() {
		RateParameter oldRateParameter = rateParameter;
		rateParameter.remove(this);
		rateParameter = null;
		update();
		return new ClearRateParameterEdit(this, oldRateParameter);
	}

	public UndoableEdit changeRateParameter(RateParameter _rateParameter) {
		RateParameter oldRateParameter = rateParameter;
		rateParameter.remove(this);
		rateParameter = _rateParameter;
		rateParameter.add(this);
		rate = rateParameter.getValue();
		update();
		return new ChangeRateParameterEdit(this, oldRateParameter,
				rateParameter);
	}

	public void update() {
		pnName.setText(getAttributes());
		pnName.zoomUpdate(zoom);
		super.update();
		this.repaint();
	}

	public void delete() {
		if (rateParameter != null) {
			rateParameter.remove(this);
			rateParameter = null;
		}
		super.delete();
	}

	class ArcAngleCompare implements Comparable {

		private final static boolean SOURCE = false;
		private final static boolean TARGET = true;
		private Arc arc;
		private Transition transition;
		private double angle;

		public ArcAngleCompare(Arc _arc, Transition _transition) {
			arc = _arc;
			transition = _transition;
			calcAngle();
		}

		public int compareTo(Object arg0) {
			double angle2 = ((ArcAngleCompare) arg0).angle;

			return (angle < angle2 ? -1 : (angle == angle2 ? 0 : 1));
		}

		private void calcAngle() {
			int index = sourceOrTarget() ? arc.getArcPath().getEndIndex() - 1
					: 1;
			Point2D.Double p1 = new Point2D.Double(positionX
					+ centreOffsetLeft(), positionY + centreOffsetTop());
			Point2D.Double p2 = new Point2D.Double(arc.getArcPath().getPoint(
					index).x, arc.getArcPath().getPoint(index).y);

			if (p1.y <= p2.y) {
				angle = Math.atan((p1.x - p2.x) / (p2.y - p1.y));
			} else {
				angle = Math.atan((p1.x - p2.x) / (p2.y - p1.y)) + Math.PI;
			}

			// This makes sure the angle overlap lies at the intersection
			// between
			// edges of a transition
			// Yes it is a nasty hack (a.k.a. ingeneous solution). But it works!
			if (angle < (Math.toRadians(30 + transition.getAngle()))) {
				angle += (2 * Math.PI);
			}

			// Needed to eliminate an exception on Windows
			if (p1.equals(p2)) {
				angle = 0;
			}

		}

		private boolean sourceOrTarget() {
			return (arc.getSource() == transition ? SOURCE : TARGET);
		}

	}
	public void bindToGroup(GroupTransition _groupTransition){
		groupTransition = _groupTransition;
	}
	
	public boolean isGrouped(){
		return groupTransition != null;
	}
	
	public GroupTransition getGroup(){
		return groupTransition;
	}
	
	public void ungroupTransition(){
		groupTransition=null;
	}
	
	private ArrayList<Transition> groupTransitionsValidation(){
		if (!this.isSelected()) {
			JOptionPane.showMessageDialog(null,
					"You can only choose this option on selected transitions",
					"Invalid selection", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		GuiView view = CreateGui.getView();
		// Get all objects currently in model
		ArrayList<PetriNetObject> pnObjects = view.getPNObjects();
		// Any transitions that will be grouped are added in here
		ArrayList<Transition> transitionsToHide = new ArrayList<Transition>();

		// A list containing all input places of this transition
		// This list will be used to ensure that all other selected
		// transitions have the same input places
		ArrayList<Place> thisOutputPlaces = new ArrayList<Place>();
		Iterator thisArcsFrom = this.getArcsFrom();
		Arc tempArc;
		while (thisArcsFrom.hasNext()) {
			tempArc = (Arc)thisArcsFrom.next();
			thisOutputPlaces.add((Place) (tempArc.getTarget()));
		}

		// A list containing all output places of this transition
		// This list will be used to ensure that all other selected
		// transitions have the same output places
		ArrayList<Place> thisInputPlaces = new ArrayList<Place>();
		Iterator thisArcsTo = this.getArcsTo();
		while (thisArcsTo.hasNext()) {
			tempArc = (Arc)thisArcsTo.next();
			thisInputPlaces.add((Place) (tempArc.getSource()));
		}
		
		// These lists will contain the input and output places of the pnObject in the loop below
		ArrayList<Place> currentOutputPlaces;
		ArrayList<Place> currentInputPlaces;

		for (int i = 0; i < pnObjects.size(); i++) {
			// Is this object currently selected on the canvas
			if (pnObjects.get(i).isSelected()) {
				pnObjects.get(i).deselect();
				if (pnObjects.get(i) instanceof Transition) {
					// If this is the current transition then there is no need ot check that 
					// its input and output places are the same
					if (this != ((Transition) pnObjects.get(i))) {
						// Gather this pnObject's input places and compare them with the input places of this
						currentOutputPlaces = new ArrayList<Place>();
						Iterator arcOutputIterator = ((Transition) pnObjects
								.get(i)).getArcsFrom();
						while (arcOutputIterator.hasNext()) {
							tempArc = (Arc)arcOutputIterator.next();
							currentOutputPlaces.add((Place) (tempArc.getTarget()));
						}
						if (!thisOutputPlaces.equals(currentOutputPlaces)) {
							JOptionPane
									.showMessageDialog(
											null,
											"In order to be grouped, selected transitions must have the same output places",
											"Invalid selection",
											JOptionPane.ERROR_MESSAGE);
							return null;
						}
						// Gather this pnObject's output places and compare them with the input places of this
						currentInputPlaces = new ArrayList<Place>();
						Iterator arcInputIterator = ((Transition) pnObjects
								.get(i)).getArcsTo();
						while (arcInputIterator.hasNext()) {
							tempArc = (Arc)arcInputIterator.next();
							currentInputPlaces.add((Place) (tempArc.getSource()));
						}
						if (!thisInputPlaces.equals(currentInputPlaces)) {
							JOptionPane
									.showMessageDialog(
											null,
											"In order to be grouped, selected transitions must have the same input places",
											"Invalid selection",
											JOptionPane.ERROR_MESSAGE);
							return null;
						}
					}
					transitionsToHide.add(((Transition) pnObjects
							.get(i)));
				}
			}
		}

		if(transitionsToHide.size() < 2){
			JOptionPane.showMessageDialog(
					null,
					"Please select 2 or more transitions to group",
					"Invalid selection",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return transitionsToHide;
	}
	public UndoableEdit groupTransitions() {
		// Ensure that selected transitions can be grouped
		ArrayList<Transition> transitionsToHide = groupTransitionsValidation();
		GroupTransition newGroupTransition = new GroupTransition(this, 
				this.getPositionX(), this.getPositionY());
		groupTransitionsHelper(transitionsToHide, newGroupTransition);
		return new GroupTransitionEdit(newGroupTransition);
	}
	
	public void groupTransitionsHelper(ArrayList<Transition> transitionsToHide, 
			GroupTransition newGroupTransition){

		// Check if validation failed
		if(transitionsToHide == null){
			return;
		}
		
		GuiView view = CreateGui.getView();
		DataLayerInterface model = CreateGui.getModel();

		// Create a new grouped transition
		/*Transition newTransition = (Transition)this.clone();*/
		//Transition newGroupTransition = new Transition(this.getPositionX(), this
		//		.getPositionY());
		
		// Get all objects currently in model
		
		Arc tempArc;
		//Hide selected transitions and add them to the group transition created above
		int i = 0;
		for (Transition transitionToGroup:transitionsToHide) {
			transitionToGroup.hideFromCanvas();
			// Hide all associated arcs
			transitionToGroup.hideAssociatedArcs();
			transitionToGroup.bindToGroup(newGroupTransition);
			newGroupTransition.addTransition(transitionToGroup);
			if(i == 0){
				newGroupTransition.setName(transitionToGroup.getName());
			}
			else{
				newGroupTransition.setName(newGroupTransition.getName() + "_" + 
						transitionToGroup.getName());
			}
			i++;
		}
		
		// Add new input arcs to our new Grouped transition
		Iterator arcsTo = this.getArcsTo();
		while (arcsTo.hasNext()) {
			tempArc = (Arc)(Arc)arcsTo.next();
			
			Arc newArc = ArcFactory.createNormalArc(tempArc.getStartPositionX(), tempArc.getStartPositionY(), tempArc.getArcPath().getPoint(1).getX(),
					tempArc.getArcPath().getPoint(1).getY(), tempArc.getSource(), newGroupTransition, new LinkedList<Marking>(), "", false);
			newGroupTransition.addConnectTo(newArc);
			tempArc.getSource().addConnectFrom(newArc);
			newArc.addToView(view);
		}
		// Add new output arcs to our new Grouped transition
		Iterator arcsFrom = this.getArcsFrom();
		while (arcsFrom.hasNext()) {
			tempArc = (Arc)(Arc)arcsFrom.next();
			Arc newArc = ArcFactory.createNormalArc(tempArc.getStartPositionX(), tempArc.getStartPositionY(), tempArc.getArcPath().getPoint(1).getX(),
					tempArc.getArcPath().getPoint(1).getY(), newGroupTransition, tempArc.getTarget(), new LinkedList<Marking>(), "", false);
			newGroupTransition.addConnectFrom(newArc);
			tempArc.getTarget().addConnectTo(newArc);
			newArc.addToView(view);
		}
		newGroupTransition.setVisible(true);
		newGroupTransition.getNameLabel().setVisible(true);
		view.addNewPetriNetObject(newGroupTransition);
		model.addPetriNetObject(newGroupTransition);
		//newTransition.getArcPath().forceHidePoints();
		newGroupTransition.repaint();
	}

	public void hideFromCanvas() {
		this.setVisible(false);
		this.getNameLabel().setVisible(false);
		/*				pnObjects.get(posOfTransition).setEnabled(false);
		pnObjects.get(posOfTransition).setIgnoreRepaint(true);
		pnObjects.get(posOfTransition).setDraggable(false);
		pnObjects.get(posOfTransition).setFocusable(false);
		pnObjects.get(posOfTransition).setFocusTraversalPolicyProvider(false);
		pnObjects.get(posOfTransition).setFocusCycleRoot(false);
		pnObjects.get(posOfTransition).setFocusTraversalKeysEnabled(false);
		pnObjects.get(posOfTransition).setIgnoreRepaint(true);
		pnObjects.get(posOfTransition).setOpaque(true);
		pnObjects.get(posOfTransition).setRequestFocusEnabled(false);
		pnObjects.get(posOfTransition).setSelectable(false);
		pnObjects.get(posOfTransition).setVerifyInputWhenFocusTarget(false);*/
	}
	
	public void unhideFromCanvas() {
		this.setVisible(true);
		this.getNameLabel().setVisible(true);
	}
	
	
	public void hideAssociatedArcs(){
		// Hide all output arcs
		Iterator arcOutputIterator = this.getArcsFrom();
		Arc tempArc;
		while (arcOutputIterator.hasNext()) {
			tempArc = (NormalArc)arcOutputIterator.next();
			tempArc.removeFromView();
		}
		// Hide all input arcs
		Iterator arcInputIterator = this.getArcsTo();
		while (arcInputIterator.hasNext()) {
			tempArc = (NormalArc)arcInputIterator.next();
			tempArc.removeFromView();
		}
	}
	
	public void showAssociatedArcs(){
		GuiView view = CreateGui.getView();
		Arc tempArc;
		// Show all output arcs
		Iterator arcOutputIterator = this.getArcsFrom();
		while (arcOutputIterator.hasNext()) {
			tempArc = (NormalArc)arcOutputIterator.next();
			tempArc.addToView(view);
		}
		// Show all input arcs
		Iterator arcInputIterator = this.getArcsTo();
		while (arcInputIterator.hasNext()) {
			tempArc = (NormalArc)arcInputIterator.next();
			tempArc.addToView(view);
		}
	}

}
