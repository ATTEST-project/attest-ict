package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.util.ExcelFileFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T41FileOutputFormat extends ExcelFileFormat {

    public static final List<String> SHEETS_NAME = Stream
        .of("APC_MW", "EES_CH_MW", "EES_DCH_MW", "FL_OD_MW", "FL_UD_MW", "COST")
        .collect(Collectors.toList());

    public static final String SUFFIX_OUTPUT_FILES_EXTENSION = "_output";
}
