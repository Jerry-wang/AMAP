package pipe.common.dataLayer;

import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import javax.swing.BoxLayout;

import pipe.gui.CreateGui;
import pipe.gui.Constants;
import pipe.gui.Grid;
import pipe.gui.undo.UndoableEdit;
import pipe.gui.undo.PlaceCapacityEdit;
import pipe.gui.undo.PlaceMarkingEdit;
import pipe.gui.widgets.PlaceEditorPanel;
import pipe.gui.ZoomController;
import pipe.gui.widgets.EscapableDialog;

/**
 * <b>Place</b> - Petri-Net Place Class
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
 * @author Edwin Chung corresponding states of matrixes has been set to change
 *         when markings are altered. Users will be prompted to save their work
 *         when the markings of places are altered. (6th Feb 2007)
 * 
 * @author Edwin Chung 16 Mar 2007: modified the constructor and several other
 *         functions so that DataLayer objects can be created outside the GUI
 */
public class Place extends PlaceTransitionObject {

	private boolean					taggedToken			= false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String type = "Place";
	/** Initial Marking */
	private LinkedList<Marking> initialMarking = new LinkedList<Marking>();
	/** Current Marking */
	private LinkedList<Marking> currentMarking = new LinkedList<Marking>();

	/** Total Marking */
	private Integer totalMarking = 0;

	/** Initial Marking X-axis Offset */
	private Double markingOffsetX = 0d;

	/** Initial Marking Y-axis Offset */
	private Double markingOffsetY = 0d;

	/** Value of the capacity restriction; 0 means no capacity restriction */
	private Integer capacity = 0;

	public static final int DIAMETER = Constants.PLACE_TRANSITION_HEIGHT;

	/** Token Width */
	public static int tWidth = 4;

	/** Token Height */
	public static int tHeight = 4;

	/** Ellipse2D.Double place */
	private static Ellipse2D.Double place = new Ellipse2D.Double(0, 0,
			DIAMETER, DIAMETER);
	private static Shape proximityPlace = (new BasicStroke(
			Constants.PLACE_TRANSITION_PROXIMITY_RADIUS))
			.createStrokedShape(place);

	private TokenClass activeTokenClass;

	/**
	 * Create Petri-Net Place object
	 * 
	 * @param positionXInput
	 *            X-axis Position
	 * @param positionYInput
	 *            Y-axis Position
	 * @param idInput
	 *            Place id
	 * @param nameInput
	 *            Name
	 * @param nameOffsetXInput
	 *            Name X-axis Position
	 * @param nameOffsetYInput
	 *            Name Y-axis Position
	 * @param initialMarkingInput
	 *            Initial Marking
	 * @param markingOffsetXInput
	 *            Marking X-axis Position
	 * @param markingOffsetYInput
	 *            Marking Y-axis Position
	 * @param capacityInput
	 *            Capacity
	 */
	Place(
				double positionXInput,
				double positionYInput, 
				String idInput, 
				String nameInput,
				double nameOffsetXInput, 
				double nameOffsetYInput,
				LinkedList<Marking> initialMarkingInput,
				double markingOffsetXInput, 
				double markingOffsetYInput,
				int capacityInput) 
	
		{
			super(
					
				positionXInput, 
				positionYInput, 
				idInput, 
				nameInput,
				nameOffsetXInput, 
				nameOffsetYInput);
				initialMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(initialMarkingInput);
				currentMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(initialMarkingInput);
				totalMarking = getTotalMarking();
				markingOffsetX = new Double(markingOffsetXInput);
				markingOffsetY = new Double(markingOffsetYInput);
				componentWidth = DIAMETER;
				componentHeight = DIAMETER;
				setCapacity(capacityInput);
				setCentre((int) positionX, (int) positionY);
		// updateBounds();
	}

	// New Constructor to support tagged net creation from PNML file
	Place(
			double positionXInput,
			double positionYInput, 
			String idInput, 
			String nameInput,
			double nameOffsetXInput, 
			double nameOffsetYInput,
			LinkedList<Marking> initialMarkingInput,
			double markingOffsetXInput, 
			double markingOffsetYInput,
			int capacityInput,
			boolean tagged) 

