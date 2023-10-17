package com.attest.ict.tools.constants;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T33FileFormat extends ToolFileFormat {

    public static String OUTPUT_RESULTS_DIR = "Results";

    public static String OUTPUT_DIAGRAM_DIR = "Diagrams";

    public static final List<String> FILE_OUTPUT_SUFFIX = Stream
        .of("_planning_results.xls", "_planning_results.xlsx")
        .collect(Collectors.toList());

    public static final String DAY_COLUMN = "Day";

    public static final String NODE_COLUMN = "Node ID";

    public static final String NETWORK_NODE_ID_COLUMN = "Network Node ID";

    public static final String CONNECTION_NODE = "Connection Node ID";

    public static List<String> PAGE_TITLE = Stream
        .of(
            "Unitary Investment Cost", // 0
            "Investment Plan", //1 ,
            "Shared ESS - Secondary Reserve", //2
            "Converge Characteristic", //3
            "Shared ESS - Active Power and State of Charge", //4
            "Expected Interface Power Flow", //5
            "Expected Interface Voltage Magnitude", //6
            "Main Info", // 7
            "OF Values", // 8
            "Consumption", // 9
            "Generation", // 10
            "Branch Losses", // 11
            "Tramsformer Ratio", //12
            "Current", //13
            "Power Flows", //14,
            "Capacity Available", //15
            "Network Expected Voltage Magnitude", //16
            "Branch Loading", // 17 2023/08 this sheet replace the old 'Current Sheet' available form T33V3
            "Energy Storage" // 18 2023/08 new sheet  available form T33V3
        )
        .collect(Collectors.toList());

    public static final List<String> SHEETS_TO_SHOW = Stream
        .of(
            "Capacity Investment", // 0
            "ESS, Secondary Reserve", // 1
            "Convergence Characteristic", // 2
            "Shared ESS", // 3
            "Interface PF", // 4
            "Voltage", //5
            "Main Info", // 6
            "OF Values", // 7
            "Consumption", // 8
            "Generation", // 9
            "Branch Losses", // 10
            "Tramsformer Ratio", //11
            "Current", //12
            "Power Flows", //13,
            "Capacity Available", //14
            "Branch Loading", //15
            "Energy Storage" // 16 2023/08 new sheet  available form T33V3
        )
        .collect(Collectors.toList()); //5

    public static final List<String> headerToCorrect = new ArrayList<>(); // = Stream.of(  "1", "2",.... "3","23").collect(Collectors.toList());

    public static LinkedHashMap<String, String> MAP_PAGE_SHEETNAME = new LinkedHashMap<>();

    static {
        for (int i = 0; i < 24; i++) {
            headerToCorrect.add("" + i);
        }

        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(0), SHEETS_TO_SHOW.get(0)); // Unitary Investment Cost
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(1), SHEETS_TO_SHOW.get(0)); // Capacity Investment
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(2), SHEETS_TO_SHOW.get(1)); // ESS, Secondary Reserve
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(3), SHEETS_TO_SHOW.get(2)); // Convergence Characteristic
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(4), SHEETS_TO_SHOW.get(3)); // Shared ESS
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(5), SHEETS_TO_SHOW.get(4)); // Interface Power Flow
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(6), SHEETS_TO_SHOW.get(5)); // Voltage
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(7), SHEETS_TO_SHOW.get(6)); // Main Info
        //MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(8), SHEETS_TO_SHOW.get(7)); // OF Values
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(9), SHEETS_TO_SHOW.get(8)); // Consumption
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(10), SHEETS_TO_SHOW.get(9)); // Generation
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(11), SHEETS_TO_SHOW.get(10)); // Branch Losses
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(12), SHEETS_TO_SHOW.get(11)); // Tramsformer Ratio
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(13), SHEETS_TO_SHOW.get(12)); // Current
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(14), SHEETS_TO_SHOW.get(13)); // Power Flows
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(15), SHEETS_TO_SHOW.get(14)); // Capacity Available
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(16), SHEETS_TO_SHOW.get(5)); // Voltage
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(17), SHEETS_TO_SHOW.get(15)); // Branch Loading
        MAP_PAGE_SHEETNAME.put(PAGE_TITLE.get(18), SHEETS_TO_SHOW.get(16)); // Energy Storage
    }
}
