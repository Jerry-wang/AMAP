package pipe.gui.action.model;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

import pipe.gui.CreateGui;
import pipe.gui.action.GuiAction;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.EscapableDialog;

/**
 * 建模向导的Dialog
 * @author Jerry-wang
 * 
 *
 */
public class ModelGuideAction extends GuiAction{
	public ModelGuideAction(String name, String tooltip, String keystroke) {
		super(name, tooltip, keystroke);
	}

	public void actionPerformed(ActionEvent e) {
	//	if (this == guideAction) {
				 // Build interface
		      EscapableDialog guiDialog =
		              new EscapableDialog(CreateGui.getApp(), "建模向导", true);

		      // 1 Set layout
		      Container contentPane = guiDialog.getContentPane();
		      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		      
		      
		      // 4 Add button
		      contentPane.add(new ButtonBar("生成模型", classifyButtonClick,
		              guiDialog.getRootPane()));

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