	{
		super(
				
			positionXInput, 
			positionYInput, 
			idInput, 
			nameInput,
			nameOffsetXInput, 
			nameOffsetYInput);
			initialMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(initialMarkingInput);
			currentMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(initialMarkingInput);
			totalMarking = getTotalMarking();
			markingOffsetX = new Double(markingOffsetXInput);
			markingOffsetY = new Double(markingOffsetYInput);
			componentWidth = DIAMETER;
			componentHeight = DIAMETER;
			setCapacity(capacityInput);
			setCentre((int) positionX, (int) positionY);
			taggedToken = tagged;
			
	// updateBounds();
}
	
	/**
	 * Create Petri-Net Place object returns the position of the element in the
	 * list "markings" where the token class with this ID is.
	 */
	private int getMarkingListPos(String id, LinkedList<Marking> markings) {
		int size = markings.size();
		for (int i = 0; i < size; i++) {
			if (markings.get(i).getTokenClass().getID().equals(id)) {
				return i;
			}
		}
		// marking with such an ID does not exist
		return -1;
	}

	/**
	 * Used to quickly calculate the total marking of this place and sums up the
	 * marking of each token class.
	 */
	public int getTotalMarking() {
		int size = currentMarking.size();
		int totalMarking = 0;
		for (int i = 0; i < size; i++)
			totalMarking += currentMarking.get(i).getCurrentMarking();
		return totalMarking;
	}

	public TokenClass getActiveTokenClass() {
		return activeTokenClass;
	}

	public void setActiveTokenClass(TokenClass tokenclass) {
		this.activeTokenClass = tokenclass;
		// If no marking has been created with this token class
		int markingListPos = getMarkingListPos(tokenclass.getID(),
				currentMarking);
		if (markingListPos == -1) {
			Marking m = new Marking(tokenclass);
			m.setCurrentMarking(0);
			currentMarking.add(m);
		}
	}

	/**
	 * Create Petri-Net Place object
	 * 
	 * @param positionXInput
	 *            X-axis Position
	 * @param positionYInput
	 *            Y-axis Position
	 */
	Place(double positionXInput, double positionYInput) {
		super(positionXInput, positionYInput);
		componentWidth = DIAMETER;
		componentHeight = DIAMETER;
		setCentre((int) positionX, (int) positionY);
		// updateBounds();
	}

	public Place paste(double x, double y, boolean fromAnotherView,
			DataLayerInterface model) {
		Place copy = PlaceFactory.createPlace(Grid.getModifiedX(x + this.getX()
				+ Constants.PLACE_TRANSITION_HEIGHT / 2), Grid.getModifiedY(y
		+ this.getY() + Constants.PLACE_TRANSITION_HEIGHT / 2));

		String newName = this.pnName.getName() + "(" + this.getCopyNumber()
				+ ")";
		boolean properName = false;

		while (!properName) {
			if (model.checkPlaceIDAvailability(newName) == true) {
				copy.pnName.setName(newName);
				properName = true;
			} else {
				newName = newName + "'";
			}
		}
		this.newCopy(copy);
		copy.nameOffsetX = this.nameOffsetX;
		copy.nameOffsetY = this.nameOffsetY;
		copy.capacity = this.capacity;
		copy.attributesVisible = this.attributesVisible;
		copy.initialMarking = ObjectDeepCopier.mediumCopy(this.initialMarking);
		copy.totalMarking = this.totalMarking;
		copy.markingOffsetX = this.markingOffsetX;
		copy.markingOffsetY = this.markingOffsetY;
		copy.update();
		return copy;
	}

	public Place copy() {
		Place copy = PlaceFactory.createPlace(ZoomController.getUnzoomedValue(this.getX(), zoom), ZoomController.getUnzoomedValue(this.getY(), zoom));
		copy.pnName.setName(this.getName());
		copy.nameOffsetX = this.nameOffsetX;
		copy.nameOffsetY = this.nameOffsetY;
		copy.capacity = this.capacity;
		copy.attributesVisible = this.attributesVisible;
		copy.initialMarking = ObjectDeepCopier.mediumCopy(this.initialMarking);
		copy.totalMarking = this.totalMarking;
		copy.markingOffsetX = this.markingOffsetX;
		copy.markingOffsetY = this.markingOffsetY;
		copy.setOriginal(this);
		return copy;
	}

