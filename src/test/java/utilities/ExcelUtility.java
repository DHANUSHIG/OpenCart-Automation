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

/**
 * ExcelUtility encapsulates Apache POI workbook and sheet operations for Data-Driven Testing (DDT).
 * All stream resources are closed safely using try-with-resources to prevent file leaks.
 */
public class ExcelUtility {

    private String path;

    public ExcelUtility(String path) {
        this.path = path;
    }

    // ================== GET TOTAL ROW COUNT ==================

    public int getRowCount(String sheetName) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            return (sheet != null) ? sheet.getLastRowNum() : 0;
        }
    }

    // ================== GET TOTAL CELL COUNT ==================

    public int getCellCount(String sheetName, int rownum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return 0;
            XSSFRow row = sheet.getRow(rownum);
            return (row != null) ? row.getLastCellNum() : 0;
        }
    }

    // ================== READ CELL DATA ==================

    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return "";
            XSSFRow row = sheet.getRow(rownum);
            if (row == null) return "";
            XSSFCell cell = row.getCell(colnum);
            if (cell == null) return "";

            DataFormatter formatter = new DataFormatter();
            try {
                return formatter.formatCellValue(cell);
            } catch (Exception e) {
                return "";
            }
        }
    }

    // ================== WRITE CELL DATA ==================

    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException {
        File xlfile = new File(path);

        // If file does not exist, create new empty workbook
        if (!xlfile.exists()) {
            try (XSSFWorkbook newWb = new XSSFWorkbook();
                 FileOutputStream fo = new FileOutputStream(path)) {
                newWb.write(fo);
            }
        }

        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            XSSFRow row = sheet.getRow(rownum);
            if (row == null) {
                row = sheet.createRow(rownum);
            }

            XSSFCell cell = row.createCell(colnum);
            cell.setCellValue(data);

            try (FileOutputStream fo = new FileOutputStream(path)) {
                workbook.write(fo);
            }
        }
    }

    // ================== FILL GREEN COLOR ==================

    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return;
            XSSFRow row = sheet.getRow(rownum);
            if (row == null) return;
            XSSFCell cell = row.getCell(colnum);
            if (cell == null) return;

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);

            try (FileOutputStream fo = new FileOutputStream(path)) {
                workbook.write(fo);
            }
        }
    }

    // ================== FILL RED COLOR ==================

    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return;
            XSSFRow row = sheet.getRow(rownum);
            if (row == null) return;
            XSSFCell cell = row.getCell(colnum);
            if (cell == null) return;

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);

            try (FileOutputStream fo = new FileOutputStream(path)) {
                workbook.write(fo);
            }
        }
    }
}
