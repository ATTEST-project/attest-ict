package com.attest.ict.cimrepo;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CimRepoNetworkConverter {

    private static Double BASE_MVA = 100D;

    private final Logger log = LoggerFactory.getLogger(CimRepoNetworkConverter.class);
    private final CimRepoClient cimRepoClient;

    public CimRepoNetworkConverter(CimRepoClient cimRepoClient) {
        this.cimRepoClient = cimRepoClient;
    }

    public MatpowerModel convert(String networkName, Long networkId, Long versionId) throws IOException {
        Objects.requireNonNull(networkName);
        Objects.requireNonNull(networkId);
        Objects.requireNonNull(versionId);
        log.debug("Converting network {} version {} name {} to MatPower model", networkId, versionId, networkName);

        ObjectMapper mapper = new ObjectMapper();
        MatpowerModel matpowerModel = new MatpowerModel(networkName.replaceAll(" ", "_") + "()");
        matpowerModel.setVersion("2");
        matpowerModel.setBaseMva(BASE_MVA);

        log.debug("Adding buses to MatPower model of network {} version {}", networkId, versionId);
        String busesJson = cimRepoClient.getBuses(networkId, versionId);
        List<Map<String, Object>> busesData = mapper.readValue(busesJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Bus> buses = CimRepoNetworkMapper.mapBuses(busesData);
        log.debug("Adding {} buses to MatPower model of network {} version {}", buses.size(), networkId, versionId);
        matpowerModel.getBuses().addAll(buses);

        log.debug("Adding lines to MatPower model of network {} version {}", networkId, versionId);
        String linesJson = cimRepoClient.getLines(networkId, versionId);
        List<Map<String, Object>> linesData = mapper.readValue(linesJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Branch> lines = CimRepoNetworkMapper.mapLines(linesData);
        log.debug("Adding {} lines to MatPower model of network {} version {}", lines.size(), networkId, versionId);
        matpowerModel.getBranches().addAll(lines);

        log.debug("Adding transformers to MatPower model of network {} version {}", networkId, versionId);
        String transformersJson = cimRepoClient.getTransformers(networkId, versionId);
        List<Map<String, Object>> transformersData = mapper.readValue(transformersJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Branch> transformers = CimRepoNetworkMapper.mapTransformers(transformersData);
        log.debug("Adding {} transformers to MatPower model of network {} version {}", transformers.size(), networkId, versionId);
        matpowerModel.getBranches().addAll(transformers);

        log.debug("Adding injections to MatPower model of network {} version {}", networkId, versionId);
        String injectionsJson = cimRepoClient.getInjections(networkId, versionId);
        List<Map<String, Object>> injectionsData = mapper.readValue(injectionsJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Generator> injections = CimRepoNetworkMapper.mapGenerators(injectionsData, BASE_MVA);
        log.debug("Adding {} injections to MatPower model of network {} version {}", injections.size(), networkId, versionId);
        matpowerModel.getGenerators().addAll(injections);

        log.debug("Adding generators to MatPower model of network {} version {}", networkId, versionId);
        String generatorsJson = cimRepoClient.getGenerators(networkId, versionId);
        List<Map<String, Object>> generatorsData = mapper.readValue(generatorsJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Generator> generators = CimRepoNetworkMapper.mapGenerators(generatorsData, BASE_MVA);
        log.debug("Adding {} generators to MatPower model of network {} version {}", generators.size(), networkId, versionId);
        matpowerModel.getGenerators().addAll(generators);

        return matpowerModel;
    }

    public Map<String, MatpowerModel> convertUsingTopoIslands(String networkName, Long networkId, Long versionId) throws IOException {
        Objects.requireNonNull(networkName);
        Objects.requireNonNull(networkId);
        Objects.requireNonNull(versionId);
        log.debug("Converting network {} version {} to MatPower model using topological islands", networkId, versionId);

        ObjectMapper mapper = new ObjectMapper();
        Map<Long, MatpowerModel> matpowerModels = new HashMap<>();

        log.debug("Getting topological islands of network {} version {}", networkId, versionId);
        String topoIslandsJson = cimRepoClient.getTopoIslands(networkId, versionId);
        List<Map<String, Object>> topoIslandsData = mapper.readValue(topoIslandsJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, String> topoIslands = CimRepoNetworkMapper
            .mapTopoIslands(topoIslandsData)
            .stream()
            .collect(Collectors.toMap(TopoIsland::getNumber, TopoIsland::getName));

        log.debug("Getting buses to MatPower model of network {} version {}", networkId, versionId);
        String busesJson = cimRepoClient.getBuses(networkId, versionId);
        List<Map<String, Object>> busesData = mapper.readValue(busesJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, List<Map<String, Object>>> splittedBusesData = busesData
            .stream()
            .filter(busMap -> busMap.get("network_id") != null)
            .collect(Collectors.groupingBy(busMap -> Long.valueOf((Integer) busMap.get("network_id"))));

        log.debug("Getting lines to MatPower model of network {} version {}", networkId, versionId);
        String linesJson = cimRepoClient.getLines(networkId, versionId);
        List<Map<String, Object>> linesData = mapper.readValue(linesJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, List<Map<String, Object>>> splittedLinesData = linesData
            .stream()
            .filter(lineMap -> lineMap.get("network_id") != null)
            .collect(Collectors.groupingBy(lineMap -> Long.valueOf((Integer) lineMap.get("network_id"))));

        log.debug("Getting transformers to MatPower model of network {} version {}", networkId, versionId);
        String transformersJson = cimRepoClient.getTransformers(networkId, versionId);
        List<Map<String, Object>> transformersData = mapper.readValue(transformersJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, List<Map<String, Object>>> splittedTransformersData = transformersData
            .stream()
            .filter(transformerMap -> transformerMap.get("network_id") != null)
            .collect(Collectors.groupingBy(transformerMap -> Long.valueOf((Integer) transformerMap.get("network_id"))));

        log.debug("Getting injections to MatPower model of network {} version {}", networkId, versionId);
        String injectionsJson = cimRepoClient.getInjections(networkId, versionId);
        List<Map<String, Object>> injectionsData = mapper.readValue(injectionsJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, List<Map<String, Object>>> splittedInjectionsData = injectionsData
            .stream()
            .filter(injectionsMap -> injectionsMap.get("network_id") != null)
            .collect(Collectors.groupingBy(injectionsMap -> Long.valueOf((Integer) injectionsMap.get("network_id"))));

        log.debug("Getting generators to MatPower model of network {} version {}", networkId, versionId);
        String generatorsJson = cimRepoClient.getGenerators(networkId, versionId);
        List<Map<String, Object>> generatorsData = mapper.readValue(generatorsJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, List<Map<String, Object>>> splittedGeneratorsData = generatorsData
            .stream()
            .filter(generatorsMap -> generatorsMap.get("network_id") != null)
            .collect(Collectors.groupingBy(generatorsMap -> Long.valueOf((Integer) generatorsMap.get("network_id"))));

        for (Long topoIslandId : topoIslands.keySet()) {
            MatpowerModel matpowerModel = new MatpowerModel(
                networkName.replaceAll(" ", "_") + "_" + topoIslands.get(topoIslandId).replaceAll(" ", "_") + "()"
            );
            matpowerModel.setVersion("2");
            matpowerModel.setBaseMva(BASE_MVA);
            matpowerModels.put(topoIslandId, matpowerModel);

            if (splittedBusesData.containsKey(topoIslandId)) {
                log.debug("Adding buses to MatPower model of network {} version {} topo island {}", networkId, versionId, topoIslandId);
                List<Bus> buses = CimRepoNetworkMapper.mapBuses(splittedBusesData.get(topoIslandId));
                log.debug(
                    "Adding {} buses to MatPower model of network {} version {} topo island {}",
                    buses.size(),
                    networkId,
                    versionId,
                    topoIslandId
                );
                matpowerModel.getBuses().addAll(buses);
            }

            if (splittedLinesData.containsKey(topoIslandId)) {
                log.debug("Adding lines to MatPower model of network {} version {} topo island {}", networkId, versionId, topoIslandId);
                List<Branch> lines = CimRepoNetworkMapper.mapLines(splittedLinesData.get(topoIslandId));
                log.debug(
                    "Adding {} lines to MatPower model of network {} version {} topo island {}",
                    lines.size(),
                    networkId,
                    versionId,
                    topoIslandId
                );
                matpowerModel.getBranches().addAll(lines);
            }

            if (splittedTransformersData.containsKey(topoIslandId)) {
                log.debug(
                    "Adding transformers to MatPower model of network {} version {} topo island {}",
                    networkId,
                    versionId,
                    topoIslandId
                );
                List<Branch> transformers = CimRepoNetworkMapper.mapTransformers(splittedTransformersData.get(topoIslandId));
                log.debug(
                    "Adding {} transformers to MatPower model of network {} version {} topo island {}",
                    transformers.size(),
                    networkId,
                    versionId,
                    topoIslandId
                );
                matpowerModel.getBranches().addAll(transformers);
            }

            if (splittedInjectionsData.containsKey(topoIslandId)) {
                log.debug(
                    "Adding injections to MatPower model of network {} version {} topo island {}",
                    networkId,
                    versionId,
                    topoIslandId
                );
                List<Generator> injections = CimRepoNetworkMapper.mapGenerators(splittedInjectionsData.get(topoIslandId), BASE_MVA);
                log.debug(
                    "Adding {} injections to MatPower model of network {} version {} topo island {}",
                    injections.size(),
                    networkId,
                    versionId,
                    topoIslandId
                );
                matpowerModel.getGenerators().addAll(injections);
            }

            if (splittedGeneratorsData.containsKey(topoIslandId)) {
                log.debug(
                    "Adding generators to MatPower model of network {} version {} topo island {}",
                    networkId,
                    versionId,
                    topoIslandId
                );
                List<Generator> generators = CimRepoNetworkMapper.mapGenerators(splittedGeneratorsData.get(topoIslandId), BASE_MVA);
                log.debug(
                    "Adding {} generators to MatPower model of network {} version {} topo island {}",
                    generators.size(),
                    networkId,
                    versionId,
                    topoIslandId
                );
                matpowerModel.getGenerators().addAll(generators);
            }
        }

        Map<String, MatpowerModel> networkMatpowerModels = new HashMap<>();
        for (Long topoIslandId : matpowerModels.keySet()) {
            networkMatpowerModels.put(networkName + " " + topoIslands.get(topoIslandId), matpowerModels.get(topoIslandId));
        }
        return networkMatpowerModels;
    }

    public Map<String, MatpowerModel> convertNetwork(String networkName, Long networkId, Long versionId) throws IOException {
        Objects.requireNonNull(networkName);
        Objects.requireNonNull(networkId);
        Objects.requireNonNull(versionId);
        log.debug("Converting network {} version {} name {} to MatPower model", networkId, versionId, networkName);

        Map<String, MatpowerModel> networkMatpowerModels = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        log.debug("Getting topological islands of network {} version {}", networkId, versionId);
        String topoIslandsJson = cimRepoClient.getTopoIslands(networkId, versionId);
        List<Map<String, Object>> topoIslandsData = mapper.readValue(topoIslandsJson, new TypeReference<List<Map<String, Object>>>() {});
        Map<Long, String> topoIslands = CimRepoNetworkMapper
            .mapTopoIslands(topoIslandsData)
            .stream()
            .collect(Collectors.toMap(TopoIsland::getNumber, TopoIsland::getName));

        MatpowerModel matpowerModel = new MatpowerModel(networkName.replaceAll(" ", "_") + "()");
        matpowerModel.setVersion("2");
        matpowerModel.setBaseMva(BASE_MVA);

        log.debug("Adding buses to MatPower model of network {} version {}", networkId, versionId);
        String busesJson = cimRepoClient.getBuses(networkId, versionId);
        List<Map<String, Object>> busesData = mapper.readValue(busesJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Bus> buses = CimRepoNetworkMapper.mapBuses(busesData);
        log.debug("Adding {} buses to MatPower model of network {} version {}", buses.size(), networkId, versionId);
        matpowerModel.getBuses().addAll(buses);

        log.debug("Adding lines to MatPower model of network {} version {}", networkId, versionId);
        String linesJson = cimRepoClient.getLines(networkId, versionId);
        List<Map<String, Object>> linesData = mapper.readValue(linesJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Branch> lines = CimRepoNetworkMapper.mapLines(linesData);
        log.debug("Adding {} lines to MatPower model of network {} version {}", lines.size(), networkId, versionId);
        matpowerModel.getBranches().addAll(lines);

        log.debug("Adding transformers to MatPower model of network {} version {}", networkId, versionId);
        String transformersJson = cimRepoClient.getTransformers(networkId, versionId);
        List<Map<String, Object>> transformersData = mapper.readValue(transformersJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Branch> transformers = CimRepoNetworkMapper.mapTransformers(transformersData);
        log.debug("Adding {} transformers to MatPower model of network {} version {}", transformers.size(), networkId, versionId);
        matpowerModel.getBranches().addAll(transformers);

        log.debug("Adding injections to MatPower model of network {} version {}", networkId, versionId);
        String injectionsJson = cimRepoClient.getInjections(networkId, versionId);
        List<Map<String, Object>> injectionsData = mapper.readValue(injectionsJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Generator> injections = CimRepoNetworkMapper.mapGenerators(injectionsData, BASE_MVA);
        log.debug("Adding {} injections to MatPower model of network {} version {}", injections.size(), networkId, versionId);
        matpowerModel.getGenerators().addAll(injections);

        log.debug("Adding generators to MatPower model of network {} version {}", networkId, versionId);
        String generatorsJson = cimRepoClient.getGenerators(networkId, versionId);
        List<Map<String, Object>> generatorsData = mapper.readValue(generatorsJson, new TypeReference<List<Map<String, Object>>>() {});
        List<Generator> generators = CimRepoNetworkMapper.mapGenerators(generatorsData, BASE_MVA);
        log.debug("Adding {} generators to MatPower model of network {} version {}", generators.size(), networkId, versionId);
        matpowerModel.getGenerators().addAll(generators);

        networkMatpowerModels.put(networkName, matpowerModel);

        if (topoIslands.keySet().size() > 1) {
            Map<Long, MatpowerModel> matpowerModels = new HashMap<>();

            Map<Long, List<Map<String, Object>>> splittedBusesData = busesData
                .stream()
                .filter(busMap -> busMap.get("network_id") != null)
                .collect(Collectors.groupingBy(busMap -> Long.valueOf((Integer) busMap.get("network_id"))));

            Map<Long, List<Map<String, Object>>> splittedLinesData = linesData
                .stream()
                .filter(lineMap -> lineMap.get("network_id") != null)
                .collect(Collectors.groupingBy(lineMap -> Long.valueOf((Integer) lineMap.get("network_id"))));

            Map<Long, List<Map<String, Object>>> splittedTransformersData = transformersData
                .stream()
                .filter(transformerMap -> transformerMap.get("network_id") != null)
                .collect(Collectors.groupingBy(transformerMap -> Long.valueOf((Integer) transformerMap.get("network_id"))));

            Map<Long, List<Map<String, Object>>> splittedInjectionsData = injectionsData
                .stream()
                .filter(injectionsMap -> injectionsMap.get("network_id") != null)
                .collect(Collectors.groupingBy(injectionsMap -> Long.valueOf((Integer) injectionsMap.get("network_id"))));

            Map<Long, List<Map<String, Object>>> splittedGeneratorsData = generatorsData
                .stream()
                .filter(generatorsMap -> generatorsMap.get("network_id") != null)
                .collect(Collectors.groupingBy(generatorsMap -> Long.valueOf((Integer) generatorsMap.get("network_id"))));

            for (Long topoIslandId : topoIslands.keySet()) {
                MatpowerModel topoIslandMatpowerModel = new MatpowerModel(
                    networkName.replaceAll(" ", "_") + "_" + topoIslands.get(topoIslandId).replaceAll(" ", "_") + "()"
                );
                topoIslandMatpowerModel.setVersion("2");
                topoIslandMatpowerModel.setBaseMva(BASE_MVA);
                matpowerModels.put(topoIslandId, topoIslandMatpowerModel);

                if (splittedBusesData.containsKey(topoIslandId)) {
                    log.debug("Adding buses to MatPower model of network {} version {} topo island {}", networkId, versionId, topoIslandId);
                    List<Bus> topoIslandBuses = CimRepoNetworkMapper.mapBuses(splittedBusesData.get(topoIslandId));
                    log.debug(
                        "Adding {} buses to MatPower model of network {} version {} topo island {}",
                        topoIslandBuses.size(),
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    topoIslandMatpowerModel.getBuses().addAll(topoIslandBuses);
                }

                if (splittedLinesData.containsKey(topoIslandId)) {
                    log.debug("Adding lines to MatPower model of network {} version {} topo island {}", networkId, versionId, topoIslandId);
                    List<Branch> topoIslandLines = CimRepoNetworkMapper.mapLines(splittedLinesData.get(topoIslandId));
                    log.debug(
                        "Adding {} lines to MatPower model of network {} version {} topo island {}",
                        topoIslandLines.size(),
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    topoIslandMatpowerModel.getBranches().addAll(topoIslandLines);
                }

                if (splittedTransformersData.containsKey(topoIslandId)) {
                    log.debug(
                        "Adding transformers to MatPower model of network {} version {} topo island {}",
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    List<Branch> topoIslandTransformers = CimRepoNetworkMapper.mapTransformers(splittedTransformersData.get(topoIslandId));
                    log.debug(
                        "Adding {} transformers to MatPower model of network {} version {} topo island {}",
                        topoIslandTransformers.size(),
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    topoIslandMatpowerModel.getBranches().addAll(topoIslandTransformers);
                }

                if (splittedInjectionsData.containsKey(topoIslandId)) {
                    log.debug(
                        "Adding injections to MatPower model of network {} version {} topo island {}",
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    List<Generator> topoIslandInjections = CimRepoNetworkMapper.mapGenerators(
                        splittedInjectionsData.get(topoIslandId),
                        BASE_MVA
                    );
                    log.debug(
                        "Adding {} injections to MatPower model of network {} version {} topo island {}",
                        topoIslandInjections.size(),
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    topoIslandMatpowerModel.getGenerators().addAll(topoIslandInjections);
                }

                if (splittedGeneratorsData.containsKey(topoIslandId)) {
                    log.debug(
                        "Adding generators to MatPower model of network {} version {} topo island {}",
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    List<Generator> topoIslandGenerators = CimRepoNetworkMapper.mapGenerators(
                        splittedGeneratorsData.get(topoIslandId),
                        BASE_MVA
                    );
                    log.debug(
                        "Adding {} generators to MatPower model of network {} version {} topo island {}",
                        topoIslandGenerators.size(),
                        networkId,
                        versionId,
                        topoIslandId
                    );
                    topoIslandMatpowerModel.getGenerators().addAll(topoIslandGenerators);
                }
            }

            for (Long topoIslandId : matpowerModels.keySet()) {
                networkMatpowerModels.put(networkName + " " + topoIslands.get(topoIslandId), matpowerModels.get(topoIslandId));
            }
        }

        return networkMatpowerModels;
    }
}
