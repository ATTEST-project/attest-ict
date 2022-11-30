package com.attest.ict.helper.csv.writer;

import com.attest.ict.helper.csv.exception.CsvWriterFileException;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvFileWriter {

    public static final Logger LOGGER = LoggerFactory.getLogger(CsvFileWriter.class);
    public static String TYPE = "csv";

    private CsvFileWriter() {}

    public void writeFile(List<Object[]> objList, String outputPath) throws CsvWriterFileException {
        try (Writer outputWriter = new OutputStreamWriter(new FileOutputStream(new File(outputPath)), "UTF-8")) {
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            writer.writeRowsAndClose(objList);
        } catch (IOException e) {
            String errMsg = "Fail to write CSV file: " + outputPath + ", " + e.getMessage();
            LOGGER.error(errMsg);
            throw new CsvWriterFileException(errMsg);
        }
    }
}
