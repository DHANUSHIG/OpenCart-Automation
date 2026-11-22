package utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clean, high-performance Excel utility for Data-Driven Testing (DDT).
 * Opens the workbook once using try-with-resources to eliminate file and memory leaks.
 */
public class ExcelUtility {

    private String path;

    public ExcelUtility(String path) {
        this.path = path;
    }

    /**
     * Reads an entire sheet into a 2D String array for TestNG DataProvider in a single pass.
     * Skips the header row (row index 0).
     */
    public String[][] getSheetData(String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                return new String[0][0];
            }

            int totalRows = sheet.getLastRowNum();
            int totalCols = sheet.getRow(0).getLastCellNum();

            String[][] data = new String[totalRows][totalCols];
            DataFormatter formatter = new DataFormatter();

            // Loop starting from row 1 (skipping header row at index 0)
            for (int r = 1; r <= totalRows; r++) {
                XSSFRow row = sheet.getRow(r);
                for (int c = 0; c < totalCols; c++) {
                    XSSFCell cell = (row != null) ? row.getCell(c) : null;
                    data[r - 1][c] = (cell != null) ? formatter.formatCellValue(cell) : "";
                }
            }
            return data;
        }
    }

    /**
     * Helper to get total rows count in a sheet if needed.
     */
    public int getRowCount(String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            return (sheet != null) ? sheet.getLastRowNum() : 0;
        }
    }

    /**
     * Helper to read a specific cell value if needed.
     */
    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return "";
            XSSFRow row = sheet.getRow(rownum);
            if (row == null) return "";
            XSSFCell cell = row.getCell(colnum);
            if (cell == null) return "";
            return new DataFormatter().formatCellValue(cell);
        }
    }
}
