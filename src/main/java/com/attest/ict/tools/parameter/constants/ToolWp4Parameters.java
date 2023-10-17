package com.attest.ict.tools.parameter.constants;

import java.util.HashMap;
import java.util.Map;

public class ToolWp4Parameters {

    public static final String PARAM_CASE_NAME = "case_name";
    public static final String PARAM_SEASON = "season";
    public static final String PARAM_YEAR = "year";
    public static final String PARAM_FLEXIBILITY = "flexibility";
    public static final String PARAM_FLAG_WITH_FLEX = "with_flex";

    public static final Map<Object, String> MAP_PARAM_SEASON = new HashMap<>();

    public static final Map<String, Object> T41_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> T41_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> T44_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> T44_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> TSG_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> TSG_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> T42_MAP_PARAM_TYPE = new HashMap<>();
    public static final Map<String, Object> T42_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> T45_MAP_PARAM_TYPE = new HashMap<>();
    public static final Map<String, Object> T45_MAP_PARAM_DEF_VALUE = new HashMap<>();

    static {
        MAP_PARAM_SEASON.put(1, "Summer");
        MAP_PARAM_SEASON.put(2, "Winter");
        MAP_PARAM_SEASON.put("Su", "Summer");
        MAP_PARAM_SEASON.put("S", "Spring");
        MAP_PARAM_SEASON.put("W", "Winter");
        MAP_PARAM_SEASON.put("A", "Autumn");

        //----------------------------
        //--- Parameters for T41
        T41_MAP_PARAM_TYPE.put("network_file", String.class);
        T41_MAP_PARAM_TYPE.put("auxiliary_file", String.class);
        T41_MAP_PARAM_TYPE.put("scenario_file", String.class);
        T41_MAP_PARAM_TYPE.put("output_file", String.class);
        T41_MAP_PARAM_TYPE.put("ntp", Integer.class);
        T41_MAP_PARAM_TYPE.put("scenario", Integer.class);
        T41_MAP_PARAM_TYPE.put("flex_apc", Integer.class);
        T41_MAP_PARAM_TYPE.put("flex_oltc", Integer.class);
        T41_MAP_PARAM_TYPE.put("oltc_bin", Integer.class);
        T41_MAP_PARAM_TYPE.put("flex_adpf", Integer.class);
        T41_MAP_PARAM_TYPE.put("flex_fl", Integer.class);
        T41_MAP_PARAM_TYPE.put("fl_bin", Integer.class);
        T41_MAP_PARAM_TYPE.put("flex_str", Integer.class);
        T41_MAP_PARAM_TYPE.put("str_bin", Integer.class);
        T41_MAP_PARAM_TYPE.put(PARAM_FLAG_WITH_FLEX, Integer.class);
        //---  Since T41V2 release new parameters were included
        T41_MAP_PARAM_TYPE.put("outlog_file", String.class);
        T41_MAP_PARAM_TYPE.put(PARAM_CASE_NAME, String.class);
        // Not used by the tool but useful to the ict platform
        T41_MAP_PARAM_TYPE.put(PARAM_YEAR, Integer.class);
        T41_MAP_PARAM_TYPE.put(PARAM_SEASON, String.class);
        //--- Default values
        T41_MAP_PARAM_DEF_VALUE.put("ntp", 24);
        T41_MAP_PARAM_DEF_VALUE.put("scenario", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_apc", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_oltc", 1);
        T41_MAP_PARAM_DEF_VALUE.put("oltc_bin", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_adpf", 1);
        //--- Since T41V2 release new parameters have been included
        T41_MAP_PARAM_DEF_VALUE.put("outlog_file", "OutLog.xlsx");

        //-----------------------------------------
        // Parameters for T44
        T44_MAP_PARAM_TYPE.put("network_file", String.class);
        T44_MAP_PARAM_TYPE.put("auxiliary_file", String.class);
        T44_MAP_PARAM_TYPE.put("scenario_file", String.class);
        T44_MAP_PARAM_TYPE.put("problem", Integer.class);
        T44_MAP_PARAM_TYPE.put("output_dir", String.class);
        T44_MAP_PARAM_TYPE.put(PARAM_CASE_NAME, String.class); // "case_name": "PT",
        T44_MAP_PARAM_TYPE.put("profile", Integer.class); // "profile": 1, (Summer) , profile=2 Winter
        T44_MAP_PARAM_TYPE.put(PARAM_YEAR, Integer.class); // "year": 2030,
        T44_MAP_PARAM_TYPE.put(PARAM_FLEXIBILITY, Integer.class); //  "flexibility": 1
        // T44 set default value
        T44_MAP_PARAM_DEF_VALUE.put("problem", 4);

        //-----------------------------------------
        //--- Parameters for TSG (wind pw scenario)
        TSG_MAP_PARAM_TYPE.put("nsc", Integer.class);
        // Default values
        TSG_MAP_PARAM_DEF_VALUE.put("nsc", 10);

        //-----------------------------------------
        //--- Parameters for T42
        T42_MAP_PARAM_TYPE.put("matpower_network_file", String.class);
        T42_MAP_PARAM_TYPE.put("network_file", String.class);
        T42_MAP_PARAM_TYPE.put("out_file", String.class);
        T42_MAP_PARAM_TYPE.put("PV_production_profile_file", String.class);
        T42_MAP_PARAM_TYPE.put("state_estimation_csv_file", String.class);
        T42_MAP_PARAM_TYPE.put("current_time_period", String.class);
        T42_MAP_PARAM_TYPE.put("flex_devices_tech_char_file", String.class);
        T42_MAP_PARAM_TYPE.put("flexibity_devices_states_file", String.class);
        T42_MAP_PARAM_TYPE.put("trans_activation_file", String.class);
        //--- Not used by the tool but useful to the ict platform
        T42_MAP_PARAM_TYPE.put(PARAM_FLAG_WITH_FLEX, Integer.class);
        T42_MAP_PARAM_TYPE.put(PARAM_CASE_NAME, String.class);
        T42_MAP_PARAM_TYPE.put(PARAM_YEAR, Integer.class);
        T42_MAP_PARAM_TYPE.put(PARAM_SEASON, String.class);
        //--- set T42 default value
        T42_MAP_PARAM_DEF_VALUE.put("network_file", "converted_network_data.xlsx");
        T42_MAP_PARAM_DEF_VALUE.put("current_time_period", "t1");
        T42_MAP_PARAM_DEF_VALUE.put("trans_activation_file", "trans_decisions.xlsx");

        //-----------------------------------------
        //--- Parameters for T45
        T45_MAP_PARAM_TYPE.put("matpower_network_file", String.class);
        T45_MAP_PARAM_TYPE.put("network_file", String.class);
        T45_MAP_PARAM_TYPE.put("out_file", String.class);
        T45_MAP_PARAM_TYPE.put("PV_production_profile_file", String.class);
        T45_MAP_PARAM_TYPE.put("state_estimation_csv_file", String.class);
        T45_MAP_PARAM_TYPE.put("current_time_period", String.class);
        T45_MAP_PARAM_TYPE.put("output_distribution_bus", String.class);
        T45_MAP_PARAM_TYPE.put("flex_devices_tech_char_file", String.class);
        T45_MAP_PARAM_TYPE.put("flexibity_devices_states_file", String.class);
        T45_MAP_PARAM_TYPE.put("DA_curtailment_file", String.class);
        //--- Not used by the tool but useful to the ict platform
        T45_MAP_PARAM_TYPE.put(PARAM_FLAG_WITH_FLEX, Integer.class);
        T45_MAP_PARAM_TYPE.put(PARAM_CASE_NAME, String.class);
        T45_MAP_PARAM_TYPE.put(PARAM_YEAR, Integer.class);
        T45_MAP_PARAM_TYPE.put(PARAM_SEASON, String.class);
        //--- set T45 default value
        T45_MAP_PARAM_DEF_VALUE.put("current_time_period", "t1");
        T45_MAP_PARAM_DEF_VALUE.put("output_distribution_bus", "n1");
        T45_MAP_PARAM_TYPE.put("network_file", "converted_network_data.xlsx");
    }
}
