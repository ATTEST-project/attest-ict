package com.attest.ict.service.impl;

import com.attest.ict.cimrepo.CimRepoClient;
import com.attest.ict.cimrepo.CimRepoNetworkConverter;
import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.matpower.common.util.structure.MatpowerFileStruct;
import com.attest.ict.helper.matpower.exception.MatpowerReaderFileException;
import com.attest.ict.helper.matpower.network.writer.MatpowerNetworkWriter;
import com.attest.ict.service.CimRepoService;
import com.attest.ict.service.MatpowerNetworkService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.custom.CimRepoNetworkDTO;
import com.attest.ict.service.dto.custom.CimRepositoryNetworkDTO;
import com.attest.ict.service.dto.custom.CimRepositoryNetworkVersionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CimRepoServiceImpl implements CimRepoService {

    private final Logger log = LoggerFactory.getLogger(CimRepoServiceImpl.class);

    private final CimRepoClient cimRepoClient;

    private final CimRepoNetworkConverter cimRepoNetworkConverter;

    private final NetworkService networkService;

    private final MatpowerNetworkService matpowerNetworkService;

    public CimRepoServiceImpl(
        @Value("${cimrepo.base-url}") String cimRepoBaseUrl,
        NetworkService networkService,
        MatpowerNetworkService matpowerNetworkService
    ) {
        this.cimRepoClient = new CimRepoClient(cimRepoBaseUrl);
        this.cimRepoNetworkConverter = new CimRepoNetworkConverter(cimRepoClient);
        this.networkService = networkService;
        this.matpowerNetworkService = matpowerNetworkService;
    }

    @Override
    public List<CimRepositoryNetworkDTO> getNetworks() throws IOException {
        log.info("Request to get networks stored in CIM repository");
        String networksJson = cimRepoClient.getNetworks();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> networksData = mapper.readValue(networksJson, new TypeReference<List<Map<String, Object>>>() {});
        return networksData
            .stream()
            .map(networkMap -> mapNetwork(networkMap))
            .filter(networkDTO -> networkDTO.getNetworkId() != null)
            .sorted(Comparator.comparing(CimRepositoryNetworkDTO::getNetworkName))
            .collect(Collectors.toList());
    }

    private CimRepositoryNetworkDTO mapNetwork(Map<String, Object> networkMap) {
        CimRepositoryNetworkDTO cimRepoNetworkDTO = new CimRepositoryNetworkDTO();
        if (networkMap.containsKey("branchid") && networkMap.containsKey("branch")) {
            cimRepoNetworkDTO.setNetworkId(((Integer) networkMap.get("branchid")).longValue());
            cimRepoNetworkDTO.setNetworkName((String) networkMap.get("branch"));
        }
        return cimRepoNetworkDTO;
    }

    @Override
    public List<CimRepositoryNetworkVersionDTO> getNetworkVersions(Long networkId) throws IOException {
        log.info("Request to get versions of network {} stored in CIM repository", networkId);
        String networkversionsJson = cimRepoClient.getNetworkVersion(networkId);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> networkVersionsData = mapper.readValue(
            networkversionsJson,
            new TypeReference<List<Map<String, Object>>>() {}
        );
        return networkVersionsData
            .stream()
            .map(networkVersionMap -> mapNetworkVersion(networkVersionMap))
            .filter(networkVersionDTO -> networkVersionDTO.getNetworkId() != null && networkVersionDTO.getNetworkVersionId() != null)
            .sorted(Comparator.comparing(CimRepositoryNetworkVersionDTO::getNetworkVersionId).reversed())
            .collect(Collectors.toList());
    }

    private CimRepositoryNetworkVersionDTO mapNetworkVersion(Map<String, Object> networkVersionMap) {
        CimRepositoryNetworkVersionDTO cimRepositoryNetworkVersionDTO = new CimRepositoryNetworkVersionDTO();
        if (networkVersionMap.containsKey("branchid") && networkVersionMap.containsKey("commitid")) {
            cimRepositoryNetworkVersionDTO.setNetworkId(((Integer) networkVersionMap.get("branchid")).longValue());
            cimRepositoryNetworkVersionDTO.setNetworkVersionId(((Integer) networkVersionMap.get("commitid")).longValue());
            if (networkVersionMap.containsKey("commit")) {
                cimRepositoryNetworkVersionDTO.setNetworkVersion((String) networkVersionMap.get("commit"));
            }
            if (networkVersionMap.containsKey("committime")) {
                try {
                    Instant versionDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                        .parse((String) networkVersionMap.get("committime"))
                        .toInstant();
                    cimRepositoryNetworkVersionDTO.setNetworkVersionDate(versionDate);
                } catch (ParseException e) {}
            }
        }
        return cimRepositoryNetworkVersionDTO;
    }

    @Override
    public List<Integer> importNetwork(CimRepoNetworkDTO cimRepoNetworkDTO) throws IOException, MatpowerReaderFileException {
        log.info("Request to import network from CIM repository: {}", cimRepoNetworkDTO);
        // get network versions
        List<CimRepositoryNetworkVersionDTO> cimRepositoryNetworkVersionDTOs = getNetworkVersions(cimRepoNetworkDTO.getNetworkId());
        if (cimRepositoryNetworkVersionDTOs.isEmpty()) {
            log.warn("No network with id {} in CIM repository", cimRepoNetworkDTO.getNetworkId());
            return Collections.emptyList();
        }
        // get last network version
        CimRepositoryNetworkVersionDTO cimRepositoryNetworkVersionDTO = cimRepositoryNetworkVersionDTOs.get(0);
        // convert network from cim repo to matpower
        //        MatpowerModel modelFromCim = cimRepoNetworkConverter.convert(cimRepoNetworkDTO.getName(), cimRepoNetworkDTO.getId(), cimRepositoryNetworkVersionDTO.getNetworkVersionId());
        //        // create attest network
        //        NetworkDTO networkDTO = getNetworkDTO(cimRepoNetworkDTO, cimRepositoryNetworkVersionDTO);
        //        // save attest network
        //        NetworkDTO result = networkService.save(networkDTO);
        //        // save matpower file
        //        Path matpowerFile = getMatpowerFile(cimRepoNetworkDTO.getName(), modelFromCim);
        //        // import matpower file into attest db
        //        matpowerNetworkService.importFromMatpowerFile(getMultipartFile(matpowerFile), result.getId());
        //        // return id of saved network
        //        return Collections.singletonList(result.getId().intValue());
        Map<String, MatpowerModel> modelsFromCim = cimRepoNetworkConverter.convertNetwork(
            cimRepoNetworkDTO.getNetworkName(),
            cimRepoNetworkDTO.getNetworkId(),
            cimRepositoryNetworkVersionDTO.getNetworkVersionId()
        );
        List<Integer> networkIds = new ArrayList<>();
        for (String network : modelsFromCim.keySet()) {
            networkIds.add(
                createAndImportNetwork(network, cimRepoNetworkDTO, cimRepositoryNetworkVersionDTO, modelsFromCim.get(network)).intValue()
            );
        }
        return networkIds;
    }

    private Long createAndImportNetwork(
        String networkName,
        CimRepoNetworkDTO cimRepoNetworkDTO,
        CimRepositoryNetworkVersionDTO cimRepositoryNetworkVersionDTO,
        MatpowerModel modelFromCim
    ) throws IOException {
        // create attest network
        NetworkDTO networkDTO = getNetworkDTO(networkName, cimRepoNetworkDTO, cimRepositoryNetworkVersionDTO);
        // save attest network
        log.info("Saving network {}", networkName);
        NetworkDTO result = networkService.save(networkDTO);
        // save matpower file
        Path matpowerFile = getMatpowerFile(networkName, modelFromCim);
        // import matpower file into attest db
        log.info("Importing file {} in network {}", matpowerFile.getFileName().toString(), networkName);
        matpowerNetworkService.importFromMatpowerFile(getMultipartFile(matpowerFile), result.getId());
        return result.getId();
    }

    private NetworkDTO getNetworkDTO(
        String networkName,
        CimRepoNetworkDTO cimRepoNetworkDTO,
        CimRepositoryNetworkVersionDTO cimRepositoryNetworkVersionDTO
    ) {
        NetworkDTO networkDTO = new NetworkDTO();
        networkDTO.setName(networkName);
        networkDTO.setCountry(cimRepoNetworkDTO.getCountry());
        networkDTO.setType(cimRepoNetworkDTO.getType());
        networkDTO.setNetworkDate(cimRepositoryNetworkVersionDTO.getNetworkVersionDate());
        networkDTO.setIsDeleted(false);
        networkDTO.setCreationDateTime(Instant.now());
        networkDTO.setUpdateDateTime(Instant.now());
        networkDTO.setDescription(
            "network '" +
            networkName +
            "', " +
            "networkId " +
            cimRepoNetworkDTO.getNetworkId() +
            ", " +
            "version '" +
            cimRepositoryNetworkVersionDTO.getNetworkVersion() +
            "', " +
            "versionId " +
            cimRepositoryNetworkVersionDTO.getNetworkVersionId()
        );
        return networkDTO;
    }

    private Path getMatpowerFile(String networkName, MatpowerModel modelFromCim) throws IOException {
        MatpowerFileStruct structFromCim = MatpowerNetworkWriter.generateMatpowerStructure(modelFromCim);
        Path tempMatpowerFile = Files.createTempFile(networkName.replaceAll(" ", "_") + "_", ".m");
        Files.write(tempMatpowerFile, structFromCim.toString().getBytes());
        log.debug("Matpower data of network {} stored in file {}", networkName, tempMatpowerFile.toString());
        return tempMatpowerFile;
    }

    private MultipartFile getMultipartFile(Path matpowerFile) throws IOException {
        return new MockMultipartFile(
            matpowerFile.getFileName().toString(),
            matpowerFile.getFileName().toString(),
            FileUtils.CONTENT_TYPE.get("m"), //"application/octet-stream", // ex "text/x-matlab"
            Files.newInputStream(matpowerFile)
        );
    }
}
