package pipe.gui;

import java.io.File;
import java.util.ArrayList;
         
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;

import pipe.common.dataLayer.DataLayer;


public class CreateGui {
   
   public static GuiFrame appGui;
   private static Animator animator;
   private static JTabbedPane appTab;
   
   private static ArrayList tabs = new ArrayList();
   
   public static String imgPath, userPath; // useful for stuff
   
   private static class TabData { // a structure for holding a tab's data
      
      public DataLayer appModel;
      public GuiView appView;
      public File appFile;
   }
   
   /** The Module will go in the top pane, the animation window in the bottom pane */
   private static JSplitPane leftPane;
   private static AnimationHistory animBox;
   private static JScrollPane scroller;

   
   public static void init() {
      
      imgPath = "Images" + System.getProperty("file.separator");
      
      // make the initial dir for browsing be My Documents (win), ~ (*nix), etc
      userPath = null; 
      
      appGui = new GuiFrame("基于AFDX的航电系统Petri网建模仿真工具" +
              " 1.0");
      
      Grid.enableGrid();
      
      appTab = new JTabbedPane();
      
      animator = new Animator();
      appGui.setTab();   // sets Tab properties
           
      // create the tree
      ModuleManager moduleManager = new ModuleManager();
      JTree moduleTree = moduleManager.getModuleTree();
      
      leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,moduleTree,null);
      leftPane.setContinuousLayout(true);
      leftPane.setDividerSize(0);
      
      JSplitPane pane = 
              new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,appTab);

      pane.setContinuousLayout(true);
      pane.setOneTouchExpandable(true);
      pane.setBorder(null); // avoid multiple borders
      pane.setDividerSize(8);

      appGui.getContentPane().add(pane);
          
      appGui.createNewTab(null,false);
      
      appGui.setVisible(true);
      appGui.init();
   }
   
   
   public static GuiFrame getApp() {  //returns a reference to the application
      return appGui;
   }
   
   
   public static DataLayer getModel() {
        return getModel(appTab.getSelectedIndex());
   }
   
   public static DataLayer getModel(int index) {
      if (index < 0) {
         return null;
      }
      
      TabData tab = (TabData)(tabs.get(index));
      if (tab.appModel == null) {
         tab.appModel = new DataLayer();
      }
      return tab.appModel;
   }
   

   public static GuiView getView(int index) {
      if (index < 0) {
         return null;
      }
      
      TabData tab = (TabData)(tabs.get(index));
      while (tab.appView == null) {
         try {
            tab.appView = new GuiView(tab.appModel);
         } catch (Exception e){
            e.printStackTrace();
         }
      }
      return tab.appView;
   }
   
   
   public static GuiView getView() {
      return getView(appTab.getSelectedIndex());
   }
   
   
   public static File getFile() {
      TabData tab = (TabData)(tabs.get(appTab.getSelectedIndex()));
      return tab.appFile;
   }
   
   
   public static void setFile(File modelfile, int fileNo) {
      if (fileNo >= tabs.size()) {
         return;
      }
      TabData tab = (TabData)(tabs.get(fileNo));
      tab.appFile = modelfile;
   }
   
   
   public static int getFreeSpace() {
      tabs.add(new TabData());
      return tabs.size() - 1;
   }
   
   
   public static void removeTab(int index) {
      tabs.remove(index);
   }
   
   
   public static JTabbedPane getTab() {
      return appTab;
   }
   
   
   public static Animator getAnimator() {
      return animator;
   }
   
   
   /** returns the current dataLayer object - 
    *  used to get a reference to pass to the modules */
   public static DataLayer currentPNMLData() {
      if (appTab.getSelectedIndex() < 0) {
         return null;
      }
      TabData tab = (TabData)(tabs.get(appTab.getSelectedIndex()));
      return tab.appModel;
   }
   
   
   /** Creates a new animationHistory text area, and returns a reference to it*/
   public static void addAnimationHistory() {
       try {
//         animBox = new AnimationHistory("Animation history\n");
         animBox = new AnimationHistory("仿真记录\n");

         animBox.setEditable(false);
         
         scroller = new JScrollPane(animBox);
         scroller.setBorder(new EmptyBorder(0,0,0,0)); // make it less bad on XP
         
         leftPane.setBottomComponent(scroller);
         
         leftPane.setDividerLocation(0.5);
         leftPane.setDividerSize(8);
      } catch (javax.swing.text.BadLocationException be) {
         be.printStackTrace();
      }
   }

   
   public static void removeAnimationHistory() {
      if (scroller != null) {
         leftPane.remove(scroller);
         leftPane.setDividerLocation(0);
         leftPane.setDividerSize(0);
      }
   }
   
   
   public static AnimationHistory getAnimationHistory() {
      return animBox;
   }
   
}
