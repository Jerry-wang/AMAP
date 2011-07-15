/**
 * Classification Module
 * @author James D Bloom 2003-03-12
 * @author Maxim Gready 2004
 * @author Will Master minor changes 02/2007
 *
 * This module was severly broken in the 2003 release. It should now be
 * producing correct results but is still being fixed up.
 */
package pipe.modules.avionics;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.DataLayerWriter;
import pipe.common.dataLayer.Unfolder;
import pipe.gui.CreateGui;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.EscapableDialog;
import pipe.gui.widgets.PetriNetChooserPanel;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.modules.AvionicsModule;
import pipe.modules.EmptyNetException;
import pipe.modules.Module;


/** Classification class implements petri net classification */
public class TestAvionicsModule
        implements AvionicsModule {  

   private static final String MODULE_NAME = "建模向导";
   private PetriNetChooserPanel sourceFilePanel;
   private ResultsHTMLPane results;

   public void run(DataLayer pnmlData) {
	    
      // Build interface
      EscapableDialog guiDialog =
              new EscapableDialog(CreateGui.getApp(), MODULE_NAME, true);

      // 1 Set layout
      Container contentPane = guiDialog.getContentPane();
      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

      // 2 Add file browser
      sourceFilePanel = new PetriNetChooserPanel("Source net", pnmlData);
      contentPane.add(sourceFilePanel);

      // 3 Add results pane
      results = new ResultsHTMLPane(pnmlData.getURI());
      contentPane.add(results);

      // 4 Add button
      contentPane.add(new ButtonBar("生成模型", classifyButtonClick,
              guiDialog.getRootPane()));

      // 5 Make window fit contents' preferred size
      guiDialog.pack();

      // 6 Move window to the middle of the screen
      guiDialog.setLocationRelativeTo(null);

      guiDialog.setVisible(true);
   }


   public String getName() {
      return MODULE_NAME;
   }


   /**
    * Classify button click handler
    */
   ActionListener classifyButtonClick = new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
          System.out.println("here");
          
      }
   };


    
}
