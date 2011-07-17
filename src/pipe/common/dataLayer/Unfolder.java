package pipe.common.dataLayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;



public class Unfolder {
	private DataLayerInterface model;
	TokenClass defaultTokenClass;

	ArrayList<Place> newPlaces;
	ArrayList<Arc> newArcs;
	ArrayList<Transition> newTransitions;

	public Unfolder(DataLayerInterface model) {
		this.model = model;
		boolean foundDefaultClass = false;
		// Set default token class
		for (TokenClass tc : model.getTokenClasses()) {
			if (tc.getID().equals("Default")) {
				foundDefaultClass = true;
				defaultTokenClass = tc;
				break;
			}
		}
		// If there is no token class with name Default
		// then select class which has a token colour of black
		if (!foundDefaultClass) {
			for (TokenClass tc : model.getTokenClasses()) {
				if (tc.getColour().getBlue() == 0
						&& tc.getColour().getGreen() == 0
						&& tc.getColour().getRed() == 0) {
					foundDefaultClass = true;
					defaultTokenClass = tc;
					break;
				}
			}

		}

		// If no such class exists then set default class
		// to the first enabled token class.
		if (!foundDefaultClass) {
			for (TokenClass tc : model.getTokenClasses()) {
				if (tc.isEnabled()) {
					defaultTokenClass = tc;
					foundDefaultClass = true;
					break;
				}
			}
		}
	}

	public DataLayer unfold() {
		unfoldTransitions();
		DataLayer newDataLayer = createDataLayer();
		// saveAsXml(newDataLayer);
		return newDataLayer;
	}

