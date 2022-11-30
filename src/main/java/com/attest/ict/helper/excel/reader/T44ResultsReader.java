package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.model.FlexibleCost;
import com.attest.ict.helper.excel.model.FlexibleOptionWithContin;
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
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T44ResultsReader extends ToolResultReader {

    public final Logger log = LoggerFactory.getLogger(T44ResultsReader.class);

    private final String SEPARATOR = "_";

    public Map<String, List<FlexibleOptionWithContin>> read(File excel) throws FileNotFoundException {
        Map<String, List<FlexibleOptionWithContin>> mapDataForSheet = new HashMap<String, List<FlexibleOptionWithContin>>();
        FileInputStream fileInputStream = new FileInputStream(excel);
        Workbook workbook = null;
        try {
            if (!ExcelFileUtils.hasExcelFormat(excel)) {
                throw new Exception("File format is invalid! ");
            }
            workbook = new XSSFWorkbook(fileInputStream);
            log.debug("Excel is composed by num.: {} sheets ", workbook.getNumberOfSheets());
            // Reading each sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                //log.debug("sheets {}, index {}", sheetName, i);
                List<FlexibleOptionWithContin> sheetData = new ArrayList<FlexibleOptionWithContin>();
                if (excel.getName().contains(T44FileOutputFormat.P_CONTIN_SUB_STR)) {
                    readSheetPContin(sheet, sheetData, excel.getName(), i);
                } else {
                    readSheet(sheet, sheetData, excel.getName());
                }

                mapDataForSheet.put(sheetName, sheetData);
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

    // Costs with contingencies are:
    // Geereation Cost
    // Flexibility Cost
    // Storage Cost
    // Load curt. Cost
    // RES curt. Cost
    // Post contingency costs
    // Flexibility Cost
    // Storage Cost
    // Load curt. Cost
    // RES curt. Cost
    // Total Cost
    // Elapsed time
    // ----
    // Cost less contingencies are:
    // Geereation Cost
    // Flexibility Cost
    // Storage Cost
    // Load curt. Cost
    // RES curt. Cost
    // Total Cost
    // Elapsed time

    public void readSheetCosts(Sheet sheet, List<FlexibleOptionWithContin> sheetData, String excelPath) {
        log.debug("Read COST sheetName: {} START ", sheet.getSheetName());
        // Reading each row of the sheet
        FlexibleOptionWithContin flexibleOption = new FlexibleOptionWithContin();
        List<FlexibleCost> costs = new ArrayList<FlexibleCost>();

        for (Row currentRow : sheet) {
            FlexibleCost flexCost = new FlexibleCost();
            for (Cell currentCell : currentRow) {
                CellType cellType = currentCell.getCellType();
                //log.debug(" Read RowIdx: {} CellIdx:{}  Val: {}", currentCell.getRowIndex(), currentCell.getColumnIndex(), cellVal);

                if (cellType.equals(CellType.BLANK) && currentCell.getColumnIndex() == 0) {
                    //log.debug("Row {} is empty, skip row ", currentCell.getRowIndex(), currentCell.getColumnIndex());
                    break;
                }
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        // cost type Generation, Flexibility etc...
                        flexCost.setCostType(ExcelFileUtils.getStringCellValue(currentCell));
                        break;
                    case 1:
                        // cost value
                        flexCost.setValue(ExcelFileUtils.getDoubleCellValue(currentCell));
                        break;
                    case 2:
                        // description sometime there is a cost description or note
                        flexCost.setDescription(ExcelFileUtils.getStringCellValue(currentCell));
                        break;
                }
            }
            costs.add(flexCost);
        }

        flexibleOption.setFlexCosts(costs);
        sheetData.add(flexibleOption);
        log.debug("Parsing sheetName: {} END ", sheet.getSheetName());
    }

    public void readSheet(Sheet sheet, List<FlexibleOptionWithContin> sheetData, String excelPath) {
        String fileNameExt = this.getFileExtension(excelPath);
        //-- COSTS
        if (
            sheet.getSheetName().equals(T44FileOutputFormat.SHEETS_NAME.get(7)) ||
            (fileNameExt.equals(T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(0)))
        ) {
            readSheetCosts(sheet, sheetData, excelPath);
            return;
        }

        log.debug("Parsing sheetName: {} START ", sheet.getSheetName());
        // Reading each row of the sheet
        for (Row currentRow : sheet) {
            if (ExcelFileUtils.isHeaderToSkip(currentRow.getRowNum())) {
                continue;
            }

            FlexibleOptionWithContin flexibleOption = new FlexibleOptionWithContin();
            List<Double> values = new ArrayList<Double>();
            // read all cell in row
            for (Cell currentCell : currentRow) {
                CellType cellType = currentCell.getCellType();

                //log.debug("Read Row num: {} CellIdx:{}  Val: {}", currentCell.getRowIndex(),	currentCell.getColumnIndex(), cellVal);

                if (cellType.equals(CellType.BLANK) && currentCell.getColumnIndex() == 0) {
                    //log.info("Row {} is empty, skip row ", currentCell.getRowIndex(), currentCell.getColumnIndex());
                    break;
                }

                switch (currentCell.getColumnIndex()) {
                    case 0:
                        // -- gen_nodes,flex_nodes, STR_nodes,LOAD_nodes .etc
                        Double dvalue = ExcelFileUtils.getDoubleCellValue(currentCell);
                        flexibleOption.setBusNum(dvalue.longValue());
                        break;
                    default:
                        // temporal values
                        values.add(ExcelFileUtils.getDoubleCellValue(currentCell));
                        break;
                }
            }

            flexibleOption.setValues(values);
            sheetData.add(flexibleOption);
        }

        log.debug("Parsing sheetName: {} END ", sheet.getSheetName());
    }

    // Sheet of _PCongintencies_*.xlsx files
    public void readSheetPContin(Sheet sheet, List<FlexibleOptionWithContin> sheetData, String excelPath, int indexSheet) {
        log.debug("Parsing excel with PContin sheetName: {} START ", sheet.getSheetName());
        if (indexSheet == 0) {
            readFirstSheetPContin(sheet, sheetData, excelPath);
        } else {
            readSheet(sheet, sheetData, excelPath);
        }
    }

    public void readFirstSheetPContin(Sheet sheet, List<FlexibleOptionWithContin> sheetData, String excelPath) {
        log.debug("Parsing excel with PContin First sheetName: {} START ", sheet.getSheetName());

        FlexibleOptionWithContin flexibleOption = new FlexibleOptionWithContin();
        Map<String, Integer> countMap = new HashMap<String, Integer>();

        // Reading each row of the sheet
        for (Row currentRow : sheet) {
            String label = "";
            Integer value = null;

            // Reading each cell in row
            for (Cell currentCell : currentRow) {
                CellType cellType = currentCell.getCellType();
                //log.debug("Read Row num: {} CellIdx:{}  Val: {}", currentCell.getRowIndex(),						currentCell.getColumnIndex(), cellVal);

                if (cellType.equals(CellType.BLANK) && currentCell.getColumnIndex() == 0) {
                    //log.info("Row {} is empty, skip row ", currentCell.getRowIndex(), currentCell.getColumnIndex());
                    break;
                }

                switch (currentCell.getColumnIndex()) {
                    case 0:
                        label = ExcelFileUtils.getStringCellValue(currentCell);
                        break;
                    case 1:
                        // contingencies num, scenario num, flexible num, storage num ...
                        if (cellType.equals(CellType.NUMERIC)) {
                            Double dvalue = ExcelFileUtils.getDoubleCellValue(currentCell);
                            value = dvalue.intValue();
                            break;
                        }
                        if (cellType.equals(CellType.STRING)) {
                            //possible values are:  'No flexible load', 'No Storage' in these case set to zero
                            value = 0;
                            break;
                        }
                }
            }
            countMap.put(label, value);
        }
        flexibleOption.setFlexibleCountMap(countMap);
        sheetData.add(flexibleOption);

        log.debug("Parsing excel with PContin First sheetName: {} END ", sheet.getSheetName());
    }

    /**
     * Parsing output filenames
     * @param output fileName
     * @return outputFileName's extension. Possible value are (_Costs, _Normal, _STR, RES_C, LC, FL_Dec, FL_Inc, Reactive_P, Active_P )
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) throw new RuntimeException("fileName shouldn't be empty");

        String ext = "";

        int pos = fileName.contains(T44FileOutputFormat.P_CONTIN_SUB_STR)
            ? fileName.lastIndexOf(T44FileOutputFormat.P_CONTIN_SUB_STR)
            : fileName.lastIndexOf("_");
        int posStart = fileName.contains(T44FileOutputFormat.P_CONTIN_SUB_STR)
            ? pos + (T44FileOutputFormat.P_CONTIN_SUB_STR.length())
            : pos + 1;
        int posEnd = fileName.lastIndexOf(".");

        try {
            ext = fileName.substring(posStart, posEnd);
        } catch (IndexOutOfBoundsException ie) {
            log.error("Error getting fileExtension: ", ie.getMessage());
        }

        return ext;
    }

    public boolean isFilePresent(String outputDirName, String extension) throws FileNotFoundException {
        boolean present = false;
        File outputDir = new File(outputDirName);
        if (!outputDir.isDirectory()) {
            throw new FileNotFoundException("OutputDir not found: " + outputDirName);
        }

        File[] fileList = outputDir.listFiles();
        if (fileList.length == 0) {
            return false;
        }

        for (File file : fileList) {
            String fileName = file.getName();
            String fileExt = this.getFileExtension(fileName);
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return present;
    }

    public Integer readAllFileInDir(String directoryName, Map<String, List<FlexibleOptionWithContin>> mapAllExcelData)
        throws FileNotFoundException {
        int numFileRead = 0;
        File dir = new File(directoryName);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory not found: " + directoryName);
        }
        File[] fileList = dir.listFiles();
        if (fileList.length == 0) {
            log.warn("Directory: {}, doesn't contains any files ", directoryName);
            return 0;
        }

        for (File excelFile : fileList) {
            if (!isT44Results(excelFile)) {
                // skip
                continue;
            }

            try {
                Map<String, List<FlexibleOptionWithContin>> mapDataForSheet = new HashMap<String, List<FlexibleOptionWithContin>>();
                log.info(" fileName " + excelFile);
                mapDataForSheet = this.read(excelFile);
                numFileRead++;
                mapAllExcelData.putAll(mapDataForSheet);
            } catch (FileNotFoundException e) {
                throw e;
            }
        }
        return numFileRead;
    }

    /**
     *
     * @param directoryName
     * @param mapAllExcelData
     * @param type
     * @return number of file read
     * @throws FileNotFoundException
     */
    public Integer readFileInDirByType(String directoryName, Map<String, List<FlexibleOptionWithContin>> mapAllExcelData, String type)
        throws FileNotFoundException {
        int numFileRead = 0;
        log.info("ReadFile by Type: {} ", type);

        File dir = new File(directoryName);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory not found: " + directoryName);
        }

        File[] fileList = dir.listFiles();
        if (fileList.length == 0) {
            log.warn("Directory: {}, doesn't contains any files ", directoryName);
            return 0;
        }

        for (File excelFile : fileList) {
            if (!isT44Results(excelFile) || !isInFileName(excelFile, type)) {
                // skip
                continue;
            }
            Map<String, List<FlexibleOptionWithContin>> mapDataForSheet = new HashMap<String, List<FlexibleOptionWithContin>>();
            log.info(" Read fileName: " + excelFile);
            mapDataForSheet = this.read(excelFile);
            numFileRead++;
            mapAllExcelData.putAll(mapDataForSheet);
        }

        return numFileRead;
    }

    private boolean isT44Results(File excelFile) {
        if (!excelFile.isFile()) {
            log.error("{} is Not a file, skip ", excelFile.getName());
            return false;
        }

        String fileName = excelFile.getName();
        String extension = this.getFileExtension(fileName);
        return T44FileOutputFormat.OUTPUT_FILES_EXTENSION.contains(extension);
    }

    /**
     * @param mapAllExcelData map with data from all files provided in post contingency operation model
     * @return Map<String, Integer> keys possible values are: 'Number of Contingencies', 'Number of Scenarios' or
     * empyMap in case of error or no data present in mapAllExcelData
     */
    public Map<String, Integer> getNContingNSc(Map<String, List<FlexibleOptionWithContin>> mapAllExcelData) {
        // sheet 'Active_power'
        String key = T44FileOutputFormat.SHEETS_NAME_ACTIVE_P.get(0);
        List<FlexibleOptionWithContin> activePFlexOpts = mapAllExcelData.get(key);

        if (activePFlexOpts.size() == 1) {
            FlexibleOptionWithContin flexOpt = activePFlexOpts.get(0);
            Map<String, Integer> countNscNConting = flexOpt.getFlexibleCountMap();
            //countNscNConting.entrySet().forEach(entry -> log.debug(" CountMap: {}    ", entry));
            return countNscNConting;
        } else {
            // if file is *_OPF.xlsx
            return Collections.emptyMap();
        }
    }

    /**
     * Get flexibility options related to a specific contingency (contingIdx) and scenario (nScIdx)
     * @param contingIdx
     * @param nScIdx
     * @param mapAllExcelData
     * @return map
     */
    public Map<String, List<FlexibleOptionWithContin>> getDataByNContingAndNsc(
        int contingIdx,
        int nScIdx,
        Map<String, List<FlexibleOptionWithContin>> mapAllExcelData
    ) {
        if (mapAllExcelData.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<FlexibleOptionWithContin>> mapDataByNContingNsc = new HashMap<String, List<FlexibleOptionWithContin>>();

        // -- ("Active_power", "Reactive_Power", "FL_inc", "FL_dec", "STR", "Load_curtailment", "RES_curtailment", "COSTS")
        for (String sheetName : T44FileOutputFormat.SHEETS_NAME) {
            // SheetName != COST
            if (!sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(7))) {
                String key = findKeyWithConting(sheetName, contingIdx, nScIdx);
                if (mapAllExcelData.containsKey(key)) {
                    mapDataByNContingNsc.put(sheetName, mapAllExcelData.get(key));
                } else {
                    //No FLexible option present
                    mapDataByNContingNsc.put(sheetName, Collections.EMPTY_LIST);
                }
            }
        }

        return mapDataByNContingNsc;
    }

    /**
     *
     * @param sheetName
     * @return Active_power_Contin_1_Scen_1, FL_dec_Contin_1_Scen_1,
     *         FL_inc_Contin_1_Scen_1 etc
     */
    private String findKeyWithConting(String sheetName, int nContingIdx, int nScIndex) {
        String key = "";
        // -- Active_power
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(0))) {
            key = T44FileOutputFormat.SHEETS_NAME_ACTIVE_P.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }

        // -- Reactive_Power
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(1))) {
            key = T44FileOutputFormat.SHEETS_NAME_REACTIVE_P.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }

        // -- "FL_inc"
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(2))) {
            key = T44FileOutputFormat.SHEETS_NAME_FL_INC.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }

        // -- "FL_dec"
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(3))) {
            key = T44FileOutputFormat.SHEETS_NAME_FL_DEC.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }

        // "STR"
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(4))) {
            key = T44FileOutputFormat.SHEETS_NAME_STR.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }
        // "Load_curtailment"
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(5))) {
            key = T44FileOutputFormat.SHEETS_NAME_LC.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }
        // "RES_curtailment"
        if (sheetName.equals(T44FileOutputFormat.SHEETS_NAME.get(6))) {
            key = T44FileOutputFormat.SHEETS_NAME_RES_C.get(1);
            return (!key.isEmpty())
                ? key.concat(SEPARATOR + nContingIdx).concat(SEPARATOR).concat(T44FileOutputFormat.P_CONTIN_SCEN_STR + nScIndex)
                : "";
        }

        return key;
    }
}
