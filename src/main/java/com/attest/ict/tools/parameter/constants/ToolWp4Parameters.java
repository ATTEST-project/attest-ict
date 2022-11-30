package com.attest.ict.tools.parameter.constants;

import java.util.HashMap;
import java.util.Map;

public class ToolWp4Parameters {

    public static final Map<String, Object> T41_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> T41_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> T44_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> T44_MAP_PARAM_DEF_VALUE = new HashMap<>();

    public static final Map<String, Object> TSG_MAP_PARAM_TYPE = new HashMap<>();

    public static final Map<String, Object> TSG_MAP_PARAM_DEF_VALUE = new HashMap<>();

    static {
        // Parameters for T41
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

        // Default values
        T41_MAP_PARAM_DEF_VALUE.put("ntp", 24);
        T41_MAP_PARAM_DEF_VALUE.put("scenario", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_apc", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_oltc", 1);
        T41_MAP_PARAM_DEF_VALUE.put("oltc_bin", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_adpf", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_fl", 1);
        T41_MAP_PARAM_DEF_VALUE.put("fl_bin", 1);
        T41_MAP_PARAM_DEF_VALUE.put("flex_str", 1);
        T41_MAP_PARAM_DEF_VALUE.put("str_bin", 1);

        // Parameters for T44
        T44_MAP_PARAM_TYPE.put("network_file", String.class);
        T44_MAP_PARAM_TYPE.put("auxiliary_file", String.class);
        T44_MAP_PARAM_TYPE.put("scenario_file", String.class);
        T44_MAP_PARAM_TYPE.put("output_files_prefix", String.class);

        T44_MAP_PARAM_TYPE.put("load_correction", Double.class);

        T44_MAP_PARAM_TYPE.put("problem", Integer.class);

        // Default values
        T44_MAP_PARAM_DEF_VALUE.put("problem", 2);
        T44_MAP_PARAM_DEF_VALUE.put("load_correction", 1.0);

        //Parameters for TSG (wind pw scenario)
        TSG_MAP_PARAM_TYPE.put("nsc", Integer.class);
        // Default values
        TSG_MAP_PARAM_DEF_VALUE.put("nsc", 10);
    }
}
