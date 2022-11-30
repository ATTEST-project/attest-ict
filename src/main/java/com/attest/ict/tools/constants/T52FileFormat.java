package com.attest.ict.tools.constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T52FileFormat extends ToolFileFormat {

    public static List<String> INPUT_CONTENT_TYPE = Stream
        .of("text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel")
        .collect(Collectors.toList());

    public static final String RESULTS_FILES_EXTENSION = ".html";

    public static List<String> DOWNLOAD_FILES_EXTENSION = Stream.of(".html", ".pdf", ".csv").collect(Collectors.toList());
}
