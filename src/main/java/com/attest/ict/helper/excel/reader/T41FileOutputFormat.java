package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.util.ExcelFileFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T41FileOutputFormat extends ExcelFileFormat {

    public static final List<String> FLEX_SHEETS_NAME = Stream
        .of("APC_MW", "EES_CH_MW", "EES_DCH_MW", "FL_OD_MW", "FL_UD_MW", "COST")
        .collect(Collectors.toList());
    public static final List<String> HEADERS_WITH_FILTER = Stream
        .of("Scen", "ID", "Itr", "T", "Num", "Bus", "F_Bus", "T_Bus")
        .collect(Collectors.toList());
    public static final List<String> HEADERS_VIOLATION_DATA = Stream
        .of("V", "Viol(V)", "S", "S_limit", "Viol(S)")
        .collect(Collectors.toList());

    public static final String SUFFIX_OUTPUT_FILES_EXTENSION = "_output";
    public static final String OUTPUT_FILES_EXTENSION = "_output.xlsx";
    public static final String OUTPUT_LOG_FILE = "OutLog.xlsx";

    public static List<String> OUTPUT_FILES_EXTENSION_LIST = Stream
        .of(OUTPUT_FILES_EXTENSION, OUTPUT_LOG_FILE)
        .collect(Collectors.toList());

    public static final String LOG_FILE_SHEET = "Log";
    public static final boolean IS_HEADER_EXISTS_IN_LOG_FILE = true;
    public static final List<String> SHEETS_NAME = Stream
        .of("VOLT", "Crnt_PU", "Crnt_SI", "P_load", "Q_load", "Pg_max", "Qg_max", "Vlt_Viol", "Crnt_Viol")
        .collect(Collectors.toList());
    public static Map<String, String> SHEETNAME_TITLE_MAP = new HashMap<String, String>();

    public static Map<String, String> SHEETNAME_SHORT_TITLE_MAP = new HashMap<String, String>();

    static {
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(0), "Voltage Magnitude for on each bus for each weather scenario. "); // VOLT
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(1), "Current flow on each line, in PU."); //Crnts_PU
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(2), "Current flow on each line, in SI units (Ampere). "); //Crnt_SI
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(3), "Active/Real power component of load. "); //P_load
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(4), "Reactive/Imaginary component of load. "); //Q_load
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(5), "Active/Real power component of RES generation. "); //"Pg_max",
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(6), "Reactive/Imaginary component of RES generation "); // "Qg_max",
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(7), "List of violations of the voltage limit."); //"Vlt_Viol",
        SHEETNAME_TITLE_MAP.put(SHEETS_NAME.get(8), "Violations of the lines thermal limits (i.e. high current flow) "); // "Crnt_Viol"

        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(0), "Voltage Magnitude [VOLT]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(1), "Current [PU]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(2), "Current in SI units [Ampere]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(3), "Active Power [MW]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(4), "Reactive Power [MVar]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(5), "Active Power [MW]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(6), "Reactive Power [MVar]");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(7), "Voltage violation");
        SHEETNAME_SHORT_TITLE_MAP.put(SHEETS_NAME.get(8), "High current flow");
    }
}
