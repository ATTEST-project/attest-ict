package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelReader {

    public final Logger log = LoggerFactory.getLogger(ExcelReader.class);

    public String[] parseHeaderFile(String relativePath, int sheetIndex) throws ExcelReaderFileException, FileNotFoundException {
        return parseHeaderFile(new FileInputStream(new File(relativePath)), sheetIndex);
    }

    public String[] parseHeaderFile(MultipartFile file, int sheetIndex) throws ExcelReaderFileException {
        try {
            return parseHeaderFile(file.getInputStream(), sheetIndex);
        } catch (IOException ioex) {
            throw new ExcelReaderFileException(ioex.getMessage(), ioex.getCause());
        }
    }

    public String[] parseHeaderFile(InputStream inputStream, int sheetIndex) throws ExcelReaderFileException {
        Workbook workbook = null;
        List<String> headers = new ArrayList<String>();
        try {
            workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row headerRow = sheet.getRow(0);
            Iterator<Cell> header = headerRow.iterator();
            while (header.hasNext()) {
                Cell cell = header.next();
                headers.add(getCellData(cell));
            }
            workbook.close();

            String[] headersArray = new String[headers.size()];
            headers.toArray(headersArray);
            return headersArray;
        } catch (IOException e) {
            throw new ExcelReaderFileException("Error parsing Excel file: " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("Error closing workbook: " + e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error closing fileInputStream " + e.getMessage());
                }
            }
        }
    }

    private String getCellData(Cell cell) {
        try {
            String cellValue = null;
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    cellValue = String.valueOf(cell.getNumericCellValue());

                    break;
                case BOOLEAN:
                    cellValue = Boolean.toString(cell.getBooleanCellValue());
                    break;
                default:
                    cellValue = "";
                    break;
            }
            return cellValue;
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String args[]) {
        ExcelReader reader = new ExcelReader();
        String relativePath =
            "C:\\SVILUPPO\\ATTEST\\GIT-LAB\\BR_JHI_BACKEND-NEW\\jhipster-attest\\src\\test\\resources\\excel_file\\tools_info.xlsx";
        String[] headers;
        try {
            headers = reader.parseHeaderFile(relativePath, 0);
            for (String header : headers) {
                System.out.println(header);
            }
        } catch (ExcelReaderFileException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
