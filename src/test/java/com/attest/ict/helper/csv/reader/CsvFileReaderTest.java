package com.attest.ict.helper.csv.reader;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.domain.Bus;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.helper.csv.exception.CsvReaderFileException;
import com.attest.ict.helper.csv.reader.annotation.ToolByFieldName;
import com.attest.ict.helper.utils.ProfileUtil;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvFileReaderTest {

    private String load_15m = "src\\test\\resources\\csv_file\\test_15m.csv";

    private String load_30m = "src\\test\\resources\\csv_file\\test_30m.csv";

    private String load_1h = "src\\test\\resources\\csv_file\\test_1h.csv";

    private String load_1h_with_err = "src\\test\\resources\\csv_file\\test_1h_meno_Col.csv";

    private String toolCsvFile = "src\\test\\resources\\csv_file\\tool.csv";

    private CsvFileReader reader = new CsvFileReader();

    @Test
    void testParseCsvFile15min() {
        int numRowExpected = 96;
        List<String[]> ld;
        try {
            ld = reader.parseCsvRelativePathFile(load_15m, true);

            int numSkipColumns = 2;
            int numColMax = ProfileUtil.getMaxNumCol(ld, numSkipColumns);
            double ti = ProfileUtil.getTimeInterval(numColMax);
            int step = (int) (1 / ti);
            int incremento = 60 / step;

            LinkedHashMap<String, LoadElVal> mapLoadElVal = new LinkedHashMap<String, LoadElVal>();
            LoadElVal loadElVal;
            for (String[] col : ld) {
                Long busNum = Long.valueOf(col[0]);
                Bus bus = new Bus();
                bus.setBusNum(busNum);
                String type = col[1];
                int size = col.length;
                int colMissing = numColMax + numSkipColumns - size;
                if (size < numColMax + numSkipColumns) {
                    col = Arrays.copyOf(col, col.length + colMissing);
                    for (int k = 0; k < colMissing; k++) {
                        col[size + k] = null;
                    }
                }
                int countHour = 0;
                int countStep = 1;
                int countMin = 0;

                for (int i = 0; i < numColMax; i++) {
                    String mapKey = col[0] + "_" + countHour + "_" + countMin;
                    Double value;
                    try {
                        value = (col[i + numSkipColumns] != null) ? Double.valueOf(col[i + numSkipColumns].trim()) : null;
                    } catch (NumberFormatException ne) {
                        value = null;
                    }

                    if (!mapLoadElVal.containsKey(mapKey)) {
                        loadElVal = new LoadElVal();
                        loadElVal.setBus(bus);
                        loadElVal.setHour(countHour);
                        loadElVal.setMin(countMin);
                    } else {
                        loadElVal = mapLoadElVal.get(mapKey);
                    }
                    if (type.equals("P")) {
                        loadElVal.setP(value);
                    } else {
                        loadElVal.setQ(value);
                    }

                    mapLoadElVal.put(mapKey, loadElVal);

                    if (countStep < step) {
                        countStep++;
                        countMin = countMin + incremento;
                    } else {
                        countStep = 1;
                        countMin = 0;
                        countHour++;
                    }
                }
            }

            int numRow = mapLoadElVal.size();
            assertThat(numRow).isEqualTo(numRowExpected);
        } catch (CsvReaderFileException e) {
            e.printStackTrace();
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void testParseToolCsvFile() {
        int expected = 19;
        List<ToolByFieldName> tools;
        try {
            tools = reader.parseToolCsvRelativePathFile(toolCsvFile);
            int numRow = tools.size();
            assertThat(numRow).isEqualTo(expected);
        } catch (CsvReaderFileException | FileNotFoundException e) {
            e.printStackTrace();
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }
}
