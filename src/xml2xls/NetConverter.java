package xml2xls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.xml.sax.SAXException;

/**
 * SuperClass for validating xml files, according to a
 * provided xsd schema file.
 */
public class NetConverter {

   protected String netFileName;
   protected String xlsFileName;
   protected XLScreator xls;

   /**
    *
    * @param netFileName
    * @param xlsFileName
    * @throws java.io.FileNotFoundException
    */
   public NetConverter(String netFileName, String xlsFileName)
           throws FileNotFoundException {
      this.netFileName = netFileName;
      this.xlsFileName = xlsFileName;
   }

   /**
    * Validates one xml file, providing a xsd file.
    *
    * @param schemaFileName    .xsd filename
    * @return                  <code>true</code> if it's valid,
    *                          <code>false</code> false otherwise.
    */
   protected boolean validate(File schemaFile) {
      SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*
      if ((schemaFile != null) && (schemaFile.exists())) {
      System.out.println("schema file found");
      } else {
      System.out.println("schema file not found");
      }
*/
      try {
         Schema schema = factory.newSchema(schemaFile);
         Validator validator = schema.newValidator();
         try {
            //System.out.println("this.netFileName: " + this.netFileName);
            Source source = null;
            source = new StreamSource(new File(this.netFileName));/*
               try {
                  source = new StreamSource((new URI(this.netFileName)).toString());

               } catch (URISyntaxException ex) {
                  Logger.getLogger(NetConverter.class.getName()).log(Level.SEVERE, null, ex);
               }
*/
            //Source source = new StreamSource( this.netFileName.replaceAll(" ", "%20"));
/*
            try {
               // /*pere* / per traduir: dona problemes amb espais!! els espais s'han de substituir %20
              URI uri = new URI(this.netFileName);
               System.out.println("uri: " + uri.toString());
               source = new StreamSource(uri.toString());
               System.out.println("it's ok.");
            } catch (URISyntaxException ex) {
               Logger.getLogger(NetConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
 */
            try {
               validator.validate(source);
            } catch (IOException ex) {
               Logger.getLogger(PetriNetConverter.class.getName()).
                       log(Level.SEVERE, null, ex);
            }
            System.out.println(this.netFileName + " is valid.");
            return true;
         } catch (SAXException ex) {
            System.out.println(this.netFileName + " is not valid because ");
            System.out.println(ex.getMessage());
            return false;
         }
      } catch (SAXException ex) {
         Logger.getLogger(PetriNetConverter.class.getName()).
                 log(Level.SEVERE, null, ex);
      }
      return true;
   }
}