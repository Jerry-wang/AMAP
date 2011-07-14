package pipe.common.dataLayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;

public interface DataLayerInterface {

	/**
	 * Method to clone a DataLayer obejct
	 */
	public abstract DataLayerInterface clone();

	public abstract void setTokenClasses(LinkedList<TokenClass> tokenclasses);

	public abstract LinkedList<TokenClass> getTokenClasses();

	public abstract TokenClass getActiveTokenClass();

	public abstract void setActiveTokenClass(TokenClass tc);

	public abstract void lockTokenClass(String id);

	public abstract void unlockTokenClass(String id);

	public abstract int getPosInList(String tokenClassID,
			LinkedList<Marking> markings);

	public abstract TokenClass getTokenClassFromID(String id);

	/**
	 * Add arcInput to back of the Arc ArrayList All observers are notified of
	 * this change (Model-View Architecture)
	 * 
	 * @param arcInput
	 *            Arc Object to add
	 */
	public abstract void addArc(NormalArc arcInput);

	/**
	 * Add inhibitorArcInput to back of the InhibitorArc ArrayList All observers
	 * are notified of this change (Model-View Architecture)
	 * 
	 * @param arcInput
	 *            Arc Object to add
	 */
	public abstract void addArc(InhibitorArc inhibitorArcInput);

	public abstract void addStateGroup(StateGroup stateGroupInput);

	/**
	 * Add any PetriNetObject - the object will be added to the appropriate
	 * list. If the object passed in isn't a Transition, Place or Arc nothing
	 * will happen. All observers are notified of this change.
	 * 
	 * @param pnObject
	 *            The PetriNetObject to be added.
	 */
	public abstract void addPetriNetObject(PetriNetObject pnObject);

	/**
	 * Removes the specified object from the appropriate ArrayList of objects.
	 * All observers are notified of this change.
	 * 
	 * @param pnObject
	 *            The PetriNetObject to be removed.
	 */
	public abstract void removePetriNetObject(PetriNetObject pnObject);

	/**
	 * This method removes a state group from the arrayList
	 * 
	 * @param SGObject
	 *            The State Group objet to be removed
	 */
	public abstract void removeStateGroup(StateGroup SGObject);

	/**
	 * Checks whether a state group with the same name exists already as the
	 * argument * @param stateName
	 * 
	 * @return
	 */
	public abstract boolean stateGroupExistsAlready(String stateName);

	/**
	 * Returns an iterator for the transitions array. Used by Animator.class to
	 * set all enabled transitions to highlighted
	 */
	public abstract Iterator returnTransitions();

	/**
	 * Returns an iterator of all PetriNetObjects - the order of these cannot be
	 * guaranteed.
	 * 
	 * @return An iterator of all PetriNetObjects
	 */
	public abstract Iterator getPetriNetObjects();

	public abstract boolean hasPlaceTransitionObjects();

	/**
	 * Creates all Petri-Net Matrixes from current Petri-Net
	 */
	public abstract void createMatrixes();

	/**
	 * Stores Current Marking
	 */
	public abstract void storeState();

	/**
	 * Restores To previous Stored Marking
	 */
	public abstract void restoreState();

	/**
	 * Fire a specified transition, no affect if transtions not enabled
	 * 
	 * @param transition
	 *            Reference of specifiec Transition
	 */
	public abstract void fireTransition(Transition transition);

	/**
	 * Gets a random transition (enabled), takes rate (probability) of
	 * Transitions into account
	 */
	public abstract Transition getRandomTransition();

	/**
	 * Gets all enabled transitions (enabled)
	 */
	public abstract ArrayList<Transition> getEnabledTransitions();