	/**
	 * Paints the Place component taking into account the number of tokens from
	 * the currentMarking
	 * 
	 * @param g
	 *            The Graphics object onto which the Place is drawn.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		Insets insets = getInsets();
		int x = insets.left;
		int y = insets.top;

		if (hasCapacity()) {
			g2.setStroke(new BasicStroke(2.0f));
			setToolTipText("k = " + this.getCapacity());
		} else {
			g2.setStroke(new BasicStroke(1.0f));
			setToolTipText("k = \u221E");
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (selected && !ignoreSelection) {
			g2.setColor(Constants.SELECTION_FILL_COLOUR);
		} else {
			g2.setColor(Constants.ELEMENT_FILL_COLOUR);
		}
		g2.fill(place);

		if (selected && !ignoreSelection) {
			g2.setPaint(Constants.SELECTION_LINE_COLOUR);
		} else {
			g2.setPaint(Constants.ELEMENT_LINE_COLOUR);
		}
		g2.draw(place);

		g2.setStroke(new BasicStroke(1.0f));

		// Paints border round a tagged place - paint component is called after any action on the place, so this bit
		// of code doesn't have to be called specially
		
		if (this.isTagged())
		{
			final AffineTransform oldTransform = g2.getTransform();

			final AffineTransform scaleTransform = new AffineTransform();
			scaleTransform.setToScale(1.2, 1.2);

			g2.transform(scaleTransform);

			g2.translate(-2, -2);

			g2.fill(Place.place);
			// g2.draw(place);

			g2.translate(2, 2);

			g2.setTransform(oldTransform);
			
			
			//draw one tagged token
			//g2.setColor(Color.red);
			//g2.drawOval(x + 12, y + 13, Place.tWidth, Place.tHeight);		
			//g2.fillOval(x + 12, y + 13, Place.tWidth, Place.tHeight);
			//g2.setColor(Color.black);
			//g2.setTransform(saveXform);
			
			
		}
		
		
		int tempTotalMarking = getTotalMarking();

		// Markings do not fit in the place and will be displayed as numbers
		// The number for each token class is displayed below one another.
		if (tempTotalMarking > 5) {
			int i = 0;
			for (Marking m : currentMarking) {
				int currTokenClassMarking = m.getCurrentMarking();
				g.setColor(m.getTokenClass().getColour());
				if (currTokenClassMarking > 999) {
					g.drawString(String.valueOf(currTokenClassMarking), x, y
							+ 10 + i);
				} else if (currTokenClassMarking > 99) {
					g.drawString(String.valueOf(currTokenClassMarking), x + 3,
							y + 10 + i);
				} else if (currTokenClassMarking > 9) {
					g.drawString(String.valueOf(currTokenClassMarking), x + 7,
							y + 10 + i);
				} else if (currTokenClassMarking != 0) {
					g.drawString(String.valueOf(currTokenClassMarking), x + 12,
							y + 10 + i);
				}
				if (currTokenClassMarking != 0)
					i += 10;
			}
		} else {
			for (Marking m : currentMarking) {
				int currTokenClassMarking = m.getCurrentMarking();
				g.setColor(m.getTokenClass().getColour());
				for (int i = 0; i < currTokenClassMarking; i++) {

					switch (tempTotalMarking) {
					case 5:
						g.drawOval(x + 6, y + 6, tWidth, tHeight);
						g.fillOval(x + 6, y + 6, tWidth, tHeight);
						break;
					case 4:
						g.drawOval(x + 18, y + 20, tWidth, tHeight);
						g.fillOval(x + 18, y + 20, tWidth, tHeight);
						break;
					case 3:
						g.drawOval(x + 6, y + 20, tWidth, tHeight);
						g.fillOval(x + 6, y + 20, tWidth, tHeight);
						break;
					case 2:
						g.drawOval(x + 18, y + 6, tWidth, tHeight);
						g.fillOval(x + 18, y + 6, tWidth, tHeight);
						break;
					case 1:
						g.drawOval(x + 12, y + 13, tWidth, tHeight);
						g.fillOval(x + 12, y + 13, tWidth, tHeight);
						break;
					case 0:
						break;
					default:
						break;
					}
					tempTotalMarking--;
				}
			}
		}
	}

	/**
	 * Set current marking
	 * 
	 * @param currentMarkingInput
	 *            Linked List for current marking of all token classes
	 */
	public UndoableEdit setCurrentMarking(
		LinkedList<Marking> currentMarkingInput) {
	    int totalMarking = 0;
	    for(Marking inputtedMarking:currentMarkingInput){
	    	totalMarking += inputtedMarking.getCurrentMarking();
	    }
	    // If total marking exceeds capacity then leave the marking as is
	    if(capacity !=0 && totalMarking > capacity){
			return new PlaceMarkingEdit(this, currentMarking, currentMarking);
	    }
		// Must clone because we then change currentMarking below and we do not
		// want
		// oldMarking to be changed too
		LinkedList<Marking> oldMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(currentMarking);
	
		// if a marking for a specific class that existed before does not exist
		// in
		// the new input then this place must release the lock for that token
		// class
		// to allow it to be edited. Also if a previously positive marking is
		// now
		// 0 then the same should happen.
		for (Marking m : currentMarking) {
			int newMarkingPos = getMarkingListPos(m.getTokenClass().getID(),
					currentMarkingInput);
			if ((newMarkingPos == -1)
					|| (currentMarkingInput.get(newMarkingPos)
							.getCurrentMarking() == 0 && m.getCurrentMarking() != 0)) {
				CreateGui.getModel().unlockTokenClass(m.getTokenClass().getID());
			}
		}
		// if a marking for a specific class that didnt exist before is in
		// the new input then this place must acquire the lock for that token
		// class
		// to avoid it from being edited. Also if a now positive marking was
		// previously
		// 0 then the same should happen.
		for (Marking m : currentMarkingInput) {
			int oldMarkingPos = getMarkingListPos(m.getTokenClass().getID(),
					currentMarking);
			if ((oldMarkingPos == -1 && m.getCurrentMarking() > 0)
					|| (currentMarking.get(oldMarkingPos).getCurrentMarking() == 0 && m
							.getCurrentMarking() != 0)) {
				CreateGui.getModel().lockTokenClass(m.getTokenClass().getID());
			}
			// Now update the current marking if such a marking exists, otherwise create a new one
			if(oldMarkingPos == -1){
				currentMarking.add(m);
			}
			else{
				currentMarking.get(oldMarkingPos).setCurrentMarking(m.getCurrentMarking());
			}
		}

		/*
		 * USEFUL FOR DEBUGGING
		 * 
		 * System.out.println("Total is now: " + this.currentMarking); for(int i
		 * = 0; i< markings.size(); i++){ System.out.println("Item " + i +
		 * " in markings has ID: " + markings.get(i).getTokenClass().getID() +
		 * " and marking: " + markings.get(i).getCurrentMarking()); }
		 */
		// First of all, each place should have a marking for every token class
		// that exists, even if that tokenClass is 0. To make sure of this we 
		// shall iterate through all enabled tokenClasses and if such a marking
		// does not exist in the current marking then we must create a new marking
		// set to 0.
		LinkedList<TokenClass> tokenClasses = CreateGui.getModel().getTokenClasses();
		for(TokenClass tc:tokenClasses){
			if(tc.isEnabled()){
				if(getMarkingListPos(tc.getID(), currentMarking) == -1){
					Marking m = new Marking(tc, 0);
					currentMarking.add(m);
				}
			}
		}
		repaint();
		LinkedList<Marking> newMarking = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(currentMarking);
		return new PlaceMarkingEdit(this, oldMarking, newMarking);
	}

