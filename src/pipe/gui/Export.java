/*
 * Export class
 *
 * Created on 27-Feb-2004
 *
 */
package pipe.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.PetriNetObject;
import pipe.common.dataLayer.TNTransformer;
import pipe.gui.widgets.FileBrowser;



/**
 * Class for exporting things to other formats, as well as printing.
 * @author Maxim
 */
public class Export {
   
   public static final int PNG = 1;
   public static final int POSTSCRIPT = 2;
   public static final int PRINTER = 3;
   public static final int TN = 4;
   
   
   public static void toPostScript(Object g,String filename) 
           throws PrintException, IOException {
      // Input document type
      DocFlavor flavour = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
      // Output stream MIME type
      String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
      
      // Look up a print service factory that can handle this job
      StreamPrintServiceFactory[] factories = 
               StreamPrintServiceFactory.lookupStreamPrintServiceFactories(
                       flavour, psMimeType);
      if (factories.length == 0) {
         throw new RuntimeException("No suitable factory found for export to PS");
      }
      
      FileOutputStream f = new FileOutputStream(filename);
      // Get a print service from the factory, create a print job and print
      factories[0].getPrintService(f).createPrintJob().print(
               new SimpleDoc(g, flavour, null),
               new HashPrintRequestAttributeSet());
      f.close();
   }
   
   
   public static void toPNG(JComponent g,String filename) throws IOException {
      Iterator i = ImageIO.getImageWritersBySuffix("png");
      if (!i.hasNext()) {
         throw new RuntimeException("No ImageIO exporters can handle PNG");
      }
      
      File f = new File(filename);
      BufferedImage img = new BufferedImage(g.getPreferredSize().width,
                                            g.getPreferredSize().height,
                                            BufferedImage.TYPE_3BYTE_BGR);
      g.print(img.getGraphics());
      ImageIO.write(img, "png", f);
   }
   
   
   public static void toTN(DataLayerInterface net, String filename) throws IOException{
      TNTransformer tnt = new TNTransformer();
      try{
        tnt.saveTN(new File(filename), net);
      }catch(javax.xml.parsers.ParserConfigurationException e){
          System.out.println(e);
      }catch(javax.xml.transform.TransformerConfigurationException e){
          System.out.println(e);
      }
      catch(javax.xml.transform.TransformerException e){
          System.out.println(e);
      }
  }
   
   
   private static void toPrinter(Object g) throws PrintException {
      ///* The Swing way
      PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
      DocFlavor flavour = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
      PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavour, pras);
      
      if (printService.length == 0) {
         throw new PrintException("\nUnable to locate a compatible printer service." +
                  "\nTry exporting to PostScript.");
      }
      PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
      PrintService service = 
              ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavour, pras);
      if (service != null) {
         DocPrintJob job = service.createPrintJob();
         DocAttributeSet das = new HashDocAttributeSet();
         Doc doc = new SimpleDoc(g, flavour, das);
         job.print(doc, pras);
      }
      //*/
    /* The AWT way:
    PrinterJob pjob = PrinterJob.getPrinterJob();
    PageFormat pf = pjob.defaultPage();
    pjob.setPrintable(g, pf);
    try {
      if (pjob.printDialog()) pjob.print();
    } catch (PrinterException e) {
      error=e.toString();
    }
    //*/
   }

   
   public static void exportGuiView(GuiView g,int format, DataLayerInterface model) {
      boolean gridEnabled = Grid.isEnabled();
      String filename = null;
      
      if (g.getComponentCount() == 0) {
         return;
      }
      
      if (CreateGui.getFile() != null) {
         filename=CreateGui.getFile().getAbsolutePath();
         // change file extension
         int dotpos = filename.lastIndexOf('.');
         if (dotpos > filename.lastIndexOf(System.getProperty("file.separator"))) {
            // dot is for extension
            filename = filename.substring(0,dotpos+1);
            switch (format) {
               case PNG:        
                  filename += "png"; 
                  break;
               case POSTSCRIPT: 
                  filename += "ps";
                  break;
               case TN:
                  filename += "xml";
                  break;                  
            }
         }
      }
      
      // Stuff to make it export properly
      g.updatePreferredSize();
      PetriNetObject.ignoreSelection(true);
      if (gridEnabled) {
         Grid.disableGrid();
      }
      
      try {
         switch (format) {
            case PNG:
               filename = new FileBrowser("PNG image","png",filename).saveFile();
               if (filename != null) {
                  toPNG(g, filename);
               }
               break;
            case POSTSCRIPT:
               filename = new FileBrowser("PostScript file","ps",filename).saveFile();
               if (filename != null) {
                  toPostScript(g, filename);
               }
               break;
            case PRINTER:
               toPrinter(g);
               break;
            case TN:
               filename=new FileBrowser("TN net","xml",filename).saveFile();
               if (filename!=null) {
                  toTN(model,filename);
               }
               break;               
         }
      } catch (Exception e) {
         // There was some problem with the action
         JOptionPane.showMessageDialog(CreateGui.getApp(),
                  "There were errors performing the requested action:\n" + e,
                  "Error", JOptionPane.ERROR_MESSAGE
                  );
      }
      
      if (gridEnabled) {
         Grid.enableGrid();
      }
      PetriNetObject.ignoreSelection(false);
      g.repaint();
      
      return;
   }
   
}
