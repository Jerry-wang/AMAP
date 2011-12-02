/**
 * Incidence and Marking module
 * @author James D Bloom 2003-03-12
 * @author Maxim 2004 (better GUI, cleaned up code)
 */
package pipe.modules.matrices;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.DataLayerWriter;
import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.Place;
import pipe.common.dataLayer.Transition;
import pipe.common.dataLayer.Unfolder;
import pipe.gui.CreateGui;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.EscapableDialog;
import pipe.gui.widgets.PetriNetChooserPanel;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.modules.Module;

public class Matrices
        implements Module {

//   private static final String MODULE_NAME = "Incidence & Marking";
   private static final String MODULE_NAME = "关联矩阵";
   private PetriNetChooserPanel sourceFilePanel;
   private ResultsHTMLPane results;

   public void run(DataLayer pnmlData) {
	   // Check if this net is a CGSPN. If it is, then this
	   // module won't work with it and we must convert it.
	   if(pnmlData.getTokenClasses().size() > 1){
		   Unfolder unfolder = new Unfolder(pnmlData);
		   pnmlData = unfolder.unfold();
	   }
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
      contentPane.add(new ButtonBar("Calculate", calculateButtonClick,
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
    * Calculate button click handler
    */
   ActionListener calculateButtonClick = new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
         DataLayerInterface data = sourceFilePanel.getDataLayer();
         String s = "<h2>Petri net incidence and marking</h2>";
         if (data == null) {
            return;
         }
         if (!data.hasPlaceTransitionObjects()) {
            s += "No Petri net objects defined!";
         } else {
            try {

               DataLayerWriter.saveTemporaryFile(data, this.getClass().getName());

               s += ResultsHTMLPane.makeTable(new String[]{
                          "Forwards incidence matrix <i>I<sup>+</sup></i>",
                          renderMatrix(data, data.getActiveTokenClass().getForwardsIncidenceMatrix(
                        		  data.getArcsArrayList(), data.getTransitionsArrayList(),
                        		  data.getPlacesArrayList()))
                       }, 1, false, false, true, false);
               s += ResultsHTMLPane.makeTable(new String[]{
                          "Backwards incidence matrix <i>I<sup>-</sup></i>",
                          renderMatrix(data, data.getActiveTokenClass().getBackwardsIncidenceMatrix(
                        		  data.getArcsArrayList(), data.getTransitionsArrayList(),
                        		  data.getPlacesArrayList()))
                       }, 1, false, false, true, false);
               s += ResultsHTMLPane.makeTable(new String[]{
                          "Combined incidence matrix <i>I</i>",
                          renderMatrix(data, data.getActiveTokenClass().getIncidenceMatrix(
                        		  data.getArcsArrayList(), data.getTransitionsArrayList(),
                        		  data.getPlacesArrayList()))
                       }, 1, false, false, true, false);
               s += ResultsHTMLPane.makeTable(new String[]{
                          "Inhibition matrix <i>H</i>",
                          renderMatrix(data, data.getActiveTokenClass().getInhibitionMatrix(
                        		  data.getInhibitorsArrayList(), data.getTransitionsArrayList(),
                        		  data.getPlacesArrayList()))
                       }, 1, false, false, true, false);
               s += ResultsHTMLPane.makeTable(new String[]{
                          "Marking",
                          renderMarkingMatrices(data)
                       }, 1, false, false, true, false);
               s += ResultsHTMLPane.makeTable(new String[]{
                          "Enabled transitions",
                          renderTransitionStates(data)
                       }, 1, false, false, true, false);
            } catch (OutOfMemoryError oome) {
               System.gc();
               results.setText("");
               s = "Memory error: " + oome.getMessage();

               s += "<br>Not enough memory. Please use a larger heap size." + "<br>" + "<br>Note:" + "<br>The Java heap size can be specified with the -Xmx option." + "<br>E.g., to use 512MB as heap size, the command line looks like this:" + "<br>java -Xmx512m -classpath ...\n";
               results.setText(s);
               return;
            } catch (Exception e) {
               e.printStackTrace();
               s = "<br>Error" + e.getMessage();
               results.setText(s);
               return;
            }
         }
         results.setEnabled(true);
         results.setText(s);
      }
   };

   public String renderMatrix(DataLayerInterface data, int[][] matrix) {
      if ((matrix.length == 0) || (matrix[0].length == 0)) {
         return "n/a";
      }

      ArrayList result = new ArrayList();
      // add headers to table
      result.add("");
      for (int i = 0; i < matrix[0].length; i++) {
         result.add(data.getTransition(i).getName());
      }

      for (int i = 0; i < matrix.length; i++) {
         result.add(data.getPlace(i).getName());
         for (int j = 0; j < matrix[i].length; j++) {
            result.add(Integer.toString(matrix[i][j]));
         }
      }

      return ResultsHTMLPane.makeTable(
              result.toArray(), matrix[0].length + 1, false, true, true, true);
   }

   private String renderMarkingMatrices(DataLayerInterface data) {
      Place[] places = data.getPlaces();
      if (places.length == 0) {
         return "n/a";
      }

      LinkedList<Marking>[] markings = data.getInitialMarkingVector();
      int[] initial = new int[markings.length];
      for(int i = 0; i < markings.length; i++){
    	  initial[i] = markings[i].getFirst().getCurrentMarking();
      }
      
      markings = data.getCurrentMarkingVector();
      int[] current = new int[markings.length];
      for(int i = 0; i < markings.length; i++){
    	  current[i] = markings[i].getFirst().getCurrentMarking();
      }
      
      ArrayList result = new ArrayList();
      // add headers t o table
      result.add("");
      for (int i = 0; i < places.length; i++) {
         result.add(places[i].getName());
      }

      result.add("Initial");
      for (int i = 0; i < initial.length; i++) {
         result.add(Integer.toString(initial[i]));
      }
      result.add("Current");
      for (int i = 0; i < current.length; i++) {
         result.add(Integer.toString(current[i]));
      }

      return ResultsHTMLPane.makeTable(
              result.toArray(), places.length + 1, false, true, true, true);
   }

   private String renderTransitionStates(DataLayerInterface data) {
      Transition[] transitions = data.getTransitions();
      if (transitions.length == 0) {
         return "n/a";
      }

      ArrayList result = new ArrayList();
      data.setEnabledTransitions();
      for (int i = 0; i < transitions.length; i++) {
         result.add(transitions[i].getName());
      }
      for (int i = 0; i < transitions.length; i++) {
         result.add((transitions[i].isEnabled() ? "yes" : "no"));
      }
      data.resetEnabledTransitions();

      return ResultsHTMLPane.makeTable(
              result.toArray(), transitions.length, false, true, true, false);
   }
}