	/**
	 * This method will fire a random transition, and gives precedence to
	 * immediate transitions before considering "timed" transitions. The "rate"
	 * property of the transition is used as a weighting factor so the
	 * probability of selecting a transition is the rate of that transition
	 * divided by the sum of the weights of the other enabled transitions of its
	 * class. The "rate" property can now be used to give priority among several
	 * enabled, immediate transitions, or when there are no enabled, immediate
	 * transitions to give priority among several enabled, "timed" transitions.
	 * 
	 * Note: in spite of the name "timed" there is no probabilistic rate
	 * calculated -- just a weighting factor among similar transitions.
	 * 
	 * Changed by David Patterson Jan 2, 2006
	 * 
	 * Changed by David Patterson Apr 24, 2007 to clean up problems caused by
	 * fractional rates, and to speed up processing when only one transition of
	 * a kind is enabled.
	 * 
	 * Changed by David Patterson May 10, 2007 to properly handle fractional
	 * weights for immeditate transitions.
	 * 
	 * THe same logic is also used for timed transitions until the exponential
	 * distribution is added. When that happens, the code will only be used for
	 * immediate transitions. / public Transition fireRandomTransition() {
	 * Transition result = null; Transition t; setEnabledTransitions(); // int
	 * transitionsSize =
	 * transitionsArray.size()*transitionsArray.size()*transitionsArray.size();
	 * int transitionNo = 0;
	 * 
	 * double rate = 0.0d; double sumOfImmedWeights = 0.0d; double
	 * sumOfTimedWeights = 0.0d; ArrayList timedTransitions = new ArrayList();
	 * // ArrayList<Transition> ArrayList immedTransitions = new ArrayList(); //
	 * ArrayList<Transition>
	 * 
	 * for(transitionNo = 0 ; transitionNo < transitionsArray.size() ;
	 * transitionNo++){ t = (Transition) transitionsArray.get( transitionNo );
	 * rate = t.getRate(); if ( t.isEnabled()) { if ( t.isTimed() ) { // is it a
	 * timed transition timedTransitions.add( t ); sumOfTimedWeights += rate; }
	 * else { // immediate transition immedTransitions.add( t );
	 * sumOfImmedWeights += rate; } } // end of if isEnabled } // end of for
	 * transitionNo
	 * 
	 * // Now, if there are immediate transitions, pick one // next block
	 * changed by David Patterson to fix bug int count =
	 * immedTransitions.size(); switch ( count ) { case 0: // no immediate
	 * transitions break; // skip out case 1: // only one immediate transition
	 * result = (Transition) immedTransitions.get( 0 ); break; // skip out
	 * default: // several immediate transitions double rval = sumOfImmedWeights
	 * * randomNumber.nextDouble(); for ( int index = 0; index < count; index++
	 * ) { t = (Transition) immedTransitions.get( index ); rval -= t.getRate();
	 * if ( rval <= 0.0d ) { result = t; break; } } } if ( result == null ) { //
	 * no immediate transition found count = timedTransitions.size(); // count
	 * of timed, enabled transitions switch( count ) { case 0: // trouble! No
	 * enabled transition found break; case 1: // only one timed transition
	 * result = ( Transition ) timedTransitions.get( 0 ); break; default: //
	 * several timed transitions -- for now, pick one double rval =
	 * sumOfTimedWeights * randomNumber.nextDouble(); for ( int index = 0; index
	 * < count; index++ ) { t = (Transition) timedTransitions.get( index ); rval
	 * -= t.getRate(); if ( rval <= 0.0d ) { result = t; break; } } } }
	 * 
	 * if ( result == null ) { System.out.println(
	 * "no random transition to fire" ); } else { fireTransition(result);
	 * createCurrentMarkingVector(); } resetEnabledTransitions(); return result;
	 * } // end of method fireRandomTransition
	 */

	public abstract void fireTransitionBackwards(Transition transition);

	public abstract void resetEnabledTransitions();

	/**
	 * Calculate whether a transition is enabled given a specific marking
	 * 
	 * @param DataLayer
	 *            - the net
	 * @param int[] - the marking
	 * @param int - the specific transition to test for enabled status
	 * @return boolean - an array of booleans specifying which transitions are
	 *         enabled in the specified marking
	 */
	public abstract boolean getTransitionEnabledStatus(
			LinkedList<Marking>[] markings, int transition);

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
	public abstract boolean[] getTransitionEnabledStatusArray(
			LinkedList<Marking>[] markings);

	/**
	 * Determines whether all transitions are enabled and sets the correct value
	 * of the enabled boolean
	 */
	public abstract void setEnabledTransitionsBackwards();

