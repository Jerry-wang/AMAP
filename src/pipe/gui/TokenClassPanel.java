package pipe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTable;
import java.util.LinkedList;
import java.util.Random;

import pipe.common.dataLayer.TokenClass;

/**
 * @author Alex Charalambous, June 2010: ColorDrawer, ColorPicker,
 *         TokenClassPanel and TokenClassDialog are four classes used to display
 *         the Token Classes dialog (accessible through the button toolbar).
 */

public class TokenClassPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTable table;
	private LinkedList<TokenClass> tokenClasses;

	public TokenClassPanel() {
		// super(new GridLayout(1,0));
 		this.tokenClasses = CreateGui.getModel().getTokenClasses();
		table = new JTable(new TableModel());
		table.setPreferredScrollableViewportSize(new Dimension(500, 170));
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setDefaultRenderer(Color.class, new ColorDrawer(true));
		table.setDefaultEditor(Color.class, new ColorPicker());

		add(scrollPane);
	}

	public class TableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/*private String[] columnNames = { "Enabled", "Token Class Name",
				"Token Class Colour", };
*/
		
		private String[] columnNames = { "应用", "名称",
				"颜色", };
		
		private Object[][] data;
		private static final int dataSize = 100;// Default is a size of 100 different
		// tokenClasses as defined in
		// constructor
		final static int enabledCol = 0;
		final static int nameCol = 1;
		final static int colorCol = 2;

		public TableModel() {
			super();
			data = new Object[dataSize][3];
			Random generator = new Random();
			for (int i = 0; i < dataSize; i++) {
				// Set rows 0-6 as the basic different colours and the rest as
				// a random colour
				switch (i) {
				case 0:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.black;
					break;
				case 1:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.RED;
					break;
				case 4:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.BLUE;
					break;
				case 3:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.YELLOW;
					break;
				case 2:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.GREEN;
					break;
				case 5:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.ORANGE;
					break;
				case 6:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = Color.PINK;
					break;
				default:
					data[i][enabledCol] = new Boolean(false);
					data[i][nameCol] = "";
					data[i][colorCol] = new Color(generator.nextInt(256),
							generator.nextInt(256), generator.nextInt(256));
				}
			}

			int noTokenClasses = tokenClasses.size();
			for (int i = 0; i < noTokenClasses; i++) {
				Object[] tokenClass = {
						(Boolean) tokenClasses.get(i).isEnabled(),
						(String) tokenClasses.get(i).getID(),
						(Color) tokenClasses.get(i).getColour() };
				data[i] = tokenClass;
			}
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			boolean shouldChange = true;
			if (col == enabledCol) { // The enabled column has been changed
				if (((String) data[row][nameCol]).equals("")) {
					shouldChange = false;
					JOptionPane.showMessageDialog(new JPanel(),
//							"The token class name cannot be empty", "Warning",
							"名称不能为空", "Warning",
							JOptionPane.WARNING_MESSAGE);

				} else if ((Boolean) value) {
					for (int i = 0; i < dataSize; i++) {
						if (i != row && (Boolean) data[i][enabledCol]) {
							if (((String) data[i][nameCol])
									.equals((String) data[row][nameCol])) {
								shouldChange = false;
								JOptionPane
										.showMessageDialog(
												new JPanel(),
//												"Another token class exists with that name",
												"名称重复！",
												"Warning",
												JOptionPane.WARNING_MESSAGE);
								break;
							}
						}
					}

				} else {
					// There must be at least one enabled column in the list
					boolean enabledRowFound = false;
					for (int i = 0; i < dataSize; i++) {
						if (i != row && (Boolean) data[i][enabledCol]) {
							enabledRowFound = true;
							break;
						}
					}
					if (!enabledRowFound) {
						shouldChange = false;
						JOptionPane.showMessageDialog(new JPanel(),
								"至少应该有1个分类被应用！",
//								"At least one token class must be enabled",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			} else if (col == nameCol) { // The name column has been changed

				if ((Boolean) data[row][enabledCol]) {
					if (((String) value).equals("")) {
						shouldChange = false;
						JOptionPane.showMessageDialog(new JPanel(),
								"名称不能为空！", "Warning",
//								"The token class name cannot be empty", "Warning",
								JOptionPane.WARNING_MESSAGE);

					}
					for (int i = 0; i < dataSize; i++) {
						if (i != row && (Boolean) data[i][enabledCol]) {
							if (((String) data[i][nameCol])
									.equals((String) value)) {
								shouldChange = false;
								JOptionPane
										.showMessageDialog(
												new JPanel(),
//												"Another token class exists with that name",
												"名称重复！",
												"Warning",
												JOptionPane.WARNING_MESSAGE);
								break;
							}
						}
					}
				}
			}
			if (shouldChange) {
				for (TokenClass tc : tokenClasses) {
					if (tc.getID().equals(data[row][nameCol])) {
						if (tc.isLocked()) {
							shouldChange = false;
							JOptionPane
									.showMessageDialog(
											new JPanel(),
											"Places exist that use this token class. "
													+ "Such markings must be removed before this class can be edited",
											"Warning",
											JOptionPane.WARNING_MESSAGE);
							break;
						}
					}
				}
			}
			for (int i = 0; i < dataSize; i++) {
				if ((Boolean) data[i][enabledCol]) {

				}
			}

			if (shouldChange) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}

		}
	}

	private static void displayGUI() {
//		JFrame frame = new JFrame("Token Classes");
		JFrame frame = new JFrame("标记分类");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel tablePane = new TokenClassPanel();
		tablePane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tablePane.setOpaque(true);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(new JButton(""));
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(new JButton());

		// frame.setContentPane(tablePane);
		/*
		 * Container container = new Container(); container.add(tablePane,
		 * BorderLayout.CENTER); container.add(buttonPane,
		 * BorderLayout.PAGE_END); container.setVisible(true);
		 */// 
		frame.add(tablePane, BorderLayout.CENTER);
		frame.add(buttonPane, BorderLayout.PAGE_END);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayGUI();
			}
		});
	}
}