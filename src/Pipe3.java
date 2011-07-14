
import javax.swing.SwingUtilities;
import pipe.gui.CreateGui;

/**
 * <b>RunGui</b> - Start-up class with main function
 * @version 1.0
 * @author Alex Duncan 
 * 总体的入口类
 */
public class Pipe3 {

   public static void main(String args[]) {
      SwingUtilities.invokeLater(new Runnable() {

         public void run() {
            CreateGui.init();
         }
      });
   }
}
