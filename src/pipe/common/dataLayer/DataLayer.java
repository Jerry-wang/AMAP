package pipe.common.dataLayer;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pipe.gui.CreateGui;
import pipe.gui.Grid;
import pipe.gui.Constants;

/**
 * <b>DataLayer</b> - Encapsulates entire Petri-Net, also contains functions to
 * perform calculations
 * 
 * @see <p>
 *      <a href="..\PNMLSchema\index.html">PNML - Petri-Net XMLSchema
 *      (stNet.xsd)</a>
 * @see </p>
 *      <p>
 *      <a href="uml\DataLayer.png">DataLayer UML</a>
 *      </p>
 * @version 1.0
 * @author James D Bloom
 * 
 * @author David Patterson Jan 2, 2006: Changed the fireRandomTransition method
 *         to give precedence to immediate transitions.
 * 
 * @author Edwin Chung added a boolean attribute to each matrix generated to
 *         prevent them from being created again when they have not been changed
 *         (6th Feb 2007)
 * 
 * @author Ben Kirby Feb 10, 2007: Removed savePNML method and the
 *         createPlaceElement, createAnnotationElement, createArcElement,
 *         createArcPoint, createTransitionElement methods it uses to a separate
 *         DataLayerWriter class, as part of refactoring to remove XML related
 *         actions from the DataLayer class.
 * 
 * @author Ben Kirby Feb 10, 2007: Split loadPNML into two bits. All XML work
 *         (Files, transformers, documents) is done in new PNMLTransformer
 *         class. The calls to actually populate a DataLayer object with the
 *         info contained in the PNML document have been moved to a
 *         createFromPNML method. The DataLayer constructor which previously
 *         used loadPNML has been changed to reflect these modifications. Also
 *         moved getDOM methods to PNMLTranformer class, as getDom is XML
 *         related. Removed getDom() (no arguments) completely as this is not
 *         called anywhere in the application.
 * 
 * @author Will Master Feb 13 2007: Added methods getPlacesCount and
 *         getTransitionsCount to avoid needlessly copying place and transition
 *         arrayLists.
 * 
 * @author Edwin Chung 15th Mar 2007: modified the createFromPNML function so
 *         that DataLayer objects can be created outside GUI
 * 
 * @author Dave Patterson 24 April 2007: Modified the fireRandomTransition
 *         method so it is quicker when there is only one transition to fire
 *         (just fire it, don't get a random variable first). Also, throw a
 *         RuntimeException if a rate less than 1 is detected. The current code
 *         uses the rate as a weight, and a rate such as 0.5 leads to a
 *         condition like that of bug 1699546 where no transition is available
 *         to fire.
 * 
 * @author Dave Patterson 10 May 2007: Modified the fireRandomTransitino method
 *         so it now properly handles fractional weights. There is no
 *         RuntimeException thrown now. The code for timed transitions uses the
 *         same logic, but will soon be changed to use exponentially distributed
 *         times where fractional rates are valid.
 * 
 * @author Barry Kearns August 2007: Added clone functionality and storage of
 *         state groups.
 * 
 **/
public class DataLayer extends Observable implements Cloneable, DataLayerInterface, DataLayerSimulationInterface {

	private static Random randomNumber = new Random(); // Random number
	// generator

	/** PNML File Name */
	public String pnmlName = null;

	/** List containing all the Place objects in the Petri-Net */
	private ArrayList<Place> placesArray = null;
	/** ArrayList containing all the Transition objects in the Petri-Net */
	private ArrayList<Transition> transitionsArray = null;
	/** ArrayList containing all the Arc objects in the Petri-Net */
	private ArrayList<Arc> arcsArray = null;

	/** ArrayList containing all the Arc objects in the Petri-Net */
	private ArrayList<InhibitorArc> inhibitorsArray = null;

	/**
	 * ArrayList for net-level label objects (as opposed to element-level
	 * labels).
	 */
	private ArrayList<AnnotationNote> labelsArray = null;

	/** ArrayList for rate Parmameters objects. */
	private ArrayList<RateParameter> rateParametersArray = null;

	/* boolean determining if tagged structure has been validated*/
	private boolean validated = false;
	
	/**
	 * An ArrayList used to point to either the Arc, Place or Transition
	 * ArrayLists when these ArrayLists are being update
	 */
	@SuppressWarnings("unchecked")
	private ArrayList changeArrayList = null;

	/** Initial Marking Vector */
	private LinkedList<Marking>[] initialMarkingVector = null;
	/** Current Marking Vector */
	private LinkedList<Marking>[] currentMarkingVector = null;
	/** Capacity Matrix */
	private int[] capacityVector = null;
	/** Priority Matrix */
	private int[] priorityVector = null;
	/** Timed Matrix */
	private boolean[] timedVector = null;
	/** Marking Vector Storage used during animation */
	private LinkedList<Marking>[] markingVectorAnimationStorage = null;

	/** Used to determine whether the matrixes have been modified */
	static boolean initialMarkingVectorChanged = true;

	static boolean currentMarkingVectorChanged = true;

	/** X-Axis Scale Value */
	private final int DISPLAY_SCALE_FACTORX = 7; // Scale factors for loading
	// other Petri-Nets (not yet
	// implemented)
	/** Y-Axis Scale Value */
	private final int DISPLAY_SCALE_FACTORY = 7; // Scale factors for loading
	// other Petri-Nets (not yet
	// implemented)
	/** X-Axis Shift Value */
	private final int DISPLAY_SHIFT_FACTORX = 270; // Scale factors for loading
	// other Petri-Nets (not yet
	// implemented)
	/** Y-Axis Shift Value */
	private final int DISPLAY_SHIFT_FACTORY = 120; // Scale factors for loading
	// other Petri-Nets (not yet
	// implemented)

	/**
	 * Hashtable which maps PlaceTransitionObjects to their list of connected
	 * arcs
	 */
	private Hashtable arcsMap = null;

	/**
	 * Hashtable which maps PlaceTransitionObjects to their list of connected
	 * arcs
	 */
	private Hashtable inhibitorsMap = null;

	/**
	 * An ArrayList used store the source / destination state groups associated
	 * with this Petri-Net
	 */
	private ArrayList stateGroups = null;

	private HashSet rateParameterHashSet = new HashSet();

	private TokenClass activeTokenClass;

	private LinkedList<TokenClass> tokenClasses;

	/**
	 * Create Petri-Net object from PNML file with URI pnmlFileName
	 * 
	 * @param pnmlFileName
	 *            Name of PNML File
	 */
	public DataLayer(String pnmlFileName) {
		tokenClasses = null;
		initializeMatrices();
		PNMLTransformer transform = new PNMLTransformer();
		File temp = new File(pnmlFileName);
		pnmlName = temp.getName();
		createFromPNML(transform.transformPNML(pnmlFileName));
	}

	/**
	 * Create Petri-Net object from pnmlFile
	 * 
	 * @param pnmlFile
	 *            PNML File
	 */
	public DataLayer(File pnmlFile) {
		this(pnmlFile.getAbsolutePath());
		tokenClasses = null;
	}

