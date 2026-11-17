package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * ExcelUtility class is used for:
 * 1) Reading data from Excel file
 * 2) Writing data into Excel file
 * 3) Applying color formatting (for PASS/FAIL)
 *
 * This is commonly used in Data Driven Testing (DDT).
 */

public class ExcelUtility {

    // Input & Output streams
    public FileInputStream fi;
    public FileOutputStream fo;

    // Workbook = entire Excel file
    public XSSFWorkbook workbook;

    // Sheet = one tab inside Excel
    public XSSFSheet sheet;

    // Row & Cell objects
    public XSSFRow row;
    public XSSFCell cell;

    // Used for coloring cells
    public CellStyle style;

    // File path of Excel
    String path;

    /*
     * Constructor
     * When creating object, we pass Excel file path.
     */
    public ExcelUtility(String path) {
        this.path = path;
    }

    // ================== GET TOTAL ROW COUNT ==================

    /*
     * Returns last row number in given sheet.
     * Used to loop through test data.
     */
    public int getRowCount(String sheetName) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        int rowcount = sheet.getLastRowNum();

        workbook.close();
        fi.close();

        return rowcount;
    }

    // ================== GET TOTAL CELL COUNT ==================

    /*
     * Returns number of cells in a particular row.
     * Used when reading column-wise data.
     */
    public int getCellCount(String sheetName, int rownum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        row = sheet.getRow(rownum);
        int cellcount = row.getLastCellNum();

        workbook.close();
        fi.close();

        return cellcount;
    }

    // ================== READ CELL DATA ==================

    /*
     * Returns data from specific cell.
     * DataFormatter ensures all cell types (string, number, date)
     * are converted safely into String.
     */
    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        DataFormatter formatter = new DataFormatter();
        String data;

        try {
            data = formatter.formatCellValue(cell);
        } catch (Exception e) {
            data = "";
        }

        workbook.close();
        fi.close();

        return data;
    }

    // ================== WRITE CELL DATA ==================

    /*
     * Writes data into specific cell.
     * If file/sheet/row does not exist,
     * it creates them dynamically.
     */
    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException {

        File xlfile = new File(path);

        // If file does not exist → create new workbook
        if (!xlfile.exists()) {
            workbook = new XSSFWorkbook();
            fo = new FileOutputStream(path);
            workbook.write(fo);
            fo.close();
            workbook.close();
        }

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        // If sheet not present → create sheet
        if (workbook.getSheetIndex(sheetName) == -1)
            workbook.createSheet(sheetName);

        sheet = workbook.getSheet(sheetName);

        // If row not present → create row
        if (sheet.getRow(rownum) == null)
            sheet.createRow(rownum);

        row = sheet.getRow(rownum);

        // Create cell and set value
        cell = row.createCell(colnum);
        cell.setCellValue(data);

        // Write updated data into file
        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }

    // ================== FILL GREEN COLOR ==================

    /*
     * Used to highlight cell in GREEN color.
     * Typically used for marking test PASS.
     */
    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }

    // ================== FILL RED COLOR ==================

    /*
     * Used to highlight cell in RED color.
     * Typically used for marking test FAIL.
     */
    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }
}