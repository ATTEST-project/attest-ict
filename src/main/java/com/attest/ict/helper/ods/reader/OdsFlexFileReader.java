package com.attest.ict.helper.ods.reader;

import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.github.miachm.sods.Sheet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class OdsFlexFileReader extends OdsFileReader {

    public final Logger log = LoggerFactory.getLogger(OdsFlexFileReader.class);

    public OdsFlexFileReader(MultipartFile file) {
        super(file);
    }

    public OdsFlexFileReader(String strPath) {
        super(strPath);
    }

    public OdsFlexFileReader(File relativePath) {
        super(relativePath);
    }

    public List<Sheet> parseOdsFlexFile() throws Exception {
        List<Sheet> sheetsForTools = new ArrayList<Sheet>();
        try {
            log.debug("Start reading file: {} ", this.odsFileName);
            List<Sheet> sheets = this.spreadSheet.getSheets();
            for (Sheet sheet : sheets) {
                String name = sheet.getName();

                if (T41FileInputFormat.flexSheetMap.containsKey(name)) {
                    sheetsForTools.add(sheet);
                } else {
                    log.debug("sheet: {} not required, will be skip ", name);
                }
            }
            return sheetsForTools;
        } catch (OdsReaderFileException orfe) {
            String errMsg = "Error parsing ODS file: " + this.odsFileName + " " + orfe.getMessage();
            log.error(errMsg);
            return new ArrayList<Sheet>();
        }
    }

    public static void main(String args[]) {}
}
