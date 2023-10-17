package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.util.ExcelFileFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T44V3FileOutputFormat extends ExcelFileFormat {

    public static List<String> OUTPUT_FILE_EXTENSION = Stream.of(".xlsx", ".csv").collect(Collectors.toList());
    public static final String WITH_FLEXIBILITY = "wf";
    public static final String WITHOUT_FLEXIBILITY = "wof";
    public static final String TABLE_KEY_NUMBER_OF_CONTIN = "number_of_contingencies";
    public static final String TABLE_KEY_NSC = "number_of_scenarios";
    public static final String TABLE_KEY_NORMAL = "normal";

    public static final String PAGE_NAME_NORMAL = "Normal";
    public static final String PAGE_NAME_POST_CONTING = "Post Contingencies";

    public static final List<String> OUTPUT_FILES_SUFFIX = Stream.of("Normal", "post_contin").collect(Collectors.toList());

    // file: <networkName>_<flex>_Normal.xlsx
    public static final List<String> NORMAL_SHEETS_NAME = Stream
        .of("Active_power", "Reactive_Power", "FL_inc", "FL_dec", "STR", "Load_curtailment", "RES_curtailment", "COSTS")
        .collect(Collectors.toList());

    public static final List<String> POST_CONTIN_SHEETS_NAME = Stream
        .of("Contin_map", "Active_power", "Reactive_power", "FL_inc", "FL_dec", "STR", "Load_curtail", "RES_curtail", "Costs")
        .collect(Collectors.toList());

    public static final Map<String, String> MAP_POST_CONTIN_NORMAL_SHEETS = new HashMap<>();

    static {
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(0), ""); // --"Contin_map"
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(1), NORMAL_SHEETS_NAME.get(0)); // Active_power
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(2), NORMAL_SHEETS_NAME.get(1)); // Reactive_power
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(3), NORMAL_SHEETS_NAME.get(2)); // FL_inc
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(4), NORMAL_SHEETS_NAME.get(3)); // FL_dec
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(5), NORMAL_SHEETS_NAME.get(4)); // STR
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(6), NORMAL_SHEETS_NAME.get(5)); // Load_curtail
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(7), NORMAL_SHEETS_NAME.get(6)); // RES_curtail
        MAP_POST_CONTIN_NORMAL_SHEETS.put(POST_CONTIN_SHEETS_NAME.get(8), NORMAL_SHEETS_NAME.get(7)); // Costs
    }
}
