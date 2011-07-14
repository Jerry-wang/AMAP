package pipe.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.ObjectDeepCopier;
import pipe.common.dataLayer.Place;
import pipe.gui.CreateGui;
import pipe.gui.Constants;
import pipe.gui.ZoomController;
import pipe.gui.action.ShowHideInfoAction;
import pipe.gui.undo.UndoManager;

/**
 * Class used to implement methods corresponding to mouse events on places.
 */
public class PlaceHandler extends PlaceTransitionObjectHandler {

	public PlaceHandler(Container contentpane, Place obj) {
		super(contentpane, obj);
	}

	/**
	 * Creates the popup menu that the user will see when they right click on a
	 * component
	 */
	public JPopupMenu getPopup(MouseEvent e) {
		int index = 0;
		JPopupMenu popup = super.getPopup(e);

		JMenuItem menuItem = new JMenuItem("Edit Place");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Place) myObject).showEditor();
			}
		});
		popup.insert(menuItem, index++);

		menuItem = new JMenuItem(new ShowHideInfoAction((Place) myObject));
		if (((Place) myObject).getAttributesVisible() == true) {
			menuItem.setText("Hide Attributes");
		} else {
			menuItem.setText("Show Attributes");
		}
		popup.insert(menuItem, index++);
		popup.insert(new JPopupMenu.Separator(), index);

		return popup;
	}

	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (e.getClickCount() == 2
					&& CreateGui.getApp().isEditionAllowed()
					&& (CreateGui.getApp().getMode() == Constants.PLACE || CreateGui
							.getApp().getMode() == Constants.SELECT)) {
				((Place) myObject).showEditor();
			} else {
				LinkedList<Marking> oldMarkings = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(((LinkedList<Marking>)((Place) myObject).getCurrentMarking()));
				UndoManager undoManager = CreateGui.getView().getUndoManager();
				switch (CreateGui.getApp().getMode()) {
				case Constants.ADDTOKEN:
					for (Marking m : oldMarkings) {
						if (m.getTokenClass().hasSameId(
								((Place) myObject).getActiveTokenClass())) {
							m.setCurrentMarking(m.getCurrentMarking() + 1);
							m.setTokenClass(CreateGui.getModel().getTokenClassFromID(m.getTokenClass().getID()));
							undoManager.addNewEdit(((Place) myObject)
									.setCurrentMarking(oldMarkings));
							break;
						}
					}
					break;
				case Constants.DELTOKEN:
					for (Marking currentMarking : oldMarkings) {
						if (currentMarking.getTokenClass().hasSameId(
								((Place) myObject).getActiveTokenClass())) {
							if (currentMarking.getCurrentMarking() > 0) {
								currentMarking.setCurrentMarking(currentMarking.getCurrentMarking() - 1);
								undoManager.addNewEdit(((Place) myObject)
										.setCurrentMarking(oldMarkings));
							}
						}
					}
					break;
				default:
					break;
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (CreateGui.getApp().isEditionAllowed() && enablePopup) {
				JPopupMenu m = getPopup(e);
				if (m != null) {
					int x = ZoomController.getZoomedValue(((Place) myObject)
							.getNameOffsetXObject().intValue(), myObject
							.getZoom());
					int y = ZoomController.getZoomedValue(((Place) myObject)
							.getNameOffsetYObject().intValue(), myObject
							.getZoom());
					m.show(myObject, x, y);
				}
			}
		}/*
		 * else if (SwingUtilities.isMiddleMouseButton(e)){ ; }
		 */
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// 
		if (CreateGui.getApp().isEditionAllowed() == false || e.isControlDown()) {
			return;
		}

		UndoManager undoManager = CreateGui.getView().getUndoManager();
		if (e.isShiftDown()) {
			int oldCapacity = ((Place) myObject).getCapacity();
			LinkedList<Marking> oldMarkings = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(((LinkedList<Marking>)((Place) myObject).getCurrentMarking()));

			int newCapacity = oldCapacity - e.getWheelRotation();
			if (newCapacity < 0) {
				newCapacity = 0;
			}

			undoManager.newEdit(); // new "transaction""
			for (Marking m : oldMarkings) {
				if (m.getTokenClass().hasSameId(
						((Place) myObject).getActiveTokenClass())) {
					if ((newCapacity > 0)
							&& (m.getCurrentMarking() > newCapacity)) {
						undoManager.addEdit(((Place) myObject)
								.setCurrentMarking(oldMarkings));
					}
				}
			}
			undoManager.addEdit(((Place) myObject).setCapacity(newCapacity));
		} else {
			LinkedList<Marking> oldMarkings = (LinkedList<Marking>)ObjectDeepCopier.mediumCopy(((LinkedList<Marking>)((Place) myObject).getCurrentMarking()));
			int markingChange = e.getWheelRotation();
			for (Marking m : oldMarkings) {
				if (m.getTokenClass().hasSameId(
						((Place) myObject).getActiveTokenClass())) {
					//m.setTokenClass(CreateGui.getModel().getTokenClassFromID(m.getTokenClass().getID()));		
					int oldMarking = m.getCurrentMarking();
					int newMarking = m.getCurrentMarking() - markingChange;
					if (newMarking < 0) {
						newMarking = 0;
					}
					if (oldMarking != newMarking) {
						m.setCurrentMarking(newMarking);
						undoManager.addNewEdit(((Place) myObject)
								.setCurrentMarking(oldMarkings));
					}
					break;
				}
			}
		}
	}

}