	// Will iterate through each transition, analyse its input and output arcs
	// and create new places and connecting arcs as necessary.
	private void unfoldTransitions() {
		// Get current transitions. These should remain as is (remember they are
		// ungrouped
		// and hence number of transitions in folded model = no. of transitions
		// in unfolded model)
		Transition[] transitions = model.getTransitions();
		newPlaces = new ArrayList<Place>();
		newArcs = new ArrayList<Arc>();
		newTransitions = new ArrayList<Transition>();
		for (int i = 0; i < transitions.length; i++) {

			// Create a copy of existing transition and add it to the list
			double transPositionXInput = transitions[i].getPositionX();
			double transPositionYInput = transitions[i].getPositionY();
			String transIdInput = transitions[i].getId();
			double transNameOffsetXInput = transitions[i].nameOffsetX;
			double transNameOffsetYInput = transitions[i].nameOffsetY;
			double rateInput = transitions[i].getRate();  
			boolean timedTransition = transitions[i].isTimed();
			boolean deterministicTransition = transitions[i].isDeterministic();
			boolean infServer = transitions[i].isInfiniteServer();
			int angleInput = transitions[i].getAngle();
			int priority = transitions[i].getPriority();
			double delay = transitions[i].getDelay();
			Transition newTransition = TransitionFactory.createTransition(transPositionXInput, transPositionYInput, transIdInput,
					transIdInput, transNameOffsetXInput, transNameOffsetYInput, rateInput, timedTransition, deterministicTransition, infServer,
					angleInput, priority,delay);
			newTransitions.add(newTransition);

			// Now analyse all arcs connected to this transition
			Iterator<?> arcsFromIterator = transitions[i].getArcsFrom();
			Iterator<?> arcsToIterator = transitions[i].getArcsTo();
			while (arcsFromIterator.hasNext()) {
				Arc arcFrom = (Arc) arcsFromIterator.next();
				Place oldPlace = (Place) arcFrom.getTarget();
				String newPlaceName = oldPlace.getId();
				int newMarking = 0;
				int newArcWeight = 0;
				for (Marking m : arcFrom.getWeight()) {
					if (m.getCurrentMarking() > 0) {
						newPlaceName += "_" + m.getTokenClass().getID();
						// Now calculate marking of new place to be created
						for (Marking placeMarking : oldPlace
								.getCurrentMarking()) {
							if (m.getTokenClass().getID().equals(
									placeMarking.getTokenClass().getID())) {
								newMarking = placeMarking.getCurrentMarking();
								newArcWeight = m.getCurrentMarking();
							}
						}
					}
				}

				/*
				 * if(newMarkings.isEmpty()){ newMarking = 0; } else{ newMarking
				 * = newMarkings.get(0); for(int j = 1; j < newMarkings.size();
				 * j++){ // Marking of new place is equal to the minimum of
				 * newMarkings newMarking = Math.min(newMarking,
				 * newMarkings.get(j)); } }
				 */

				Place newPlace = null;
				for (Place p : newPlaces) {
					if (p.getId().equals(newPlaceName)) {
						newPlace = p;
					}
				}

				if (newPlace == null) {
					// Create a new place
					double positionXInput = transitions[i].getPositionX();
					double positionYInput = oldPlace.getPositionY();
					Double nameOffsetXInput = oldPlace.getNameOffsetX();
					Double nameOffsetYInput = oldPlace.getNameOffsetYObject();
					LinkedList<Marking> markingInput = new LinkedList<Marking>();
					markingInput
							.add(new Marking(defaultTokenClass, newMarking));
					double markingOffsetXInput = oldPlace
							.getMarkingOffsetXObject();
					double markingOffsetYInput = oldPlace
							.getMarkingOffsetYObject();
					int capacityInput = oldPlace.getCapacity();

					newPlace = PlaceFactory.createPlace(positionXInput, positionYInput, newPlaceName,
							newPlaceName, nameOffsetXInput, nameOffsetYInput, markingInput, markingOffsetXInput,
							markingOffsetYInput, capacityInput);
					newPlaces.add(newPlace);
				}

				// Create a new Arc
				double startPositionXInput = arcFrom.getStartPositionX();
				double startPositionYInput = arcFrom.getStartPositionY();
				double endPositionXInput = newPlace.getPositionX();
				double endPositionYInput = newPlace.getPositionY();
				;
				PlaceTransitionObject source = newTransition;
				PlaceTransitionObject target = newPlace;
				LinkedList<Marking> weight = new LinkedList<Marking>();
				weight.add(new Marking(defaultTokenClass, newArcWeight));
				String idInput = arcFrom.getId();

				Arc newArc = ArcFactory.createNormalArc(startPositionXInput, startPositionYInput, endPositionXInput, endPositionYInput,
						source, target, weight, idInput, false);

				// Join arc, place and transition and add all to appropriate
				// lists
				newPlace.addConnectTo(newArc);
				newTransition.addConnectFrom(newArc);
				newArcs.add(newArc);

			}

			// Now do exactly the same for the arcs entering the transition
			while (arcsToIterator.hasNext()) {
				Arc arcTo = (Arc) arcsToIterator.next();
				Place oldPlace = (Place) arcTo.getSource();
				String newPlaceName = oldPlace.getId();
				int newMarking = 0;
				int newArcWeight = 0;
				for (Marking m : arcTo.getWeight()) {
					if (m.getCurrentMarking() > 0) {
						newPlaceName += "_" + m.getTokenClass().getID();
						// Now calculate marking of new place to be created
						for (Marking placeMarking : oldPlace
								.getCurrentMarking()) {
							if (m.getTokenClass().getID().equals(
									placeMarking.getTokenClass().getID())) {
								newMarking = placeMarking.getCurrentMarking();
								newArcWeight = m.getCurrentMarking();
							}
						}
					}
				}

				Place newPlace = null;
				for (Place p : newPlaces) {
					if (p.getId().equals(newPlaceName)) {
						newPlace = p;
					}
				}
				if (newPlace == null) {
					// Create a new place
					double positionXInput = transitions[i].getPositionX();
					double positionYInput = oldPlace.getPositionY();
					Double nameOffsetXInput = oldPlace.getNameOffsetX();
					Double nameOffsetYInput = oldPlace.getNameOffsetYObject();
					LinkedList<Marking> markingInput = new LinkedList<Marking>();
					markingInput
							.add(new Marking(defaultTokenClass, newMarking));
					double markingOffsetXInput = oldPlace
							.getMarkingOffsetXObject();
					double markingOffsetYInput = oldPlace
							.getMarkingOffsetYObject();
					int capacityInput = oldPlace.getCapacity();

					newPlace = PlaceFactory.createPlace(positionXInput, positionYInput, newPlaceName,
							newPlaceName, nameOffsetXInput, nameOffsetYInput, markingInput, markingOffsetXInput,
							markingOffsetYInput, capacityInput);
					newPlaces.add(newPlace);
				}

				// Create a new Arc
				double startPositionXInput = newPlace.getPositionX();
				double startPositionYInput = newPlace.getPositionY();
				double endPositionXInput = arcTo.getStartPositionX();
				double endPositionYInput = arcTo.getStartPositionY();
				PlaceTransitionObject source = newPlace;
				PlaceTransitionObject target = newTransition;
				LinkedList<Marking> weight = new LinkedList<Marking>();
				weight.add(new Marking(defaultTokenClass, newArcWeight));
				String idInput = arcTo.getId();

				Arc newArc = ArcFactory.createNormalArc(startPositionXInput, startPositionYInput, endPositionXInput, endPositionYInput,
						source, target, weight, idInput, false);

				// Join arc, place and transition and add all to appropriate
				// lists
				newPlace.addConnectFrom(newArc);
				newTransition.addConnectTo(newArc);
				newArcs.add(newArc);
			}
		}

	}

