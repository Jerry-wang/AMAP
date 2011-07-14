package pipe.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;

/**
 * @author Alex Charalambous, June 2010: ColorDrawer, ColorPicker, 
 * TokenClassPanel and TokenClassDialog are four classes used 
 * to display the Token Classes dialog (accessible through the button 
 * toolbar).
 */

public class ColorPicker extends AbstractCellEditor implements ActionListener,
		TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button;
	private Color currColor;
	private JColorChooser colorChooser;
	private JDialog dialog;
	private String edit = "edit";

	public ColorPicker() {
		button = new JButton();
		button.setBorderPainted(false);
		button.addActionListener(this);
		button.setActionCommand(edit);

		// Dialog brought up by button:
		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(button, "Pick a Colour", true,
				colorChooser, this, null);
	}
	
	public Object getCellEditorValue() {
		return currColor;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currColor = (Color) value;
		return button;
	}
	public void actionPerformed(ActionEvent e) {
		if (edit.equals(e.getActionCommand())) {
			colorChooser.setColor(currColor);
			button.setBackground(currColor);
			dialog.setVisible(true);

			// Renderer re-appears.
			fireEditingStopped();

		} else { //OK Pressed
			currColor = colorChooser.getColor();
		}
	}
}
