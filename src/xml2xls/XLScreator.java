package xml2xls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Class for creating XLS files. Defining basic methods
 * for common operations: add a sheet, set sheet name, 
 * add rows and write text in cells.
 */

public class XLScreator {
    
    private FileOutputStream out;
    private HSSFWorkbook wb = null;
    private HSSFSheet s = null;
    private HSSFSheet main_s = null; // the Main sheet
    private HSSFRow r = null;
    private HSSFRow main_r = null; // the row for the Main sheet
    private HSSFCell c = null;
    private HSSFCell main_c = null; // the cell for the Main sheet
    private HSSFCellStyle cs, cs2 = null;
    private HSSFFont f, f2 = null;
    
    private short row = 0;
    private short main_row = 0; // the row counter for the Main sheet
    
    private HashMap<String,Integer> sheets = new HashMap<String,Integer>();
    
    /**
     * Create and initialize a Workbook.
     * 
     * @param fileName  Output filename (.xls file)
     */
    
    public XLScreator(String fileName) {
        try {
            this.out = new FileOutputStream(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XLScreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.wb = new HSSFWorkbook();
        
        this.cs = wb.createCellStyle();
        this.f = wb.createFont();
        this.cs.setFont(this.f);
        
        this.cs2 = wb.createCellStyle();
        this.f2 = wb.createFont();
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        this.cs2.setFont(this.f2);

        // Create the Main sheet
        this.main_s = this.wb.createSheet("Main");
        for(int i = 0; i< 9; i++) {
            this.main_s.setColumnWidth((short)i, (short)4500);
        }
        this.main_row = 0;
    }
    
    /**
     * Create rows in your current sheet.
     * 
     * @param delta     Number of rows to be created.
     */
    public void newRow(int delta) {
        this.r = this.s.createRow(this.row+delta);
        this.row += delta;
        this.main_r = this.main_s.createRow(this.main_row+delta);
        this.main_row += delta;        
    }
    
    /**
     * Write text/number in a cell (for the new sheet and the Main one).
     * 
     * @param text          The text that it's going to be written
     * @param col           The column where the cell is located
     * @param style         0 means normal, 1 is bold.
     * @param text_number   <code>TRUE</code> if the first parameter represents
     *                      a text, <code>FALSE</code> if it's a number.
     */
    public void writeCell(String text, int col, int style, boolean text_number) {
        this.c = this.r.createCell((short) col);
        this.main_c = this.main_r.createCell((short) col);
        if(0 == style) {
            this.c.setCellStyle(this.cs);
            this.main_c.setCellStyle(this.cs);
        } else {
            this.c.setCellStyle(this.cs2);
            this.main_c.setCellStyle(this.cs2);
        } 
        if(text_number) {
            HSSFRichTextString richText = new HSSFRichTextString(text);
            this.c.setCellValue(richText);
            this.main_c.setCellValue(richText);
        } else {
            this.c.setCellValue(Double.parseDouble(text));
            this.main_c.setCellValue(Double.parseDouble(text));
        }
    }            
    
    /**
     * Adds a sheet.
     * A sheet name cannot be repeated.
     * 
     * @param name  Name of the sheet
     */
    public void newSheet(String name) {
        this.s = this.wb.createSheet(name);
        for(int i = 0; i< 9; i++) {
            this.s.setColumnWidth((short)i, (short)4500);
        }
        this.row = 0;
    }
    
    /**
     * Set a sheet name, a suffix is appended automatically
     * to avoid repeated sheet names.
     * 
     * @param num   Number of the sheet in the xls file
     * @param name  Name of the sheet
     */
    public void setSheetName(int num, String name) {
        if (this.sheets.containsKey(name)) {
            Integer suffix = (Integer) this.sheets.get(name);
            String newName = new String(name.concat("_").concat(Integer.toString(suffix+1)));
            this.sheets.put(new String(name), new Integer(suffix+1));
            this.wb.setSheetName(num, newName);
        } else {
            this.sheets.put(new String(name), new Integer(1));
            String newName = new String(name.concat("_1"));
            this.wb.setSheetName(num, newName);
        }
    }
    
    /**
     * Closes the .xls file and flushes.
     * You MUST call this.
     */
    
    public void close(){
        try {
            this.wb.write(this.out);
            this.out.close();
        } catch (IOException ex) {
            Logger.getLogger(XLScreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