	private DataLayer createDataLayer() {
		// organizeNetGraphically();
		DataLayer dataLayer = new DataLayer();
		LinkedList<TokenClass> tokenClasses = new LinkedList<TokenClass>();
		tokenClasses.add(defaultTokenClass);
		dataLayer.setTokenClasses(tokenClasses);
		dataLayer.setActiveTokenClass(defaultTokenClass);
		for (Transition t : newTransitions) {
			dataLayer.addPetriNetObject(t);
		}
		for (Place p : newPlaces) {
			dataLayer.addPetriNetObject(p);
		}
		for (Arc a : newArcs) {
			dataLayer.addPetriNetObject(a);
		}

		return dataLayer;
	}

	// Not used. Could be implemented in a better way
	void organizeNetGraphically() {
		int thresholdXDistanceApart = 100;
		int thresholdYDistanceApart = 30;
		double currentX;
		double currentY;
		int size = newTransitions.size();
		// otherwise there are no transitons to seperate from each other
		if (size > 1) {
			boolean madeAMove = true;
			while (madeAMove) {
				madeAMove = false;
				/*
				 * for(int i = 0; i < size-1; i++){ double prevX =
				 * newTransitions.get(i).getPositionX(); double prevY =
				 * newTransitions.get(i).getPositionY(); for(int j = i+1;
				 * j<size; j++){ currentX =
				 * newTransitions.get(j).getPositionX(); currentY =
				 * newTransitions.get(j).getPositionY(); if((currentX - prevX) >
				 * -thresholdXDistanceApart && Math.abs(currentY - prevY) <
				 * thresholdYDistanceApart){
				 * newTransitions.get(j).setPositionX(-thresholdXDistanceApart +
				 * prevX); System.out.println("shifting" +
				 * newTransitions.get(i).getId() + ": " +
				 * -thresholdXDistanceApart + prevX); } else if((currentX -
				 * prevX) < thresholdXDistanceApart && Math.abs(currentY -
				 * prevY) < thresholdYDistanceApart){
				 * newTransitions.get(j).setPositionX(thresholdXDistanceApart +
				 * prevX); System.out.println("shifting" +
				 * newTransitions.get(i).getId() + ": " +
				 * thresholdXDistanceApart + prevX); madeAMove = true; break; }
				 * 
				 * } }
				 */
			}
		}

		// Now align all x position of places with respective transitions
		for (int i = 0; i < newPlaces.size(); i++) {
			Iterator<?> it = newPlaces.get(i).getConnectFromIterator();
			if (it.hasNext()) {
				Arc a = (Arc) it.next();
				Transition t = (Transition) a.getTarget();
				newPlaces.get(i).setPositionX(t.getPositionX());
			}
		}

		// Now separate all places from each other
		size = newPlaces.size();
		// otherwise there are no transitons to seperate from each other
		if (size > 1) {
			for (int i = 0; i < size - 1; i++) {
				double prevX = newPlaces.get(i).getPositionX();
				double prevY = newPlaces.get(i).getPositionY();
				for (int j = i + 1; j < size; j++) {
					currentX = newPlaces.get(j).getPositionX();
					currentY = newPlaces.get(j).getPositionY();
					/*
					 * if((currentX - prevX) < -thresholdXDistanceApart){
					 * newPlaces.get(j).setPositionX(-thresholdXDistanceApart +
					 * prevX && Math.abs(currentY - prevY) <
					 * thresholdYDistanceApart); } else
					 */if ((currentX - prevX) < thresholdXDistanceApart
							&& Math.abs(currentY - prevY) < thresholdYDistanceApart) {
						newPlaces.get(j).setPositionX(
								thresholdXDistanceApart + prevX);
					}
				}
			}
		}

	}

	public File saveAsXml(DataLayerInterface dataLayer) {
		File file = null;
		try {
			file = File.createTempFile("unfoldedNet", ".xml");
			file.deleteOnExit();
			DataLayerWriter writer = new DataLayerWriter(dataLayer);
			writer.savePNML(file);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return file;
	}
}
