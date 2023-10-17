package com.attest.ict.helper.csv.reader;

import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.helper.csv.exception.CsvReaderFileException;
import com.attest.ict.helper.csv.reader.annotation.ToolByFieldName;
import com.attest.ict.helper.csv.util.CsvConstants;
import com.univocity.parsers.common.processor.BatchedColumnProcessor;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class CsvFileReader {

    public final Logger log = LoggerFactory.getLogger(CsvFileReader.class);

    private CsvParserSettings settings;

    public CsvFileReader() {
        settings = new CsvParserSettings();
    }

    public static boolean hasCSVFormat(MultipartFile mpFile) {
        String contentType = MimeUtils.detect(mpFile);
        if (!FileUtils.CONTENT_TYPE.get("csv").equals(contentType)) {
            return false;
        }
        return true;
    }

    public List<String[]> parseCsvRelativePathFile(String relativePath, boolean headerEnabled)
        throws CsvReaderFileException, FileNotFoundException {
        return parseCsvFile(new FileInputStream(new File(relativePath)), headerEnabled);
    }

    public List<String[]> parseCsvMultiPartFile(MultipartFile file, boolean headerEnabled) throws CsvReaderFileException {
        try {
            return parseCsvFile(file.getInputStream(), headerEnabled);
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public List<String[]> parseCsvFile(InputStream inputStream, boolean headerEnabled) throws CsvReaderFileException {
        settings.getFormat().setDelimiter(CsvConstants.FIELD_DELIMITER);
        settings.getFormat().setLineSeparator(CsvConstants.LINE_DELIMITER);
        settings.setSkipEmptyLines(true);

        settings.setHeaderExtractionEnabled(headerEnabled);

        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            CsvParser parser = new CsvParser(settings);

            List<String[]> parsedRows = parser.parseAll(inputReader);
            return parsedRows;
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public List<String[]> parseCsvFileInBatches(String relativePath, int batchSize) throws CsvReaderFileException {
        settings.getFormat().setDelimiter(CsvConstants.FIELD_DELIMITER);
        settings.getFormat().setLineSeparator(CsvConstants.LINE_DELIMITER);
        settings.setSkipEmptyLines(true);
        settings.setHeaderExtractionEnabled(false);
        try (Reader inputReader = new InputStreamReader(new FileInputStream(new File(relativePath)), "UTF-8")) {
            CsvParser parser = new CsvParser(settings);
            settings.setProcessor(
                new BatchedColumnProcessor(batchSize) {
                    @Override
                    public void batchProcessed(int rowsInThisBatch) {}
                }
            );

            List<String[]> parsedRows = parser.parseAll(inputReader);
            return parsedRows;
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file:  " + relativePath + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public List<ToolByFieldName> parseToolCsvRelativePathFile(String relativePath) throws CsvReaderFileException, FileNotFoundException {
        return parseToolCsv(new FileInputStream(new File(relativePath)));
    }

    public List<ToolByFieldName> parseToolCsvMultiPartFile(MultipartFile file) throws CsvReaderFileException {
        try {
            return parseToolCsv(file.getInputStream());
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    // Parse Header File

    public String[] parseCsvHeaderMultiPartFile(MultipartFile file, String fieldDelimiter) throws CsvReaderFileException {
        try {
            if (fieldDelimiter == null) {
                fieldDelimiter = CsvConstants.COMMA_FIELD_DELIMITER;
            }
            return parseCsvHeaderFile(file.getInputStream(), fieldDelimiter);
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public String[] parseCsvHeaderFile(String relativePath) throws CsvReaderFileException {
        try {
            return parseCsvHeaderFile(new FileInputStream(new File(relativePath)));
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + relativePath + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public String[] parseCsvHeaderMultiPartFile(MultipartFile file) throws CsvReaderFileException {
        try {
            return parseCsvHeaderFile(file.getInputStream());
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public String[] parseCsvHeaderFile(String relativePath, String fieldDelimiter) throws CsvReaderFileException {
        try {
            if (fieldDelimiter == null) {
                fieldDelimiter = CsvConstants.COMMA_FIELD_DELIMITER;
            }
            return parseCsvHeaderFile(new FileInputStream(new File(relativePath)), fieldDelimiter);
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file: " + relativePath + " " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public String[] parseCsvHeaderFile(InputStream inputStream, String fieldDelimiter) throws CsvReaderFileException {
        if (fieldDelimiter == null) {
            fieldDelimiter = CsvConstants.COMMA_FIELD_DELIMITER;
        }
        settings.getFormat().setDelimiter(fieldDelimiter);
        settings.getFormat().setLineSeparator(CsvConstants.LINE_DELIMITER);
        settings.setSkipEmptyLines(true);
        RowListProcessor rowProcessor = new RowListProcessor();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);

        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            String[] headers = rowProcessor.getHeaders();
            return headers;
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public String[] parseCsvHeaderFile(InputStream inputStream) throws CsvReaderFileException {
        settings.setDelimiterDetectionEnabled(true);
        settings.getFormat().setLineSeparator(CsvConstants.LINE_DELIMITER);
        settings.setSkipEmptyLines(true);
        RowListProcessor rowProcessor = new RowListProcessor();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);

        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            String[] headers = rowProcessor.getHeaders();
            return headers;
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    /**
     * @param inputStream
     * @return List<ToolByFieldName>
     */
    public List<ToolByFieldName> parseToolCsv(InputStream inputStream) throws CsvReaderFileException {
        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            // BeanListProcessor converts each parsed row to an instance of a given class,
            // then stores each instance into a list.
            BeanListProcessor<ToolByFieldName> rowProcessor = new BeanListProcessor<ToolByFieldName>(ToolByFieldName.class);
            settings.setProcessor(rowProcessor);
            settings.getFormat().setDelimiter(CsvConstants.FIELD_DELIMITER);
            settings.getFormat().setLineSeparator(CsvConstants.LINE_DELIMITER);
            settings.setSkipEmptyLines(true);
            settings.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            List<ToolByFieldName> beans = rowProcessor.getBeans();
            return beans;
        } catch (IOException e) {
            String errMsg = "fail to parse CSV file " + e.getMessage();
            log.error(errMsg);
            throw new CsvReaderFileException(errMsg);
        }
    }

    public static void main(String args[]) {
        CsvFileReader reader = new CsvFileReader();
        String relativePath =
            "C:\\SVILUPPO\\ATTEST\\GIT-LAB\\BR_JHI_BACKEND-NEW\\jhipster-attest\\src\\test\\resources\\csv_file\\HEP_economic_impact_all.csv";
        String[] headers = reader.parseCsvHeaderFile(relativePath, null);
        for (String header : headers) {
            System.out.println(header);
        }
    }
}
