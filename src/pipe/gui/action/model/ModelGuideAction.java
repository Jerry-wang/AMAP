package pipe.gui.action.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pipe.gui.CreateGui;
import pipe.gui.action.GuiAction;
import pipe.gui.widgets.avionic.ModelGuideDialog1;

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
		     /* EscapableDialog guiDialog =
		              new EscapableDialog(CreateGui.getApp(), "建模向导", true);
  
 		      Container contentPane = guiDialog.getContentPane();
		      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
 		      
 		      contentPane.add(new ButtonBar("生成模型", classifyButtonClick,
		              guiDialog.getRootPane()));

 		      guiDialog.pack();

 		      guiDialog.setLocationRelativeTo(null);

		      guiDialog.setVisible(true);*/ 
		      
		      
			  ModelGuideDialog1 guiDialog =  new ModelGuideDialog1(CreateGui.getApp(), true);  
		      guiDialog.pack();

 		      guiDialog.setLocationRelativeTo(null);

		      guiDialog.setVisible(true);
		      
 		 
	}
	
	ActionListener classifyButtonClick = new ActionListener() {

	      public void actionPerformed(ActionEvent arg0) {
	          System.out.println("here111");
	          
	      }
	   };
}


  
