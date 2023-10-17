package com.attest.ict.helper.ods.writer;

import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkOdsExporter {

    public final Logger log = LoggerFactory.getLogger(NetworkOdsExporter.class);

    private SpreadSheet spreadSheet;

    public NetworkOdsExporter() {
        this.spreadSheet = new SpreadSheet();
    }

    public ByteArrayOutputStream writeData(
        Object[][] buses,
        Object[][] branches,
        Object[][] loads,
        Object[][] generators,
        Object[][] baseMVA
    ) throws IOException {
        // -- add spreadsheet: Buses
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeSheet(buses, T41FileInputFormat.networkSheetHeaders.get(0));

        // -- add spreadsheet: Lines (Branch)
        writeSheet(branches, T41FileInputFormat.networkSheetHeaders.get(1));

        // -- add spreadsheet: BaseMVA
        writeSheet(baseMVA, T41FileInputFormat.networkSheetHeaders.get(4));

        // -- add spreadsheet: Generators
        writeSheet(generators, T41FileInputFormat.networkSheetHeaders.get(3));

        // -- add spreadsheet: Loads
        writeSheet(loads, T41FileInputFormat.networkSheetHeaders.get(2));

        spreadSheet.save(out);
        return out;
    }

    private void writeSheet(Object[][] data, String sheetName) {
        if (data == null) {
            return;
        }
        Object[] header = T41FileInputFormat.netwrokSheetMap.get(sheetName).toArray();
        int recordFound = (data != null) ? data.length : 0;
        int numRows = recordFound + 1;
        int numCols = header.length;
        log.info("Prepare sheetName: {}, numRows {}, num Columns: {} ", sheetName, numRows, numCols);
        Object[][] values = new Object[numRows][numCols];
        values[0] = header;

        for (int i = 0; i < recordFound; i++) {
            values[i + 1] = data[i];
        }
        Sheet sheet = new Sheet(sheetName, values.length, values[0].length);
        Range range = sheet.getDataRange();
        range.setValues(values);
        spreadSheet.appendSheet(sheet);
        log.info(" Sheet {}, created succesfully! ", sheetName);
    }

    public static void main(String args[]) {
        try {
            int rows = 3;
            int columns = 3;
            Sheet sheet = new Sheet("A", rows, columns);

            sheet.getDataRange().setValues(1, 2, 3, 4, 5, 6, 7, 8, 9);

            // Set the underline style in the (3,3) cell
            sheet.getRange(2, 2).setFontUnderline(true);

            // Set a bold font to the first 2x2 grid
            sheet.getRange(0, 0, 2, 2).setFontBold(true);

            SpreadSheet spread = new SpreadSheet();
            spread.appendSheet(sheet);
            spread.save(new File("C:\\temp\\Out2.ods"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
