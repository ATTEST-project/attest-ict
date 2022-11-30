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
            Boolean existMatPowerFile = inputFileService.isNetworkFileAvailable(network.getId());
            if (!existMatPowerFile) {
                // 20220824 L.p
                String message = "Please upload: " + networkName + msgMissingFile;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

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
        log.debug("Request to generate SimpleLineDiagram for Entire Network: {}", networkId);
        Map<String, Object> svgData = new LinkedHashMap<>();
        try {
            Optional<Network> optNet = networkService.findById(networkId);
            if (!optNet.isPresent()) {
                String message = "Network with id " + networkId + " not found";
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
                log.warn(message + " for network: " + networkId);
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

            if (network.getBuses().isEmpty() || network.getBranches().isEmpty() || network.getGenerators().isEmpty()) {
                String message = msgEmptyAsset;
                log.warn(message + " for network id: " + networkName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }
            allSvgData = createSubstationsSvgData(network);
            return new ResponseEntity<>(allSvgData, HttpStatus.OK);
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

            allSvgData = createSubstationsSvgData(network);
            return new ResponseEntity<>(allSvgData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
    /**
    @GetMapping("/generateSubstations/{networkName}")
    public ResponseEntity<List<String>> generateSubstations(@PathVariable("networkName") String networkName) {
        try {
            List<String> substations = new ArrayList<>();

            Optional<Network> optNet = networkService.findNetworkByName(networkName);
            Network network = optNet.get();

            com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);

            for (int i = 0; i < network1.getSubstationCount(); ++i) {
                String subId = "sub_" + (i + 1);
                GraphBuilder graphBuilder = new NetworkGraphBuilder(network1);
                String svgSub = SingleLineDiagramTool.createSubstationDiagram(network1, network1.getSubstation(subId), graphBuilder);
                /*String svgSub = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));*/
    /*         substations.add(svgSub);
            }
            return new ResponseEntity<>(substations, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v1/generateSubstations/{networkName}")
    public ResponseEntity<Map<String, String>> generateSubstationsV1(@PathVariable("networkName") String networkName) {
        try {
            Map<String, String> substations = new LinkedHashMap<>();
            Optional<Network> optNet = networkService.findNetworkByName(networkName);
            log.info("optNet: " + optNet);
            Network network = optNet.get();

            log.info("network: " + network.getName());

            com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);

            for (int i = 0; i < network1.getSubstationCount(); ++i) {
                String subId = "sub_" + (i + 1);
                GraphBuilder graphBuilder = new NetworkGraphBuilder(network1);
                String svgSub = SingleLineDiagramTool.createSubstationDiagram(network1, network1.getSubstation(subId), graphBuilder);
                /*String svgSub = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));*/
    /*     substations.put(subId, svgSub);
            }
            return new ResponseEntity<>(substations, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   */
    /** TODO REMOVE
    @GetMapping("/convertData/{networkId}")
    public ResponseEntity<String> convertData(@PathVariable Long networkId) {
        String message = "";
        Optional<Network> netOpt = networkService.findById(networkId);
        if (!netOpt.isPresent()) {
            message = "Network by id " + networkId + " not found!";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        Network network = netOpt.get();
        try {
            Path path = Paths.get(CURRENT_DIR + "/svg/");
            Files.createDirectory(path);

            String folderPath = path.toString();

            com.powsybl.iidm.network.Network network1 = DataConverter.createNetwork(network);

            SingleLineDiagramTool.draw(network1, folderPath, true);

            // upload svg files in db and delete all files from folder
            Files
                .walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .forEach(f -> dbFileStorageServiceImpl.uploadSVGFileToDB(f, network.getName()));

            Files.delete(path);

            message = "Converted data successfully!";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            message = "Conversion error!";
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    /** TO REMOVE
    @GetMapping(value = "/getSubstation/{networkName}/{subId}")
    public ResponseEntity<?> getSubstationSVG(@PathVariable("networkName") String networkName, @PathVariable("subId") String subId) {
        Long networkId = networkService.getNetworkIdByName(networkName);
        // List<DbFile> svgFiles = dbFileStorageServiceImpl.getSubstationsFileList(networkId);

        try {
            NetworkDisplayFile file = dbFileStorageServiceImpl.getSubstationFile(networkId, subId);

            if (file == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            /*for (DbFile svgFile : svgFiles) {
                String fileName = svgFile.getFileName();
                if (fileName.substring(fileName.lastIndexOf('_')+1, fileName.indexOf('.')).equals(subId)) {
                    file = svgFile;
                }
            }

            if (file == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }*/
    /*
            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    /**
    @GetMapping("/v1/exportData/{networkName}")
    public ResponseEntity<Resource> exportDataV1(@PathVariable("networkName") String networkName) throws IOException {
        InputStream is = fileServiceImpl.getNetworkData(networkName);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] byteArray = buffer.toByteArray();
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + networkName + ".m\"")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(new ByteArrayResource(byteArray));
    }

    @GetMapping("/v2/exportData/{networkName}")
    public StreamingResponseBody exportDataV2(HttpServletResponse response, @PathVariable("networkName") String networkName)
        throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + networkName + ".m\"");
        InputStream inputStream = fileServiceImpl.getNetworkData(networkName);
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            inputStream.close();
        };
    }
    */

}
