package com.attest.ict.helper;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.ProtectionToolRepository;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProtectionToolsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtectionToolsHelper.class);

    // to call repository in a static function
    static ProtectionToolRepository protectionToolsRepository;

    @Autowired
    ProtectionToolRepository protectionToolsRepository1;

    @PostConstruct
    private void initProtectionToolsRepository() {
        protectionToolsRepository = this.protectionToolsRepository1;
    }

    // to call repository in a static function
    static BusRepository busRepository;

    @Autowired
    BusRepository busRepository1;

    @PostConstruct
    private void initBusRepository() {
        busRepository = this.busRepository1;
    }

    // to call repository in a static function
    static BranchRepository branchRepository;

    @Autowired
    BranchRepository branchRepository1;

    @PostConstruct
    private void initBranchRepository() {
        branchRepository = this.branchRepository1;
    }

    /* readProt method
     * using CSV parser to read and parse line by line a protection tools file
     */
    public static List<String[]> readProt(InputStream inputStream) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator(";");
        settings.getFormat().setDelimiter(" ");
        settings.setMaxColumns(600);
        settings.setHeaderExtractionEnabled(true);
        settings.setSkipEmptyLines(true);
        settings.setEmptyValue("<EMPTY>");

        CsvParser parser = new CsvParser(settings);

        List<String[]> parsedLines = parser.parseAll(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        /*List<String[]> parsedLines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line = "";

            while((line = reader.readLine()) != null) {
                parsedLines.add(parser.parseLine(line));
            }

        }*/

        return parsedLines;
    }

    /* createProtTools method
     * return a list of protection tools
     */
    public static List<ProtectionTool> createProtTools(List<String[]> prots) {
        List<ProtectionTool> protectionTools = new ArrayList<>();

        for (String[] prot : prots) {
            if (prot != null && prot.length > 1) {
                // Bus bus = busRepository.findBusById(Integer.parseInt(prot[1]));
                Branch branch = branchRepository.findBranchById(Long.parseLong(prot[2]));
                ProtectionTool pt = new ProtectionTool();
                pt.setType(prot[0]);
                //pt.setBus(bus);
                pt.setBranch(branch);
                protectionTools.add(pt);
            }
        }

        return protectionTools;
    }
    /*
    // OLD and not efficient
    public static void saveProt(List<String[]> prots) {

        for (String[] prot : prots) {
            if (prot != null) {
                Bus bus = busRepository.findBusById(Long.parseLong(prot[1]));
                Branch branch = branchRepository.findBranchById(Long.parseLong(prot[2]));
                protectionToolsRepository.save(new ProtectionTools(prot[0], bus, branch));
            }
        }

    }*/

}
