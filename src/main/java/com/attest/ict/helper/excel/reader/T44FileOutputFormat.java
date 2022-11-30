package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.util.ExcelFileFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hibernate.mapping.Collection;

public class T44FileOutputFormat extends ExcelFileFormat {

    public static final String P_CONTIN_SUB_STR = "PContin_";

    public static final String P_CONTIN_SCEN_STR = "Scen_";

    public static final String COSTS_SUB_STR = "_Costs";

    public static final List<String> OUTPUT_FILES_EXTENSION = Stream
        .of("Costs", "OPF", "Normal", "ActiveP", "ReactiveP", "FL_dec", "FL_inc", "LC", "RES_C", "STR")
        .collect(Collectors.toList());

    // file: networkName_Costs.xlsx
    public static final List<String> SHEETS_NAME_COST = Stream.of("Sheet1").collect(Collectors.toList());

    // file: networkName_Normal.xlsx and _OPF.xlsx
    public static final List<String> SHEETS_NAME = Stream
        .of("Active_power", "Reactive_Power", "FL_inc", "FL_dec", "STR", "Load_curtailment", "RES_curtailment", "COSTS")
        .collect(Collectors.toList());

    // file: networkName _ActiveP.xlsx
    // first sheet contains Number of Contingencies and Number of Scenarios
    // others sheets Active_power_Contin_Scen_1  and so on contains Gen_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_ACTIVE_P = Stream.of("Active_power", "Active_power_Contin").collect(Collectors.toList());

    //file: networkName _ReactiveP.xlsx
    public static final List<String> SHEETS_NAME_REACTIVE_P = Stream
        .of("Reactive_power", "Reactive_power_Contin")
        .collect(Collectors.toList());

    //FL_dec.xlsx"
    // First sheet contains Number of flexible loads
    // others sheet FL_dec_Contin_1_Scen_1  and so on contains FL_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_FL_DEC = Stream.of("Flexible_Load", "FL_dec_Contin").collect(Collectors.toList());

    //"FL_inc.xlsx",
    // First sheet contains Number of flexible loads
    // others sheet FL_inc_Contin_1_Scen_1  and so on contains FL_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_FL_INC = Stream.of("Flexible_Load", "FL_inc_Contin").collect(Collectors.toList());

    //"LC.xlsx",
    // First sheet contains Number of Contingencies  Number of Scenario
    // others sheets: LC_Contin_1_Scen_1  and so on contains LC_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_LC = Stream.of("LC", "LC_Contin").collect(Collectors.toList());

    //RES_C.xlsx
    // First sheet contains Number of Contingencies  Number of Scenario
    // others sheets: RES_C_Contin_1_Scen_1  and so on contains LC_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_RES_C = Stream.of("RES", "RES_C_Contin").collect(Collectors.toList());

    //"STR.xlsx"
    // First sheet 'Storage' contains Number   Number of storage
    // others sheets: STR_Contin_1_Scen_1  and so on contains LC_nodes, t1,..tn for each scenario and contingencies
    public static final List<String> SHEETS_NAME_STR = Stream.of("Storage", "STR_Contin").collect(Collectors.toList());

    //key: fileExtension name and list of headers
    public static final Map<String, List<String>> mapFileNameSheets = new HashMap<>();

    static {
        //_Costs.xlsx
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(0), SHEETS_NAME_COST);

        //"OPF.xlsx",
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(1), SHEETS_NAME);

        //"Normal.xlsx",
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(2), SHEETS_NAME);

        //"ActiveP.xlsx"
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(3), SHEETS_NAME_ACTIVE_P);

        //"ReactiveP.xlsx"
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(4), SHEETS_NAME_REACTIVE_P);

        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(5), SHEETS_NAME_FL_DEC);

        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(6), SHEETS_NAME_FL_INC);

        //"LC.xlsx",
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(7), SHEETS_NAME_LC);

        //"RES_C.xlsx",
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(8), SHEETS_NAME_RES_C);

        //"STR.xlsx"
        mapFileNameSheets.put(OUTPUT_FILES_EXTENSION.get(9), SHEETS_NAME_STR);
    }
}
