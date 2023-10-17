package com.attest.ict.web.rest;

import com.attest.ict.custom.diagrams.converter.DataConverter;
import com.attest.ict.custom.diagrams.draw.SingleLineDiagramTool;
import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.domain.Network;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.powsybl.iidm.network.Identifiable;
import com.powsybl.iidm.network.Substation;
import com.powsybl.sld.GraphBuilder;
import com.powsybl.sld.NetworkGraphBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SingleLineDiagramResource {

    private final Logger log = LoggerFactory.getLogger(SingleLineDiagramResource.class);

    @Autowired
    private NetworkService networkService;

    @Autowired
    private InputFileService inputFileService;

    private String msgMissingFile = " matpower/ods network file, before proceding! ";

    private String msgEmptyAsset = "Network's buses or branches or generators are empty! ";

    @Transactional
    // EX @GetMapping("/generateEntireNetwork/{networkName}")
    @GetMapping("/sld-network/{networkName}")
    public ResponseEntity<?> generateEntireNetwork(@PathVariable("networkName") String networkName) {
        log.debug("Request to generate SimpleLineDiagram for Entire Network: {}", networkName);
        Map<String, Object> svgData = new LinkedHashMap<>();
        try {
            Optional<Network> optNet = networkService.findNetworkByName(networkName);
            if (!optNet.isPresent()) {
                String message = "Network with name " + networkName + " not found";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            Network network = optNet.get();
            log.debug("Network: {}", network);
            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                // 20220824 L.p
                String message = "Please upload: " + networkName + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            log.debug("Network buses Num: {}", network.getBuses().size());
            log.debug("Network branches NUM: {}", network.getBranches().size());
            log.debug("Network generators NUM: {}", network.getGenerators().size());
            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network: " + networkName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            svgData = createSvgData(network);
            return new ResponseEntity<>(svgData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("/sld-network/id/{networkId}")
    public ResponseEntity<?> generateEntireNetwork(@PathVariable("networkId") Long networkId) {
        log.debug("Request to generate SimpleLineDiagram for Entire Network with Id: {}", networkId);
        Map<String, Object> svgData = new LinkedHashMap<>();
        try {
            Optional<Network> optNet = networkService.findById(networkId);
            if (!optNet.isPresent()) {
                String message = "Network with id " + networkId + " not found! ";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();

            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network id: " + networkId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            svgData = createSvgData(network);
            return new ResponseEntity<>(svgData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("/v1/sld-network/{networkName}")
    public ResponseEntity<?> generateEntireNetworkMetadata(@PathVariable("networkName") String networkName) {
        log.debug("Request to generate SimpleLineDiagram for Entire Network: {}", networkName);
        Map<String, Object> svgData = new LinkedHashMap<>();
        try {
            Optional<Network> optNet = networkService.findNetworkByName(networkName);
            if (!optNet.isPresent()) {
                String message = "Network with name " + networkName + " not found";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();

            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network: " + networkName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            svgData = createSvgMetaData(network);
            return new ResponseEntity<>(svgData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("/v1/sld-network/id/{networkId}")
    public ResponseEntity<?> generateEntireNetworkMetadataById(@PathVariable("networkId") Long networkId) {
        log.debug("Request to generate SimpleLineDiagram for Entire NetworkId: {}", networkId);
        Map<String, Object> svgData = new LinkedHashMap<>();
        try {
            Optional<Network> optNet = networkService.findById(networkId);
            if (!optNet.isPresent()) {
                String message = "Network with id " + networkId + " not found";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();
            log.debug("Network found: {}", network);

            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            log.debug("Num buses: {}", network.getBuses().size());
            log.debug("Num branches: {}", network.getBranches().size());
            log.debug("Num generators: {}", network.getGenerators().size());

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network: " + networkId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            try {
                svgData = createSvgMetaData(network);
            } catch (Exception e) {
                log.error("createSvgMetaData raise an Exception: ", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(svgData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    //EX @GetMapping("/v2/generateSubstations/{networkName}")
    @GetMapping("/sld-substations/{networkName}")
    public ResponseEntity<?> generateSubstationsMetadata(@PathVariable("networkName") String networkName) {
        log.debug("Request to generate SimpleLineDiagram for Substations of Network: {}", networkName);
        try {
            Map<String, Map<String, String>> allSvgData = new LinkedHashMap<>();
            Optional<Network> optNet = networkService.findNetworkByName(networkName);
            if (!optNet.isPresent()) {
                String message = "Network " + networkName + " not found! ";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();

            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            log.debug("Num buses: {}", network.getBuses().size());
            log.debug("Num branches: {}", network.getBranches().size());
            log.debug("Num generators: {}", network.getGenerators().size());

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network id: " + networkName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            try {
                allSvgData = createSubstationsSvgData(network);
                return new ResponseEntity<>(allSvgData, HttpStatus.OK);
            } catch (Exception e) {
                log.error("createSubstationsSvgData raise an Exception: ", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("/sld-substations/id/{networkId}")
    public ResponseEntity<?> generateSubstationsMetadata(@PathVariable("networkId") Long networkId) {
        log.debug("Request to generate SimpleLineDiagram for Substations of Network with id: {}", networkId);
        try {
            Map<String, Map<String, String>> allSvgData = new LinkedHashMap<>();
            Optional<Network> optNet = networkService.findById(networkId);

            if (!optNet.isPresent()) {
                String message = "Network with id " + networkId + " not found! ";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();

            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network id: " + networkId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            try {
                allSvgData = createSubstationsSvgData(network);
                return new ResponseEntity<>(allSvgData, HttpStatus.OK);
            } catch (Exception e) {
                log.error("createSubstationsSvgData raise an Exception: ", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/substations-num/{networkName}")
    public ResponseEntity<?> getNumberOfSubstations(@PathVariable("networkName") String networkName) {
        try {
            Optional<Network> optNet = networkService.findNetworkByName(networkName);

            if (!optNet.isPresent()) {
                String message = "Network: " + networkName + " not found! ";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            Network network = optNet.get();
            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                String message = "Please upload: " + network.getName() + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);
            int nSubs = network1.getSubstationCount();

            if (nSubs == 0) {
                String message = "No substation found for: " + networkName;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
                //return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map<String, Integer> response = new HashMap<>();
            response.put("nSubstations", nSubs);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createSvgData(Network network) {
        Map<String, Object> svgData = new LinkedHashMap<>();
        com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);

        List<String> substationsIds = network1.getSubstationStream().map(Identifiable::getId).collect(Collectors.toList());
        String networkSvg = SingleLineDiagramTool.createEntireNetwork(network1);

        svgData.put("ids", substationsIds);
        svgData.put("svg", networkSvg);
        return svgData;
    }

    private Map<String, Object> createSvgMetaData(Network network) {
        Map<String, Object> svgData = new LinkedHashMap<>();
        com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);

        List<String> substationsIds = network1.getSubstationStream().map(Identifiable::getId).collect(Collectors.toList());
        Map<String, String> networkSvg = SingleLineDiagramTool.createEntireNetworkMetadata(network1);

        svgData.put("ids", substationsIds);
        svgData.put("svg", networkSvg);
        return svgData;
    }

    private Map<String, Map<String, String>> createSubstationsSvgData(Network network) {
        Map<String, Map<String, String>> allSvgData = new LinkedHashMap<>();
        com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);
        for (Substation substation : network1.getSubstations()) {
            // String subId = "sub_" + (i+1);
            log.info(substation.getId());
            GraphBuilder graphBuilder = new NetworkGraphBuilder(network1);
            Map<String, String> svgData = SingleLineDiagramTool.createSubstationDiagramMetadata(
                network1,
                network1.getSubstation(substation.getId()),
                graphBuilder
            );
            allSvgData.put(substation.getId(), svgData);
        }
        return allSvgData;
    }
}
