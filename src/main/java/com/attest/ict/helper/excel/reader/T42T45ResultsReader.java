package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import com.attest.ict.service.dto.custom.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T42T45ResultsReader {

    public static final Logger log = LoggerFactory.getLogger(T42T45ResultsReader.class);

    public ToolResultsPagesDTO getPagesToShow(File relativePath) {
        log.info("getPagesToShow() - START reading sheets' name from file: {}", relativePath);
        ExcelReader excelReader = new ExcelReader(relativePath);
        List<String> sheetsName = excelReader.getSheetsName();
        List<ToolPageDTO> pages = new ArrayList<>();
        sheetsName.stream().forEach(sheetName -> mapPagesSheetNameAndTitle(sheetName, pages));

        ToolPageDTO allCustomPage = new ToolPageDTO();
        allCustomPage.setSheetName("ALL");
        allCustomPage.setSection("ALL");
        allCustomPage.setTitle("All Flexible Results");
        pages.add(allCustomPage);

        ToolResultsPagesDTO resultsPages = new ToolResultsPagesDTO();
        resultsPages.setPages(pages);
        excelReader.closeWorkBook();
        log.info("getPagesToShow() - END reading sheets' name from file: {}, return {} ", relativePath, resultsPages);
        return resultsPages;
    }

    /**
     * @param excelFile
     * @return true if fileName end with '_output'.xlsx
     */
    public static boolean isT42T45Results(File excelFile) {
        if (!excelFile.isFile()) {
            log.error("isT42T45Results() - {} is Not a file, skips ", excelFile.getName());
            return false;
        }

        String fileName = excelFile.getName();
        String extension = T42T45ResultsReader.getFileExtension(fileName);
        return T42T45FileOutputFormat.OUTPUT_FILES_EXTENSION.contains(extension);
    }

    /**
     * Parsing output filenames
     *
     * @param fileName
     * @return outputFileName's suffix: '_output'
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) throw new ExcelReaderFileException("FileName shouldn't be empty");

        String ext = "";
        int posStart = fileName.lastIndexOf("_") + 1;
        int posEnd = fileName.lastIndexOf(".");

        try {
            ext = fileName.substring(posStart, posEnd);
        } catch (IndexOutOfBoundsException ie) {
            log.error("getFileExtension() - Error: ", ie.getMessage());
        }
        return ext;
    }

    /**
     * @param excel file to parse
     * @return Map with key = [sheetName;columnName] and List<FlexibleOption> as values
     * @throws ExcelReaderFileException
     */
    public Map<String, List<FlexibleOption>> parseFlexDataSheets(File excel) throws ExcelReaderFileException {
        log.info("parseFlexDataSheets() - START reading excel file {} ", excel.getName());

        Map<String, List<FlexibleOption>> mapDataForSheet = new HashMap<String, List<FlexibleOption>>();

        FileInputStream fileInputStream = null;
        Workbook workbook = null;
        try {
            if (!ExcelFileUtils.hasExcelFormat(excel)) {
                throw new ExcelReaderFileException("File format is invalid; expected format is '.xlsx'");
            }
            fileInputStream = new FileInputStream(excel);
            workbook = new XSSFWorkbook(fileInputStream);

            // Reading each sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                if (!T42T45FileOutputFormat.FLEX_SHEETS_NAME.contains(sheetName)) {
                    log.debug("parseFlexDataSheets() - Sheet {}, not required skip ", sheetName);
                } else {
                    log.info("parseFlexDataSheets() - Parsing sheet: {}", sheetName);

                    List<String> headers = new ArrayList<>();
                    // Reading each row of the sheet
                    Double busNum = null;
                    for (Row currentRow : sheet) {
                        // log.debug("   row num: {} ", currentRow.getRowNum());
                        if (currentRow.getRowNum() == 0) {
                            headers = readHeader(currentRow);

                            for (int colunmIndex = 1; colunmIndex < headers.size(); colunmIndex++) {
                                List<FlexibleOption> flexibleOptionList = new ArrayList<FlexibleOption>();
                                // eg: Sheet P_S_p keys are:
                                // P_S_p;PupS_p
                                // P_S_p;PdnS_p
                                // P_S_p;QupS_p
                                // P_S_p;QdnS_p
                                String key = sheetName.concat(";").concat(headers.get(colunmIndex));
                                mapDataForSheet.put(key, flexibleOptionList);
                            }
                            continue;
                        }

                        if (ExcelFileUtils.isEmptyRow(currentRow)) {
                            log.debug("parseFlexDataSheets() - Skip Empty Row");
                            continue;
                        }

                        int cellIdx = 0;

                        // read all cell in row
                        for (Cell currentCell : currentRow) {
                            CellType cellType = currentCell.getCellType();
                            if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                                // log.info("--- Row {} is empty, skip row ", currentRow.getRowNum(), cellIdx);
                                break;
                            }
                            // log.debug("---  Row: {},  Column: {},  CellType:{}", currentRow.getRowNum(), cellIdx, cellType);
                            switch (cellIdx) {
                                case 0:
                                    try {
                                        busNum = ExcelFileUtils.getDoubleCellValue(currentCell);
                                        log.debug("parseFlexDataSheets() - BusNum: {} ", Double.valueOf(busNum).longValue());
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
                                        double cellval = ExcelFileUtils.getDoubleCellValue(currentCell);
                                        log.debug("parseFlexDataSheets() -  cellval: {} ", cellval);
                                        // temporal values (for T45 only one hour has been  set)
                                        List<Double> values = new ArrayList<Double>();
                                        values.add(cellval);
                                        if (busNum != null) {
                                            FlexibleOption flexOptColumn = new FlexibleOption();
                                            flexOptColumn.setBusNum(Double.valueOf(busNum).longValue());
                                            flexOptColumn.setValues(values);
                                            String key = sheetName.concat(";").concat(headers.get(cellIdx));
                                            List<FlexibleOption> flexOptForColumnList = mapDataForSheet.get(key);
                                            flexOptForColumnList.add(flexOptColumn);
                                        } else {
                                            log.debug("parseFlexDataSheets() -  Bus Num is null... row is empty!");
                                        }
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
                        }
                    }
                }
            }
            // log.debug (" return mapDataForSheet: {} " , Arrays.deepToString(mapDataForSheet.entrySet().toArray()));
            log.info("parseFlexDataSheets() - END reading excel file {} ", excel.getName());
        } catch (IOException e) {
            throw new ExcelReaderFileException("Error parsing Excel file: " + excel.getPath() + " " + e.getMessage());
        } catch (Exception e) {
            throw new ExcelReaderFileException("Error parsing Excel file: " + excel.getPath() + " " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("parseFlexDataSheets() - Error closing workbook: " + e.getMessage());
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("parseFlexDataSheets() - Error closing fileInputStream " + e.getMessage());
                }
            }
        }
        return mapDataForSheet;
    }

    private List<String> readHeader(Row firstRow) {
        log.debug("readHeader() - START reading header ");
        List<String> headers = new ArrayList<>();
        int cellIdx = 0;
        for (Cell currentCell : firstRow) {
            CellType cellType = currentCell.getCellType();
            if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                log.debug("---   Row {} is empty, skip row ", 0, cellIdx);
                break;
            }
            headers.add(ExcelFileUtils.getStringCellValue(currentCell));
            cellIdx++;
        }
        log.debug("readHeader() - END reading header. Return: {}", headers.toString());
        return headers;
    }

    public List<T42T45ActivationDTO> parseSheetRequestActivation(File fileToParse) {
        // Read sheet:  RActivation
        String fileToParsePath = fileToParse.getAbsolutePath();
        String sheetName = T42T45FileOutputFormat.ALL_SHEETS_NAME.get(3);
        log.info("parseSheetRequestActivation() - START Parsing file: {} sheet: {} ", fileToParsePath, sheetName);
        ExcelReader excelReader = new ExcelReader(fileToParsePath);
        try {
            Sheet sheetRActivation = excelReader.getSheetByName(sheetName);
            if (sheetRActivation == null) {
                return Collections.emptyList();
            }
            List<T42T45ActivationDTO> activations = new ArrayList<T42T45ActivationDTO>();
            List<String> headers = new ArrayList<>();
            for (Row currentRow : sheetRActivation) {
                log.debug("parseSheetRequestActivation() - Row num: {} ", currentRow.getRowNum());
                T42T45ActivationDTO activationRequest = new T42T45ActivationDTO();

                if (currentRow.getRowNum() == 0) {
                    headers = readHeader(currentRow);
                    continue;
                }

                if (ExcelFileUtils.isEmptyRow(currentRow)) {
                    log.debug("parseSheetRequestActivation() - Skip Empty Row!");
                    continue;
                }
                int cellIdx = 0;
                // read all cell in currentRow
                for (Cell currentCell : currentRow) {
                    CellType cellType = currentCell.getCellType();
                    if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                        // log.info("--- Row {} is empty, skip row ", currentRow.getRowNum(), cellIdx);
                        break;
                    }

                    log.debug(
                        "parseSheetRequestActivation() - Row: {},  Column: {},  CellType:{}",
                        currentRow.getRowNum(),
                        cellIdx,
                        cellType
                    );
                    switch (cellIdx) {
                        case 0:
                            try {
                                Double value = ExcelFileUtils.getDoubleCellValue(currentCell);
                                log.debug("parseSheetRequestActivation() - Pup_tot CellNum: {}, Value: {} ", cellIdx, value);
                                activationRequest.setActivePowerUpTot(value);
                                break;
                            } catch (NumberFormatException nfe) {
                                throw new ExcelnvalidDataException(
                                    "fail to parse  File " +
                                    fileToParsePath +
                                    ", check data at sheetName: " +
                                    sheetRActivation.getSheetName() +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx
                                );
                            }
                        case 1:
                            try {
                                Double value = ExcelFileUtils.getDoubleCellValue(currentCell);
                                log.debug("parseSheetRequestActivation() - Pdn_tot CellNum: {}, Value: {} ", cellIdx, value);
                                activationRequest.setActivePowerDnTot(value);
                                break;
                            } catch (NumberFormatException nfe) {
                                throw new ExcelnvalidDataException(
                                    "fail to parse File " +
                                    fileToParsePath +
                                    ", check data at sheetName: " +
                                    sheetRActivation.getSheetName() +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx
                                );
                            }
                        case 2:
                            try {
                                Double value = ExcelFileUtils.getDoubleCellValue(currentCell);
                                log.debug("parseSheetRequestActivation() - Qup_tot CellNum: {}, value: {} ", cellIdx, value);
                                activationRequest.setReactivePowerUpTot(value);
                                break;
                            } catch (NumberFormatException nfe) {
                                throw new ExcelnvalidDataException(
                                    "fail to parse  File " +
                                    fileToParsePath +
                                    ", check data at sheetName: " +
                                    sheetRActivation.getSheetName() +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx
                                );
                            }
                        case 3:
                            try {
                                Double value = ExcelFileUtils.getDoubleCellValue(currentCell);
                                log.debug("parseSheetRequestActivation() - Qdn_tot Cell: {}, value: {} ", cellIdx, value);
                                activationRequest.setReactivePowerDnTot(value);
                                break;
                            } catch (NumberFormatException nfe) {
                                throw new ExcelnvalidDataException(
                                    "fail to parse File " +
                                    fileToParsePath +
                                    ", check data at sheetName: " +
                                    sheetRActivation.getSheetName() +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx
                                );
                            }
                    } // end Switch
                    cellIdx++;
                }
                activations.add(activationRequest);
            }
            log.info("parseSheetRequestActivation() - END Parsing file: {} sheet: {} ", fileToParsePath, sheetName);
            return activations;
        } catch (Exception ex) {
            log.error(
                "parseSheetRequestActivation() Exception parsing file {}, Sheet: {} Error:{}, return null ",
                fileToParsePath,
                sheetName,
                ex.getMessage()
            );
            ex.printStackTrace();
            return null;
        } finally {
            excelReader.closeWorkBook();
        }
    }

    private void mapPagesSheetNameAndTitle(String sheetName, List<ToolPageDTO> pages) {
        log.debug("mapPagesSheetNameAndTitle() - SheetName: {}", sheetName);

        ToolPageDTO page = new ToolPageDTO();
        page.setSheetName(sheetName);

        switch (sheetName.toUpperCase()) {
            case "P_DG_P":
                page.setTitle("Flexible generator: Active Power Curtailment");
                page.setSection(T42T45FileOutputFormat.MAP_SHEET_NAME_CHART_SECTIONS.get(sheetName));
                pages.add(page);
                break;
            case "P_L_P":
                page.setTitle("Flexible EV: Down and Up Active Power");
                page.setSection(T42T45FileOutputFormat.MAP_SHEET_NAME_CHART_SECTIONS.get(sheetName));
                pages.add(page);
                break;
            case "P_S_P":
                page.setTitle("Flexible Storage: Down and Up Active Power. ");
                page.setSection(T42T45FileOutputFormat.MAP_SHEET_NAME_CHART_SECTIONS.get(sheetName).concat("P_"));
                pages.add(page);
                ToolPageDTO pageQ = new ToolPageDTO();
                pageQ.setSheetName(sheetName);
                pageQ.setTitle("Flexible Storage: Down and Up Reactive Power. ");
                pageQ.setSection(T42T45FileOutputFormat.MAP_SHEET_NAME_CHART_SECTIONS.get(sheetName).concat("Q_"));
                pages.add(pageQ);
                break;
        }
        log.debug("mapPagesSheetNameAndTitle() - page setted : {}", page);
    }

    private boolean isPagePresent(String pageTitle, String sheetName, List<ToolPageDTO> pages) {
        int count = pages.stream().filter(p -> (p.getSheetName().equals(sheetName) && p.getTitle().equals(pageTitle))).toArray().length;
        return (count > 0);
    }
}
