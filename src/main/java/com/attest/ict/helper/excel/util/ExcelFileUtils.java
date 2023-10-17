package com.attest.ict.helper.excel.util;

import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelFileUtils {

    public static final Logger log = LoggerFactory.getLogger(ExcelFileUtils.class);

    public static boolean hasExcelFormat(File file) {
        boolean isExcelFormat = false;
        Path filePath = file.toPath();
        String mimeType;
        try {
            mimeType = FileUtils.probeContentType(filePath);
            // mimeType = Files.probeContentType(filePath);
            isExcelFormat = FileUtils.CONTENT_TYPE.get("xlsx").equals(mimeType);
        } catch (IOException e) {
            String errMsg = "Error parsing Excel: " + filePath + " " + e.getMessage();
            log.error(errMsg);
        }
        return isExcelFormat;
    }

    public static boolean hasExcelFormat(MultipartFile mpFile) {
        String contentType = MimeUtils.detect(mpFile);
        //return ExcelFileFormat.CONTENT_TYPE.equals(file.getContentType());
        return FileUtils.CONTENT_TYPE.get("xlsx").equals(contentType);
    }

    public static boolean isHeaderToSkip(int rowNum) {
        boolean skip = false;
        if (rowNum == 0) {
            // log.debug("Skip header");
            skip = true;
        }
        return skip;
    }

    /**
     * @param row excelFile's row
     * @return true if row doesn't contain any values
     */
    public static boolean isEmptyRow(Row row) {
        boolean isEmptyRow = true;
        for (Cell cell : row) {
            // log.debug("isEmptyRow cellType: "+ cell.getCellType());
            if (cell.getCellType() != CellType.BLANK) {
                isEmptyRow = false;
                break;
            }
        }
        return isEmptyRow;
    }

    public static String getStringCellValue(Cell cell) {
        String val = "";

        switch (cell.getCellType()) {
            case NUMERIC:
                val = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                val = cell.getStringCellValue();
                break;
            case BLANK:
                break;
            case BOOLEAN:
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                break;
            case FORMULA:
                break;
            case _NONE:
                break;
            default:
                break;
        }

        return val;
    }

    public static Double getDoubleCellValue(Cell cell) {
        // log.debug("--- Cell Type: {} ", cell.getCellType());
        Double val = null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    val = Double.valueOf(cell.getNumericCellValue());
                    break;
                case STRING:
                    val = Double.valueOf(cell.getStringCellValue());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("Error converting cell: {} into Double format. Return null value. Error: {}", cell, e.getMessage());
        }
        return val;
    }

    public static String getCellData(Cell cell) {
        try {
            String cellValue = null;
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                case NUMERIC:
                    if (cell.getCellStyle().getDataFormatString().contains("%")) {
                        // Detect Percent Values
                        cellValue = String.valueOf(cell.getNumericCellValue() * 100) + "%";
                        //log.debug("Percent value found = " + cellValue.toString() +"%");
                    } else if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = cell.getDateCellValue() + "";
                    } else {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                default:
                    cellValue = "";
                    break;
            }
            // log.debug(" Cell Type: {} , value: {}", cell.getCellType(), cellValue);
            return cellValue;
        } catch (Exception e) {
            log.error("Error return  cell: {} value, from excel file. Error: {}", cell, e.getMessage());
            return "";
        }
    }
}