	/**
	 * Set capacity This method doesn't check if marking fulfilles current
	 * capacity restriction
	 * 
	 * @param newCapacity
	 *            Integer value for capacity restriction
	 */
	public UndoableEdit setCapacity(int newCapacity) {
		int oldCapacity = capacity;

		if (capacity != newCapacity) {
			capacity = newCapacity;
			update();
		}
		return new PlaceCapacityEdit(this, oldCapacity, newCapacity);
	}

	/**
	 * Get initial marking
	 * 
	 * @return LinkedList<Marking> which contains the markings for each
	 *         individual token class
	 */
	public LinkedList<Marking> getInitialMarking() {
	      return initialMarking;
	}

	/**
	 * Get current marking
	 * 
	 * @return LinkedList<Marking> which contains the markings for each
	 *         individual token class
	 */
	public LinkedList<Marking> getCurrentMarking() {
		return currentMarking;
	}

	/**
	 * Get current capacity
	 * 
	 * @return Integer value for current capacity
	 */
	public int getCapacity() {
		return ((capacity == null) ? 0 : capacity.intValue());
	}

	/**
	 * Get current marking
	 * 
	 * @return LinkedList<Marking> which contains the markings for each
	 *         individual token class
	 */
	public LinkedList<Marking> getCurrentMarkingObject() {
		return currentMarking;
	}

