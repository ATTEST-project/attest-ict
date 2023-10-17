package com.attest.ict.helper.ods.reader;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.github.miachm.sods.Sheet;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OdsFileReaderTest {

    private File odsNetFile = new File("src\\test\\resources\\ods_file\\pt_dx_01_2020.ods");
    private File odsFlexFile = new File("src\\test\\resources\\ods_file\\pt_dx_01_2020_flex.ods");
    private File odsScenFile = new File("src\\test\\resources\\ods_file\\scenario_gen.ods");

    private OdsNetworkFileReader reader = new OdsNetworkFileReader(odsNetFile);

    private OdsFlexFileReader readerFlex = new OdsFlexFileReader(odsFlexFile);

    private OdsTSGResultsReader readerScen = new OdsTSGResultsReader(odsScenFile);

    @Test
    void testScenFileHasOdsFormat() {
        assertThat(reader.hasOdsFormat(odsScenFile)).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testParseScenOdsFile() {
        int actualSheets = 0;
        int expectedSheetsNum = TSGFileOutputFormat.scenarioGenSheets.size();
        try {
            Map<String, List<ScenarioValues>> mapDataForSheet = readerScen.parseOdsTSGResults();
            actualSheets = mapDataForSheet.keySet().size();
        } catch (Exception e) {
            actualSheets = -1;
        }
        assertThat(actualSheets).isEqualTo(expectedSheetsNum);
    }

    @Test
    void testNetFileHasOdsFormat() {
        assertThat(reader.hasOdsFormat(odsNetFile)).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testParseNetwotkOdsFile() {
        int actualSheets = 0;
        int expectedSheetsNum = T41FileInputFormat.netwrokSheetMap.keySet().size();
        try {
            List<Sheet> sheets = reader.parseOdsNetworkFile();
            actualSheets = sheets.size();
        } catch (Exception e) {
            actualSheets = -1;
        }
        assertThat(actualSheets).isEqualTo(expectedSheetsNum);
    }

    @Test
    void testFlexFileHasOdsFormat() {
        assertThat(reader.hasOdsFormat(odsFlexFile)).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testParseFlexOdsFile() {
        int actualSheets = 0;
        int expectedSheetsNum = T41FileInputFormat.flexSheetMap.keySet().size() - 2; //   "pLoad_Orig(W)" and "qLoad_Orig(W)") not required
        try {
            List<Sheet> sheets = readerFlex.parseOdsFlexFile();
            actualSheets = sheets.size();
        } catch (Exception e) {
            actualSheets = -1;
        }
        assertThat(actualSheets).isGreaterThanOrEqualTo(expectedSheetsNum);
    }
}
