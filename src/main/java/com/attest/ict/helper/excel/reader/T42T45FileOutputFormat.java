package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.util.ExcelFileFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T42T45FileOutputFormat extends ExcelFileFormat {

    public static final String OUTPUT_FILES_EXTENSION = "_output.xlsx";

    public static final List<String> FLEX_SHEETS_NAME = Stream.of("P_DG_p", "P_L_p", "P_S_p").collect(Collectors.toList());
    public static final List<String> ALL_SHEETS_NAME = new ArrayList<>();

    public static final Map<String, String> MAP_FLEX_SHEETS = new HashMap<>();

    public static final Map<String, String> MAP_SHEET_NAME_CHART_SECTIONS = new HashMap<>();

    static {
        ALL_SHEETS_NAME.addAll(FLEX_SHEETS_NAME);
        ALL_SHEETS_NAME.add("R_activation");

        // --  sheet: P_DG_p
        MAP_FLEX_SHEETS.put("APC_MW", FLEX_SHEETS_NAME.get(0).concat(";PdnDG_p"));

        // -- sheet: P_L_p columns: PupL_p PdnL_p
        MAP_FLEX_SHEETS.put("FL_OD_MW", FLEX_SHEETS_NAME.get(1).concat(";PupL_p"));
        MAP_FLEX_SHEETS.put("FL_UD_MW", FLEX_SHEETS_NAME.get(1).concat(";PdnL_p"));

        // -- sheet: P_S_p
        // Active Power for storage
        MAP_FLEX_SHEETS.put("EES_P_CH_MW", FLEX_SHEETS_NAME.get(2).concat(";PupS_p"));
        MAP_FLEX_SHEETS.put("EES_P_DCH_MW", FLEX_SHEETS_NAME.get(2).concat(";PdnS_p"));

        // Reactive Power for storage
        MAP_FLEX_SHEETS.put("EES_Q_CH_MW", FLEX_SHEETS_NAME.get(2).concat(";QupS_p"));
        MAP_FLEX_SHEETS.put("EES_Q_DCH_MW", FLEX_SHEETS_NAME.get(2).concat(";QdnS_p"));

        // Set Section used for chart aggregation in component visualization results
        MAP_SHEET_NAME_CHART_SECTIONS.put(ALL_SHEETS_NAME.get(0), "APC");
        MAP_SHEET_NAME_CHART_SECTIONS.put(ALL_SHEETS_NAME.get(1), "FL_");
        MAP_SHEET_NAME_CHART_SECTIONS.put(ALL_SHEETS_NAME.get(2), "EES_");
        MAP_SHEET_NAME_CHART_SECTIONS.put(ALL_SHEETS_NAME.get(3), "ACTIVATION");
    }
}
