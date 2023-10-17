package com.attest.ict.helper.excel.reader;

import static java.util.stream.Collectors.toList;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import com.attest.ict.service.dto.custom.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41ResultsReader {

    public static final Logger log = LoggerFactory.getLogger(T41ResultsReader.class);

    public static final List<String> STATISTIC_DATA = Stream.of("Min", "Max", "Avg", "Sum").collect(Collectors.toList());

    public List<T41LogInfoDTO> parseOutLogFile(File relativePath) {
        ExcelReader excelReader = new ExcelReader(relativePath);
        Sheet logSheet = excelReader.getSheetByName(T41FileOutputFormat.LOG_FILE_SHEET);
        if (logSheet == null) {
            throw new ExcelReaderFileException("Sheet: " + logSheet + " not found!");
        }
        int indexHeader = 0;
        Map<Integer, List<String>> mapRowData = excelReader.parseRowData(
            logSheet,
            T41FileOutputFormat.IS_HEADER_EXISTS_IN_LOG_FILE,
            indexHeader
        );
        excelReader.closeWorkBook();
        T41LogInfoResults t41LogInfoResults = new T41LogInfoResults();
        return t41LogInfoResults.setRowData(mapRowData, T41FileOutputFormat.LOG_FILE_SHEET);
    }

    public Map<String, List<FlexibleOption>> parseFlexDataSheets(File excel) throws ExcelReaderFileException {
        log.info("parseFlexDataSheets() - START parse excel file {} ", excel.getName());

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
                if (!T41FileOutputFormat.FLEX_SHEETS_NAME.contains(sheetName)) {
                    log.debug("parseFlexDataSheets() - Sheet {}, not required skip ", sheetName);
                } else {
                    log.info("parseFlexDataSheets() - sheet: {}", sheetName);
                    List<FlexibleOption> sheetData = new ArrayList<FlexibleOption>();
                    // Reading each row of the sheet
                    for (Row currentRow : sheet) {
                        // log.debug("   row num: {} ", currentRow.getRowNum());

                        FlexibleOption flexibleOption = new FlexibleOption();
                        List<Double> values = new ArrayList<Double>();

                        // skip header
                        if (currentRow.getRowNum() == 0) {
                            // log.debug("   Skip header");
                            continue;
                        }
                        if (ExcelFileUtils.isEmptyRow(currentRow)) {
                            // log.debug("   Skip Empty Row");
                            continue;
                        }

                        int cellIdx = 0;

                        // read all cell in row
                        for (Cell currentCell : currentRow) {
                            CellType cellType = currentCell.getCellType();
                            if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                                // log.info("   Row {} is empty, skip row ", currentRow.getRowNum(), cellIdx);
                                break;
                            }
                            if (cellType.equals(CellType.STRING) && cellIdx == 0 && isStatisticData(currentCell.getStringCellValue())) {
                                log.debug(
                                    "parseFlexDataSheets() - Row {} contatins statistic data, skip row ",
                                    currentRow.getRowNum(),
                                    cellIdx
                                );
                                break;
                            }
                            switch (cellIdx) {
                                case 0:
                                    log.info(
                                        "parseFlexDataSheets() - Row: {}  Column: {}  CellType:{}",
                                        currentRow.getRowNum(),
                                        cellIdx,
                                        cellType
                                    );

                                    try {
                                        double cellval = ExcelFileUtils.getDoubleCellValue(currentCell);
                                        if (sheetName.equals(T41FileOutputFormat.FLEX_SHEETS_NAME.get(5))) {
                                            // cost
                                            flexibleOption.setCost(cellval);
                                        } else {
                                            // -- busNum
                                            flexibleOption.setBusNum(Double.valueOf(cellval).longValue());
                                            log.info("parseFlexDataSheets() - BusNum: {} ", Double.valueOf(cellval).longValue());
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
                                        double cellval = ExcelFileUtils.getDoubleCellValue(currentCell);
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
                        if (flexibleOption.getBusNum() != null || flexibleOption.getCost() != null) {
                            log.debug("parseFlexDataSheets() - Add FlexibleOption: {} ", flexibleOption);
                            sheetData.add(flexibleOption);
                        } // else log.debug(" Bus Num is null!");
                    }
                    //T41, configured to run without flexibility, generates an output file containing completely empty  flexibility sheets
                    if (!sheetData.isEmpty()) {
                        mapDataForSheet.put(sheetName, sheetData);
                    } else {
                        log.warn("parseFlexDataSheets() - SheetName: {}, is empty", sheetName);
                    }
                }
                log.info("parseFlexDataSheets() - END reading excel file {} ", excel.getName());
            }
        } catch (Exception e) {
            throw new ExcelReaderFileException(
                "parseFlexDataSheets() - Error parsing Excel file: " + excel.getPath() + " " + e.getMessage()
            );
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

    public T41ResultsPagesDTO getPagesToShow(File relativePath) {
        ExcelReader excelReader = new ExcelReader(relativePath);
        List<String> sheetsName = excelReader.getSheetsName();
        List<T41PageDTO> pages = new ArrayList<>();
        sheetsName.stream().forEach(sheetName -> mapPagesSheetNameAndOptions(sheetName, pages));
        T41ResultsPagesDTO resultsPages = new T41ResultsPagesDTO();
        resultsPages.setPages(pages);
        excelReader.closeWorkBook();
        return resultsPages;
    }

    public T41TableDataDTO parseResultsBySheetNameAndScenario(File outputResults, String sheetName, Integer scenario) {
        log.debug(
            "parseResultsBySheetNameAndScenario() - outputFile: {}, sheetName: {}, scenario: {} " + outputResults.getName(),
            sheetName,
            scenario
        );
        boolean headerExists = true;
        T41TableDataDTO table = new T41TableDataDTO();
        ExcelReader reader = new ExcelReader(outputResults);
        try {
            log.debug("parseResultsBySheetNameAndScenario() - Start Parsing sheet: " + sheetName);
            Sheet sheet = reader.getSheetByName(sheetName);
            if (sheet == null) {
                // log.debug("Sheet referring to pageTitle: " + pageTitle + " not found! ");
                throw new ExcelReaderFileException("Sheet: " + sheetName + " not found!");
            }

            if (headerExists) {
                String[] headers = reader.parseHeaderBySheetName(sheetName, 0);
                log.debug("parseResultsBySheetNameAndScenario() - Sheet header: " + " " + Arrays.stream(headers).collect(toList()));
                List<ColumnDefDTO> columnsDefs = setColumnDefs(headers);
                table.setColumnDefs(columnsDefs);
            }
            int indexHeader = 0;
            Map<Integer, List<String>> data = reader.parseRowData(sheet, headerExists, indexHeader);
            if (data.isEmpty()) {
                log.error("parseResultsBySheetNameAndScenario() - No data found in Sheet: {} ", sheetName);
                throw new ExcelReaderFileException("No data found in Sheet " + sheetName);
            }

            table.setTitle(sheetName);
            table.setTitleLongDescription(T41FileOutputFormat.SHEETNAME_TITLE_MAP.get(sheetName));
            table.setyAxisTitle(T41FileOutputFormat.SHEETNAME_SHORT_TITLE_MAP.get(sheetName));
            switch (sheetName) {
                case "VOLT":
                case "Crnt_PU":
                case "Crnt_SI":
                case "P_load":
                case "Q_load":
                case "Pg_max":
                case "Qg_min":
                case "Qg_max":
                    T41QuantityForScenAndIdResults t41QuantityForScenAndIdResults = new T41QuantityForScenAndIdResults();
                    List<T41QuantityForScenAndIdDTO> rowDataQuantityForScen = t41QuantityForScenAndIdResults.setRowData(
                        data,
                        sheetName,
                        scenario
                    );
                    table.setScenarios(rowDataQuantityForScen.stream().map(row -> row.getScen()).distinct().sorted().collect(toList()));
                    table.setRowData(rowDataQuantityForScen);
                    break;
                case "Vlt_Viol":
                    T41VoltageViolationResults vltViolationResults = new T41VoltageViolationResults();
                    List<T41VoltageViolationDTO> rowDataVltViol = vltViolationResults.setRowData(data, sheetName, scenario);
                    table.setScenarios(rowDataVltViol.stream().map(row -> row.getScen()).distinct().sorted().collect(toList()));
                    table.setRowData(rowDataVltViol);
                    break;
                case "Crnt_Viol":
                    T41LineViolationResults lineViolationResults = new T41LineViolationResults();
                    List<T41LineViolationDTO> rowDataLineViol = lineViolationResults.setRowData(data, sheetName, scenario);
                    table.setScenarios(rowDataLineViol.stream().map(row -> row.getScen()).distinct().sorted().collect(toList()));
                    table.setRowData(rowDataLineViol);
                    break;
            }
        } catch (Exception ex) {
            log.error("parseResultsBySheetNameAndScenario() - Error parsing T41 Results ", ex.getMessage());
        } finally {
            reader.closeWorkBook();
        }
        return table;
    }

    private List<ColumnDefDTO> setColumnDefs(String[] headers) {
        List<ColumnDefDTO> columnsDefs = new ArrayList<>();
        for (String header : headers) {
            // field, filter, headerName;
            ColumnDefDTO colDef = new ColumnDefDTO(setFieldName(header), setFilterOption(header), header);
            columnsDefs.add(colDef);
        }
        return columnsDefs;
    }

    /**
     * @param excelFile output file generated by T41 tool.
     * @return true if excelFile name contains '_output'.xlsx
     */
    public static boolean isT41Results(File excelFile) {
        if (!excelFile.isFile()) {
            log.error("isT41Results() - {} is Not a file, skips ", excelFile.getName());
            return false;
        }

        String fileName = excelFile.getName();
        String extension = T41ResultsReader.getFileExtension(fileName);
        return T41FileOutputFormat.SUFFIX_OUTPUT_FILES_EXTENSION.contains(extension);
    }

    /**
     * Parsing output filenames
     *
     * @param fileName name of the output file generated by the tool: T41
     * @return outputFileName's extension: '_output'
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) throw new ExcelReaderFileException("FileName shouldn't be empty");

        String ext = "";
        int posStart = fileName.lastIndexOf("_") + 1;
        int posEnd = fileName.lastIndexOf(".");

        try {
            ext = fileName.substring(posStart, posEnd);
        } catch (IndexOutOfBoundsException ie) {
            log.error("getFileExtension() - Error get fileExtension: ", ie.getMessage());
        }

        return ext;
    }

    private boolean isStatisticData(String stringCellValue) {
        return STATISTIC_DATA.contains(stringCellValue);
    }

    private boolean setFilterOption(String header) {
        return T41FileOutputFormat.HEADERS_WITH_FILTER.contains(header);
    }

    private String setFieldName(String header) {
        if (!T41FileOutputFormat.HEADERS_WITH_FILTER.contains(header) && !T41FileOutputFormat.HEADERS_VIOLATION_DATA.contains(header)) {
            return getTimeSeriesIndex(header);
        }
        return header;
    }

    private String getTimeSeriesIndex(String header) {
        int pos = header.indexOf("T");
        if (pos < 0) {
            return header;
        }
        //Class TimeSeriesHourDTO is zero based and attribute's name are in lower case (t0,t1,...t24)
        int index = Integer.parseInt(header.substring(pos + 1)) - 1;
        return "timeSeriesHour.".concat("t" + index);
    }

    private void mapPagesSheetNameAndOptions(String sheetName, List<T41PageDTO> pages) {
        T41PageDTO page = new T41PageDTO();
        page.setSheetName(sheetName);
        String operationalPointTitle = T41FileOutputFormat.SHEETNAME_TITLE_MAP.get(sheetName);
        page.setTitle(operationalPointTitle);
        switch (sheetName) {
            case "VOLT":
                page.setInitialPF(true);
                page.setViolation(false);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Crnt_PU":
                page.setInitialPF(true);
                page.setViolation(false);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Crnt_SI":
                page.setInitialPF(true);
                page.setViolation(false);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "P_load":
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(true);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Q_load":
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(true);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Pg_max":
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(true);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Qg_max":
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(true);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Vlt_Viol":
                page.setInitialPF(false);
                page.setViolation(true);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "Crnt_Viol":
                page.setInitialPF(false);
                page.setViolation(true);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(false);
                pages.add(page);
                break;
            case "APC_MW":
                page.setTitle("Active power curtailment");
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(true);
                pages.add(page);
                break;
            case "EES_CH_MW":
            case "EES_DCH_MW":
                String title = "Energy storage charging/discharging events";
                sheetName = "EES_";
                if (!isPagePresent(title, sheetName, pages)) {
                    page.setSheetName(sheetName);
                    page.setTitle(title);
                    page.setInitialPF(false);
                    page.setViolation(false);
                    page.setLoadAndResInputData(false);
                    page.setFlexibilityData(true);
                    pages.add(page);
                }
                break;
            case "FL_OD_MW":
            case "FL_UD_MW":
                sheetName = "FL_";
                title = "Flexible load events of over-demand/under-demand (increasing/decreasing consumption)";
                if (!isPagePresent(title, sheetName, pages)) {
                    page.setSheetName(sheetName);
                    page.setTitle(title);
                    page.setInitialPF(false);
                    page.setViolation(false);
                    page.setLoadAndResInputData(false);
                    page.setFlexibilityData(true);
                    pages.add(page);
                }
                break;
            default:
                page.setSheetName("ALL");
                page.setTitle("All Flexible Results");
                page.setInitialPF(false);
                page.setViolation(false);
                page.setLoadAndResInputData(false);
                page.setFlexibilityData(true);
                pages.add(page);
                break;
        }
    }

    private boolean isPagePresent(String pageTitle, String sheetName, List<T41PageDTO> pages) {
        int count = pages.stream().filter(p -> (p.getSheetName().equals(sheetName) && p.getTitle().equals(pageTitle))).toArray().length;
        return (count > 0);
    }
}
