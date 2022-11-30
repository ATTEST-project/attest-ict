package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.util.ExcelFileFormat;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41ResultsReader {

    public final Logger log = LoggerFactory.getLogger(T41ResultsReader.class);

    public Map<String, List<FlexibleOption>> read(File excel) throws ExcelReaderFileException {
        Map<String, List<FlexibleOption>> mapDataForSheet = new HashMap<String, List<FlexibleOption>>();

        FileInputStream fileInputStream = null;
        Workbook workbook = null;
        try {
            if (!ExcelFileUtils.hasExcelFormat(excel)) {
                throw new ExcelReaderFileException("File format is invalid! ");
            }
            fileInputStream = new FileInputStream(excel);
            workbook = new XSSFWorkbook(fileInputStream);

            // Reading each sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                if (!T41FileOutputFormat.SHEETS_NAME.contains(sheetName)) {
                    log.debug("Sheet {}, not required skip ", sheetName);
                } else {
                    log.debug("Parsing sheet: {}", sheetName);
                    List<FlexibleOption> sheetData = new ArrayList<FlexibleOption>();
                    // Reading each row of the sheet
                    for (Row currentRow : sheet) {
                        // log.debug(" row num: {} ", currentRow.getRowNum());

                        FlexibleOption flexibleOption = new FlexibleOption();
                        List<Double> values = new ArrayList<Double>();

                        // skip header
                        if (currentRow.getRowNum() == 0) {
                            // log.debug("Skip header");
                            continue;
                        }
                        if (ExcelFileUtils.isEmptyRow(currentRow)) {
                            // log.debug("Skip Empty Row");
                            continue;
                        }

                        int cellIdx = 0;

                        // read all cell in row
                        for (Cell currentCell : currentRow) {
                            CellType cellType = currentCell.getCellType();
                            if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                                // log.info("Row {} is empty, skip row ", currentRow.getRowNum(), cellIdx);
                                break;
                            }
                            switch (cellIdx) {
                                case 0:
                                    try {
                                        double cellval = currentCell.getNumericCellValue();
                                        if (sheetName.equals(T41FileOutputFormat.SHEETS_NAME.get(5))) {
                                            // cost
                                            flexibleOption.setCost(cellval);
                                        } else {
                                            // -- busNum
                                            flexibleOption.setBusNum(Double.valueOf(cellval).longValue());
                                        }
                                        break;
                                    } catch (NumberFormatException nfe) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse  File " +
                                            excel.getPath() +
                                            ", check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ", cell: " +
                                            cellIdx
                                        );
                                    }
                                default:
                                    try {
                                        double cellval = currentCell.getNumericCellValue();
                                        // temporal values
                                        values.add(cellval);
                                        break;
                                    } catch (NumberFormatException nfe) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse File " +
                                            excel.getPath() +
                                            ", check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ", cell: " +
                                            cellIdx
                                        );
                                    }
                            }
                            cellIdx++;
                            if (!values.isEmpty()) {
                                flexibleOption.setValues(values);
                            }
                        }
                        sheetData.add(flexibleOption);
                    }
                    mapDataForSheet.put(sheetName, sheetData);
                }
            }
        } catch (IOException e) {
            throw new ExcelReaderFileException("Error parsing Excel file: " + excel.getPath() + " " + e.getMessage());
        } catch (Exception e) {
            throw new ExcelReaderFileException("Error parsing Excel file: " + excel.getPath() + " " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("Error closing workbook: " + e.getMessage());
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Error closing fileInputStream " + e.getMessage());
                }
            }
        }
        return mapDataForSheet;
    }

    public Map<String, List<FlexibleOption>> readFileInDir(String directoryName) throws FileNotFoundException, ExcelReaderFileException {
        File dir = new File(directoryName);

        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory not found: " + directoryName);
        }
        File[] fileList = dir.listFiles();

        //tool doesn't produce any file
        if (fileList.length == 0) {
            return Collections.EMPTY_MAP;
        }

        if (fileList.length > 1) {
            throw new ExcelReaderFileException("Directory: " + directoryName + ", contains more files than expected!");
        }

        File excelFile = fileList[0];
        if (!isT41Results(excelFile)) {
            throw new ExcelReaderFileException(
                "Directory: " +
                directoryName +
                ", doesn't contain file with extension: " +
                T41FileOutputFormat.SUFFIX_OUTPUT_FILES_EXTENSION +
                ExcelFileFormat.FILE_EXTENSION
            );
        }

        return this.read(excelFile);
    }

    /**
     *
     * @param excelFile
     * @return true if excelFile name contains '_output'.xlsx
     */
    private boolean isT41Results(File excelFile) {
        if (!excelFile.isFile()) {
            log.error("{} is Not a file, skip ", excelFile.getName());
            return false;
        }

        String fileName = excelFile.getName();
        String extension = this.getFileExtension(fileName);
        return T41FileOutputFormat.SUFFIX_OUTPUT_FILES_EXTENSION.contains(extension);
    }

    /**
     * Parsing output filenames
     *
     * @param output fileName
     * @return outputFileName's extension: _output
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) throw new ExcelReaderFileException("FileName shouldn't be empty");

        String ext = "";
        int posStart = fileName.lastIndexOf("_") + 1;
        int posEnd = fileName.lastIndexOf(".");

        try {
            ext = fileName.substring(posStart, posEnd);
        } catch (IndexOutOfBoundsException ie) {
            log.error("Error getting fileExtension: ", ie.getMessage());
        }

        return ext;
    }
}
