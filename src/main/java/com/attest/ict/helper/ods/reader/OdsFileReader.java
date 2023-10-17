package com.attest.ict.helper.ods.reader;

import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class OdsFileReader {

    public static final Logger log = LoggerFactory.getLogger(OdsFileReader.class);

    protected SpreadSheet spreadSheet = null;

    protected String odsFileName = null;

    public OdsFileReader(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            this.spreadSheet = new SpreadSheet(inputStream);
            this.odsFileName = file.getOriginalFilename();
        } catch (IOException e) {
            throw new OdsReaderFileException(" Error Parsing Ods file " + e);
        }
    }

    public OdsFileReader(String relativePath) {
        try {
            this.spreadSheet = new SpreadSheet(new File(relativePath));
            this.odsFileName = relativePath;
        } catch (IOException ioe) {
            throw new OdsReaderFileException(" Error Parsing Ods file " + ioe.getMessage());
        }
    }

    public OdsFileReader(File relativePath) {
        try {
            this.spreadSheet = new SpreadSheet(relativePath);
            this.odsFileName = relativePath.getName();
        } catch (IOException ioe) {
            throw new OdsReaderFileException(" Error Parsing Ods file " + ioe.getMessage());
        }
    }

    public boolean hasOdsFormat(File file) {
        boolean isOdsFormat = false;
        Path filePath = file.toPath();
        String mimeType;
        try {
            mimeType = FileUtils.probeContentType(filePath);
            // mimeType = Files.probeContentType(filePath);
            log.debug("hasOdsFormat() - File: {}, MimeType: {} ", file.getName());
            isOdsFormat = FileUtils.CONTENT_TYPE.get("ods").equals(mimeType);
        } catch (IOException e) {
            String errMsg = "hasOdsFormat() - Error parsing ODS: " + filePath + " " + e.getMessage();
            log.error(errMsg);
        }
        return isOdsFormat;
    }

    public static boolean hasOdsFormat(MultipartFile mpFile) {
        String contentType = MimeUtils.detect(mpFile);
        return FileUtils.CONTENT_TYPE.get("ods").equals(contentType);
    }

    protected boolean hasContent(Range range) {
        Object value = range.getValue();
        if (value != null && !value.toString().isEmpty()) {
            return true;
        }

        return false;
    }
}
