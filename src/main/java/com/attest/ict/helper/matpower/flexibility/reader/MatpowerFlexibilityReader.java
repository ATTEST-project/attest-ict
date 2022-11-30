package com.attest.ict.helper.matpower.flexibility.reader;

import com.attest.ict.custom.model.matpower.MatpowerFlexibilityModel;
import com.attest.ict.helper.matpower.common.reader.MatpowerReader;
import com.attest.ict.helper.matpower.flexibility.annotated.FlexProfileAnnotated;
import com.attest.ict.helper.matpower.flexibility.util.MatpowerFlexibilitySection;
import com.univocity.parsers.tsv.TsvParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatpowerFlexibilityReader extends MatpowerReader {

    private final Logger log = LoggerFactory.getLogger(MatpowerFlexibilityReader.class);

    private MatpowerFlexibilityReader() {}

    /* read method
     * read file passing InputStream, other 'read' method will be called with BufferedReader parameter
     */
    public static MatpowerFlexibilityModel read(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return read(reader);
        }
    }

    public static MatpowerFlexibilityModel read(BufferedReader reader) throws IOException {
        Objects.requireNonNull(reader);

        String line = "";

        MatpowerFlexibilityModel model = null;
        MatpowerFlexibilitySection section = null;
        List<String> lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (canSkipLine(line)) {
                //skip comments and empty lines
            } else if (line.startsWith("function")) {
                String title = processCaseName(line);
                model = new MatpowerFlexibilityModel(title);
            } else if (line.startsWith("mpc.flex.flexbus")) {
                section = MatpowerFlexibilitySection.FLEXBUS;
            } else if (line.startsWith("mpc.flex.flexprof")) {
                section = MatpowerFlexibilitySection.FLEXPROF;
            } else if (line.startsWith("mpc.flex.PF_UP")) {
                section = MatpowerFlexibilitySection.PF_UP;
            } else if (line.startsWith("mpc.flex.PF_DN")) {
                section = MatpowerFlexibilitySection.PF_DN;
            } else if (line.startsWith("mpc.flex.QF_UP")) {
                section = MatpowerFlexibilitySection.QF_UP;
            } else if (line.startsWith("mpc.flex.QF_DN")) {
                section = MatpowerFlexibilitySection.QF_DN;
            } else if (line.contains("];")) {
                section = processEndSection(model, section, lines);
            } else {
                if (section != null) {
                    lines.add(line);
                }
            }
        }

        return model;
    }

    /* canSkipLine method
     */
    private static boolean canSkipLine(String line) {
        return line.startsWith("%%") || line.trim().length() == 0;
    }

    private static MatpowerFlexibilitySection processEndSection(
        MatpowerFlexibilityModel model,
        MatpowerFlexibilitySection section,
        List<String> lines
    ) {
        if (section != null) {
            parseLines(lines, model, section);
            lines.clear();
            return null;
        }
        return section;
    }

    private static void parseLines(List<String> lines, MatpowerFlexibilityModel model, MatpowerFlexibilitySection section) {
        switch (section) {
            case FLEXBUS:
                model.getFlexBuses().addAll(parseLines(lines));
                break;
            case FLEXPROF:
                model.setFlexProfile(parseFlexProfileLine(lines, FlexProfileAnnotated.class));
                break;
            case PF_UP:
                model.getFlexPfUp().addAll(parseFlexValuesLines(lines));
                break;
            case PF_DN:
                model.getFlexPfDn().addAll(parseFlexValuesLines(lines));
                break;
            case QF_UP:
                model.getFlexQfUp().addAll(parseFlexValuesLines(lines));
                break;
            case QF_DN:
                model.getFlexQfDn().addAll(parseFlexValuesLines(lines));
                break;
        }
    }

    private static List<Long> parseLines(List<String> lines) {
        TsvParser parser = getTsvParser();

        List<Long> parsedLines = lines
            .stream()
            .map(MatpowerReader::cleanLine)
            .map(parser::parseLine)
            .map(e -> Long.valueOf(e[0]))
            .collect(Collectors.toList());
        return parsedLines;
    }

    private static <T> T parseFlexProfileLine(List<String> lines, Class<T> aClass) {
        return parseLines(lines, aClass).get(0);
    }

    private static List<List<Double>> parseFlexValuesLines(List<String> lines) {
        TsvParser parser = getTsvParser();

        List<List<Double>> parsedLines = lines
            .stream()
            .map(MatpowerReader::cleanLine)
            .map(parser::parseLine)
            .filter(Objects::nonNull)
            .map(MatpowerFlexibilityReader::stringArrayToDoubleList)
            .collect(Collectors.toList());

        return parsedLines;
    }

    private static List<Double> stringArrayToDoubleList(String[] array) {
        return Stream.of(array).map(Double::parseDouble).collect(Collectors.toList());
    }
}
