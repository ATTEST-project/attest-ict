package com.attest.ict.tools.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T32FileFormat extends ToolFileFormat {

    public static String RESULTS_FILES_EXTENSION = ".json";

    public static String OUTPUT_DIR = "outputs";
    public static String INPUT_DIR = "inputs";
    public static String TEST_DIR = "tests";
    public static String INPUT_SCOPF_R5_DIR = "SCOPF_R5";

    public static final List<String> OUTPUT_SUFFIX = Stream.of("_pt1.json", "_pt2.json").collect(Collectors.toList());

    //public static String OUTPUT_SUFFIX = "_pt2.json";

    public static List<String> DOWNLOAD_FILES_EXTENSION = Stream.of(".json", ".txt").collect(Collectors.toList());

    public static Map<String, String> MAP_INPUT_CONTENT_TYPE = new HashMap<>();

    static {
        MAP_INPUT_CONTENT_TYPE.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        MAP_INPUT_CONTENT_TYPE.put("json", "application/json");
        MAP_INPUT_CONTENT_TYPE.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
