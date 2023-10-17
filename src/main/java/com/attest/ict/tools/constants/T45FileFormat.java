package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T45FileFormat extends ToolFileFormat {

    public static String T45_NETWORK_FILE_XLSX = "network_data.xlsx";

    public static final List<String> OUTPUT_SUFFIX = Stream.of("_output.xlsx").collect(Collectors.toList());

    public static final List<String> T45_CONTENT_TYPES = Arrays.asList(
        FileUtils.CONTENT_TYPE.get("csv"),
        FileUtils.CONTENT_TYPE.get("xlsx")
    );
}
