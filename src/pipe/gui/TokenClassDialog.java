package pipe.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

/**
 * @author Alex Charalambous, June 2010: ColorDrawer, ColorPicker, 
 * TokenClassPanel and TokenClassDialog are four classes used 
 * to display the Token Classes dialog (accessible through the button 
 * toolbar).
 */

public class TokenClassDialog extends JDialog implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean acceptChanges = false;
    
	public TokenClassDialog(Frame owner, String title, boolean modal){
		super(owner, title, modal);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK")){
			acceptChanges = true;
		}
		else if(e.getActionCommand().equals("Cancel")){
			acceptChanges = false;
		}
		setVisible(false);
	}
	
	public boolean shouldAcceptChanges(){
		return acceptChanges;
	}

}
