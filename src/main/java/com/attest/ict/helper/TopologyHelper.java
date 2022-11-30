package com.attest.ict.helper;

import com.attest.ict.domain.Topology;
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.repository.TopologyRepository;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopologyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyHelper.class);

    // to call repository in a static function
    static TopologyRepository topologyRepository;

    @Autowired
    TopologyRepository topologyRepository1;

    @PostConstruct
    private void initTopologyRepository() {
        topologyRepository = this.topologyRepository1;
    }

    // to call repository in a static function
    static NetworkRepository networkRepository;

    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    // to call repository in a static function
    static TopologyBusRepository topologyBusesRepository;

    @Autowired
    TopologyBusRepository topologyBusesRepository1;

    @PostConstruct
    private void initTopBusesRepository() {
        topologyBusesRepository = this.topologyBusesRepository1;
    }

    /*public static List<String[]> read(InputStream inputStream) throws IOException {

        Objects.requireNonNull(inputStream);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return read(reader);
        }

    }*/

    /* readTopology method
     * using CSV parser to read and parse line by line a topology file (coordinates of each branch)
     */
    public static List<String[]> readTopology(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);

        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(";");
        settings.setMaxColumns(600);
        settings.setHeaderExtractionEnabled(false);
        settings.setSkipEmptyLines(true);

        CsvParser parser = new CsvParser(settings);

        // OLD without null as value in rows
        /*List<String[]> finalParsedLines = parser.parseAll(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return splitColumns(finalParsedLines);*/

        // new way to parse also rows with null values
        List<String[]> finalParsedLines = new ArrayList<>();

        parser.beginParsing(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String[] row;
        while ((row = parser.parseNext()) != null) {
            row = Arrays.stream(row).filter(Objects::nonNull).toArray(String[]::new);
            finalParsedLines.add(row);
        }

        parser.stopParsing();

        return splitColumns(finalParsedLines);
    }

    /* splitColumns method
     * split columns to have the same number of columns (structure is branch, branch_parent, p1, p2)
     */
    private static List<String[]> splitColumns(List<String[]> list) {
        List<String[]> splitList = new ArrayList<>();

        for (String[] line : list) {
            if (line != null) {
                if (line.length > 3) {
                    int j = 0;
                    for (int i = 1; i < line.length && i + 1 < line.length; ++i) {
                        String[] arr = new String[] { line[0] + "-" + j, line[i], line[i + 1], line[0] };
                        splitList.add(arr);
                        j++;
                    }
                } else {
                    String[] arr = new String[4];
                    for (int i = 0; i < line.length; ++i) {
                        arr[i] = line[i];
                    }
                    arr[3] = line[0];
                    splitList.add(arr);
                }
            }
        }

        return splitList;
    }

    /* createTopologies method
     * return a list of topologies (branch coordinates)
     */
    public static List<Topology> createTopologies(List<String[]> list) {
        List<Topology> topologies = new ArrayList<>();

        for (String[] line : list) {
            if (line != null) {
                TopologyBus topologyBuses = topologyBusesRepository.findTopologyBusesByPLB(line[3]);
                Topology topology = new Topology();
                topology.setPowerLineBranch(line[0]);
                topology.setp1(line[1]);
                topology.setp2(line[2]);
                topology.setPowerLineBranchParent(topologyBuses);
                topologies.add(topology);
            }
        }

        return topologies;
    }

    /*
    // OLD and not efficient
    public static void parseTopology(List<String[]> list) {

        int i = 1;
        for (String[] line : list) {
            LOGGER.info("Line number " + i + " of " + list.size());
            TopologyBuses topologyBuses = topologyBusesRepository.findTopologyBusesByPLB(line[3]);
            if (topologyBuses != null)
                topologyRepository.save(new Topology(line[0], line[1], line[2], topologyBuses));
            i++;
        }

    }*/

    /* stringToDoubleCoords method
     * convert points from string to double
     */
    public static List<Double[]> stringToDoubleCoords(List<String> points) {
        String strPoints = String.join(";", points);
        strPoints = strPoints.replaceAll("\\),\\(", ");(");
        List<String> newPoints = Arrays.asList(strPoints.split(";"));
        newPoints = newPoints.stream().distinct().collect(Collectors.toList());

        List<Double[]> doubles_coords = new ArrayList<>();

        for (String s : newPoints) {
            String[] coords_array = s.replaceAll("^.|.$", "").split(",");
            Double[] d_coords_array = new Double[coords_array.length];
            for (int i = 0; i < coords_array.length; ++i) {
                d_coords_array[i] = Double.parseDouble(coords_array[i]);
            }
            doubles_coords.add(d_coords_array);
        }

        return doubles_coords;
    }
}