	/**
	 * Determines whether all transitions are enabled and sets the correct value
	 * of the enabled boolean
	 */
	public abstract void setEnabledTransitions();

	/**
	 * Get position of Petri-Net Object in ArrayList of given Petri-Net Object's
	 * type
	 * 
	 * @param pnObject
	 *            PlaceTransitionObject to get the position of
	 * @return Position (-1 if not present) of Petri-Net Object in ArrayList of
	 *         given Petri-Net Object's type
	 */
	public abstract int getListPosition(PetriNetObject pnObject);

	/**
	 * Get a List of all the Place objects in the Petri-Net
	 * 
	 * @return A List of all the Place objects
	 */
	public abstract Place[] getPlaces();

	/**
	 * Get an ArrayList of all the Place objects in the Petri-Net
	 * 
	 * @return A ArrayList of all the Place objects
	 */
	public abstract ArrayList<Place> getPlacesArrayList();

	public abstract int getPlacesCount();

	/* wjk added 03/10/2007 */
	/**
	 * Get the current marking of the Petri net
	 * 
	 * @return The current marking of the Petri net
	 */
	public abstract LinkedList<Marking>[] getMarking();

	/**
	 * Get a List of all the net-level NameLabel objects in the Petri-Net
	 * 
	 * @return A List of all the net-level (as opposed to element-specific)
	 *         label objects
	 */
	public abstract AnnotationNote[] getLabels();

	/**
	 * Get a List of all the marking Parameter objects in the Petri-Net
	 * 
	 * @return A List of all the marking Parameter objects
	 */
	public abstract RateParameter[] getRateParameters();

	/**
	 * Get an List of all the Transition objects in the Petri-Net
	 * 
	 * @return An List of all the Transition objects
	 */
	public abstract Transition[] getTransitions();

	/**
	 * Get an ArrayList of all the Transition objects in the Petri-Net
	 * 
	 * @return An List of all the Transition objects
	 */
	public abstract ArrayList<Transition> getTransitionsArrayList();

	public abstract int getTransitionsCount();

	/**
	 * Get an List of all the Arcs objects in the Petri-Net
	 * 
	 * @return An List of all the Arc objects
	 */
	public abstract Arc[] getArcs();

	/**
	 * Get an ArrayList of all the Arcs objects in the Petri-Net
	 * 
	 * @return An List of all the Arc objects
	 */
	public abstract ArrayList<Arc> getArcsArrayList();

	/**
	 * Get an List of all the InhibitorArc objects in the Petri-Net
	 * 
	 * @return An List of all the InhibitorArc objects
	 */
	public abstract InhibitorArc[] getInhibitors();

	/**
	 * Get an ArrayList of all the InhibitorArc objects in the Petri-Net
	 * 
	 * @return An ArrayList of all the InhibitorArc objects
	 */
	public abstract ArrayList<InhibitorArc> getInhibitorsArrayList();

	/**
	 * Return the Transition called transitionName from the Petri-Net
	 * 
	 * @param transitionID
	 *            ID of Transition object to return
	 * @return The first Transition object found with a name equal to
	 *         transitionName
	 */
	public abstract Transition getTransitionById(String transitionID);

	/**
	 * Return the Transition called transitionName from the Petri-Net
	 * 
	 * @param transitionName
	 *            Name of Transition object to return
	 * @return The first Transition object found with a name equal to
	 *         transitionName
	 */
	public abstract Transition getTransitionByName(String transitionName);

	/**
	 * Return the Transition called transitionName from the Petri-Net
	 * 
	 * @param transitionNo
	 *            No of Transition object to return
	 * @return The Transition object
	 */
	public abstract Transition getTransition(int transitionNo);

	/**
	 * Return the Place called placeName from the Petri-Net
	 * 
	 * @param placeId
	 *            ID of Place object to return
	 * @return The first Place object found with id equal to placeId
	 */
	public abstract Place getPlaceById(String placeID);

	/**
	 * Return the Place called placeName from the Petri-Net
	 * 
	 * @param placeName
	 *            Name of Place object to return
	 * @return The first Place object found with a name equal to placeName
	 */
	public abstract Place getPlaceByName(String placeName);

