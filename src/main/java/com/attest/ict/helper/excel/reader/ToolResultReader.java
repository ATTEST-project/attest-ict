package com.attest.ict.helper.excel.reader;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolResultReader {

    public final Logger log = LoggerFactory.getLogger(ToolResultReader.class);

    public ToolResultReader() {
        super();
    }

    protected boolean isInFileName(File excelFile, String filter) {
        if (!excelFile.isFile()) {
            log.error("{} is Not a file, skip ", excelFile.getName());
            return false;
        }

        String fileName = excelFile.getName();
        return fileName.contains(filter);
    }
}
