package pipe.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/* Status Bar to let users know what to do*/
public class StatusBar 
        extends JPanel{
   
   /* Provides the appropriate text for the mode that the user is in */
   public String textforDrawing =
//           "Drawing Mode: Click on a button to start adding components to the "
//           + "Editor";
	   		"绘制:点击图标-> 添加相应的元素";
   public String textforPlace =
//           "Place Mode: Right click on a Place to see menu options " +
//           "[Mouse wheel -> marking; Shift + Mouse wheel -> capacity]";
	   		"库所：点击右键-> 查看菜单，滑动滚轮-> 调整标记数目，Shift+滚轮-> 调整容量大小";
   public String textforTrans = 
//           "Immediate Transition Mode: Right click on a Transition to see menu " +
//           "options [Mouse wheel -> rotate]";
	   		"瞬时变迁：点击右键-> 查看菜单，滑动滚轮-> 调整方向";
   public String textforTimedTrans =
//           "Timed Transition Mode: Right click on a Transition to see menu " +
//           "options [Mouse wheel -> rotate]";
	   		"随机变迁：点击右键-> 查看菜单，滑动滚轮-> 调整方向";
   
   public String textforDeterminTrans =
	   		"确定变迁：点击右键-> 查看菜单，滑动滚轮-> 调整方向";
   
   public String textforAddtoken =
//           "Add Token Mode: Click on a Place to add a Token";
	   		"添加标记：点击库所-> 添加标记";
   public String textforDeltoken = 
//           "Delete Token Mode: Click on a Place to delete a Token ";
	   		"删除标记：点击库所-> 删除标记";
   public String textforAnimation = 
//           "Animation: Red transitions are enabled, click a transition to "
//           + "fire it";
	   		"手动仿真：红色变迁是可以触发的，点击以触发变迁";
   public String textforArc =
//           "Arc Mode: Right-Click on an Arc to see menu options " +
//           "[Mouse wheel -> weight]"; //NOU-PERE
	   		"弧：点击右键-> 查看菜单，滑动滚轮-> 调整权值";
   public String textforInhibArc =
//           "Inhibitor Mode: Right-Click on an Arc to see menu options " +
//           "[Mouse wheel -> weight]"; //NOU-PERE
	   		"抑制弧：点击右键-> 查看菜单，滑动滚轮-> 调整权值";
   public String textforMove =
//           "Select Mode: Click/drag to select objects; drag to move them";
   			"选择: 单击并拖动所选图形";
   public String textforAnnotation =
//           "Annotation Mode: Right-Click on an Annotation to see menu options; " +
//            "Double click to edit";   
	   		"注释：点击右键-> 查看菜单，双击-> 编辑内容";
   
   public String textforDrag = "Drag Mode";
   
   public String textforMarking = "Add a marking parameter";
   
   public String textforRate = "Add a rate parameter";
   
   private JLabel label;
   
   
   public StatusBar(){
      super();
      label = new JLabel(textforDrawing); // got to put something in there
      this.setLayout(new BorderLayout(0,0));
      this.add(label);
   }
   
   
   public void changeText(String newText){
      label.setText(newText);
   }
   
   
   public void changeText(int type ){
      switch(type){
         case Constants.PLACE:
            changeText(textforPlace);
            break;
            
         case Constants.IMMTRANS:
            changeText(textforTrans);
            break;
            
         case Constants.TIMEDTRANS:
            changeText(textforTimedTrans);
            break;
            
         case Constants.DETERMINRANS:
             changeText(textforDeterminTrans);
             break;
            
         case Constants.ARC:
            changeText(textforArc);
            break;
            
         case Constants.INHIBARC:
            changeText(textforInhibArc);
            break;            
            
         case Constants.ADDTOKEN:
            changeText(textforAddtoken);
            break;
            
         case Constants.DELTOKEN:
            changeText(textforDeltoken);
            break;
            
         case Constants.SELECT:
            changeText(textforMove);
            break;
            
         case Constants.DRAW:
            changeText(textforDrawing);
            break;
            
         case Constants.ANNOTATION:
            changeText(textforAnnotation);
            break;
            
         case Constants.DRAG:
            changeText(textforDrag);
            break;            
            
         case Constants.MARKING:
            changeText(textforMarking);
            break;

         case Constants.RATE:
            changeText(textforRate);
            break;            
            
         default:
            changeText("To-do (textfor" + type);
            break;
      }
   }

}