	/**
	 * Return the Place called placeName from the Petri-Net
	 * 
	 * @param placeNo
	 *            No of Place object to return
	 * @return The Place object
	 */
	public abstract Place getPlace(int placeNo);

	/**
	 * Return the PlaceTransitionObject called ptoName from the Petri-Net
	 * 
	 * @param ptoId
	 *            Id of PlaceTransitionObject object to return
	 * @return The first Arc PlaceTransitionObject found with a name equal to
	 *         ptoName
	 */
	public abstract PlaceTransitionObject getPlaceTransitionObject(String ptoId);

	/**
	 * Return the Initial Marking Vector for the Petri-Net
	 * 
	 * @return The Initial Marking Vector for the Petri-Net
	 */
	public abstract LinkedList<Marking>[] getInitialMarkingVector();

	/**
	 * Return the Initial Marking Vector for the Petri-Net
	 * 
	 * @return The Initial Marking Vector for the Petri-Net
	 */
	public abstract LinkedList<Marking>[] getCurrentMarkingVector();

	/**
	 * Return the capacity Matrix for the Petri-Net
	 * 
	 * @return The capacity Matrix for the Petri-Net
	 */
	public abstract int[] getCapacityVector();

	/**
	 * Return the capacity Matrix for the Petri-Net
	 * 
	 * @return The capacity Matrix for the Petri-Net
	 */
	public abstract int[] getPriorityVector();

	/**
	 * Return the capacity Matrix for the Petri-Net
	 * 
	 * @return The capacity Matrix for the Petri-Net
	 */
	public abstract boolean[] getTimedVector();

	/**
	 * Create model from transformed PNML file
	 * 
	 * @author Ben Kirby, 10 Feb 2007
	 * @param filename
	 *            URI location of PNML
	 * 
	 * @author Edwin Chung This code is modified so that dataLayer objects can
	 *         be created outside the GUI
	 */
	public abstract void createFromPNML(Document PNMLDoc);

	public abstract StateGroup[] getStateGroups();

	public abstract ArrayList<StateGroup> getStateGroupsArray();

	/**
	 * Return a URI for the PNML file for the Petri-Net
	 * 
	 * @return A DOM for the Petri-Net
	 */
	public abstract String getURI();

	/** prints out a brief representation of the dataLayer object */
	public abstract void print();

	public abstract boolean existsRateParameter(String name);

	public abstract boolean changeRateParameter(String oldName, String newName);

	/**
	 * See if the supplied net has any timed transitions.
	 * 
	 * @param DataLayer
	 * @return boolean
	 * @author Matthew
	 */
	public abstract boolean hasTimedTransitions();

	/**
	 * See if the net has any timed transitions.
	 * 
	 * @return boolean
	 * @author Matthew
	 */
	public abstract boolean hasImmediateTransitions();

	/**
	 * Work out if a specified marking describes a tangible state. A state is
	 * either tangible (all enabled transitions are timed) or vanishing (there
	 * exists at least one enabled state that is transient, i.e. untimed). If an
	 * immediate transition exists, it will automatically fire before a timed
	 * transition.
	 * 
	 * @param DataLayer
	 *            - the net to be tested
	 * @param int[] - the marking of the net to be tested
	 * @return boolean - is it tangible or not
	 */
	public abstract boolean isTangibleState(LinkedList<Marking>[] marking);

	public abstract String getTransitionName(int i);

	// Function to check the structure of the Petri Net to ensure that if tagged
	// arcs are included then they obey the restrictions on how they can be used
	// (i.e. a transition may only have one input tagged Arc and one output
	// tagged Arc and if it has one it must have the other).
	public abstract boolean validTagStructure();

	public abstract boolean checkTransitionIDAvailability(String newName);

	public abstract boolean checkPlaceIDAvailability(String newName);

	public abstract int getPlaceIndex(String placeName);

	// Added for passage time analysis of tagged nets
	/*use to check if structure contain any tagged token or tagged arc, then the structure
	 * needs to be validated before animation
	 */
	public abstract boolean hasValidatedStructure();

	public abstract void setValidate(boolean valid);

}