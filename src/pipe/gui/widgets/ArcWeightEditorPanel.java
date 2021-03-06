package pipe.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.ObjectDeepCopier;
import pipe.common.dataLayer.TokenClass;
import pipe.gui.CreateGui;
import pipe.gui.GuiView;

/**
 * 
 * @author Alex Charalambous
 */
public class ArcWeightEditorPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Arc arc;
	boolean attributesVisible;
	String name;

	DataLayerInterface pnmlData;
	GuiView view;
	JRootPane rootPane;

	/**
	 * Creates new form Arc Weight Editor
	 */
	public ArcWeightEditorPanel(JRootPane _rootPane, Arc _arc,
			DataLayerInterface _pnmlData, GuiView _view) {
		arc = _arc;
		pnmlData = _pnmlData;
		view = _view;
		name = arc.getName();
		rootPane = _rootPane;

		initComponents();

		rootPane.setDefaultButton(okButton);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		inputtedWeights = new LinkedList<JSpinner>();
		inputtedTokenClassNames = new LinkedList<String>();
		tokenClasses = CreateGui.getModel().getTokenClasses();
		java.awt.GridBagConstraints gridBagConstraints;

		arcEditorPanel = new javax.swing.JPanel();
		buttonPanel = new javax.swing.JPanel();
		cancelButton = new javax.swing.JButton();
		okButton = new javax.swing.JButton();

		setLayout(new java.awt.GridBagLayout());

		arcEditorPanel.setBorder(javax.swing.BorderFactory
//				.createTitledBorder("Arc Weight Editor"));
				.createTitledBorder("编辑权值"));
		arcEditorPanel.setLayout(new java.awt.GridBagLayout());
		/*Dimension d = new Dimension();
		d.setSize(150, 350);*///此处自适应高度比较好
		//arcEditorPanel.setPreferredSize(d);
		// Now set new dimension used in for loop below
		Dimension d = new Dimension();
		d.setSize(50, 19);
		int x = 0;
		int y = 0;
		LinkedList<Marking> weights = arc.getWeight();
		for (TokenClass tc : tokenClasses) {
			if (tc.isEnabled()) {
				JLabel tokenClassName = new JLabel();
				JSpinner tokenClassWeight = new JSpinner();
				inputtedWeights.add((JSpinner) tokenClassWeight);

				tokenClassName.setText(tc.getID() + ": ");
				inputtedTokenClassNames.add(tc.getID());
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = x;
				gridBagConstraints.gridy = y;
				gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
				gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
				arcEditorPanel.add(tokenClassName, gridBagConstraints);
				tokenClassWeight.setPreferredSize(d);
				tokenClassWeight.setValue(0);
				for (Marking currentWeight : weights) {
					if (tc.hasSameId(currentWeight.getTokenClass())) {
						tokenClassWeight.setValue(currentWeight
								.getCurrentMarking());
					}
				}

				tokenClassWeight
						.addFocusListener(new java.awt.event.FocusAdapter() {
							public void focusGained(
									java.awt.event.FocusEvent evt) {
								nameTextFieldFocusGained(evt);
							}

							public void focusLost(java.awt.event.FocusEvent evt) {
								nameTextFieldFocusLost(evt);
							}
						});
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = x + 1;
				gridBagConstraints.gridy = y;
				gridBagConstraints.gridwidth = 3;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
				arcEditorPanel.add(tokenClassWeight, gridBagConstraints);
				y++;
			}
		}
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
		add(arcEditorPanel, gridBagConstraints);
		buttonPanel.setLayout(new java.awt.GridBagLayout());

		cancelButton.setText("取消");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonHandler(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
		buttonPanel.add(cancelButton, gridBagConstraints);

		okButton.setText("确定");
		okButton.setMaximumSize(new java.awt.Dimension(75, 25));
		okButton.setMinimumSize(new java.awt.Dimension(75, 25));
		okButton.setPreferredSize(new java.awt.Dimension(75, 25));
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonHandler(evt);
			}
		});
		okButton.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				okButtonKeyPressed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
		buttonPanel.add(okButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 8, 3);
		add(buttonPanel, gridBagConstraints);
	}// </editor-fold>//GEN-END:initComponents

	
	private void nameTextFieldFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_nameTextFieldFocusLost
		// focusLost(nameTextField);
	}// GEN-LAST:event_nameTextFieldFocusLost

	private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_nameTextFieldFocusGained
		// focusGained(nameTextField);
	}// GEN-LAST:event_nameTextFieldFocusGained

	private void okButtonKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_okButtonKeyPressed
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			okButtonHandler(new java.awt.event.ActionEvent(this, 0, ""));
		}
	}// GEN-LAST:event_okButtonKeyPressed

	CaretListener caretListener = new javax.swing.event.CaretListener() {
		public void caretUpdate(javax.swing.event.CaretEvent evt) {
			JTextField textField = (JTextField) evt.getSource();
			textField.setBackground(new Color(255, 255, 255));
			// textField.removeChangeListener(this);
		}
	};

	private void okButtonHandler(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonHandler
		LinkedList<Marking> newWeight;
		newWeight = (LinkedList<Marking>) ObjectDeepCopier.mediumCopy(arc
				.getWeight());
	    view.getUndoManager().newEdit(); // new "transaction""
	    int totalMarking = 0;
		for (int i = 0; i < inputtedWeights.size(); i++) {
			String tokenClassName = inputtedTokenClassNames.get(i);

			int pos = CreateGui.getModel().getPosInList(tokenClassName,
					newWeight);
			Marking m;
			if (pos >= 0) {
				m = newWeight.get(pos);
			} else {
				m = new Marking(CreateGui.getModel().getTokenClassFromID(
						tokenClassName), 0);
				newWeight.add(m);
			}
			int currentMarking = m.getCurrentMarking();


				int newMarking = Integer.valueOf((Integer)inputtedWeights.get(i)
						.getValue());
				if (newMarking < 0) {
					JOptionPane.showMessageDialog(null,
//							"Weighting cannot be less than 0. Please re-enter");
							"权值不能小于0，请重新输入");
					return;
				} else if (newMarking != currentMarking) {
					m.setCurrentMarking(newMarking);
				}
				totalMarking += newMarking;
/*			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null,
						"Please enter a positive integer greater than 0.",
						"Invalid entry", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Please enter a positive integer greater than 0.",
						"Invalid entry", JOptionPane.ERROR_MESSAGE);
				return;
			}
*/		}
		if(totalMarking <= 0){
			JOptionPane.showMessageDialog(null,
//					"Total weight of arc must be greater than 0. Please re-enter");
					"总权值不能小于0，请重新输入");
			return;
		}
		CreateGui.getView().getUndoManager().addEdit(
				arc.setWeight(newWeight));
		
		arc.repaint();
		exit();
	}

	/*
	 * 
	 * view.getUndoManager().newEdit(); // new "transaction"" String newName =
	 * "";//nameTextField.getText(); if (!newName.equals(name)) { if
	 * (pnmlData.checkTransitionIDAvailability(newName)) {
	 * view.getUndoManager().addEdit(arc.setPNObjectName(newName)); } else { //
	 * aquest nom no est� disponible... JOptionPane.showMessageDialog(null,
	 * "There is already a transition named " + newName, "Error",
	 * JOptionPane.WARNING_MESSAGE); return; } }
	 */

	// GEN-LAST:event_okButtonHandler

	private void exit() {
		rootPane.getParent().setVisible(false);
	}

	private void cancelButtonHandler(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonHandler
		// Provisional!
		exit();
	}// GEN-LAST:event_cancelButtonHandler

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton okButton;
	private javax.swing.JPanel arcEditorPanel;
	private LinkedList<TokenClass> tokenClasses;
	private LinkedList<JSpinner> inputtedWeights;
	private LinkedList<String> inputtedTokenClassNames;
	// End of variables declaration//GEN-END:variables

}
