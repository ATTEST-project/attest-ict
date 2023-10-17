package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T31FileFormat extends ToolFileFormat {

    public static final String ATTEST_INPUT_SUFFIX = "_inputs.json";

    public static final String CASE_INPUT_SUFFIX = ".m";

    public static final String OUTPUT_SUFFIX = "_outputs.json";
    public static final String ALL_OUTPUT_SUFFIX = ".json";

    public static List<String> INPUT_CONTENT_TYPE_LIST = Stream.of(FileUtils.CONTENT_TYPE.get("xlsx")).collect(Collectors.toList());
}
