package com.attest.ict.helper.excel.reader;

import static java.util.stream.Collectors.toList;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.service.dto.custom.*;
import com.attest.ict.tools.constants.T33FileFormat;
import java.io.File;
import java.util.*;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33ResultsReader {

    public final Logger log = LoggerFactory.getLogger(T33ResultsReader.class);

    public T33ResultsPagesDTO getPagesToShow(File relativePath) {
        ExcelReader excelReader = new ExcelReader(relativePath);
        List<String> sheetsName = excelReader.getSheetsName();
        List<T33PageDTO> pages = new ArrayList<>();
        List<String> filteredSheetName = this.filterSheets(sheetsName, T33FileFormat.SHEETS_TO_SHOW);
        for (String name : filteredSheetName) {
            mapPagesSheetNameAndFilterOptions(name, pages);
        }
        T33ResultsPagesDTO resultsPages = new T33ResultsPagesDTO();
        resultsPages.setPages(pages);
        excelReader.closeWorkBook();
        return resultsPages;
    }

    /**
     * @param sheetName list of sheetName present in the xls output file
     * @param filters   list of sheet containing interesting data to show in ATTEST platform GUI
     * @return only sheetName that are contained in filters
     */
    private List<String> filterSheets(List<String> sheetName, List<String> filters) {
        return sheetName.stream().filter(s -> filters.contains(s)).collect(toList());
    }

    private void mapPagesSheetNameAndFilterOptions(String sheetName, List<T33PageDTO> pages) {
        T33PageDTO page = new T33PageDTO();
        boolean filterDay = false;
        boolean filterNode = false;
        page.setFilterDay(filterDay);
        page.setFilterNode(filterNode);
        switch (sheetName) {
            case "Capacity Investment":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(0)); // Unitary Investment Cost;
                page.setSheetName(sheetName);
                pages.add(page);

                T33PageDTO pageInvPlan = new T33PageDTO();
                pageInvPlan.setTitle(T33FileFormat.PAGE_TITLE.get(1)); //"Investment Plan"
                pageInvPlan.setSheetName(sheetName);
                pageInvPlan.setFilterDay(filterDay);
                pageInvPlan.setFilterNode(filterNode);
                pages.add(pageInvPlan);
                break;
            case "ESS, Secondary Reserve":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(2)); //"Shared ESS - Secondary Reserve");
                page.setSheetName(sheetName);
                page.setFilterDay(true);
                pages.add(page);
                break;
            case "Convergence Characteristic":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(3)); //"Converge Characteristic");
                page.setSheetName(sheetName);
                pages.add(page);
                break;
            case "Shared ESS":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(4)); //"Shared ESS - Active Power and State of Charge");
                page.setSheetName(sheetName);
                page.setFilterDay(true);
                pages.add(page);
                break;
            case "Interface PF":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(5)); //"Expected Interface Power Flow";
                page.setSheetName(sheetName);
                pages.add(page);
                page.setFilterDay(true);
                page.setFilterNode(true);
                break;
            case "Voltage":
                page.setTitle(T33FileFormat.PAGE_TITLE.get(6)); //"Expected Interface Voltage Magnitude");
                page.setSheetName(sheetName);
                page.setFilterDay(true);
                pages.add(page);

                T33PageDTO pageNetVoltagePlan = new T33PageDTO();
                pageNetVoltagePlan.setTitle(T33FileFormat.PAGE_TITLE.get(16)); //Network Voltage Magnitude
                pageNetVoltagePlan.setSheetName(sheetName);
                pageNetVoltagePlan.setFilterDay(true);
                pageNetVoltagePlan.setFilterNode(true);
                pages.add(pageNetVoltagePlan);
                break;
            default:
                page.setTitle(sheetName);
                page.setSheetName(sheetName);
                pages.add(page);
                break;
        }
    }

    public TableDataDTO getTableData(File outputFile, String pageTitle) {
        TableDataDTO table = new TableDataDTO();
        // log.debug("Selected Page Title: " + pageTitle);
        ExcelReader reader = new ExcelReader(outputFile);
        try {
            String sheetName = T33FileFormat.MAP_PAGE_SHEETNAME.get(pageTitle);
            log.debug("Start Parsing sheet: " + sheetName);

            Sheet sheet = reader.getSheetByName(sheetName);
            if (sheet == null) {
                // log.debug("Sheet referring to pageTitle: " + pageTitle + " not found! ");
                throw new ExcelReaderFileException("Sheet " + pageTitle + " not found");
            }

            /*
            Sheet MainInfo compliant with t33 old version
            boolean headerExists = !sheetName.equals(T33FileFormat.SHEETS_TO_SHOW.get(6));
            if (sheetName.equals(T33FileFormat.SHEETS_TO_SHOW.get(6))) {
                List<ColumnDefDTO> columnsDefs = Stream
                    .of(new ColumnDefDTO("Info", false, "Info"), new ColumnDefDTO("Description", false, "Description"))
                    .collect(toList());
                table.setColumnDefs(columnsDefs);
            }
            */

            boolean headerExists = true;
            int indexHeader = 0;
            // Manage sheet Main Info included in new T33V3 release
            if (sheetName.equals(T33FileFormat.SHEETS_TO_SHOW.get(6))) {
                String[] years = reader.parseHeaderBySheetName(sheetName, 0);
                log.debug("Main Info Sheet years: " + " " + Arrays.stream(years).collect(toList()));
                String[] headers = reader.parseHeaderBySheetName(sheetName, 1);
                log.debug("Main Info  Sheet header: " + " " + Arrays.stream(headers).collect(toList()));
                List<ColumnDefDTO> columnsDefs = new ArrayList<>();
                int indexYear = 3;
                for (int i = 0; i < headers.length; i++) {
                    String field = (i < indexYear) ? headers[i] : years[i - indexYear] + " " + headers[i];
                    log.debug("MainInfo columnDef field: " + " " + field);
                    ColumnDefDTO colDef = new ColumnDefDTO(field, true, field);
                    columnsDefs.add(colDef);
                }
                table.setColumnDefs(columnsDefs);
                headerExists = false;
                indexHeader = 2;
            }
            if (headerExists) {
                String[] headers = reader.parseHeaderBySheetName(sheetName, indexHeader);
                log.debug("Sheet header: " + " " + Arrays.stream(headers).collect(toList()));
                // The first version of tool: T33  doesn't contain the Operator column in the sheet: 'Shared ESS'. To allow correct visualization, we add it with a default value = 'ESSO'
                List<ColumnDefDTO> columnsDefs = (sheetName.equals(T33FileFormat.SHEETS_TO_SHOW.get(3)))
                    ? setColumnDefsForSharedESS(headers)
                    : setColumnDefs(headers);
                table.setColumnDefs(columnsDefs);
            }

            Map<Integer, List<String>> data = reader.parseRowData(sheet, headerExists, indexHeader);
            if (data.isEmpty()) {
                log.error("No data found in Sheet " + pageTitle);
                throw new ExcelReaderFileException("No data found in Sheet " + pageTitle);
            }
            List<String> filters = new ArrayList<>();
            List<String> filtersMarket = Stream.of("Expected").collect(toList());
            T33CapacityResults t33CapacityResults = new T33CapacityResults();
            table.setTitle(pageTitle);
            switch (pageTitle) {
                case "Unitary Investment Cost":
                    filters = Stream.of("Cost S, [m.u.]", "Cost E, [m.u.]").collect(toList());
                    List<T33CapacityDTO> costData = t33CapacityResults.setCapacityRowData(data, filters, sheetName);
                    table.setRowData(costData);
                    break;
                case "Investment Plan":
                    filters = Stream.of("S, [MVA]", "E, [MVAh]").collect(toList());
                    List<T33CapacityDTO> investmentData = t33CapacityResults.setCapacityRowData(data, filters, sheetName);
                    table.setRowData(investmentData);
                    break;
                case "Converge Characteristic":
                    T33Convergence t33CapacityConvergence = new T33Convergence();
                    List<T33ConvergenceDTO> convergenceData = t33CapacityConvergence.setConvergenceRowData(data, sheetName);
                    table.setRowData(convergenceData);
                    break;
                case "Capacity Available":
                    // -- filters is empty list
                    filters = Collections.emptyList();
                    List<T33CapacityDTO> capacityData = t33CapacityResults.setCapacityRowData(data, filters, sheetName);
                    table.setRowData(capacityData);
                    break;
                case "Shared ESS - Secondary Reserve":
                    T33SecondaryReserveResults t33SecondaryReserveResults = new T33SecondaryReserveResults();
                    List<T33EssSecondaryReserveDTO> essSecondaryReserveData = t33SecondaryReserveResults.setEssSecondaryReserveRowData(
                        data,
                        sheetName
                    );
                    table.setRowData(essSecondaryReserveData);
                    break;
                case "Shared ESS - Active Power and State of Charge":
                    T33QuantityForOperatorResults t33SharedEssResults = new T33QuantityForOperatorResults();
                    List<T33QuantityForNodeAndOperatorDTO> sharedEssData = t33SharedEssResults.setRowData(data, sheetName, filtersMarket);
                    table.setRowData(sharedEssData);
                    table.setConnectionNodesId(sharedEssData.stream().map(row -> row.getNodeId()).distinct().sorted().collect(toList()));
                    break;
                case "Expected Interface Power Flow":
                    table.setTitle("Transmission - Distribution Interface: " + pageTitle);
                    T33QuantityForOperatorResults t33InterfacePowerFlow = new T33QuantityForOperatorResults();
                    List<T33QuantityForNodeAndOperatorDTO> interfacePfData = t33InterfacePowerFlow.setRowData(
                        data,
                        sheetName,
                        filtersMarket
                    );
                    table.setRowData(interfacePfData);
                    table.setConnectionNodesId(interfacePfData.stream().map(row -> row.getNodeId()).distinct().sorted().collect(toList()));
                    break;
                case "Expected Interface Voltage Magnitude":
                    table.setTitle("Transmission - Distribution Interface: " + pageTitle);
                    T33ConnectionNodeQuantityResults t33ConnectionNodeQuantityResults = new T33ConnectionNodeQuantityResults();
                    List<T33ConnectionNodeQuantityDTO> voltageMagnitudeData = t33ConnectionNodeQuantityResults.setRowData(
                        data,
                        sheetName,
                        filtersMarket
                    );
                    table.setRowData(voltageMagnitudeData);
                    table.setConnectionNodesId(
                        voltageMagnitudeData
                            .stream()
                            .filter(row -> !row.getConnectionNodeId().contains("-"))
                            .map(row -> new Integer(row.getConnectionNodeId()))
                            .distinct()
                            .sorted()
                            .collect(toList())
                    );
                    break;
                case "Network Expected Voltage Magnitude":
                    table.setTitle(pageTitle);
                    T33ConnectionNodeQuantityResults t33NetVoltageResults = new T33ConnectionNodeQuantityResults();
                    List<T33ConnectionNodeQuantityDTO> t33NetVoltageData = t33NetVoltageResults.setRowData(data, sheetName, filtersMarket);
                    table.setRowData(t33NetVoltageData);
                    table.setConnectionNodesId(
                        t33NetVoltageData
                            .stream()
                            .filter(row -> !row.getConnectionNodeId().contains("-"))
                            .map(row -> Integer.valueOf(row.getConnectionNodeId()))
                            .distinct()
                            .sorted()
                            .collect(toList())
                    );
                    break;
                case "Consumption":
                case "Energy Storage": //2023/08 new T33V3
                    table.setTitle("Network Results: " + pageTitle);
                    T33ConnectionNodeQuantityResults t33ConsumptionResults = new T33ConnectionNodeQuantityResults();
                    List<T33ConnectionNodeQuantityDTO> consumptionData = t33ConsumptionResults.setRowData(data, sheetName, filtersMarket);
                    table.setRowData(consumptionData);
                    table.setConnectionNodesId(
                        consumptionData
                            .stream()
                            .filter(row -> !row.getConnectionNodeId().contains("-"))
                            .map(row -> Integer.valueOf(row.getConnectionNodeId()))
                            .distinct()
                            .sorted()
                            .collect(toList())
                    );
                    break;
                case "Generation":
                    table.setTitle("Network Results: " + pageTitle);
                    T33GenerationResults t33GenerationResults = new T33GenerationResults();
                    List<T33GeneratorDTO> generationData = t33GenerationResults.setGenerationPowerFlow(data, sheetName, filtersMarket);
                    table.setRowData(generationData);
                    table.setConnectionNodesId(
                        generationData
                            .stream()
                            .filter(row -> !row.getConnectionNodeId().contains("-"))
                            .map(row -> Integer.valueOf(row.getConnectionNodeId()))
                            .distinct()
                            .sorted()
                            .collect(toList())
                    );
                    break;
                case "Branch Losses":
                case "Power Flows":
                case "Transformer Ratio":
                case "Current":
                case "Branch Loading": //2023/08 new T33V3
                    table.setTitle("Network Results: " + pageTitle);
                    T33QuantityFromNodeToNode t33QuantityFromNodeToNode = new T33QuantityFromNodeToNode();
                    List<T33QuantityFromNodeToNodeDTO> powerFlowData = t33QuantityFromNodeToNode.setQuantityFromNodeToNode(
                        data,
                        sheetName,
                        filtersMarket
                    );
                    table.setRowData(powerFlowData);
                    table.setConnectionNodesId(
                        powerFlowData
                            .stream()
                            .filter(row -> !row.getConnectionNodeId().contains("-"))
                            .map(row -> Integer.valueOf(row.getConnectionNodeId()))
                            .distinct()
                            .sorted()
                            .collect(toList())
                    );
                    break;
                case "Main Info":
                    T33MainInfoResults t33MainInfoResults = new T33MainInfoResults();
                    List<T33MainInfoDTO> mainInfoData = t33MainInfoResults.setMainInfoData(data, sheetName);
                    table.setRowData(mainInfoData);
                    break;
                case "OF Values":
                    T33OfValueResults t33OfValueResults = new T33OfValueResults();
                    List<T33OfValueDTO> ofValueData = t33OfValueResults.setOfValueRowData(data, sheetName);
                    table.setRowData(ofValueData);
                    break;
            }
        } catch (Exception ex) {
            log.error(" Error parsing T33 Results ", ex.getMessage());
        } finally {
            reader.closeWorkBook();
        }
        return table;
    }

    private List<ColumnDefDTO> setColumnDefs(String[] headers) {
        List<ColumnDefDTO> columnsDefs = new ArrayList<>();
        for (String header : headers) {
            ColumnDefDTO colDef = new ColumnDefDTO(checkHeader(header), true, removePart(header));
            columnsDefs.add(colDef);
        }
        return columnsDefs;
    }

    private List<ColumnDefDTO> setColumnDefsForSharedESS(String[] headers) {
        List<ColumnDefDTO> columnsDefs = new ArrayList<>();
        for (int i = 0; i < headers.length; i++) {
            if (headers.length < 31 && i == 1) {
                ColumnDefDTO colDef = new ColumnDefDTO("Operator", true, "Operator");
                columnsDefs.add(colDef);
            }
            ColumnDefDTO colDef = new ColumnDefDTO(checkHeader(headers[i]), true, removePart(headers[i]));
            columnsDefs.add(colDef);
        }
        return columnsDefs;
    }

    private String checkHeader(String header) {
        String newHeader = header;
        if (header.equals("Network Node ID")) {
            newHeader = T33FileFormat.NODE_COLUMN;
        }
        String newHeader1 = removePart(newHeader);
        String newHeader2 = setTimeSeriesColumnHeader(newHeader1);
        return removeMisUnitFormHeader(newHeader2);
    }

    // remove ".0" from column's header =  2020.0 2030.0 2040.0 2050.0
    // return 2020, 2030, 2040, 2050
    private String removePart(String header) {
        int pos = header.indexOf(".0");
        if (pos > 0) {
            return header.substring(0, pos);
        } else {
            return header;
        }
    }

    private String removeMisUnitFormHeader(String header) {
        int pos = header.indexOf(',');
        if (pos > 0) {
            return header.substring(0, pos);
        } else {
            return header;
        }
    }

    private String setTimeSeriesColumnHeader(String header) {
        if (header.equals("Year") || header.equals("Day")) {
            return "timeSeriesHour.".concat(header);
        }
        if (T33FileFormat.headerToCorrect.contains(header)) {
            return "timeSeriesHour.t".concat(header);
        }
        return header;
    }
}
