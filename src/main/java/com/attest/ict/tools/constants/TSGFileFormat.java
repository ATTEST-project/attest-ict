package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSGFileFormat extends ToolFileFormat {

    public static String TSG_INPUT_FILE = "data_scenarios.ods";
    public static String TSG_OUTPUT_FILE = "scenario_gen.ods";
    public static final List<String> OUTPUT_SUFFIX = Stream.of(".ods").collect(Collectors.toList());
    public static final List<String> TSG_CONTENT_TYPE = Arrays.asList(FileUtils.CONTENT_TYPE.get("ods"));
}
