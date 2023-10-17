package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.helper.excel.model.FlexibleCost;
import com.attest.ict.helper.excel.model.FlexibleOptionWithContin;
import com.attest.ict.helper.excel.model.PostContingenciesRow;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T44V3ResultsReader extends ToolResultReader {

    public final Logger log = LoggerFactory.getLogger(T44V3ResultsReader.class);

    private final String SEPARATOR = "_";

    //-- 20230412 New Class

    /**
     * @param filePostContin file to parse generated by the T44V3 tool with extension post_contin.xlsx
     *                       parse the sheeta: 'Contin_map' sheet, to identify the number of contingencies and sheet 'Active_power' to identify the number of number of scenarios.
     * @return Map  with keys: "number_of_contingencies","number_of_scenarios".
     */
    public Map<String, Integer> parseContinMapAndActivePowerSheets(File filePostContin) {
        String excelPath = filePostContin.getAbsolutePath();
        ExcelReader excelReader = new ExcelReader(filePostContin.getAbsolutePath());

        try {
            //--- reads 'Contin_map' sheet, to identify the number of contingencies
            Sheet sheetContinMap = excelReader.getSheetByName(T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(0));
            int continColumnNum = 0;
            log.info("Parsing excel file: {}, sheetName: {} , column: {} ", excelPath, sheetContinMap.getSheetName(), continColumnNum);
            List<String> contingencies = excelReader.parseColumn(sheetContinMap, continColumnNum, true);
            Integer numContingencies = contingencies.isEmpty()
                ? 0
                : contingencies.stream().map(ConverterUtils::convertStringToInteger).max(Integer::compare).get();
            log.debug(" -- Number of Contingencies: {} ", numContingencies);

            Map<String, Integer> flexibleCountMap = new HashMap<String, Integer>();
            flexibleCountMap.put(T44V3FileOutputFormat.TABLE_KEY_NUMBER_OF_CONTIN, numContingencies);

            //--- read 'Active_power' sheet, to identify the number of total scenarios
            Sheet sheetActivePowerScenarios = excelReader.getSheetByName(T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(1));
            int scenarionColumnNum = 1;
            log.info(
                "Parsing excel file: {}, sheetName: {}, scenarionColumnNum: {}  ",
                excelPath,
                sheetActivePowerScenarios.getSheetName(),
                scenarionColumnNum
            );

            List<String> scenarios = excelReader.parseColumn(sheetActivePowerScenarios, scenarionColumnNum, true);
            Integer numScenario = scenarios.isEmpty()
                ? 0
                : scenarios.stream().map(ConverterUtils::convertStringToInteger).max(Integer::compare).get();
            log.debug(" -- Number of scenarios: {} ", numScenario);
            flexibleCountMap.put(T44V3FileOutputFormat.TABLE_KEY_NSC, numScenario);
            return flexibleCountMap;
        } catch (Exception ex) {
            log.error(" Error parsing T44 Results ", ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            excelReader.closeWorkBook();
        }
    }

    /**
     * @param filePostContin file generated by the T44V3 tool eg. <CaseName>_<wfOrwof>_post_contin.xlsx
     *                       Parse all _post_contin.xlsx's sheets without considering 'Contin_map'
     * @return a Map with sheetName as key and list of all value read from file. Values are  fileterd by  'numContin' and 'numScenario'
     * @throws FileNotFoundException
     */
    public Map<String, List<FlexibleOptionWithContin>> getPostContinDataByNumContinAndNumScenario(
        File filePostContin,
        Integer numContin,
        Integer numScenario
    ) {
        Map<String, List<FlexibleOptionWithContin>> mapDataForAllSheets = new HashMap<String, List<FlexibleOptionWithContin>>();
        //-- read  sheets: "Active_power", "Reactive_power", "FL_inc", "FL_dec", "STR", "Load_curtail", "RES_curtail"
        Map<String, List<PostContingenciesRow>> mapPostContinDataForSheets = parseFlexDataByNumContinAndNumScenario(
            filePostContin,
            numContin,
            numScenario
        );
        for (Map.Entry<String, List<PostContingenciesRow>> sheetData : mapPostContinDataForSheets.entrySet()) {
            String sheetName = sheetData.getKey();
            List<PostContingenciesRow> contingenciesRows = mapPostContinDataForSheets.get(sheetName);
            // contingency and scenario are fixed, group by AssetUnits to obtain all hourly values
            Map<Long, List<PostContingenciesRow>> postContinGroupByAssetUnit = groupByAssetUnit(mapPostContinDataForSheets, sheetName);
            List<FlexibleOptionWithContin> flexibleOptionList = setFlexOptionList(postContinGroupByAssetUnit);
            mapDataForAllSheets.put(sheetName, flexibleOptionList);
        }
        // --- sheet: Costs
        List<FlexibleOptionWithContin> flexibleOptionCostList = parseFlexCost(filePostContin);
        String convertSheetNameIntoNormalFile = T44V3FileOutputFormat.MAP_POST_CONTIN_NORMAL_SHEETS.get(
            T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(8)
        );
        mapDataForAllSheets.put(convertSheetNameIntoNormalFile, flexibleOptionCostList);
        return mapDataForAllSheets;
    }

    /**
     * @param filePostContin file  generated by the T44V3 tool
     * @param numContin      null or id of a contingency
     * @param numScenario    null or id of a scenario
     *                       Parse all _post_contin.xlsx file without considering the sheets: 'Contin_map' and 'Costs'
     * @return a Map with sheetName as key and list of all value read from file. Values are  fileterd by  'numContin' and 'numScenario' if they are specified as input parameter
     * @throws FileNotFoundException
     */
    public Map<String, List<PostContingenciesRow>> parseFlexDataByNumContinAndNumScenario(
        File filePostContin,
        Integer numContin,
        Integer numScenario
    ) {
        // Read file with extension: post_contin,
        // Discard sheets: Contin_map, Costs
        List<String> sheetsToSkip = Stream
            .of(T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(0), T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(8))
            .collect(Collectors.toList());
        String excelPath = filePostContin.getAbsolutePath();
        ExcelReader excelReader = new ExcelReader(filePostContin.getAbsolutePath());
        try {
            Map<String, List<PostContingenciesRow>> mapData = new HashMap<>();
            List<String> sheetsName = excelReader
                .getSheetsName()
                .stream()
                .filter(str -> !sheetsToSkip.contains(str))
                .collect(Collectors.toList());
            for (String sheetName : sheetsName) {
                List<PostContingenciesRow> listRowForSheet = new ArrayList<>();
                Sheet sheet = excelReader.getSheetByName(sheetName);
                log.info("Parsing file: {} sheet: {} START ", excelPath, sheet.getSheetName());
                // Reading each row of the sheet
                for (Row currentRow : sheet) {
                    if (ExcelFileUtils.isHeaderToSkip(currentRow.getRowNum())) {
                        continue;
                    }

                    PostContingenciesRow postContinRow = new PostContingenciesRow();

                    List<Double> values = new ArrayList<Double>();
                    // read all cell in row
                    for (Cell currentCell : currentRow) {
                        CellType cellType = currentCell.getCellType();

                        // log.info(" Reading Row {}, Column {}  ", currentCell.getRowIndex(), currentCell.getColumnIndex());
                        if (cellType.equals(CellType.BLANK) && currentCell.getColumnIndex() == 0) {
                            // log.debug("-- Row num: {} is empty, skip!  ", currentCell.getRowIndex());
                            break;
                        }

                        switch (currentCell.getColumnIndex()) {
                            case 0:
                                // Contingency
                                Double dValue = ExcelFileUtils.getDoubleCellValue(currentCell);
                                postContinRow.setContingency(dValue.longValue());
                                break;
                            case 1:
                                // Scenario
                                dValue = ExcelFileUtils.getDoubleCellValue(currentCell);
                                postContinRow.setScenario(dValue.longValue());
                                break;
                            case 2:
                                // time
                                dValue = ExcelFileUtils.getDoubleCellValue(currentCell);
                                postContinRow.setTime(dValue.longValue());
                                break;
                            case 3:
                                // assetUnit
                                dValue = ExcelFileUtils.getDoubleCellValue(currentCell);
                                postContinRow.setAssetUnits(dValue.longValue());
                                break;
                            case 4:
                                // value
                                Double dvalue = ExcelFileUtils.getDoubleCellValue(currentCell);
                                postContinRow.setValues(dvalue);
                                break;
                        }
                    }
                    // log.debug("-- Row: {}", postContinRow);
                    listRowForSheet.add(postContinRow);
                }
                if (numContin != null && numScenario != null) {
                    List<PostContingenciesRow> listRowForSheetFiltered = listRowForSheet
                        .stream()
                        .filter(row ->
                            (row.getContingency().equals(numContin.longValue()) && row.getScenario().equals(numScenario.longValue()))
                        )
                        .collect(Collectors.toList());

                    mapData.put(T44V3FileOutputFormat.MAP_POST_CONTIN_NORMAL_SHEETS.get(sheetName), listRowForSheetFiltered);
                } else {
                    mapData.put(T44V3FileOutputFormat.MAP_POST_CONTIN_NORMAL_SHEETS.get(sheetName), listRowForSheet);
                }
                log.info("Parsing file: {} sheet: {}  END ", excelPath, sheet.getSheetName());
            }
            return mapData;
        } catch (Exception ex) {
            log.error("Exception parsing T44V3 Results file {}. Error:{} ", excelPath, ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            excelReader.closeWorkBook();
        }
    }

    public List<FlexibleOptionWithContin> parseFlexCost(File filePostContin) {
        // Read sheet:  Costs
        String sheetName = T44V3FileOutputFormat.POST_CONTIN_SHEETS_NAME.get(8);
        String excelPath = filePostContin.getAbsolutePath();
        ExcelReader excelReader = new ExcelReader(filePostContin.getAbsolutePath());
        try {
            Sheet sheet = excelReader.getSheetByName(sheetName);
            log.info("Parsing file: {} sheet: {} START ", excelPath, sheet.getSheetName());
            log.debug("Parsing sheet: {} START ", sheet.getSheetName());
            // Reading each row of the sheet
            FlexibleOptionWithContin flexibleOption = new FlexibleOptionWithContin();
            List<FlexibleCost> costs = new ArrayList<FlexibleCost>();

            for (Row currentRow : sheet) {
                FlexibleCost flexCost = new FlexibleCost();
                if (ExcelFileUtils.isHeaderToSkip(currentRow.getRowNum())) {
                    continue;
                }
                for (Cell currentCell : currentRow) {
                    CellType cellType = currentCell.getCellType();
                    log.debug(
                        "--- Read RowIdx: {} CellIdx:{}  Val: {}",
                        currentCell.getRowIndex(),
                        currentCell.getColumnIndex(),
                        currentCell
                    );

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

            List<FlexibleOptionWithContin> sheetData = new ArrayList<FlexibleOptionWithContin>();
            sheetData.add(flexibleOption);
            log.debug("Parsing sheet: {} END ", sheet.getSheetName());
            return sheetData;
        } catch (Exception ex) {
            log.error("Exception parsing T44 Results file {}, Sheet: {} Error:{} ", excelPath, sheetName, ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            excelReader.closeWorkBook();
        }
    }

    // --- Private Methods

    private Map<Long, List<PostContingenciesRow>> groupByAssetUnit(
        Map<String, List<PostContingenciesRow>> mapPostContinDataForSheets,
        String sheetName
    ) {
        // contingency and scenario are fixed, group by AssetUnits
        List<PostContingenciesRow> contingenciesRows = mapPostContinDataForSheets.get(sheetName);
        Map<Long, List<PostContingenciesRow>> multipleFieldsMapList = contingenciesRows
            .stream()
            .collect(Collectors.groupingBy(PostContingenciesRow::getAssetUnits));
        log.debug(" -- SheetName: {}", sheetName);
        log.debug(" -- GroupBy AssetUnits: {}", Arrays.deepToString(multipleFieldsMapList.entrySet().stream().toArray()));
        return multipleFieldsMapList;
    }

    private List<FlexibleOptionWithContin> setFlexOptionList(Map<Long, List<PostContingenciesRow>> groupByAssetUnitMapList) {
        List<FlexibleOptionWithContin> flexibleOptionList = new ArrayList<FlexibleOptionWithContin>();
        for (Map.Entry<Long, List<PostContingenciesRow>> entry : groupByAssetUnitMapList.entrySet()) {
            Long assetUnit = entry.getKey();
            List<PostContingenciesRow> postContingenciesRow = entry.getValue();
            List<Double> values = postContingenciesRow.stream().map(pcr -> pcr.getValue()).collect(Collectors.toList());
            log.debug(" -- AssetUnit {} ,values {}", assetUnit, values);
            FlexibleOptionWithContin flexOpt = new FlexibleOptionWithContin();
            flexOpt.setBusNum(assetUnit);
            flexOpt.setValues(values);
            flexibleOptionList.add(flexOpt);
        }
        return flexibleOptionList;
    }

    private int getPosStart(String fileName) {
        if (fileName == null) throw new RuntimeException("FileName shouldn't be empty!");

        int posStart = fileName.contains(T44V3FileOutputFormat.WITH_FLEXIBILITY)
            ? fileName.lastIndexOf(T44V3FileOutputFormat.WITH_FLEXIBILITY + "_") + T44V3FileOutputFormat.WITH_FLEXIBILITY.length()
            : fileName.lastIndexOf(T44V3FileOutputFormat.WITHOUT_FLEXIBILITY + "_") + +T44V3FileOutputFormat.WITHOUT_FLEXIBILITY.length();

        return posStart + 1;
    }

    public static void main(String[] args) {
        String relativePath = "C:\\ATSIM\\WP4\\T44\\655d675b-df87-4305-ab71-301868308446\\output_data\\PT 2030_2030_SU_wf_post_contin.xlsx";
        File fileToParse = new File(relativePath);
        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        boolean test4 = false;
        boolean test5 = false;
        boolean test6 = false;
        boolean test7 = false;
        boolean test8 = true;

        T44V3ResultsReader t44V3ResultsReader = new T44V3ResultsReader();

        if (test1) {
            try {
                Map<String, Integer> tableResults = new HashMap<String, Integer>();
                tableResults = t44V3ResultsReader.parseContinMapAndActivePowerSheets(fileToParse);
                System.out.println("=========================== ");
                System.out.println(" Contingencies and Scenarios Values: ");
                System.out.println("  " + Arrays.deepToString(tableResults.entrySet().stream().toArray()));
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (test2) {
            try {
                Map<String, List<PostContingenciesRow>> mapData = new HashMap<>();
                mapData = t44V3ResultsReader.parseFlexDataByNumContinAndNumScenario(fileToParse, 2, 5);
                System.out.println("=========================== ");
                System.out.println(" Contingencies and Scenarios Values: ");
                System.out.println("  " + Arrays.deepToString(mapData.entrySet().stream().toArray()));
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (test3) {
            try {
                Map<String, List<PostContingenciesRow>> mapData = new HashMap<>();
                mapData = t44V3ResultsReader.parseFlexDataByNumContinAndNumScenario(fileToParse, 20, 5);
                System.out.println("=========================== ");
                System.out.println(" Contingencies and Scenarios Values: ");
                System.out.println("  " + Arrays.deepToString(mapData.entrySet().stream().toArray()));
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (test4) {
            try {
                Integer numContin = 2;
                Integer numScenario = 5;
                String sheetName = "Active_power";
                Map<String, List<PostContingenciesRow>> mapPostContinDataForSheets = t44V3ResultsReader.parseFlexDataByNumContinAndNumScenario(
                    fileToParse,
                    numContin,
                    numScenario
                );
                Map<Long, List<PostContingenciesRow>> groupByAssetUnit = t44V3ResultsReader.groupByAssetUnit(
                    mapPostContinDataForSheets,
                    sheetName
                );
                System.out.println("=========================== ");
                System.out.println(" Hourly value  For sheet: " + sheetName);
                System.out.println("  " + Arrays.deepToString(groupByAssetUnit.entrySet().stream().toArray()));
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (test5) {
            try {
                Integer numContin = 2;
                Integer numScenario = 5;
                String sheetName = "Active_power";
                Map<String, List<PostContingenciesRow>> mapPostContinDataForSheets = t44V3ResultsReader.parseFlexDataByNumContinAndNumScenario(
                    fileToParse,
                    numContin,
                    numScenario
                );
                Map<Long, List<PostContingenciesRow>> groupByAssetUnit = t44V3ResultsReader.groupByAssetUnit(
                    mapPostContinDataForSheets,
                    sheetName
                );
                List<FlexibleOptionWithContin> flexibleOptionList = t44V3ResultsReader.setFlexOptionList(groupByAssetUnit);
                System.out.println("=========================== ");
                System.out.println(" FlexOpt  For sheet: " + sheetName);
                System.out.println(" flexOpt: " + flexibleOptionList);
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (test7) {
            try {
                List<FlexibleOptionWithContin> flexOptList = new ArrayList<FlexibleOptionWithContin>();
                flexOptList = t44V3ResultsReader.parseFlexCost(fileToParse);
                System.out.println("=========================== ");
                System.out.println(" readCostValues: ");
                System.out.println("  " + flexOptList.stream().toArray());
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (test8) {
            try {
                Integer numContin = 2;
                Integer numScenario = 1;
                Map<String, List<FlexibleOptionWithContin>> mapFlexData = t44V3ResultsReader.getPostContinDataByNumContinAndNumScenario(
                    fileToParse,
                    numContin,
                    numScenario
                );
                System.out.println("=========================== ");
                System.out.println(" value ");
                System.out.println("  " + Arrays.deepToString(mapFlexData.entrySet().stream().toArray()));
                System.out.println("=========================== ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}