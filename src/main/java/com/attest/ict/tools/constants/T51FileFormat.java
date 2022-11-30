package com.attest.ict.tools.constants;

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
        .of("text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel")
        .collect(Collectors.toList());

    public static List<String> MONITORING_INPUT_CONTENT_TYPE = Stream
        .of(
            "text/x-hdf5",
            "application/octet-stream",
            "application/json",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/csv",
            "application/vnd.ms-excel"
        )
        .collect(Collectors.toList());
}
