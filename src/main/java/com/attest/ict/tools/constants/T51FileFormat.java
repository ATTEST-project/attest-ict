package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T51FileFormat extends ToolFileFormat {

    public static final String CHARACTERIZATION_FILE_LAUNCH = "launch_characterization.bat";

    public static final String MONITORING_FILE_LAUNCH = "launch_monitoring.bat";

    public static final String CHARACTERIZATION_FILE_CONFIG = "launch_characterization.json";

    public static final String MONITORING_FILE_CONFIG = "launch_monitoring.json";

    public static final String RESULTS_FILES_EXTENSION = ".html";

    public static final String CHARACTERIZATION_DIR = "restore_files";

    public static List<String> DOWNLOAD_FILES_EXTENSION = Stream.of(".html").collect(Collectors.toList());

    public static List<String> CHARACT_INPUT_CONTENT_TYPE = Stream
        .of(FileUtils.CONTENT_TYPE.get("csv"), FileUtils.CONTENT_TYPE.get("xlsx"), FileUtils.CONTENT_TYPE.get("xls"))
        .collect(Collectors.toList());

    public static List<String> MONITORING_INPUT_CONTENT_TYPE = Stream
        .of(
            FileUtils.CONTENT_TYPE.get("h5"), //  "application/octet-stream
            FileUtils.CONTENT_TYPE.get("json"), // "application/json"
            FileUtils.CONTENT_TYPE.get("xlsx"), // "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" xlsx
            FileUtils.CONTENT_TYPE.get("xls") //"application/vnd.ms-excel")
        )
        .collect(Collectors.toList());
}
