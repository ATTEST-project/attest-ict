package com.attest.ict.helper.excel.util;

import java.util.Arrays;
import java.util.List;

public class ExcelProfilesFormat {

    // Power type:
    // P for active power
    // R for reactive power
    public static final List<String> POWER_TYPE = Arrays.asList("P", "Q");

    public static final String ACTIVE_POWER = "P";

    public static final String REACTIVE_POWER = "Q";

    public static final String GEN_STATUS = "Status";

    public static final String VOLTAGE_MAGNITUDE = "Vg";

    public static final String TRANSF_STATUS = "Status";

    public static final String TRANSF_TAP_RATIO = "TapRatio";

    public static final String BRANCH_STATUS = "Status";

    // Files /LOADS/Network_season_typicalDay.xlsx
    public static final Integer[] LOAD_SHEET_NUM = { 0 };

    // first column = busNum , second columns contaians power type (P or Q)
    public static final int LOAD_NUM_COL_TO_SKIP = 2;

    // Files /Generator/Network_season_typicalDay_GEN.xlsx
    public static final Integer[] GEN_SHEET_NUM = { 0 };

    // first column = busNum , second columns contaians power type (P or Q)
    public static final int GEN_NUM_COL_TO_SKIP = 2;

    //Files: Flexibility/Network_season_typicalDay.xlsx
    public static final Integer[] FLEXIBILITY_SHEET_NUM = { 0, 1 };

    public static final String[] FLEXIBILITY_SHEET_NAMES = { "Flex_decrease", "Flex_increase" };

    // first column = busNum , second columns contaians power type (P or Q)
    public static final int FLEXIBILITY_NUM_COL_TO_SKIP = 2;

    public static final String[] TX_BRANCH_TRANSF_COMMON_HEADER = { "Fbus", "Tbus", "Un (kV)", "ID" };

    public static final String[] TX_BUS_GEN_COMMON_HEADER = { "Bus Number", "Un (kV)", "ID", "Type" };

    public static final Double TX_TIME_INTERVAL = 1.0; //1 hour

    //"Transf Data" and "Lines_Cables Data" are not always present
    public static final String[] PT_TX_SHEETS_NAME = {
        "Load P (MW)",
        "Load Q (Mvar)",
        "Gen Status",
        "Gen P (MW)",
        "Gen Q (Mvar)",
        "Gen Vg (pu)",
        "Transf Tap Ratio",
        "Transf Data",
        "Lines_Cables Data",
        "Auxiliary Data",
        "Downward flexibility",
        "Upward flexibility",
    };

    public static final String[] HR_TX_SHEETS_NAME = {
        "Load P (MW)",
        "Load Q (Mvar)",
        "Gen Status",
        "Gen P (MW)",
        "Gen Q (Mvar)", // add on 2023/03 e.g:
        "Gen Vg (p.u.)",
        "Transformer Tap Ratio",
        "Transformer Status",
        "Branch Status",
    };

    public static final String[] TX_ALL_SHEETS_NAME = {
        "Load P (MW)",
        "Load Q (Mvar)",
        "Gen Status",
        "Gen P (MW)",
        "Gen Q (Mvar)",
        "Gen Vg (pu)",
        "Gen Vg (p.u.)",
        "Transf Tap Ratio",
        "Transformer Tap Ratio",
        "Transformer Status",
        "Branch Status",
        "Transf Data",
        "Lines_Cables Data",
        "Auxiliary Data",
        "Downward flexibility",
        "Upward flexibility",
    };
}
