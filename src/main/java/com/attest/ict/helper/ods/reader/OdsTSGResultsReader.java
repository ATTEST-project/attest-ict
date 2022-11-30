package com.attest.ict.helper.ods.reader;

import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdsTSGResultsReader extends OdsFileReader {

    public final Logger log = LoggerFactory.getLogger(OdsTSGResultsReader.class);
    public final String toolOutputFile = "scenario_gen.ods";

    public Map<String, List<ScenarioValues>> read(File odsFile) throws OdsReaderFileException {
        try {
            Map<String, List<ScenarioValues>> mapDataForSheet = new HashMap<String, List<ScenarioValues>>();
            if (!hasOdsFormat(odsFile)) {
                throw new OdsReaderFileException("Invalid file format! ");
            }

            log.debug("Start reading file: {} ", odsFile.getName());
            SpreadSheet spread = new SpreadSheet(odsFile);
            // log.debug("File contains:  {} sheets.", spread.getNumSheets());
            List<Sheet> sheets = spread.getSheets();
            for (Sheet sheet : sheets) {
                String name = sheet.getName();
                if (TSGFileOutputFormat.scenarioGenSheets.contains(name)) {
                    //log.debug("parsing sheet: {}", name);
                    // -- sheet "wind_scenarios" or "pv_scenarios" or probabilities
                    mapDataForSheet.put(name, parseSheet(sheet));
                } else {
                    log.debug("Sheet {}, not required, skip ", name);
                }
            }
            return mapDataForSheet;
        } catch (IOException e) {
            String errMsg = "Error parsing ODS file: " + odsFile.getName() + " " + e.getMessage();
            log.error(errMsg);
            throw new OdsReaderFileException(errMsg);
        } catch (Exception e) {
            throw new OdsReaderFileException("Error parsing ODS file: " + odsFile.getName() + " " + e.getMessage());
        }
    }

    /**
     * @param sheet with wind speed or solar value or probability for a particolar
     *              instant of the day
     * @return list of values (speed or solar values or probability) for each
     *         scenario
     */
    public List<ScenarioValues> parseSheet(Sheet sheet) {
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
                                scVal.setScNum((String) rangeCell.getValue());
                                break;
                            default:
                                // value
                                log.debug("Val type:{} , value: {}", rangeCell.getValue().getClass(), rangeCell.getValue());
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
        return scNumValues;
    }

    public Map<String, List<ScenarioValues>> readFileInDir(String directoryName) throws FileNotFoundException, OdsReaderFileException {
        File dir = new File(directoryName);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + directoryName + " not found! ");
        }

        File[] fileList = dir.listFiles();
        if (fileList.length == 0) {
            log.warn("Directory:{} is empty", directoryName);
            return Collections.EMPTY_MAP;
        }

        if (fileList.length > 1) {
            log.warn("Directory: {}, contains more files than expected as results from T41_windpv execution!", directoryName);
            throw new OdsReaderFileException("Directory: " + directoryName + ", contains more files than expected!");
        }

        File odsFile = fileList[0];
        if (!isTSGResults(odsFile)) {
            throw new OdsReaderFileException("Directory: " + directoryName + ", doesn't contain file: " + this.toolOutputFile);
        }
        return this.read(odsFile);
    }

    /**
     *
     * @param odsFile
     * @return true if ods output file name is: 'scenario_gen.ods'
     */
    private boolean isTSGResults(File odsFile) {
        if (!odsFile.isFile()) {
            log.error("{} is Not a file, skip ", odsFile.getName());
            return false;
        }
        String fileName = odsFile.getName();
        return fileName.equals(this.toolOutputFile);
    }

    public static void main(String args[]) {
        File odsScenFile = new File("C:\\ATSIM\\WP4\\TSG\\42563759-3ce3-49d4-9e7c-6e21e75d4240\\output_data\\scenario_gen.ods");
        OdsTSGResultsReader reader = new OdsTSGResultsReader();
        try {
            Map<String, List<ScenarioValues>> mapDataForSheet = reader.read(odsScenFile);
            mapDataForSheet.entrySet().forEach(entry -> System.out.println("     " + entry));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
