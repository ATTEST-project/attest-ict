package com.attest.ict.helper.matpower.common.reader;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public abstract class MatpowerReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatpowerReader.class);

    public static boolean isDotMFile(MultipartFile file) {
        String ext = "";
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            ext = fileName.substring(index + 1);
        }
        return ext.equals("m");
    }

    /* processCaseName
     * get model name from 'function mpc'
     */
    protected static String processCaseName(String str) {
        return str.replace(";", " ").substring(str.indexOf("=") + 1).trim();
    }

    /* parseLines method (template)
     * parse lines using TSVParser and annotated classes
     * in case of only simple spaces between values, change them to tab spaces
     * in case of inline comments in rows with data it is necessary to remove them
     * both of these replacements are made with regex (faster)
     * return List of objects
     */
    protected static <T> List<T> parseLines(List<String> lines, Class<T> aClass) {
        LOGGER.debug("Parsing data for class {}", aClass);
        BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(aClass);
        TsvParserSettings settings = new TsvParserSettings();
        settings.setProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(false);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator(";");
        TsvParser parser = new TsvParser(settings);
        lines.stream().map(MatpowerReader::cleanLine).forEach(parser::parseLine);
        return rowProcessor.getBeans();
    }

    protected static TsvParser getTsvParser() {
        TsvParserSettings settings = new TsvParserSettings();
        settings.setHeaderExtractionEnabled(false);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator(";");
        return new TsvParser(settings);
    }

    protected static String cleanLine(String line) {
        return line
            .replaceAll("\\s+", "\t")
            .replaceAll("\\s*;\\t*\\s*%\\s*.*", ";") // rows with values and inline comment
            .trim();
    }
}
