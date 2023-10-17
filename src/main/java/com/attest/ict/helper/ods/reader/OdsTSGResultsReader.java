package com.attest.ict.helper.ods.reader;

import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class OdsTSGResultsReader extends OdsFileReader {

    private final Logger log = LoggerFactory.getLogger(OdsTSGResultsReader.class);

    public OdsTSGResultsReader(MultipartFile file) {
        super(file);
    }

    public OdsTSGResultsReader(String strPath) {
        super(strPath);
    }

    public OdsTSGResultsReader(File relativePath) {
        super(relativePath);
    }

    public Map<String, List<ScenarioValues>> parseOdsTSGResults() throws OdsReaderFileException {
        try {
            Map<String, List<ScenarioValues>> mapDataForSheet = new HashMap<String, List<ScenarioValues>>();

            log.debug("parseOdsTSGResults() - Reading file: {} ", this.odsFileName);

            // log.debug("File contains:  {} sheets.", spread.getNumSheets());
            List<Sheet> sheets = this.spreadSheet.getSheets();
            for (Sheet sheet : sheets) {
                String name = sheet.getName();
                if (TSGFileOutputFormat.scenarioGenSheets.contains(name)) {
                    //log.debug("parsing sheet: {}", name);
                    // -- sheet "wind_scenarios" or "pv_scenarios" or probabilities
                    mapDataForSheet.put(name, parseSheet(sheet));
                } else {
                    // log.debug("Sheet {}, not required, skip ", name);
                }
            }
            log.debug("parseOdsTSGResults() - End reading file: {} ", this.odsFileName);
            return mapDataForSheet;
        } catch (Exception e) {
            throw new OdsReaderFileException("Error parsing ODS file: " + this.odsFileName + " " + e.getMessage());
        }
    }

    /**
     * @param sheet with wind speed or solar value or probability for a particolar
     *              instant of the day
     * @return list of values (speed or solar values or probability) for each
     * scenario
     */
    public List<ScenarioValues> parseSheet(Sheet sheet) {
        log.debug("parseSheet() - sheet {} ", sheet.getName());
        List<ScenarioValues> scNumValues = new ArrayList<ScenarioValues>();
        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // skips first row contains header
            // -- for each row
            for (int r = 1; r < rows; r++) {
                ScenarioValues scVal = new ScenarioValues();
                List<Double> values = new ArrayList<Double>();
                // -- for each columns
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    if (hasContent(rangeCell)) {
                        // "scn", "t1", "t2",...,"t24"
                        switch (c) {
                            case 0:
                                // scenario
                                log.debug("parseSheet() - scenario: {}", rangeCell.getValue());
                                scVal.setScNum((String) rangeCell.getValue());
                                break;
                            default:
                                // value
                                log.debug("parseSheet() - Val type:{} , value: {}", rangeCell.getValue().getClass(), rangeCell.getValue());
                                values.add((Double) rangeCell.getValue());
                                break;
                        }
                    }
                }
                if (scVal.getScNum() != null && values != null && !values.isEmpty()) {
                    scVal.setValues(values);
                    scNumValues.add(scVal);
                }
            }
        }
        log.debug("parseSheet() - END ");
        return scNumValues;
    }

    public static void main(String args[]) {
        File odsScenFile = new File("C:\\ATSIM\\WP4\\TSG\\8485fb2c-c031-407d-ba04-c66ee6a7d5db\\output_data\\scenario_gen.ods");
        OdsTSGResultsReader reader = new OdsTSGResultsReader(odsScenFile);
        try {
            Map<String, List<ScenarioValues>> mapDataForSheet = reader.parseOdsTSGResults();
            mapDataForSheet.entrySet().forEach(entry -> System.out.println("     " + entry));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
