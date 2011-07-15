package pipe.gui.action.model;

import java.awt.Container;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

import pipe.gui.CreateGui;
import pipe.gui.action.GuiAction;
import pipe.gui.widgets.EscapableDialog;

/**
 * 模型导入并转换的Dialog
 * @author Jerry-wang
 *
 */
public class ModelImportAction extends GuiAction{
	public ModelImportAction(String name, String tooltip, String keystroke) {
		super(name, tooltip, keystroke);
	}

	public void actionPerformed(ActionEvent e) {
	//	if (this == guideAction) {
				 // Build interface
		      EscapableDialog guiDialog =
		              new EscapableDialog(CreateGui.getApp(), "模型导入", true);

		      // 1 Set layout
		      Container contentPane = guiDialog.getContentPane();
		      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		      
		      
		      // 4 Add button
		      Label tip = new Label();
		      tip.setText("not finished");
		      contentPane.add(tip);
		             

		      // 5 Make window fit contents' preferred size
		      guiDialog.pack();

		      // 6 Move window to the middle of the screen
		      guiDialog.setLocationRelativeTo(null);

		      guiDialog.setVisible(true);
			//}  
		 
	}
	
	ActionListener classifyButtonClick = new ActionListener() {

	      public void actionPerformed(ActionEvent arg0) {
	          System.out.println("here111");
	          
	      }
	   };
}
