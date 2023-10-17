package com.attest.ict.tools.constants;

import com.attest.ict.custom.utils.FileUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WP2FileFormat extends ToolFileFormat {

    public static String INPUT_DIR = "input_data";
    public static String ENERGY_DIR = "energy";
    public static String NETWORK_DIR = "network";
    public static String SECONDARY_DIR = "secondary";
    public static String TERTIARY_DIR = "tertiary";
    public static List<String> DOWNLOAD_FILES_EXTENSION = Stream.of(".xls", ".csv").collect(Collectors.toList());
    public static List<String> T26_DOWNLOAD_FILES_EXTENSION = Stream.of(".csv").collect(Collectors.toList());

    public static List<String> INPUT_CONTENT_TYPE_LIST = Stream
        .of(FileUtils.CONTENT_TYPE.get("xlsx"), FileUtils.CONTENT_TYPE.get("xls"), FileUtils.CONTENT_TYPE.get("csv"))
        .collect(Collectors.toList());
}
