package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T52FileFormat extends ToolFileFormat {

    public static List<String> INPUT_CONTENT_TYPE = Stream
        .of(
            FileUtils.CONTENT_TYPE.get("csv"),
            FileUtils.CONTENT_TYPE.get("xlsx"), //application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            FileUtils.CONTENT_TYPE.get("xls")
        ) // "application/vnd.ms-excel")
        .collect(Collectors.toList());

    public static final String RESULTS_FILES_EXTENSION = ".html";

    public static List<String> DOWNLOAD_FILES_EXTENSION = Stream.of(".html", ".pdf", ".csv").collect(Collectors.toList());
}
