package pipe.common.dataLayer;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class TokenClass implements Serializable {
	private static final long serialVersionUID = 1L;
	private Color tokenColour;
	private String id = "";
	private boolean enabled = false;
	private int lockCount = 0; // So that users cannot change this class while
	// places are marked with it
	public PNMatrix incidenceMatrix = null;
	public PNMatrix forwardsIncidenceMatrix = null;
	public PNMatrix backwardsIncidenceMatrix = null;
	public PNMatrix inhibitionMatrix = null;

	public TokenClass(boolean enabled, String id, Color colour) {
		this.enabled = enabled;
		this.id = id;
		tokenColour = colour;
	}

	/**
	 * Return the Incidence Matrix for the Petri-Net
	 * 
	 * @return The Incidence Matrix for the Petri-Net
	 */
	public int[][] getIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		if (incidenceMatrix == null || incidenceMatrix.matrixChanged) {
			createIncidenceMatrix(arcsArray, transitionsArray, placesArray);
		}
		return (incidenceMatrix != null ? incidenceMatrix.getArrayCopy() : null);
	}
	
	// New method for TransModel.java
	public int[][] simpleMatrix() {
		return incidenceMatrix.getArrayCopy();
	}

	public void setIncidenceMatrix(PNMatrix incidenceMatrix) {
		this.incidenceMatrix = incidenceMatrix;
	}

	public int[][] getForwardsIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		if (forwardsIncidenceMatrix == null
				|| forwardsIncidenceMatrix.matrixChanged) {
			createForwardIncidenceMatrix(arcsArray, transitionsArray,
					placesArray);
		}
		return (forwardsIncidenceMatrix != null ? forwardsIncidenceMatrix
				.getArrayCopy() : null);
	}
	
	public int[][] simpleForwardsIncidenceMatrix() {
		return forwardsIncidenceMatrix.getArrayCopy();
	}

	public void setForwardsIncidenceMatrix(PNMatrix forwardsIncidenceMatrix) {
		this.forwardsIncidenceMatrix = forwardsIncidenceMatrix;
	}

	/**
	 * Return the Backward Incidence Matrix for the Petri-Net
	 * 
	 * @return The Backward Incidence Matrix for the Petri-Net
	 */
	public int[][] getBackwardsIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		if (backwardsIncidenceMatrix == null
				|| backwardsIncidenceMatrix.matrixChanged) {
			createBackwardsIncidenceMatrix(arcsArray, transitionsArray,
					placesArray);
		}
		return (backwardsIncidenceMatrix != null ? backwardsIncidenceMatrix
				.getArrayCopy() : null);
	}
	
	public int [][] simpleBackwardsIncidenceMatrix() {
		return backwardsIncidenceMatrix.getArrayCopy();
	}

	public void setBackwardsIncidenceMatrix(PNMatrix backwardsIncidenceMatrix) {
		this.backwardsIncidenceMatrix = backwardsIncidenceMatrix;
	}

	/**
	 * Return the Incidence Matrix for the Petri-Net
	 * 
	 * @return The Incidence Matrix for the Petri-Net
	 */
	public int[][] getInhibitionMatrix(ArrayList<InhibitorArc> inhibitorArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		if (inhibitionMatrix == null || inhibitionMatrix.matrixChanged) {
			createInhibitionMatrix(inhibitorArray, transitionsArray,
					placesArray);
		}
		return (inhibitionMatrix != null ? inhibitionMatrix.getArrayCopy()
				: null);
	}

	public void setInhibitionMatrix(PNMatrix inhibitionMatrix) {
		this.inhibitionMatrix = inhibitionMatrix;
	}

	public Color getColour() {
		return tokenColour;
	}

	public void setColour(Color colour) {
		tokenColour = colour;
	}

	public void incrementLock() {
		lockCount++;
	}

	public void decrementLock() {
		lockCount--;
	}

	public boolean isLocked() {
		return lockCount > 0;
	}

	public int getLockCount() {
		return lockCount;
	}

	public void setLockCount(int newLockCount) {
		lockCount = newLockCount;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public boolean hasSameId(TokenClass otherTokenClass) {
		return otherTokenClass.getID().equals(this.getID());
	}

	/**
	 * Creates Forward Incidence Matrix from current Petri-Net
	 */
	public void createForwardIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		int placeSize = placesArray.size();
		int transitionSize = transitionsArray.size();

		forwardsIncidenceMatrix = new PNMatrix(placeSize, transitionSize);

		for (int i = 0; i < arcsArray.size(); i++) {
			Arc arc = (Arc) arcsArray.get(i);
			if (arc != null) {
				PetriNetObject pnObject = arc.getTarget();
				if (pnObject != null) {
					if (pnObject instanceof Place) {
						Place place = (Place) pnObject;
						pnObject = arc.getSource();
						if (pnObject != null) {
							if (pnObject instanceof Transition) {
								Transition transition = (Transition) pnObject;
								int transitionNo = transitionsArray
										.indexOf(transition);
								int placeNo = placesArray.indexOf(place);
								for (Marking m : arc.getWeight()) {
									if (m.getTokenClass().getID().equals(id)) {
										try {
											forwardsIncidenceMatrix
													.set(
															placeNo,
															transitionNo,
															arc
																	.getWeightOfTokenClass(id));
										} catch (Exception e) {
											JOptionPane
													.showMessageDialog(null,
															"Problem in forwardsIncidenceMatrix");
											System.out.println("p:" + placeNo
													+ ";t:" + transitionNo
													+ ";w:" + arc.getWeight());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates Backwards Incidence Matrix from current Petri-Net
	 */
	public void createBackwardsIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {// Matthew
		int placeSize = placesArray.size();
		int transitionSize = transitionsArray.size();

		backwardsIncidenceMatrix = new PNMatrix(placeSize, transitionSize);

		for (int i = 0; i < arcsArray.size(); i++) {
			Arc arc = (Arc) arcsArray.get(i);
			if (arc != null) {
				PetriNetObject pnObject = arc.getSource();
				if (pnObject != null) {
					if (pnObject instanceof Place) {
						Place place = (Place) pnObject;
						pnObject = arc.getTarget();
						if (pnObject != null) {
							if (pnObject instanceof Transition) {
								Transition transition = (Transition) pnObject;
								int transitionNo = transitionsArray
										.indexOf(transition);
								int placeNo = placesArray.indexOf(place);
								for (Marking m : arc.getWeight()) {
									if (m.getTokenClass().getID().equals(id)) {
										try {
											backwardsIncidenceMatrix
													.set(
															placeNo,
															transitionNo,
															arc
																	.getWeightOfTokenClass(id));
										} catch (Exception e) {
											JOptionPane
													.showMessageDialog(null,
															"Problem in backwardsIncidenceMatrix");
											System.out.println("p:" + placeNo
													+ ";t:" + transitionNo
													+ ";w:" + arc.getWeight());
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates Incidence Matrix from current Petri-Net
	 */
	public void createIncidenceMatrix(ArrayList<Arc> arcsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		createForwardIncidenceMatrix(arcsArray, transitionsArray, placesArray);
		createBackwardsIncidenceMatrix(arcsArray, transitionsArray, placesArray);
		incidenceMatrix = new PNMatrix(forwardsIncidenceMatrix);
		incidenceMatrix = incidenceMatrix.minus(backwardsIncidenceMatrix);
		incidenceMatrix.matrixChanged = false;
	}

	/**
	 * Creates Inhibition Matrix from current Petri-Net
	 */
	public void createInhibitionMatrix(ArrayList<InhibitorArc> inhibitorsArray,
			ArrayList<Transition> transitionsArray, ArrayList<Place> placesArray) {
		int placeSize = placesArray.size();
		int transitionSize = transitionsArray.size();
		inhibitionMatrix = new PNMatrix(placeSize, transitionSize);

		for (int i = 0; i < inhibitorsArray.size(); i++) {
			InhibitorArc inhibitorArc = (InhibitorArc) inhibitorsArray.get(i);
			if (inhibitorArc != null) {
				PetriNetObject pnObject = inhibitorArc.getSource();
				if (pnObject != null) {
					if (pnObject instanceof Place) {
						Place place = (Place) pnObject;
						pnObject = inhibitorArc.getTarget();
						if (pnObject != null) {
							if (pnObject instanceof Transition) {
								Transition transition = (Transition) pnObject;
								int transitionNo = transitionsArray
										.indexOf(transition);
								int placeNo = placesArray.indexOf(place);
								try {
									inhibitionMatrix.set(placeNo, transitionNo,
											1);
								} catch (Exception e) {
									JOptionPane.showMessageDialog(null,
											"Problema a inhibitionMatrix");
									System.out.println("p:" + placeNo + ";t:"
											+ transitionNo + ";w:"
											+ inhibitorArc.getWeight());
								}
							}
						}
					}
				}
			}
		}
	}

}
