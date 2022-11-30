package com.attest.ict.helper.ods.reader;

import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdsFlexFileReader extends OdsFileReader {

    public final Logger log = LoggerFactory.getLogger(OdsFlexFileReader.class);

    public List<Sheet> parseFile(File odsFile) throws Exception {
        List<Sheet> sheetsForTools = new ArrayList<Sheet>();
        if (!hasOdsFormat(odsFile)) {
            throw new Exception(" File format invalid ");
        }
        try {
            log.debug("Start reading file: {} ", odsFile.getName());
            SpreadSheet spread = new SpreadSheet(odsFile);
            List<Sheet> sheets = spread.getSheets();
            for (Sheet sheet : sheets) {
                String name = sheet.getName();

                if (T41FileInputFormat.flexSheetMap.containsKey(name)) {
                    sheetsForTools.add(sheet);
                } else {
                    log.debug("sheet: {} not required, will be skip ", name);
                }
            }
            return sheetsForTools;
        } catch (IOException e) {
            String errMsg = "Error parsing ODS file: " + odsFile.getName() + " " + e.getMessage();
            log.error(errMsg);
            return new ArrayList<Sheet>();
        }
    }

    public static void main(String args[]) {}
}
