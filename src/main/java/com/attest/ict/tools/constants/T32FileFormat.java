package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T32FileFormat extends ToolFileFormat {

    public static String RESULTS_FILES_EXTENSION = ".json";

    public static String OUTPUT_DIR = "outputs";
    public static String INPUT_DIR = "inputs";

    public static String TEST_DIR = "tests";
    public static String INPUT_SCOPF_R5_DIR = "SCOPF_R5";

    public static final List<String> OUTPUT_SUFFIX = Stream.of("_pt1.json", "_pt2.json").collect(Collectors.toList());

    public static List<String> INPUT_CONTENT_TYPE_LIST = Stream
        .of(FileUtils.CONTENT_TYPE.get("ods"), FileUtils.CONTENT_TYPE.get("json"), FileUtils.CONTENT_TYPE.get("xlsx"))
        .collect(Collectors.toList());
}
