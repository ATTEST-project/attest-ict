package com.attest.ict.helper.excel.util;

import com.attest.ict.custom.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
            isExcelFormat = ExcelFileFormat.CONTENT_TYPE.equals(mimeType);
        } catch (IOException e) {
            String errMsg = "Error parsing Excel: " + filePath + " " + e.getMessage();
            log.error(errMsg);
        }
        return isExcelFormat;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return ExcelFileFormat.CONTENT_TYPE.equals(file.getContentType());
    }

    public static boolean isHeaderToSkip(int rowNum) {
        boolean skip = false;
        if (rowNum == 0) {
            log.debug("Skip header");
            skip = true;
        }
        return skip;
    }

    //    public static File transferMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
    //        // save tempFile
    //        File convFile = File.createTempFile(multipartFile.getOriginalFilename(), ExcelFileFormat.FILE_EXTENSION);
    //        multipartFile.transferTo(convFile);
    //        log.debug(
    //                "Save temp File: {},  Path: {}, contentType: {} ",
    //                convFile.getName(),
    //                convFile.getPath(),
    //                Files.probeContentType(convFile.toPath()));
    //        return convFile;
    //    }

    // Some test cases for instance 'PT_Dx_05_2020\\Flexibility\\Network5_Su_Bd -
    // Flex.xlsx contain' many empty rows
    // TODO rivedere
    public static boolean isEmptyRow(Row row) {
        int minColIx = row.getFirstCellNum();
        if (minColIx < 0) return true;
        int maxColIx = row.getLastCellNum();
        int numEmptyCell = 0;

        for (int colIx = minColIx; colIx <= maxColIx; colIx++) {
            Cell cell = row.getCell(colIx);
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                numEmptyCell++;
            }
        }
        return (numEmptyCell > 0 && numEmptyCell == maxColIx - 1);
    }

    public static Object getObjectCellValue(Cell cell) {
        if (cell.getCellType().equals(CellType.STRING)) return cell.getStringCellValue();

        if (cell.getCellType().equals(CellType.BOOLEAN)) return cell.getBooleanCellValue();

        if (cell.getCellType().equals(CellType.NUMERIC)) return cell.getNumericCellValue();

        return "";
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
        Double val = null;

        switch (cell.getCellType()) {
            case NUMERIC:
                val = Double.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                val = Double.valueOf(cell.getStringCellValue());
                break;
            case BLANK:
                break;
            case BOOLEAN:
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
}
