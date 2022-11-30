package com.attest.ict.helper.ods.reader;

import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.ods.utils.OdsFileFormat;
import com.github.miachm.sods.Range;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class OdsFileReader {

    public static final Logger log = LoggerFactory.getLogger(OdsFileReader.class);

    protected boolean hasOdsFormat(File file) {
        boolean isOdsFormat = false;
        Path filePath = file.toPath();
        String mimeType;
        try {
            mimeType = FileUtils.probeContentType(filePath);
            // mimeType = Files.probeContentType(filePath);
            //log.debug(" File: {}, MimeType: {} ", file.getName());
            isOdsFormat = OdsFileFormat.ODS_CONTENT_MEDIA_TYPE.equals(mimeType);
        } catch (IOException e) {
            String errMsg = "Error parsing ODS: " + filePath + " " + e.getMessage();
            log.error(errMsg);
        }
        return isOdsFormat;
    }

    public static boolean hasOdsFormat(MultipartFile file) {
        boolean isOdsFormat = false;
        isOdsFormat = OdsFileFormat.ODS_CONTENT_MEDIA_TYPE.equals(file.getContentType());
        return isOdsFormat;
    }

    public static File transferMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        // save tempFile
        File convFile = File.createTempFile(multipartFile.getOriginalFilename(), OdsFileFormat.FILE_EXTENSION);
        multipartFile.transferTo(convFile);
        log.debug(
            "Save temp File: {},  Path: {}, contentType: {} ",
            convFile.getName(),
            convFile.getPath(),
            //Files.probeContentType(convFile.toPath())
            FileUtils.probeContentType(convFile.toPath())
        );
        return convFile;
    }

    protected boolean hasContent(Range range) {
        Object value = range.getValue();
        if (value != null && !value.toString().isEmpty()) {
            return true;
        }

        return false;
    }
}
