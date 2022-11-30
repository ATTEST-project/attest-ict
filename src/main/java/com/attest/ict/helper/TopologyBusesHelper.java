package com.attest.ict.helper;

import com.attest.ict.domain.Network;
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.repository.TopologyBusRepository;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopologyBusesHelper {

    // to call repository in a static function
    static TopologyBusRepository topologyBusesRepository;

    @Autowired
    TopologyBusRepository topologyBusesRepository1;

    @PostConstruct
    private void initTopologyBusesRepository() {
        topologyBusesRepository = this.topologyBusesRepository1;
    }

    // to call repository in a static function
    static NetworkRepository networkRepository;

    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    /* readTopBuses method
     * using CSV parser to read and parse line by line a topology buses file (branch, bus1, bus2)
     */
    public static List<String[]> readTopBuses(InputStream inputStream) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(";");
        settings.setMaxColumns(600);
        settings.setHeaderExtractionEnabled(true);
        settings.setSkipEmptyLines(true);

        CsvParser parser = new CsvParser(settings);

        List<String[]> finalParsedLines = parser.parseAll(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        /*List<String[]> finalParsedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line = "";

            while ((line = reader.readLine()) != null) {
                finalParsedLines.add(parser.parseLine(line));
            }
        }*/

        return finalParsedLines;
    }

    /* createTopologyBuses method
     * return a list of topology buses (branch, bus1, bus2)
     */
    public static List<TopologyBus> createTopologyBuses(List<String[]> list, Long networkId) {
        List<TopologyBus> topologyBuses = new ArrayList<>();

        Network network = networkRepository.findById(networkId).get();

        for (String[] line : list) {
            if (line != null) {
                TopologyBus tp = new TopologyBus();
                tp.setPowerLineBranch(line[0]);
                tp.setBusName1(line[1]);
                tp.setBusName2(line[2]);
                tp.setNetwork(network);
                topologyBuses.add(tp);
            }
        }

        return topologyBuses;
    }
    /*
    // OLD and not efficient
    public static void parseTopologyBuses(List<String[]> list, String networkId) {

        Network network = networkRepository.findNetworkById(networkId);

        for (String[] line : list) {
            if (line != null) {
                topologyBusesRepository.save(new TopologyBuses(line[0], line[1], line[2], network));
            }
        }
    }*/
}
