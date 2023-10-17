package com.attest.ict.tools.parameter.constants;

import java.util.Arrays;
import java.util.List;

public class ToolWp3Parameters {

    public static final String T31_PARAM_CASE_PATH = "case_path";

    public static final String T31_PARAM_OUTPUT_PATH = "output_path";

    public static final String T31_PARAM_ATTEST_INPUTS_PATH = "attest_inputs_path";

    public static final String T31_PARAM_ADD_LOAD_DATA_CASE_NAME = "add_load_data_case_name";

    public static final String T31_PARAM_EV_DATA_FILE_PATH = "EV_data_file_path";

    public static final List<String> T31_INPUT_FILES_DESCR_PARAM = Arrays.asList(T31_PARAM_EV_DATA_FILE_PATH);
    public static final String T31_PARAM_FILE_DESC_EV_PV_STR = "EV_PV_STR";

    /*        "parameters": {
        "xlsx_file_name": "",
            "ods_file_name": "Transmission_Network_UK_v3_PROF_update",
            "peak_hour": 19,
            "no_year": 2,
            "model": "investment",
            "run_both": false,
            "test_case": "Transmission_Network_UK_v3",
            "country": "UK",
            "input_dir": "\\ATSIM\\t32v2\\inputs",
            "output_dir": "\\ATSIM\\t32v2\\outputs",
            "use_load_data_update": 1,
            "add_load_data_case_name": "UK_Tx_",
            "EV_data_file_name": "EV-PV-Storage_Data_for_Simulations.xlsx"
  */

    public static final String T32_PARAM_XLS_FILE_NAME = "xlsx_file_name"; // if no file was uploaded, the parameter must be set to [""]
    public static final String T32_PARAM_ODS_FILE_NAME = "ods_file_name";

    public static final String T32_PARAM_EV_DATA_FILE_NAME = "EV_data_file_name"; // "EV-PV-Storage_Data_for_Simulations.xlsx"

    public static final String T32_SCENARIO_FILE_NAME = "scenario_gen.ods"; // "EV-PV-Storage_Data_for_Simulations.xlsx"

    public static final String T32_PARAM_INPUT_DIR = "input_dir";
    public static final String T32_PARAM_OUTPUT_DIR = "output_dir";
    public static final String T32_PARAM_TEST_CASE = "test_case";

    public static final String T32_PARAM_ADD_LOAD_DATA_CASE_NAME = "add_load_data_case_name"; // "UK_Tx_",
    public static final String T32_PARAM_USE_LOAD_DATA_UPDATE = "use_load_data_update"; // 1 or 0
}