	/**
	 * Create empty Petri-Net object
	 */
	public DataLayer() {
		initializeMatrices();
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#clone()
	 */
	public DataLayer clone() {
		DataLayer newClone = null;
		try {
			newClone = (DataLayer) super.clone();
			newClone.placesArray = deepCopy(placesArray);
			newClone.transitionsArray = deepCopy(transitionsArray);
			newClone.arcsArray = deepCopy(arcsArray);
			newClone.inhibitorsArray = deepCopy(inhibitorsArray);
			// newClone.tokensArray = deepCopy(tokensArray);
			newClone.labelsArray = deepCopy(labelsArray);
			newClone.tokenClasses = (LinkedList<TokenClass>) ObjectDeepCopier.deepCopy(tokenClasses);
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
		return newClone;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#setTokenClasses(java.util.LinkedList)
	 */
	public void setTokenClasses(LinkedList<TokenClass> tokenclasses) {
		// We do not want to simply replace the tokenclasses as there
		// are other attributes of a tokenclass that would be lost as
		// a result. So we just set the new attributes defined by the
		// user in the Specify Token Class dialog.
		if(this.tokenClasses == null){
			this.tokenClasses = tokenclasses;
		}
		else{
			int currentSize = this.tokenClasses.size();
			for (int i = 0; i < tokenclasses.size(); i++) {
				if (i < currentSize) {
					this.tokenClasses.get(i).setColour(
							tokenclasses.get(i).getColour());
					this.tokenClasses.get(i).setID(tokenclasses.get(i).getID());
					this.tokenClasses.get(i).setEnabled(
							tokenclasses.get(i).isEnabled());
				}
				else{
					this.tokenClasses.add(tokenclasses.get(i));
				}
				// Enabled. TokenClass must be added to each arcWeight and each place as a default of 0
				if(this.tokenClasses.get(i).isEnabled()){
					for (Object p : placesArray) {
						int pos = getPosInList(this.tokenClasses.get(i).getID(),((Place) p).getCurrentMarking() );
						if(pos == -1){
							((Place) p).getCurrentMarking().add(new Marking(this.tokenClasses.get(i), 0));
						}
					}
					for(Object a:arcsArray){
						int pos = getPosInList(this.tokenClasses.get(i).getID(),((Arc) a).getWeight() );
						if(pos == -1){
							((Arc) a).getWeight().add(new Marking(this.tokenClasses.get(i), 0));
						}
					}
				}
				// Not enabled. TokenClass must be removed from everywhere
				else{
					for (Object p : placesArray) {
						int pos = getPosInList(this.tokenClasses.get(i).getID(),((Place) p).getCurrentMarking() );
						if(pos != -1){
							((Place) p).getCurrentMarking().remove(pos);
						}
					}
					for(Object a:arcsArray){
						int pos = getPosInList(this.tokenClasses.get(i).getID(),((Arc) a).getWeight() );
						if(pos != -1){
							((Arc) a).getWeight().remove(pos);
						}
					}
				}
				//this.tokenClasses.get(i).createIncidenceMatrix((ArrayList<Arc>)arcsArray, (ArrayList<Transition>)transitionsArray, (ArrayList<Place>)placesArray);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTokenClasses()
	 */
	public LinkedList<TokenClass> getTokenClasses() {
		if (tokenClasses == null) {
			tokenClasses = new LinkedList<TokenClass>();
			tokenClasses.add(new TokenClass(true, "黑(默认)", Color.black));
			tokenClasses.add(new TokenClass(true, "红", Color.red));
			tokenClasses.add(new TokenClass(true, "绿", Color.green));
//			tokenClasses.add(new TokenClass(true, "Default", Color.black));
			this.setActiveTokenClass(tokenClasses.get(0));
		}
		return tokenClasses;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getActiveTokenClass()
	 */
	public TokenClass getActiveTokenClass() {
		return activeTokenClass;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#setActiveTokenClass(pipe.common.dataLayer.TokenClass)
	 */
	public void setActiveTokenClass(TokenClass tc) {
		this.activeTokenClass = tc;
		for (Object p : placesArray) {
			((Place) p).setActiveTokenClass(tc);
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#lockTokenClass(java.lang.String)
	 */
	public void lockTokenClass(String id) {
		for (TokenClass tc : tokenClasses) {
			if (tc.getID().equals(id)) {
				tc.incrementLock();
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#unlockTokenClass(java.lang.String)
	 */
	public void unlockTokenClass(String id) {
		for (TokenClass tc : tokenClasses) {
			if (tc.getID().equals(id)) {
				tc.decrementLock();
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPosInList(java.lang.String, java.util.LinkedList)
	 */
	public int getPosInList(String tokenClassID, LinkedList<Marking> markings) {
		int size = markings.size();
		for (int i = 0; i < size; i++) {
			Marking m = markings.get(i);
			if (m.getTokenClass().getID().equals(tokenClassID))
				return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTokenClassFromID(java.lang.String)
	 */
	public TokenClass getTokenClassFromID(String id){
		TokenClass isNull = null;
		for(TokenClass tc:tokenClasses){
			if(tc.getID().equals(id))
				return tc;
		}
		return isNull;
	}
	/**
	 * @param original
	 *            arraylist to be deep copied
	 * @return a clone of the arraylist
	 */
	private static ArrayList deepCopy(ArrayList original) {
		ArrayList result = (ArrayList) original.clone();
		ListIterator listIter = result.listIterator();

		while (listIter.hasNext()) {
			PetriNetObject pnObj = (PetriNetObject) listIter.next();
			listIter.set(pnObj.clone());
		}
		return result;
	}

	/**
	 * Initialize Arrays
	 */
	private void initializeMatrices() {
		placesArray = new ArrayList();
		transitionsArray = new ArrayList();
		arcsArray = new ArrayList();
		inhibitorsArray = new ArrayList();
		labelsArray = new ArrayList();
		stateGroups = new ArrayList();
		rateParametersArray = new ArrayList();
		initialMarkingVector = null;
		// may as well do the hashtable here as well
		arcsMap = new Hashtable();
		inhibitorsMap = new Hashtable();
	}

	/**
	 * Add placeInput to the back of the Place ArrayList All observers are
	 * notified of this change (Model-View Architecture)
	 * 
	 * @param placeInput
	 *            Place Object to add
	 */
	private void addPlace(Place placeInput) {
		boolean unique = true;

		if (placeInput != null) {
			if (placeInput.getId() != null && placeInput.getId().length() > 0) {
				for (int i = 0; i < placesArray.size(); i++) {
					if (placeInput.getId().equals(
							((Place) placesArray.get(i)).getId())) {
						unique = false;
					}
				}
			} else {
				String id = null;
				if (placesArray != null && placesArray.size() > 0) {
					int no = placesArray.size();
					do {
						for (int i = 0; i < placesArray.size(); i++) {
							id = "P" + no;
							if (placesArray.get(i) != null) {
								if (id.equals(((Place) placesArray.get(i))
										.getId())) {
									unique = false;
									no++;
								} else {
									unique = true;
								}
							}
						}
					} while (!unique);
				} else {
					id = "P0";
				}

				if (id != null) {
					placeInput.setId(id);
				} else {
					placeInput.setId("error");
				}
			}
			placeInput.setActiveTokenClass(activeTokenClass);
			placesArray.add(placeInput);
			setChanged();
			setMatrixChanged();
			notifyObservers(placeInput);
		}
	}

	/**
	 * Add labelInput to the back of the AnnotationNote ArrayList All observers
	 * are notified of this change (Model-View Architecture)
	 * 
	 * @param labelInput
	 *            AnnotationNote Object to add
	 */
	private void addAnnotation(AnnotationNote labelInput) {
		boolean unique = true;
		labelsArray.add(labelInput);
		setChanged();
		notifyObservers(labelInput);
	}

	/**
	 * Add rateParameterInput to the back of the Rate Parameter ArrayList All
	 * observers are notified of this change (Model-View Architecture)
	 * 
	 * @param placeInput
	 *            Place Object to add
	 */
	private void addAnnotation(RateParameter rateParameterInput) {
		boolean unique = true;
		rateParametersArray.add(rateParameterInput);
		setChanged();
		notifyObservers(rateParameterInput);
	}

	/**
	 * Add transitionInput to back of the Transition ArrayList All observers are
	 * notified of this change (Model-View Architecture)
	 * 
	 * @param transitionInput
	 *            Transition Object to add
	 */
	private void addTransition(Transition transitionInput) {
		boolean unique = true;

		if (transitionInput != null) {
			if (transitionInput.getId() != null
					&& transitionInput.getId().length() > 0) {
				for (int i = 0; i < transitionsArray.size(); i++) {
					if (transitionInput.getId().equals(
							((Transition) transitionsArray.get(i)).getId())) {
						unique = false;
					}
				}
			} else {
				String id = null;
				if (transitionsArray != null && transitionsArray.size() > 0) {
					int no = transitionsArray.size();
					do {
						for (int i = 0; i < transitionsArray.size(); i++) {
							id = "T" + no;
							if (transitionsArray.get(i) != null) {
								if (id.equals(((Transition) transitionsArray
										.get(i)).getId())) {
									unique = false;
									no++;
								} else {
									unique = true;
								}
							}
						}
					} while (!unique);
				} else {
					id = "T0";
				}

				if (id != null) {
					transitionInput.setId(id);
				} else {
					transitionInput.setId("error");
				}
			}
			transitionsArray.add(transitionInput);
			setChanged();
			setMatrixChanged();
			notifyObservers(transitionInput);
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#addArc(pipe.common.dataLayer.NormalArc)
	 */
	public void addArc(NormalArc arcInput) {
		boolean unique = true;

		if (arcInput != null) {
			if (arcInput.getId() != null && arcInput.getId().length() > 0) {
				for (int i = 0; i < arcsArray.size(); i++) {
					if (arcInput.getId().equals(
							((Arc) arcsArray.get(i)).getId())) {
						unique = false;
					}
				}
			} else {
				String id = null;
				if (arcsArray != null && arcsArray.size() > 0) {
					int no = arcsArray.size();
					do {
						for (int i = 0; i < arcsArray.size(); i++) {
							id = "A" + no;
							if (arcsArray.get(i) != null) {
								if (id.equals(((Arc) arcsArray.get(i)).getId())) {
									unique = false;
									no++;
								} else {
									unique = true;
								}
							}
						}
					} while (!unique);
				} else {
					id = "A0";
				}
				if (id != null) {
					arcInput.setId(id);
				} else {
					arcInput.setId("error");
				}
			}
			arcsArray.add(arcInput);
			addArcToArcsMap(arcInput);

			setChanged();
			setMatrixChanged();
			// notifyObservers(arcInput.getBounds());
			notifyObservers(arcInput);
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#addArc(pipe.common.dataLayer.InhibitorArc)
	 */
	public void addArc(InhibitorArc inhibitorArcInput) {
		boolean unique = true;

		if (inhibitorArcInput != null) {
			if (inhibitorArcInput.getId() != null
					&& inhibitorArcInput.getId().length() > 0) {
				for (int i = 0; i < inhibitorsArray.size(); i++) {
					if (inhibitorArcInput.getId().equals(
							((Arc) inhibitorsArray.get(i)).getId())) {
						unique = false;
					}
				}
			} else {
				String id = null;
				if (inhibitorsArray != null && inhibitorsArray.size() > 0) {
					int no = inhibitorsArray.size();
					do {
						for (int i = 0; i < inhibitorsArray.size(); i++) {
							id = "I" + no;
							if (inhibitorsArray.get(i) != null) {
								if (id.equals(((Arc) inhibitorsArray.get(i))
										.getId())) {
									unique = false;
									no++;
								} else {
									unique = true;
								}
							}
						}
					} while (!unique);
				} else {
					id = "I0";
				}
				if (id != null) {
					inhibitorArcInput.setId(id);
				} else {
					inhibitorArcInput.setId("error");
				}
			}
			inhibitorsArray.add(inhibitorArcInput);
			addInhibitorArcToInhibitorsMap(inhibitorArcInput);

			setChanged();
			setMatrixChanged();
			// notifyObservers(arcInput.getBounds());
			notifyObservers(inhibitorArcInput);
		}
	}

	/**
	 * Update the arcsMap hashtable to reflect the new arc
	 * 
	 * @param arcInput
	 *            New Arc
	 * */
	private void addArcToArcsMap(NormalArc arcInput) {
		// now we want to add the arc to the list of arcs for it's source and
		// target
		PlaceTransitionObject source = arcInput.getSource();
		PlaceTransitionObject target = arcInput.getTarget();
		ArrayList newList = null;

		if (source != null) {
			// Pete: Place/Transitions now always moveable
			// source.setMovable(false);
			if (arcsMap.get(source) != null) {
				((ArrayList) arcsMap.get(source)).add(arcInput);
			} else {
				newList = new ArrayList();
				newList.add(arcInput);

				arcsMap.put(source, newList);
			}
		}

		if (target != null) {
			// Pete: Place/Transitions now always moveable
			// target.setMovable(false);
			if (arcsMap.get(target) != null) {
				((ArrayList) arcsMap.get(target)).add(arcInput);
			} else {
				newList = new ArrayList();
				newList.add(arcInput);
				arcsMap.put(target, newList);
			}
		}
	}

	/**
	 * Update the inhibitorsMap hashtable to reflect the new inhibitor arc
	 * 
	 * @param arcInput
	 *            New Arc
	 */
	private void addInhibitorArcToInhibitorsMap(InhibitorArc inhibitorArcInput) {
		// now we want to add the inhibitor arc to the list of inhibitor arcs
		// for
		// it's source and target
		PlaceTransitionObject source = inhibitorArcInput.getSource();
		PlaceTransitionObject target = inhibitorArcInput.getTarget();
		ArrayList newList = null;

		if (source != null) {
			if (inhibitorsMap.get(source) != null) {
				((ArrayList) inhibitorsMap.get(source)).add(inhibitorArcInput);
			} else {
				newList = new ArrayList();
				newList.add(inhibitorArcInput);
				inhibitorsMap.put(source, newList);
			}
		}

		if (target != null) {
			if (inhibitorsMap.get(target) != null) {
				((ArrayList) inhibitorsMap.get(target)).add(inhibitorArcInput);
			} else {
				newList = new ArrayList();
				newList.add(inhibitorArcInput);
				inhibitorsMap.put(target, newList);
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#addStateGroup(pipe.common.dataLayer.StateGroup)
	 */
	public void addStateGroup(StateGroup stateGroupInput) {
		boolean unique = true;
		String id = null;
		int no = stateGroups.size();

		// Check if ID is set from PNML file
		if (stateGroupInput.getId() != null
				&& stateGroupInput.getId().length() > 0) {
			id = stateGroupInput.getId();

			// Check if ID is unique
			for (int i = 0; i < stateGroups.size(); i++) {
				if (id.equals(((StateGroup) stateGroups.get(i)).getId())) {
					unique = false;
				}
			}
		} else {
			unique = false;
		}

		// Find a unique ID for the new state group
		if (!unique) {
			id = "SG" + no;
			for (int i = 0; i < stateGroups.size(); i++) {
				// If a matching ID is found, increment id and reset loop
				if (id.equals(((StateGroup) stateGroups.get(i)).getId())) {
					id = "SG" + ++no;
					i = 0;
				}
			}
			stateGroupInput.setId(id);
		}
		stateGroups.add(stateGroupInput);
	}

	/**
	 * Add tokenClassInput to the back of the tokenClasses LinkedList All observers are
	 * notified of this change (Model-View Architecture)
	 * 
	 * @param tokenClassInput
	 *            tokenClass Object to add
	 */
	private void addTokenClass(TokenClass tokenClassInput) {
		boolean firstEntry = false;
		if(tokenClasses == null){
			tokenClasses = new LinkedList<TokenClass>();
			firstEntry = true;
		}
		boolean unique = true;

		if (tokenClassInput != null) {
			if (tokenClassInput.getID() != null && tokenClassInput.getID().length() > 0) {
				for (int i = 0; i < tokenClasses.size(); i++) {
					if (tokenClassInput.getID().equals(
							((TokenClass) tokenClasses.get(i)).getID())) {
						unique = false;
					}
				}
			} else {
				String id = null;
				if (tokenClasses != null && tokenClasses.size() > 0) {
					int no = tokenClasses.size();
					do {
						for (int i = 0; i < tokenClasses.size(); i++) {
							id = "tokenClass" + no;
							if (tokenClasses.get(i) != null) {
								if (id.equals(((TokenClass) tokenClasses.get(i))
										.getID())) {
									unique = false;
									no++;
								} else {
									unique = true;
								}
							}
						}
					} while (!unique);
				} else {
					id = "tokenClass0";
				}

				if (id != null) {
					tokenClassInput.setID(id);
				} else {
					tokenClassInput.setID("error");
				}
			}
			tokenClasses.add(tokenClassInput);
			if(firstEntry){
				this.setActiveTokenClass(tokenClassInput);
			}
			setChanged();
			setMatrixChanged();
			notifyObservers(tokenClassInput);
		}
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#addPetriNetObject(pipe.common.dataLayer.PetriNetObject)
	 */
	public void addPetriNetObject(PetriNetObject pnObject) {
		if (setPetriNetObjectArrayList(pnObject)) {
			if (pnObject instanceof NormalArc) {
				addArcToArcsMap((NormalArc) pnObject);
				addArc((NormalArc) pnObject);
			} else if (pnObject instanceof InhibitorArc) {
				addInhibitorArcToInhibitorsMap((InhibitorArc) pnObject);
				addArc((InhibitorArc) pnObject);
			} else if (pnObject instanceof Place) {
				addPlace((Place) pnObject);
			} else if (pnObject instanceof Transition) {
				addTransition((Transition) pnObject);
			} else if (pnObject instanceof AnnotationNote) {
				labelsArray.add((AnnotationNote) pnObject);
			} else if (pnObject instanceof RateParameter) {
				rateParametersArray.add((RateParameter) pnObject);
				rateParameterHashSet.add(pnObject.getName());
			} else { // arrows, other labels.
				changeArrayList.add(pnObject);
				setChanged();
				setMatrixChanged();
				notifyObservers(pnObject);
			}
		}
		// we reset to null so that the wrong ArrayList can't get added to
		changeArrayList = null;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#removePetriNetObject(pipe.common.dataLayer.PetriNetObject)
	 */
	public void removePetriNetObject(PetriNetObject pnObject) {
		boolean didSomething = false;
		ArrayList attachedArcs = null;

		try {
			if (setPetriNetObjectArrayList(pnObject)) {
				didSomething = changeArrayList.remove(pnObject);
				// we want to remove all attached arcs also
				if (pnObject instanceof PlaceTransitionObject) {

					if ((ArrayList) arcsMap.get(pnObject) != null) {

						// get the list of attached arcs for the object we are
						// removing
						attachedArcs = ((ArrayList) arcsMap.get(pnObject));

						// iterate over all the attached arcs, removing them all
						// Pere: in inverse order!
						// for (int i=0; i < attachedArcs.size(); i++){
						for (int i = attachedArcs.size() - 1; i >= 0; i--) {
							((Arc) attachedArcs.get(i)).delete();
						}
						arcsMap.remove(pnObject);
					}

					if ((ArrayList) inhibitorsMap.get(pnObject) != null) {

						// get the list of attached arcs for the object we are
						// removing
						attachedArcs = ((ArrayList) inhibitorsMap.get(pnObject));

						// iterate over all the attached arcs, removing them all
						// Pere: in inverse order!
						// for (int i=0; i < attachedArcs.size(); i++){
						for (int i = attachedArcs.size() - 1; i >= 0; i--) {
							((Arc) attachedArcs.get(i)).delete();
						}
						inhibitorsMap.remove(pnObject);
					}
				} else if (pnObject instanceof NormalArc) {

					// get source and target of the arc
					PlaceTransitionObject attached = ((Arc) pnObject)
							.getSource();

					if (attached != null) {
						ArrayList a = (ArrayList) arcsMap.get(attached);
						if (a != null) {
							a.remove(pnObject);
						}

						attached.removeFromArc((Arc) pnObject);
						if (attached instanceof Transition) {
							((Transition) attached)
									.removeArcCompareObject((Arc) pnObject);
							attached.updateConnected();
						}
						// attached.updateConnected(); //causing null pointer
						// exceptions (?)
					}

					attached = ((Arc) pnObject).getTarget();
					if (attached != null) {
						if (arcsMap.get(attached) != null) { // causing null
							// pointer
							// exceptions
							// (!)
							((ArrayList) arcsMap.get(attached))
									.remove(pnObject);
						}

						attached.removeToArc((Arc) pnObject);
						if (attached instanceof Transition) {
							((Transition) attached)
									.removeArcCompareObject((Arc) pnObject);
							attached.updateConnected();
						}
						// attached.updateConnected(); //causing null pointer
						// exceptions (?)
					}
				} else if (pnObject instanceof InhibitorArc) {

					// get source and target of the arc
					PlaceTransitionObject attached = ((Arc) pnObject)
							.getSource();

					if (attached != null) {
						ArrayList a = (ArrayList) inhibitorsMap.get(attached);
						if (a != null) {
							a.remove(pnObject);
						}

						attached.removeFromArc((Arc) pnObject);
						if (attached instanceof Transition) {
							((Transition) attached)
									.removeArcCompareObject((Arc) pnObject);
						}
						// attached.updateConnected(); //causing null pointer
						// exceptions (?)
					}

					attached = ((Arc) pnObject).getTarget();

					if (attached != null) {
						if (inhibitorsMap.get(attached) != null) { // causing
							// null
							// pointer
							// exceptions
							// (!)
							((ArrayList) inhibitorsMap.get(attached))
									.remove(pnObject);
						}

						attached.removeToArc((Arc) pnObject);
						if (attached instanceof Transition) {
							((Transition) attached)
									.removeArcCompareObject((Arc) pnObject);
						}
						// attached.updateConnected(); //causing null pointer
						// exceptions (?)
					}
				} else if (pnObject instanceof RateParameter) {
					rateParameterHashSet.remove(pnObject.getName());
				}

				if (didSomething) {
					setChanged();
					setMatrixChanged();
					// notifyObservers(pnObject.getBounds());
					notifyObservers(pnObject);
				}
			}
		} catch (NullPointerException npe) {
			System.out.println("NullPointerException [debug]\n"
					+ npe.getMessage());
			throw npe;
		}
		// we reset to null so that the wrong ArrayList can't get added to
		changeArrayList = null;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#removeStateGroup(pipe.common.dataLayer.StateGroup)
	 */
	public void removeStateGroup(StateGroup SGObject) {
		stateGroups.remove(SGObject);
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#stateGroupExistsAlready(java.lang.String)
	 */
	public boolean stateGroupExistsAlready(String stateName) {
		Iterator<StateGroup> i = stateGroups.iterator();
		while (i.hasNext()) {
			StateGroup stateGroup = i.next();
			String stateGroupName = stateGroup.getName();
			if (stateName.equals(stateGroupName)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#returnTransitions()
	 */
	public Iterator returnTransitions() {
		return transitionsArray.iterator();
	}

	/**
	 * Sets an internal ArrayList according to the class of the object passed
	 * in.
	 * 
	 * @param pnObject
	 *            The pnObject in question.
	 * @return Returns True if the pnObject is of type Place, Transition or Arc
	 */
	private boolean setPetriNetObjectArrayList(PetriNetObject pnObject) {

		// determine appropriate ArrayList
		if (pnObject instanceof Transition) {
			changeArrayList = transitionsArray;
			return true;
		} else if (pnObject instanceof Place) {
			changeArrayList = placesArray;
			return true;
		} else if (pnObject instanceof NormalArc) {
			changeArrayList = arcsArray;
			return true;
		} else if (pnObject instanceof InhibitorArc) {
			changeArrayList = inhibitorsArray;
			return true;
		} else if (pnObject instanceof AnnotationNote) {
			changeArrayList = labelsArray;
			return true;
		} else if (pnObject instanceof RateParameter) {
			changeArrayList = rateParametersArray;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPetriNetObjects()
	 */
	public Iterator getPetriNetObjects() {
		ArrayList all = new ArrayList(placesArray);
		all.addAll(transitionsArray);
		all.addAll(arcsArray);
		all.addAll(labelsArray);
		// tokensArray removed
		all.addAll(rateParametersArray);

		return all.iterator();
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#hasPlaceTransitionObjects()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#hasPlaceTransitionObjects()
	 */
	public boolean hasPlaceTransitionObjects() {
		return (placesArray.size() + transitionsArray.size()) > 0;
	}

	/**
	 * Creates a Label object from a Label DOM Element
	 * 
	 * @param inputLabelElement
	 *            Input Label DOM Element
	 * @return Label Object
	 */
	private AnnotationNote createAnnotation(Element inputLabelElement) {
		int positionXInput = 0;
		int positionYInput = 0;
		int widthInput = 0;
		int heightInput = 0;
		String text = null;
		boolean borderInput = true;

		String positionXTempStorage = inputLabelElement
				.getAttribute("xPosition");
		String positionYTempStorage = inputLabelElement
				.getAttribute("yPosition");
		String widthTemp = inputLabelElement.getAttribute("w");
		String heightTemp = inputLabelElement.getAttribute("h");
		String textTempStorage = inputLabelElement.getAttribute("txt");
		String borderTemp = inputLabelElement.getAttribute("border");

		if (positionXTempStorage.length() > 0) {
			positionXInput = Integer.valueOf(positionXTempStorage).intValue()
					* (false ? DISPLAY_SCALE_FACTORX : 1)
					+ (false ? DISPLAY_SHIFT_FACTORX : 1);
		}

		if (positionYTempStorage.length() > 0) {
			positionYInput = Integer.valueOf(positionYTempStorage).intValue()
					* (false ? DISPLAY_SCALE_FACTORX : 1)
					+ (false ? DISPLAY_SHIFT_FACTORX : 1);
		}

		if (widthTemp.length() > 0) {
			widthInput = Integer.valueOf(widthTemp).intValue()
					* (false ? DISPLAY_SCALE_FACTORY : 1)
					+ (false ? DISPLAY_SHIFT_FACTORY : 1);
		}

		if (heightTemp.length() > 0) {
			heightInput = Integer.valueOf(heightTemp).intValue()
					* (false ? DISPLAY_SCALE_FACTORY : 1)
					+ (false ? DISPLAY_SHIFT_FACTORY : 1);
		}

		if (borderTemp.length() > 0) {
			borderInput = Boolean.valueOf(borderTemp).booleanValue();
		} else {
			borderInput = true;
		}

		if (textTempStorage.length() > 0) {
			text = textTempStorage;
		} else {
			text = "";
		}

		return new AnnotationNote(text, positionXInput, positionYInput,
				widthInput, heightInput, borderInput);
	}

	/**
	 * Creates a Parameter object from a Definition DOM Element
	 * 
	 * @param inputDefinitionElement
	 *            Input Label DOM Element
	 * @return Parameter Object
	 */
	private Parameter createParameter(Element inputDefinitionElement) {
		int positionXInput = 0;
		int positionYInput = 0;

		String positionXTempStorage = inputDefinitionElement
				.getAttribute("positionX");
		String positionYTempStorage = inputDefinitionElement
				.getAttribute("positionY");
		String nameTemp = inputDefinitionElement.getAttribute("name");
		String expressionTemp = inputDefinitionElement
				.getAttribute("expression");

		if (positionXTempStorage.length() > 0) {
			positionXInput = Integer.valueOf(positionXTempStorage).intValue()/* *
																			 * (
																			 * false
																			 * ?
																			 * DISPLAY_SCALE_FACTORX
																			 * :
																			 * 1
																			 * )
																			 * +
																			 * (
																			 * false
																			 * ?
																			 * DISPLAY_SHIFT_FACTORX
																			 * :
																			 * 1
																			 * )
																			 */;
		}

		if (positionYTempStorage.length() > 0) {
			positionYInput = Integer.valueOf(positionYTempStorage).intValue()/* *
																			 * (
																			 * false
																			 * ?
																			 * DISPLAY_SCALE_FACTORX
																			 * :
																			 * 1
																			 * )
																			 * +
																			 * (
																			 * false
																			 * ?
																			 * DISPLAY_SHIFT_FACTORX
																			 * :
																			 * 1
																			 * )
																			 */;
		}

			rateParameterHashSet.add(nameTemp);
			return new RateParameter(nameTemp, Double
					.parseDouble(expressionTemp), positionXInput,
					positionYInput);
	}

	/**
	 * Creates a Transition object from a Transition DOM Element
	 * 
	 * @param inputTransitionElement
	 *            Input Transition DOM Element
	 * @return Transition Object
	 */
	private Transition createTransition(Element element) {
		double positionXInput = 0;
		double positionYInput = 0;
		String idInput = null;
		String nameInput = null;
		double nameOffsetYInput = 0;
		double nameOffsetXInput = 0;
		double rate = 1.0;
		boolean timedTransition = false;
		boolean deterministicTransition=false;
		boolean infiniteServer;
		int angle = 0;
		int priority = 1;
		double weight = 1.0;
		double delay = 0.0;

		String positionXTempStorage = element.getAttribute("positionX");
		String positionYTempStorage = element.getAttribute("positionY");
		String idTempStorage = element.getAttribute("id");
		String nameTempStorage = element.getAttribute("name");
		String nameOffsetXTempStorage = element.getAttribute("nameOffsetX");
		String nameOffsetYTempStorage = element.getAttribute("nameOffsetY");
		String nameRate = element.getAttribute("rate");
		String nameTimed = element.getAttribute("timed");
		String nameDeterministic = element.getAttribute("deterministic");
		String nameInfiniteServer = element.getAttribute("infiniteServer");
		String nameAngle = element.getAttribute("angle");
		String namePriority = element.getAttribute("priority");
		String nameDelay = element.getAttribute("delay");System.out.println(nameDelay+"!!!!!");
		// String nameWeight = element.getAttribute("weight");
		String parameterTempStorage = element.getAttribute("parameter");

		/*
		 * wjk - a useful little routine to display all attributes of a
		 * transition for (int i=0; ; i++) { Object obj =
		 * inputTransitionElement.getAttributes().item(i); if (obj == null) {
		 * break; } System.out.println("Attribute " + i + " = " +
		 * obj.toString()); }
		 */
 		if (nameTimed.equals("true")) { 
			timedTransition = true;
		} else  if (nameTimed.equals("false")) { 
			timedTransition = false;
		}
		
  		if (nameDeterministic.equals("true")) { 
			deterministicTransition = true;
		} else  if (nameDeterministic.equals("false")) { 
			deterministicTransition = false;
		}
		
		

		infiniteServer = !(nameInfiniteServer.length() == 0 || nameInfiniteServer
				.length() == 5);

		if (positionXTempStorage.length() > 0) {
			positionXInput = Double.valueOf(positionXTempStorage).doubleValue()
					* (false ? Constants.DISPLAY_SCALE_FACTORX : 1)
					+ (false ? Constants.DISPLAY_SHIFT_FACTORX : 1);
		}
		if (positionYTempStorage.length() > 0) {
			positionYInput = Double.valueOf(positionYTempStorage).doubleValue()
					* (false ? Constants.DISPLAY_SCALE_FACTORY : 1)
					+ (false ? Constants.DISPLAY_SHIFT_FACTORY : 1);
		}

		positionXInput = Grid.getModifiedX(positionXInput);
		positionYInput = Grid.getModifiedY(positionYInput);

		if (idTempStorage.length() > 0) {
			idInput = idTempStorage;
		} else if (nameTempStorage.length() > 0) {
			idInput = nameTempStorage;
		}

		if (nameTempStorage.length() > 0) {
			nameInput = nameTempStorage;
		} else if (idTempStorage.length() > 0) {
			nameInput = idTempStorage;
		}

		if (nameOffsetXTempStorage.length() > 0) {
			nameOffsetXInput = Double.valueOf(nameOffsetXTempStorage)
					.doubleValue();
		}

		if (nameOffsetYTempStorage.length() > 0) {
			nameOffsetYInput = Double.valueOf(nameOffsetYTempStorage)
					.doubleValue();
		}

		if (nameRate.length() == 0) {
			nameRate = "1.0";
		}
		if (nameRate != "1.0") {
			rate = Double.valueOf(nameRate).doubleValue();
		} else {
			rate = 1.0;
		}

		if (nameAngle.length() > 0) {
			angle = Integer.valueOf(nameAngle).intValue();
		}

		if (namePriority.length() > 0) {
			priority = Integer.valueOf(namePriority).intValue();
		}
		
		if (nameDelay.length() > 0) {
			delay = Double.valueOf(nameDelay).doubleValue();
		}

		Transition transition = TransitionFactory.createTransition(positionXInput, positionYInput, idInput, nameInput,
				nameOffsetXInput, nameOffsetYInput, rate, timedTransition, deterministicTransition, infiniteServer, angle, priority, delay);

		if (parameterTempStorage.length() > 0) {
			if (existsRateParameter(parameterTempStorage)) {
				for (int i = 0; i < rateParametersArray.size(); i++) {
					if (parameterTempStorage
							.equals(((RateParameter) rateParametersArray.get(i))
									.getName())) {
						transition
								.setRateParameter((RateParameter) rateParametersArray
										.get(i));
					}
				}
			}
		}

		return transition;
	}

	private Place createPlace(Element element) {
		double positionXInput = 0;
		double positionYInput = 0;
		String idInput = null;
		String nameInput = null;
		double nameOffsetYInput = 0;
		double nameOffsetXInput = 0;
		LinkedList<Marking> initialMarkingInput = new LinkedList<Marking>();
		double markingOffsetXInput = 0;
		double markingOffsetYInput = 0;
		int capacityInput = 0;

		String positionXTempStorage = element.getAttribute("positionX");
		String positionYTempStorage = element.getAttribute("positionY");
		String idTempStorage = element.getAttribute("id");
		String nameTempStorage = element.getAttribute("name");
		String nameOffsetXTempStorage = element.getAttribute("nameOffsetX");
		String nameOffsetYTempStorage = element.getAttribute("nameOffsetY");
		String initialMarkingTempStorage = element
				.getAttribute("initialMarking");
		String markingOffsetXTempStorage = element
				.getAttribute("markingOffsetX");
		String markingOffsetYTempStorage = element
				.getAttribute("markingOffsetY");
		String capacityTempStorage = element.getAttribute("capacity");
		String parameterTempStorage = element.getAttribute("parameter");

		if (positionXTempStorage.length() > 0) {
			positionXInput = Double.valueOf(positionXTempStorage).doubleValue()
					* (false ? Constants.DISPLAY_SCALE_FACTORX : 1)
					+ (false ? Constants.DISPLAY_SHIFT_FACTORX : 1);
		}
		if (positionYTempStorage.length() > 0) {
			positionYInput = Double.valueOf(positionYTempStorage).doubleValue()
					* (false ? Constants.DISPLAY_SCALE_FACTORY : 1)
					+ (false ? Constants.DISPLAY_SHIFT_FACTORY : 1);
		}
		positionXInput = Grid.getModifiedX(positionXInput);
		positionYInput = Grid.getModifiedY(positionYInput);

		if (idTempStorage.length() > 0) {
			idInput = idTempStorage;
		} else if (nameTempStorage.length() > 0) {
			idInput = nameTempStorage;
		}

		if (nameTempStorage.length() > 0) {
			nameInput = nameTempStorage;
		} else if (idTempStorage.length() > 0) {
			nameInput = idTempStorage;
		}

		if (nameOffsetYTempStorage.length() > 0) {
			nameOffsetXInput = Double.valueOf(nameOffsetXTempStorage)
					.doubleValue();
		}
		if (nameOffsetXTempStorage.length() > 0) {
			nameOffsetYInput = Double.valueOf(nameOffsetYTempStorage)
					.doubleValue();
		}

		if (initialMarkingTempStorage.length() > 0) {
			String[] stringArray = initialMarkingTempStorage.split(",");
			// Backward compatibility for pnmls without many token classes
			if(stringArray.length == 1){
				if(getActiveTokenClass() == null){
					Color c = new Color(0, 0, 0);
					TokenClass tc = new TokenClass(true, "Default", c);
					addTokenClass(tc);
					Marking marking = new Marking(tc, Integer.valueOf(stringArray[0]));
					initialMarkingInput.add(marking);
				}
				else{
					Marking marking = new Marking(getActiveTokenClass(), Integer.valueOf(stringArray[0]));
					initialMarkingInput.add(marking);
				}
			}
			else{
				int i = 0;
				while (i < stringArray.length) {
					// In case for some reason there are commas between markings
					stringArray[i] = stringArray[i].trim();
					Marking marking = new Marking(this.getTokenClassFromID(stringArray[i]), Integer
							.valueOf(stringArray[i + 1]));
					initialMarkingInput.add(marking);
					i += 2;
				}
			}
		}
		if (markingOffsetXTempStorage.length() > 0) {
			markingOffsetXInput = Double.valueOf(markingOffsetXTempStorage)
					.doubleValue();
		}
		if (markingOffsetYTempStorage.length() > 0) {
			markingOffsetYInput = Double.valueOf(markingOffsetYTempStorage)
					.doubleValue();
		}

		if (capacityTempStorage.length() > 0) {
			capacityInput = Integer.valueOf(capacityTempStorage).intValue();
		}

		Place place = PlaceFactory.createPlace(positionXInput, positionYInput, idInput, nameInput, nameOffsetXInput,
				nameOffsetYInput, initialMarkingInput, markingOffsetXInput, markingOffsetYInput, capacityInput);

		return place;
	}

	/**
	 * Creates a Arc object from a Arc DOM Element
	 * 
	 * @param inputArcElement
	 *            Input Arc DOM Element
	 * @return Arc Object
	 */
	private Arc createArc(Element inputArcElement) {
		String idInput = null;
		String sourceInput = null;
		String targetInput = null;
		LinkedList<Marking> weightInput = new LinkedList<Marking>();
		double inscriptionOffsetXInput = 0;
		double inscriptionOffsetYInput = 0;
		double startX = 0;
		double startY = 0;
		double endX = 0;
		double endY = 0;
		boolean taggedArc;

/*		// Add a default arc weight. This will be an arc weight of 
		// 1 to the first enabled token class in the list. This is
		// normally the "Default" tokenClass
		for (TokenClass tc : tokenClasses) {
			if(tc.isEnabled()){
				Marking m = new Marking(tc, 1);
				weightInput.add(m);
				break;
			}
		}*/
		sourceInput = inputArcElement.getAttribute("source");
		targetInput = inputArcElement.getAttribute("target");
		String idTempStorage = inputArcElement.getAttribute("id");
		String sourceTempStorage = inputArcElement.getAttribute("source");
		String targetTempStorage = inputArcElement.getAttribute("target");
		String inscriptionTempStorage = inputArcElement
				.getAttribute("inscription");
		String taggedTempStorage = inputArcElement.getAttribute("tagged");
		// String inscriptionOffsetXTempStorage =
		// inputArcElement.getAttribute("inscriptionOffsetX");
		// String inscriptionOffsetYTempStorage =
		// inputArcElement.getAttribute("inscriptionOffsetY");

		taggedArc = !(taggedTempStorage.length() == 0 || taggedTempStorage
				.length() == 5);

		if (idTempStorage.length() > 0) {
			idInput = idTempStorage;
		}
		if (sourceTempStorage.length() > 0) {
			sourceInput = sourceTempStorage;
		}
		if (targetTempStorage.length() > 0) {
			targetInput = targetTempStorage;
		}
		if (inscriptionTempStorage.length() > 0) {
			/*
			 * weightInput = Integer .valueOf(
			 * (inputArcElement.getAttribute("inscription") != null ?
			 * inputArcElement .getAttribute("inscription") : "1")).intValue();
			 */

				String[] stringArray = inscriptionTempStorage.split(",");
				// Backward compatibility for pnmls without many token classes
				if(stringArray.length == 1){
					Marking marking = new Marking(getActiveTokenClass(), Integer.valueOf(stringArray[0]));
					weightInput.add(marking);
				}
				else{
					int i = 0;
					while (i < stringArray.length) {
						Marking marking = new Marking(this.getTokenClassFromID(stringArray[i]), Integer
								.valueOf(stringArray[i + 1]));
						weightInput.add(marking);
						i += 2;
					}
				}
		}

		if (sourceInput.length() > 0) {
			if (getPlaceTransitionObject(sourceInput) != null) {
				startX = getPlaceTransitionObject(sourceInput).getPositionX();
				startX += getPlaceTransitionObject(sourceInput)
						.centreOffsetLeft();
				startY = getPlaceTransitionObject(sourceInput).getPositionY();
				startY += getPlaceTransitionObject(sourceInput)
						.centreOffsetTop();
			}
		}
		if (targetInput.length() > 0) {
			if (getPlaceTransitionObject(targetInput) != null) {
				endX = getPlaceTransitionObject(targetInput).getPositionX();
				endY = getPlaceTransitionObject(targetInput).getPositionY();
			}
		}

		PlaceTransitionObject sourceIn = getPlaceTransitionObject(sourceInput);
		PlaceTransitionObject targetIn = getPlaceTransitionObject(targetInput);

		// add the insets and offset
		int aStartx = sourceIn.getX() + sourceIn.centreOffsetLeft();
		int aStarty = sourceIn.getY() + sourceIn.centreOffsetTop();

		int aEndx = targetIn.getX() + targetIn.centreOffsetLeft();
		int aEndy = targetIn.getY() + targetIn.centreOffsetTop();

		double _startx = aStartx;
		double _starty = aStarty;
		double _endx = aEndx;
		double _endy = aEndy;
		// TODO

		Arc tempArc;

		String type = "normal"; // default value
		NodeList nl = inputArcElement.getElementsByTagName("type");
		if (nl.getLength() > 0) {
			type = ((Element) (nl.item(0))).getAttribute("type");
		}

		if (type.equals("inhibitor")) {
			tempArc = ArcFactory.createInhibitorArc(_startx, _starty, _endx, _endy,
					sourceIn, targetIn, weightInput, idInput);
		} else {
			tempArc = ArcFactory.createNormalArc(_startx, _starty, _endx, _endy,
					sourceIn, targetIn, weightInput, idInput, taggedArc);
		}

		getPlaceTransitionObject(sourceInput).addConnectFrom(tempArc);
		getPlaceTransitionObject(targetInput).addConnectTo(tempArc);

		// **********************************************************************************
		// The following section attempts to load and display arcpath
		// details****************

		// NodeList nodelist = inputArcElement.getChildNodes();
		NodeList nodelist = inputArcElement.getElementsByTagName("arcpath");
		if (nodelist.getLength() > 0) {
			tempArc.getArcPath().purgePathPoints();
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);
				if (node instanceof Element) {
					Element element = (Element) node;
					if ("arcpath".equals(element.getNodeName())) {
						String arcTempX = element.getAttribute("x");
						String arcTempY = element.getAttribute("y");
						String arcTempType = element
								.getAttribute("arcPointType");
						float arcPointX = Float.valueOf(arcTempX).floatValue();
						float arcPointY = Float.valueOf(arcTempY).floatValue();
						arcPointX += Constants.ARC_CONTROL_POINT_CONSTANT + 1;
						arcPointY += Constants.ARC_CONTROL_POINT_CONSTANT + 1;
						boolean arcPointType = Boolean.valueOf(arcTempType)
								.booleanValue();
						tempArc.getArcPath().addPoint(arcPointX, arcPointY,
								arcPointType);
					}
				}
			}
		}

		// Arc path creation ends
		// here***************************************************************
		// ******************************************************************************************
		return tempArc;
	}
	private TokenClass createTokenClass(Element inputTokenClassElement) {
		String id = inputTokenClassElement.getAttribute("id");
		boolean booleanEnabled = Boolean.parseBoolean(inputTokenClassElement.getAttribute("enabled"));
		int red = Integer.parseInt(inputTokenClassElement.getAttribute("red"));
		int green = Integer.parseInt(inputTokenClassElement.getAttribute("green"));
		int blue = Integer.parseInt(inputTokenClassElement.getAttribute("blue"));
		Color c = new Color(red, green,blue);
		return new TokenClass(booleanEnabled, id, c);
	}
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#createMatrixes()
	 */
	public void createMatrixes() {
		for(TokenClass tc:tokenClasses){
			if(tc.isEnabled()){
				tc.createIncidenceMatrix(arcsArray, transitionsArray, placesArray);
				tc.createInhibitionMatrix(inhibitorsArray, transitionsArray, placesArray);
			}
		}
		createInitialMarkingVector();
		createCurrentMarkingVector();
		createCapacityVector();
	}

	/**
	 * Creates Initial Marking Vector from current Petri-Net
	 */
	private void createInitialMarkingVector() {
		int placeSize = placesArray.size();

		initialMarkingVector = new LinkedList[placeSize];
		for (int placeNo = 0; placeNo < placeSize; placeNo++) {
			initialMarkingVector[placeNo] = ((Place) placesArray.get(placeNo))
					.getInitialMarking();
		}
	}

	/**
	 * Creates Current Marking Vector from current Petri-Net
	 */
	private void createCurrentMarkingVector() {
		int placeSize = placesArray.size();

		currentMarkingVector = new LinkedList[placeSize];
		for (int placeNo = 0; placeNo < placeSize; placeNo++) {
			currentMarkingVector[placeNo] = ((Place) placesArray.get(placeNo))
					.getCurrentMarking();
		}
	}

	/**
	 * Creates Capacity Vector from current Petri-Net
	 */
	private void createCapacityVector() {
		int placeSize = placesArray.size();

		capacityVector = new int[placeSize];
		for (int placeNo = 0; placeNo < placeSize; placeNo++) {
			capacityVector[placeNo] = ((Place) placesArray.get(placeNo))
					.getCapacity();
		}
	}

	/**
	 * Creates Timed Vector from current Petri-Net
	 */
	private void createTimedVector() {
		int transitionSize = transitionsArray.size();

		timedVector = new boolean[transitionSize];
		for (int transitionNo = 0; transitionNo < transitionSize; transitionNo++) {
			timedVector[transitionNo] = ((Transition) transitionsArray
					.get(transitionNo)).isTimed();
		}
	}

	/**
	 * Creates Priority Vector from current Petri-Net
	 */
	private void createPriorityVector() {
		int transitionSize = transitionsArray.size();

		priorityVector = new int[transitionSize];
		for (int transitionNo = 0; transitionNo < transitionSize; transitionNo++) {
			priorityVector[transitionNo] = ((Transition) transitionsArray
					.get(transitionNo)).getPriority();
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#storeState()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#storeState()
	 */
	public void storeState() {
		int placeSize = placesArray.size();
		markingVectorAnimationStorage = new LinkedList[placeSize];
		for (int placeNo = 0; placeNo < placeSize; placeNo++) {
			markingVectorAnimationStorage[placeNo] = ObjectDeepCopier.mediumCopy(((Place) placesArray
					.get(placeNo)).getCurrentMarking());
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#restoreState()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#restoreState()
	 */
	public void restoreState() {
		if (markingVectorAnimationStorage != null) {
			int placeSize = placesArray.size();
			for (int placeNo = 0; placeNo < placeSize; placeNo++) {
				Place place = ((Place) placesArray.get(placeNo));
				if (place != null) {
					place
							.setCurrentMarking(markingVectorAnimationStorage[placeNo]);
					setChanged();
					notifyObservers(place);
					setMatrixChanged();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#fireTransition(pipe.common.dataLayer.Transition)
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#fireTransition(pipe.common.dataLayer.Transition)
	 */
	public void fireTransition(Transition transition) {
		if (transition != null) {
			// setEnabledTransitions(); NOU-PERE (no es necessari?) No
			if (transition.isEnabled() && placesArray != null) {
				int transitionNo = transitionsArray.indexOf(transition);
				for (int placeNo = 0; placeNo < placesArray.size(); placeNo++) {
					for (Marking m : ((Place) placesArray.get(placeNo))
							.getCurrentMarking()) {
						int oldMarkingPos = getPosInList(m.getTokenClass()
								.getID(), currentMarkingVector[placeNo]);
						int oldMarking = currentMarkingVector[placeNo].get(
								oldMarkingPos).getCurrentMarking();
						int markingToBeAdded = m.getTokenClass()
								.incidenceMatrix
								.get(placeNo, transitionNo);
						m.setCurrentMarking(oldMarking + markingToBeAdded);
					}
					((Place) placesArray.get(placeNo)).repaint();
				}
			}
		}
		setMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getRandomTransition()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#getRandomTransition()
	 */
	public Transition getRandomTransition() {

		setEnabledTransitions();
		// All the enabled transitions are of the same type:
		// a) all are immediate transitions; or
		// b) all are timed transitions.

		ArrayList enabledTransitions = new ArrayList();
		double rate = 0;
		for (int i = 0; i < transitionsArray.size(); i++) {
			Transition transition = (Transition) transitionsArray.get(i);
			if (transition.isEnabled()) {
				enabledTransitions.add(transition);
				rate += transition.getRate();
			}
		}

		// if there is only one enabled transition, return this transition
		if (enabledTransitions.size() == 1) {
			return (Transition) enabledTransitions.get(0);
		}

		double random = randomNumber.nextDouble();
		double x = 0;
		for (int i = 0; i < enabledTransitions.size(); i++) {
			Transition t = (Transition) enabledTransitions.get(i);

			x += t.getRate() / rate;

			if (random < x) {
				return t;
			}
		}

		// no enabled transition found, so no transition can be fired
		return null;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getEnabledTransitions()
	 */
	public ArrayList<Transition> getEnabledTransitions() {
		setEnabledTransitions();
		// All the enabled transitions are of the same type:
		// a) all are immediate transitions; or
		// b) all are timed transitions.

		ArrayList enabledTransitions = new ArrayList();
		for (int i = 0; i < transitionsArray.size(); i++) {
			Transition transition = (Transition) transitionsArray.get(i);
			if (transition.isEnabled()) {
				enabledTransitions.add(transition);
			}
		}

		return enabledTransitions;
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#fireTransitionBackwards(pipe.common.dataLayer.Transition)
	 */

	public void fireTransitionBackwards(Transition transition) {
		if (transition != null) {
			setEnabledTransitionsBackwards();
			if (transition.isEnabled() && placesArray != null) {
				int transitionNo = transitionsArray.indexOf(transition);
				for (int placeNo = 0; placeNo < placesArray.size(); placeNo++) {
					for (Marking m : ((Place) placesArray.get(placeNo))
							.getCurrentMarking()) {
						int oldMarkingPos = getPosInList(m.getTokenClass()
								.getID(), currentMarkingVector[placeNo]);
						int oldMarking = currentMarkingVector[placeNo].get(
								oldMarkingPos).getCurrentMarking();
						int markingToBeSubtracted = m.getTokenClass()
								.incidenceMatrix
								.get(placeNo, transitionNo);
						m.setCurrentMarking(oldMarking - markingToBeSubtracted);
					}
					((Place) placesArray.get(placeNo)).repaint();
				}
			}
		}
		setMatrixChanged();
	}

	/*
	 * Method not used * / public void fireRandomTransitionBackwards() {
	 * setEnabledTransitionsBackwards(); int transitionsSize =
	 * transitionsArray.size() * transitionsArray.size() *
	 * transitionsArray.size(); int randomTransitionNumber = 0; Transition
	 * randomTransition = null; do { randomTransitionNumber =
	 * randomNumber.nextInt(transitionsArray.size()); randomTransition =
	 * (Transition)transitionsArray.get(randomTransitionNumber);
	 * transitionsSize--; if(transitionsSize <= 0){ break; } } while(!
	 * randomTransition.isEnabled()); fireTransitionBackwards(randomTransition);
	 * // System.out.println("Random Fired Transition Backwards" +
	 * ((Transition)transitionsArray.get(randonTransition)).getId()); }
	 */

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#resetEnabledTransitions()
	 */
	public void resetEnabledTransitions() {
		for (int i = 0; i < transitionsArray.size(); i++) {
			Transition transition = (Transition) transitionsArray.get(i);
			transition.setEnabled(false);
			setChanged();
			notifyObservers(transition);
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionEnabledStatus(java.util.LinkedList, int)
	 */
	public boolean getTransitionEnabledStatus(LinkedList<Marking>[] markings,
			int transition) {
		int transCount = this.getTransitionsCount();
		int placeCount = this.getPlacesCount();
		boolean[] result = new boolean[transCount];
		int[][] CMinus;

		// initialise matrix to true
		for (int k = 0; k < transCount; k++) {
			result[k] = true;
		}
		for (int i = 0; i < transCount; i++) {
			for (int j = 0; j < placeCount; j++) {
				boolean allTokenClassesEnabled = true;
				for (Marking m : markings[j]) {
					CMinus = (m.getTokenClass()).getBackwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
					if (m.getCurrentMarking() < CMinus[j][i]) {
						result[i] = false;
						break;
					}
				}
			}
		}

		return result[transition];
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionEnabledStatusArray(java.util.LinkedList)
	 */
	public final boolean[] getTransitionEnabledStatusArray(
			LinkedList<Marking>[] markings) {
		int transitionCount = getTransitionsCount();
		int placeCount = getPlacesCount();
		boolean[] result = new boolean[transitionCount];
		boolean hasTimed = false;
		boolean hasImmediate = false;

		int maxPriority = 0;

		for (int i = 0; i < transitionCount; i++) {
			result[i] = true; // inicialitzam a enabled
			for (int j = 0; j < placeCount; j++) {
				boolean allTokenClassesEnabled = true;
				int totalMarkings = 0;
				int totalForwardIncidenceMarkings = 0;
				int totalBackwardIncidenceMarkings = 0;
				for (Marking m : markings[j]) {
					totalMarkings += m.getCurrentMarking();
					totalForwardIncidenceMarkings += (m.getTokenClass()).forwardsIncidenceMatrix.get(j, i);
					totalBackwardIncidenceMarkings +=(m.getTokenClass()).backwardsIncidenceMatrix.get(j, i);
					if ((m.getCurrentMarking() < (m.getTokenClass())
							.backwardsIncidenceMatrix.get(j, i))
							&& (m.getCurrentMarking() != -1)) {
						allTokenClassesEnabled = false;
						break;

					}
					// inhibitor arcs
					if (m.getTokenClass().inhibitionMatrix.get(j, i) > 0
							&& m.getCurrentMarking() >= m.getTokenClass()
									.inhibitionMatrix.get(j, i)) {
						// an inhibitor arc prevents the firing of this
						// transition so
						// the transition is not enabled
						allTokenClassesEnabled = false;
						break;
					}
				}
				// capacities 
				if (allTokenClassesEnabled && (capacityVector[j] > 0) && (totalMarkings
				  + totalForwardIncidenceMarkings -
				  totalBackwardIncidenceMarkings > capacityVector[j]))
				 { // firing this transition would break a capacity 
				   // restriction so the transition is not enabled
					allTokenClassesEnabled = false; 
				 }
				 

				if (!allTokenClassesEnabled) {
					result[i] = false;
					break;
				}
			}

			// we look for the highest priority of the enabled transitions
			if (result[i] == true) {
				Transition t = (Transition) (transitionsArray.get(i));
				if (t.isTimed() == true) {
					hasTimed = true;
				} else {
					hasImmediate = true;
					if (t.getPriority() > maxPriority) {
						maxPriority = t.getPriority();
					}
				}
			}
		}

		// Now make sure that if any of the enabled transitions are immediate
		// transitions, only they can fire as this must then be a vanishing
		// state.
		// - disable the immediate transitions with lower priority.
		// - disable all timed transitions if there is an immediate transition
		// enabled.
		for (int i = 0; i < transitionCount; i++) {
			Transition t = (Transition) (transitionsArray.get(i));
			if (!t.isTimed() && t.getPriority() < maxPriority) {
				result[i] = false;
			}
			if (hasTimed && hasImmediate) {
				if (t.isTimed() == true) {
					result[i] = false;
				}
			}
		}

		// print("getTransitionEnabledStatusArray: ",result);//debug
		return result;
	}

	// }

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#setEnabledTransitionsBackwards()
	 */
	public void setEnabledTransitionsBackwards() {

		if (currentMarkingVectorChanged) {
			createMatrixes();
		}

		boolean[] enabledTransitions = getTransitionEnabledStatusArray(this
				.getTransitions(), this.getCurrentMarkingVector(), true, this
				.getCapacityVector(), this.getPlacesCount(), this
				.getTransitionsCount());

		for (int i = 0; i < enabledTransitions.length; i++) {
			Transition transition = (Transition) transitionsArray.get(i);
			if (enabledTransitions[i] != transition.isEnabled()) {
				transition.setEnabled(enabledTransitions[i]);
				setChanged();
				notifyObservers(transition);
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#setEnabledTransitions()
	 */
	public void setEnabledTransitions() {
		if (currentMarkingVectorChanged) {
			createMatrixes();
		}

		boolean[] enabledTransitions = getTransitionEnabledStatusArray(this
				.getTransitions(), this.getCurrentMarkingVector(), false, this
				.getCapacityVector(), this.getPlacesCount(), this
				.getTransitionsCount());

		for (int i = 0; i < enabledTransitions.length; i++) {
			Transition transition = (Transition) transitionsArray.get(i);
			if (enabledTransitions[i] != transition.isEnabled()) {
				transition.setEnabled(enabledTransitions[i]);
				setChanged();
				notifyObservers(transition);
			}
		}
	}

	/**
	 * getTransitionEnabledStatusArray() Calculate which transitions are enabled
	 * given a specific marking.
	 * 
	 * @author Matthew Cook (original code), Nadeem Akharware (optimisation)
	 * @author Pere Bonet added inhibitor arcs, place capacities and transition
	 *         priorities
	 * @param int[] the marking
	 * @return boolean[] an array of booleans specifying which transitions are
	 *         enabled in the specified marking
	 */
	private boolean[] getTransitionEnabledStatusArray(

			final Transition[] transArray,
			final LinkedList<Marking>[] markings, boolean backwards,/*
																	 * final
																	 * int[][]
																	 * CMinus,
																	 * final
																	 * int[][]
																	 * CPlus,
																	 * final
																	 * int[][]
																	 * inhibition
																	 * ,
																	 */
			final int capacities[], final int placeCount,
			final int transitionCount) {
		boolean[] result = new boolean[transitionCount];
		boolean hasTimed = false;
		boolean hasImmediate = false;

		int maxPriority = 0;

		for (int i = 0; i < transitionCount; i++) {
			result[i] = true; // inicialitzam a enabled
			for (int j = 0; j < placeCount; j++) {
				boolean allTokenClassesEnabled = true;
				int totalMarkings = 0;
				int totalCPlus = 0;
				int totalCMinus = 0;
				for (Marking m : markings[j]) {
					int[][] CMinus;
					int[][] CPlus;
					int[][] inhibition;
					if (backwards) {
						CMinus = m.getTokenClass().getForwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
						CPlus = m.getTokenClass().getBackwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
					} else {
						CPlus = m.getTokenClass().getForwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
						CMinus = m.getTokenClass().getBackwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
					}
					inhibition = m.getTokenClass().getInhibitionMatrix(inhibitorsArray, transitionsArray, placesArray);

					if ((m.getCurrentMarking() < CMinus[j][i])
							&& (m.getCurrentMarking() != -1)) {
						allTokenClassesEnabled = false;
						break;
					}
					// capacities 
					totalMarkings += m.getCurrentMarking();
					totalCPlus += (m.getTokenClass()).forwardsIncidenceMatrix.get(j, i);
					totalCMinus +=(m.getTokenClass()).backwardsIncidenceMatrix.get(j, i);
					
					if (allTokenClassesEnabled && (capacityVector[j] > 0) && (totalMarkings
					  + totalCPlus -
					  totalCMinus > capacityVector[j]))
					 { // firing this transition would break a capacity 
					   // restriction so the transition is not enabled
						allTokenClassesEnabled = false; 
					 }

					// inhibitor arcs
					if (inhibition[j][i] > 0
							&& m.getCurrentMarking() >= inhibition[j][i]) {
						// an inhibitor arc prevents the firing of this
						// transition
						// so
						// the transition is not enabled
						allTokenClassesEnabled = false;
						break;
					}
				}

				if (!allTokenClassesEnabled) {
					result[i] = false;
					break;
				}
			}
			// we look for the highest priority of the enabled transitions
			if (result[i]) {
				if (transArray[i].isTimed() == true) {
					hasTimed = true;
				} else {
					hasImmediate = true;
					if (transArray[i].getPriority() > maxPriority) {
						maxPriority = transArray[i].getPriority();
					}
				}
			}

		}
		// Now make sure that if any of the enabled transitions are immediate
		// transitions, only they can fire as this must then be a vanishing
		// state.
		// - disable the immediate transitions with lower priority.
		// - disable all timed transitions if there is an immediate transition
		// enabled.
		for (int i = 0; i < transitionCount; i++) {
			if (!transArray[i].isTimed()
					&& transArray[i].getPriority() < maxPriority) {
				result[i] = false;
			}
			if (hasTimed && hasImmediate) {
				if (transArray[i].isTimed() == true) {
					result[i] = false;
				}
			}
		}

		// print("getTransitionEnabledStatusArray: ",result);//debug
		return result;
	}

	/**
	 * Empty all attributes, turn into empty Petri-Net
	 */
	private void emptyPNML() {
		pnmlName = null;
		placesArray = null;
		transitionsArray = null;
		arcsArray = null;
		labelsArray = null;
		rateParametersArray = null;
		changeArrayList = null;
		initialMarkingVector = null;
		tokenClasses = null;
		arcsMap = null;
		initializeMatrices();
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getListPosition(pipe.common.dataLayer.PetriNetObject)
	 */
	public int getListPosition(PetriNetObject pnObject) {

		if (setPetriNetObjectArrayList(pnObject)) {
			return changeArrayList.indexOf(pnObject);
		} else {
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlaces()
	 */
	public Place[] getPlaces() {
		Place[] returnArray = new Place[placesArray.size()];

		for (int i = 0; i < placesArray.size(); i++) {
			returnArray[i] = (Place) placesArray.get(i);
		}
		return returnArray;
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlacesArrayList()
	 */
	public ArrayList<Place> getPlacesArrayList() {
		return placesArray;
	}
	

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlacesCount()
	 */
	public int getPlacesCount() {
		if (placesArray == null) {
			return 0;
		} else {
			return placesArray.size();
		}
	}

	/* wjk added 03/10/2007 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getMarking()
	 */
	public LinkedList<Marking>[] getMarking() {
		LinkedList<Marking>[] result = new LinkedList[placesArray.size()];

		for (int i = 0; i < placesArray.size(); i++) {
			result[i] = (LinkedList<Marking>) ObjectDeepCopier.deepCopy(((Place) placesArray.get(i)).getCurrentMarking());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getLabels()
	 */
	public AnnotationNote[] getLabels() {
		AnnotationNote[] returnArray = new AnnotationNote[labelsArray.size()];

		for (int i = 0; i < labelsArray.size(); i++) {
			returnArray[i] = (AnnotationNote) labelsArray.get(i);
		}
		return returnArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getRateParameters()
	 */
	public RateParameter[] getRateParameters() {
		RateParameter[] returnArray = new RateParameter[rateParametersArray
				.size()];

		for (int i = 0; i < rateParametersArray.size(); i++) {
			returnArray[i] = (RateParameter) rateParametersArray.get(i);
		}
		return returnArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitions()
	 */
	public Transition[] getTransitions() {
		Transition[] returnArray = new Transition[transitionsArray.size()];

		for (int i = 0; i < transitionsArray.size(); i++) {
			returnArray[i] = (Transition) transitionsArray.get(i);
		}
		return returnArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionsArrayList()
	 */
	public ArrayList<Transition> getTransitionsArrayList() {
		return transitionsArray;
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionsCount()
	 */
	public int getTransitionsCount() {
		if (transitionsArray == null) {
			return 0;
		} else {
			return transitionsArray.size();
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getArcs()
	 */
	public Arc[] getArcs() {
		Arc[] returnArray = new Arc[arcsArray.size()];

		for (int i = 0; i < arcsArray.size(); i++) {
			returnArray[i] = (Arc) arcsArray.get(i);
		}
		return returnArray;
	}
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getArcsArrayList()
	 */
	public ArrayList<Arc> getArcsArrayList() {
		return arcsArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getInhibitors()
	 */
	public InhibitorArc[] getInhibitors() {
		InhibitorArc[] returnArray = new InhibitorArc[inhibitorsArray.size()];

		for (int i = 0; i < inhibitorsArray.size(); i++) {
			returnArray[i] = (InhibitorArc) inhibitorsArray.get(i);
		}
		return returnArray;
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getInhibitorsArrayList()
	 */
	public ArrayList<InhibitorArc> getInhibitorsArrayList() {
		return inhibitorsArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionById(java.lang.String)
	 */
	public Transition getTransitionById(String transitionID) {
		Transition returnTransition = null;

		if (transitionsArray != null) {
			if (transitionID != null) {
				for (int i = 0; i < transitionsArray.size(); i++) {
					if (transitionID
							.equalsIgnoreCase(((Transition) transitionsArray
									.get(i)).getId())) {
						returnTransition = (Transition) transitionsArray.get(i);
					}
				}
			}
		}
		return returnTransition;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionByName(java.lang.String)
	 */
	public Transition getTransitionByName(String transitionName) {
		Transition returnTransition = null;

		if (transitionsArray != null) {
			if (transitionName != null) {
				for (int i = 0; i < transitionsArray.size(); i++) {
					if (transitionName
							.equalsIgnoreCase(((Transition) transitionsArray
									.get(i)).getName())) {
						returnTransition = (Transition) transitionsArray.get(i);
					}
				}
			}
		}
		return returnTransition;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransition(int)
	 */
	public Transition getTransition(int transitionNo) {
		Transition returnTransition = null;

		if (transitionsArray != null) {
			if (transitionNo < transitionsArray.size()) {
				returnTransition = (Transition) transitionsArray
						.get(transitionNo);
			}
		}
		return returnTransition;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlaceById(java.lang.String)
	 */
	public Place getPlaceById(String placeID) {
		Place returnPlace = null;

		if (placesArray != null) {
			if (placeID != null) {
				for (int i = 0; i < placesArray.size(); i++) {
					if (placeID.equalsIgnoreCase(((Place) placesArray.get(i))
							.getId())) {
						returnPlace = (Place) placesArray.get(i);
					}
				}
			}
		}
		return returnPlace;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlaceByName(java.lang.String)
	 */
	public Place getPlaceByName(String placeName) {
		Place returnPlace = null;

		if (placesArray != null) {
			if (placeName != null) {
				for (int i = 0; i < placesArray.size(); i++) {
					if (placeName.equalsIgnoreCase(((Place) placesArray.get(i))
							.getName())) {
						returnPlace = (Place) placesArray.get(i);
					}
				}
			}
		}
		return returnPlace;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlace(int)
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#getPlace(int)
	 */
	public Place getPlace(int placeNo) {
		Place returnPlace = null;

		if (placesArray != null) {
			if (placeNo < placesArray.size()) {
				returnPlace = (Place) placesArray.get(placeNo);
			}
		}
		return returnPlace;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlaceTransitionObject(java.lang.String)
	 */
	public PlaceTransitionObject getPlaceTransitionObject(String ptoId) {
		if (ptoId != null) {
			if (getPlaceById(ptoId) != null) {
				return getPlaceById(ptoId);
			} else if (getTransitionById(ptoId) != null) {
				return getTransitionById(ptoId);
			}
		}
		return null;
	}

	



	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getInitialMarkingVector()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#getInitialMarkingVector()
	 */
	public LinkedList<Marking>[] getInitialMarkingVector() {
		if (initialMarkingVectorChanged) {
			createInitialMarkingVector();
		}
		return initialMarkingVector;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getCurrentMarkingVector()
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerSimulationInterface#getCurrentMarkingVector()
	 */
	public LinkedList<Marking>[] getCurrentMarkingVector() {
		if (currentMarkingVectorChanged) {
			createCurrentMarkingVector();
		}
		return currentMarkingVector;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getCapacityVector()
	 */
	public int[] getCapacityVector() {
		createCapacityVector();
		return capacityVector;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPriorityVector()
	 */
	public int[] getPriorityVector() {
		createPriorityVector();
		return priorityVector;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTimedVector()
	 */
	public boolean[] getTimedVector() {
		createTimedVector();
		return timedVector;
	}

	private void setMatrixChanged() {
		for (TokenClass tc : tokenClasses) {
			if (tc.forwardsIncidenceMatrix != null) {
				tc.forwardsIncidenceMatrix.matrixChanged = true;
			}
			if (tc.backwardsIncidenceMatrix != null) {
				tc.backwardsIncidenceMatrix.matrixChanged = true;
			}
			if (tc.incidenceMatrix != null) {
				tc.incidenceMatrix.matrixChanged = true;
			}
			if (tc.inhibitionMatrix != null) {
				tc.inhibitionMatrix.matrixChanged = true;
			}
		}
		initialMarkingVectorChanged = true;
		currentMarkingVectorChanged = true;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#createFromPNML(org.w3c.dom.Document)
	 */
	public void createFromPNML(Document PNMLDoc) {//导入
		emptyPNML();
		Element element = null;
		Node node = null;
		NodeList nodeList = null;

		try {
			nodeList = PNMLDoc.getDocumentElement().getChildNodes();
			if (CreateGui.getApp() != null) {
				// Notifies used to indicate new instances.
				CreateGui.getApp().setMode(Constants.CREATING);
			}
			System.out.println("Loading...");

			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);

				if (node instanceof Element) {
					element = (Element) node;
					if ("labels".equals(element.getNodeName())) {
						addAnnotation(createAnnotation(element));
					} else if ("definition".equals(element.getNodeName())) {
						Note note = createParameter(element);
						if (note instanceof RateParameter) {
							addAnnotation((RateParameter) note);
						}
					} else if ("place".equals(element.getNodeName())) {
						addPlace(createPlace(element));
					} else if ("transition".equals(element.getNodeName())) {
						addTransition(createTransition(element));
					} else if ("arc".equals(element.getNodeName())) {
						Arc newArc = createArc(element);
						if (newArc instanceof InhibitorArc) {
							addArc((InhibitorArc) newArc);
						} else {
							addArc((NormalArc) newArc);
							checkForInverseArc((NormalArc) newArc);
						}
					} else if ("stategroup".equals(element.getNodeName())) {
						addStateGroup(createStateGroup(element));
					} else if ("tokenclass".equals(element.getNodeName())) {
						addTokenClass(createTokenClass(element));
					} else {
						System.out.println("!" + element.getNodeName());
					}
				}
			}

			if (CreateGui.getApp() != null) {
				CreateGui.getApp().restoreMode();
			}
			System.out.println("Done");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a StateGroup object from a DOM element
	 * 
	 * @param inputStateGroupElement
	 *            input state group DOM Element
	 * @return StateGroup Object
	 */
	private StateGroup createStateGroup(Element inputStateGroupElement) {
		// Create the state group with name and id
		String id = inputStateGroupElement.getAttribute("id");
		String name = inputStateGroupElement.getAttribute("name");
		StateGroup newGroup = new StateGroup(id, name);

		Node node = null;
		NodeList nodelist = null;
		StringTokenizer tokeniser;
		nodelist = inputStateGroupElement.getChildNodes();

		// If this state group contains states then add them
		if (nodelist.getLength() > 0) {
			for (int i = 1; i < nodelist.getLength() - 1; i++) {
				node = nodelist.item(i);
				if (node instanceof Element) {
					Element element = (Element) node;
					if ("statecondition".equals(element.getNodeName())) {
						// Loads the condition in the form "P0 > 4"
						String condition = element.getAttribute("value");
						// Now we tokenise the elements of the condition
						// (i.e. "P0" ">" "4") to create a state
						tokeniser = new StringTokenizer(condition);
						newGroup.addState(tokeniser.nextToken(), tokeniser
								.nextToken(), tokeniser.nextToken());
					}
				}
			}
		}
		return newGroup;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getStateGroups()
	 */
	public StateGroup[] getStateGroups() {
		StateGroup[] returnArray = new StateGroup[stateGroups.size()];
		for (int i = 0; i < stateGroups.size(); i++) {
			returnArray[i] = (StateGroup) stateGroups.get(i);
		}
		return returnArray;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getStateGroupsArray()
	 */
	public ArrayList<StateGroup> getStateGroupsArray()
	{
		return this.stateGroups;
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getURI()
	 */
	public String getURI() {
		return pnmlName;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#print()
	 */
	public void print() {
		System.out.println("No of Places = " + placesArray.size() + "\"");
		System.out.println("No of Transitions = " + transitionsArray.size()
				+ "\"");
		System.out.println("No of Arcs = " + arcsArray.size() + "\"");
		System.out.println("No of Labels = " + labelsArray.size()
				+ "\" (Model View Controller Design Pattern)");
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#existsRateParameter(java.lang.String)
	 */
	public boolean existsRateParameter(String name) {
		return rateParameterHashSet.contains(name);
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#changeRateParameter(java.lang.String, java.lang.String)
	 */
	public boolean changeRateParameter(String oldName, String newName) {
		if (rateParameterHashSet.contains(newName)) {
			return false;
		}
		rateParameterHashSet.remove(oldName);
		rateParameterHashSet.add(newName);
		return true;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#hasTimedTransitions()
	 */
	public boolean hasTimedTransitions() {
		Transition[] transitions = this.getTransitions();
		int transCount = transitions.length;

		for (int i = 0; i < transCount; i++) {
			if (transitions[i].isTimed() == true) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#hasImmediateTransitions()
	 */
	public boolean hasImmediateTransitions() {
		Transition[] transitions = this.getTransitions();
		int transCount = transitions.length;

		for (int i = 0; i < transCount; i++) {
			if (transitions[i].isTimed() == false) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#isTangibleState(java.util.LinkedList)
	 */
	public boolean isTangibleState(LinkedList<Marking>[] marking) {
		Transition[] trans = this.getTransitions();
		int numTrans = trans.length;
		boolean hasTimed = false;
		boolean hasImmediate = false;

		for (int i = 0; i < numTrans; i++) {
			if (this.getTransitionEnabledStatus(marking, i) == true) {
				if (trans[i].isTimed() == true) {
					// If any immediate transtions exist, the state is vanishing
					// as they will fire immediately
					hasTimed = true;
				} else if (trans[i].isTimed() != true) {
					hasImmediate = true;
				}
			}
		}
		return (hasTimed == true && hasImmediate == false);
	}

	private void checkForInverseArc(NormalArc newArc) {
		Iterator iterator = newArc.getSource().getConnectToIterator();

		Arc anArc;
		while (iterator.hasNext()) {
			anArc = (Arc) iterator.next();
			if (anArc.getTarget() == newArc.getSource()
					&& anArc.getSource() == newArc.getTarget()) {
				if (anArc.getClass() == NormalArc.class) {
					if (!newArc.hasInverse()) {
						((NormalArc) anArc).setInverse(newArc,
								Constants.JOIN_ARCS);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getTransitionName(int)
	 */
	public String getTransitionName(int i) {
		return ((Transition) transitionsArray.get(i)).getName();
	}

	// Function to check the structure of the Petri Net to ensure that if tagged
	// arcs are included then they obey the restrictions on how they can be used
	// (i.e. a transition may only have one input tagged Arc and one output
	// tagged Arc and if it has one it must have the other).
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#validTagStructure()
	 */
	public boolean validTagStructure() {
		ArrayList inputArcsArray = new ArrayList();
		ArrayList outputArcsArray = new ArrayList();

		Transition currentTrans = null;
		NormalArc currentArc = null;

		boolean taggedNet = false;
		boolean taggedTransition = false;
		boolean taggedInput = false;
		boolean taggedOutput = false;
		boolean validStructure = true;
		String checkResult = null;
		int noTaggedInArcs = 0;
		int noTaggedOutArcs = 0;

		checkResult = "Tagged structure validation result:\n";

		if (transitionsArray != null && transitionsArray.size() > 0) {
			// we need to check all the arcs....
			for (int i = 0; i < transitionsArray.size(); i++) {
				currentTrans = (Transition) transitionsArray.get(i);
				taggedTransition = false;
				taggedInput = false;
				taggedOutput = false;
				// invalidStructure = false;
				noTaggedInArcs = 0;
				noTaggedOutArcs = 0;
				inputArcsArray.clear();
				outputArcsArray.clear();

				// we must:
				// i) find the arcs attached to this transition
				// ii) determine whether they are input arcs or output arcs
				// iii) check that if there is one tagged input arc there is
				// also
				// one output arc

				if (arcsArray != null && arcsArray.size() > 0) {
					for (int j = 0; j < arcsArray.size(); j++) {
						currentArc = (NormalArc) arcsArray.get(j);
						if (currentArc.getSource() == currentTrans) {
							outputArcsArray.add(currentArc);
							if (currentArc.isTagged()) {
								taggedNet = true;
								taggedTransition = true;
								taggedOutput = true;
								noTaggedOutArcs++;
								if (noTaggedOutArcs > 1) {
									checkResult = checkResult + "  Transition "
											+ currentTrans.getName()
											+ " has more than one"
											+ " tagged output arc\n";
									validStructure = false;
								}
							}
						} else if (currentArc.getTarget() == currentTrans) {
							inputArcsArray.add(currentArc);
							if (currentArc.isTagged()) {
								taggedNet = true;
								taggedTransition = true;
								taggedInput = true;
								noTaggedInArcs++;
								if (noTaggedInArcs > 1) {
									checkResult = checkResult + "  Transition "
											+ currentTrans.getName()
											+ " has more than one"
											+ " tagged input arc\n";
									validStructure = false;
								}
							}
						}
					}
				}

				// we have now built lists of input arcs and output arcs and
				// verified that there is at most one of each.
				// we must check, however, that if there is a tagged input there
				// is
				// a tagged output and vice-versa
				if (taggedTransition) {
					if ((taggedInput && !taggedOutput)
							|| (!taggedInput && taggedOutput)) {
						checkResult = checkResult + "  Transition "
								+ currentTrans.getName()
								+ " does not have matching tagged arcs\n";
						validStructure = false;
					}
				}
			}
		}

		// if we reach the end with validStructure still true then everything
		// must
		// be OK!
		if (validStructure) {
			// System.out.println("Tagged arc structure is valid");
			checkResult = "Tagged structure validation result:\n  Tagged arc structure is valid\n";
			JOptionPane.showMessageDialog(null, checkResult,
					"Validation Results", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, checkResult,
					"Validation Results", JOptionPane.ERROR_MESSAGE);
		}

		// System.out.println(checkResult);

		return validStructure;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#checkTransitionIDAvailability(java.lang.String)
	 */
	public boolean checkTransitionIDAvailability(String newName) {
		for (int i = 0; i < transitionsArray.size(); i++) {
			if (((Transition) (transitionsArray.get(i))).getId()
					.equals(newName)) {
				// ID/name isn't available
				return false;
			}
		}
		// ID/name is available
		return true;
	}

	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#checkPlaceIDAvailability(java.lang.String)
	 */
	public boolean checkPlaceIDAvailability(String newName) {
		for (int i = 0; i < placesArray.size(); i++) {
			if (((Place) (placesArray.get(i))).getId().equals(newName)) {
				// ID/name isn't available
				return false;
			}
		}
		// ID/name is available
		return true;
	}


	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#getPlaceIndex(java.lang.String)
	 */
	public int getPlaceIndex(String placeName){
		int index = -1;
		for(int i=0; i<placesArray.size(); i++) {
			if(((Place)placesArray.get(i)).getId()==placeName)
			{
				index = i;
				break;
			}
		}
		//		System.out.println("Returning " + index);

		return index;
	}

	// Added for passage time analysis of tagged nets
	/*use to check if structure contain any tagged token or tagged arc, then the structure
	 * needs to be validated before animation
	 */
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#hasValidatedStructure()
	 */
	public boolean hasValidatedStructure()
	{
	
		boolean tagged = false;
		
		for (int i = 0; i < this.placesArray.size(); i++)
		{
			if (  ((Place) this.placesArray.get(i)).isTagged()) tagged = true;
		}
		
		for (int i = 0; i < this.arcsArray.size(); i++)
		{
			if (  ((Arc) this.arcsArray.get(i)).isTagged()) tagged = true;
		}
			
		if(tagged && validated)return true;
		else if( !tagged ) return true;
		else return false;
			
				
		
	}
	
	/* (non-Javadoc)
	 * @see pipe.common.dataLayer.DataLayerInterface#setValidate(boolean)
	 */
	public void setValidate(boolean valid){
		validated = valid;
	}

}