	/**
	 * Get X-axis offset for initial marking
	 * 
	 * @return Double value for X-axis offset of initial marking
	 */
	public Double getMarkingOffsetXObject() {
		return markingOffsetX;
	}

	/**
	 * Get Y-axis offset for initial marking
	 * 
	 * @return Double value for X-axis offset of initial marking
	 */
	public Double getMarkingOffsetYObject() {
		return markingOffsetY;
	}

	/**
	 * Returns the diameter of this Place at the current zoom
	 */
	private int getDiameter() {
		return (int) (ZoomController.getZoomedValue(DIAMETER, zoom));
	}

	public boolean contains(int x, int y) {
		double unZoomedX = ZoomController.getUnzoomedValue(x - getComponentDrawOffset(),
				zoom);
		double unZoomedY = ZoomController.getUnzoomedValue(y - getComponentDrawOffset(),
				zoom);

		someArc = CreateGui.getView().createArc;
		if (someArc != null) { // Must be drawing a new Arc if non-NULL.
			if ((proximityPlace.contains((int) unZoomedX, (int) unZoomedY) || place
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
					updateConnected();
				}
				return false;
			}
		} else {
			return place.contains((int) unZoomedX, (int) unZoomedY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pipe.dataLayer.PlaceTransitionObject#updateEndPoint(pipe.dataLayer.Arc)
	 */
	public void updateEndPoint(Arc arc) {
		if (arc.getSource() == this) {
			// Make it calculate the angle from the centre of the place rather
			// than
			// the current start point
			arc.setSourceLocation(positionX + (getDiameter() * 0.5), positionY
					+ (getDiameter() * 0.5));
			double angle = arc.getArcPath().getStartAngle();
			arc.setSourceLocation(positionX + centreOffsetLeft()
					- (0.5 * getDiameter() * (Math.sin(angle))), positionY
					+ centreOffsetTop()
					+ (0.5 * getDiameter() * (Math.cos(angle))));
		} else {
			// Make it calculate the angle from the centre of the place rather
			// than the current target point
			arc.setTargetLocation(positionX + (getDiameter() * 0.5), positionY
					+ (getDiameter() * 0.5));
			double angle = arc.getArcPath().getEndAngle();
			arc.setTargetLocation(positionX + centreOffsetLeft()
					- (0.5 * getDiameter() * (Math.sin(angle))), positionY
					+ centreOffsetTop()
					+ (0.5 * getDiameter() * (Math.cos(angle))));
		}
	}

	public void toggleAttributesVisible() {
		attributesVisible = !attributesVisible;
		update();
	}

	public boolean hasCapacity() {
		return capacity > 0;
	}

	public void addedToGui() {
		super.addedToGui();
		update();
	}

	public void showEditor() {
		// Build interface
		EscapableDialog guiDialog = new EscapableDialog(CreateGui.getApp(),
				"PIPE2", true);

		Container contentPane = guiDialog.getContentPane();

		// 1 Set layout
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		// 2 Add Place editor
		contentPane.add(new PlaceEditorPanel(guiDialog.getRootPane(), this,
				CreateGui.getModel(), CreateGui.getView()));

		guiDialog.setResizable(false);

		// Make window fit contents' preferred size
		guiDialog.pack();

		// Move window to the middle of the screen
		guiDialog.setLocationRelativeTo(null);
		guiDialog.setVisible(true);
	}

	public void update() {
		if (attributesVisible == true) {
			pnName.setText("\nk="
					+ (capacity > 0 ? capacity : "\u221E"));
		} else {
			pnName.setText("");
		}
		pnName.zoomUpdate(zoom);
		super.update();
		repaint();
	}

	public void delete() {
		super.delete();
	}

	public boolean isTagged()
	{
		return this.taggedToken;
	}
}
