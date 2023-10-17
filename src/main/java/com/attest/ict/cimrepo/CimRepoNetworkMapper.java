package com.attest.ict.cimrepo;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CimRepoNetworkMapper {

    private CimRepoNetworkMapper() {}

    public static List<Bus> mapBuses(List<Map<String, Object>> busesData) {
        Objects.requireNonNull(busesData);
        return busesData
            .stream()
            .filter(busMap -> busMap.get("network_id") != null)
            .map(busMap -> mapBus(busMap))
            .sorted(Comparator.comparing(Bus::getBusNum))
            .collect(Collectors.toList());
    }

    public static Bus mapBus(Map<String, Object> busMap) {
        Objects.requireNonNull(busMap);
        Bus bus = new Bus();
        bus.setBusNum(Long.parseLong((String) busMap.get("bus_num")));
        bus.setType((Integer) busMap.get("type"));
        bus.setActivePower(busMap.get("active_power") == null ? 0D : (Double) busMap.get("active_power"));
        bus.setReactivePower(busMap.get("reactive_power") == null ? 0D : (Double) busMap.get("reactive_power"));
        bus.setConductance(0D);
        bus.setSusceptance(0D);
        bus.setArea(1L);
        bus.setVm(busMap.get("vmpu") == null ? 0D : (Double) busMap.get("vmpu"));
        bus.setVa(busMap.get("vadeg") == null ? 0D : (Double) busMap.get("vadeg"));
        bus.setBaseKv(busMap.get("bus_kv") == null ? 0D : (Double) busMap.get("bus_kv"));
        bus.setZone(1L);
        bus.setVmax(busMap.get("vmax") == null ? 0D : Double.valueOf((String) busMap.get("vmax")));
        bus.setVmin(busMap.get("vmin") == null ? 0D : Double.valueOf((String) busMap.get("vmin")));
        BusExtension busExtension = new BusExtension();
        busExtension.setmRid((String) busMap.get("mridtext"));
        busExtension.setBus(bus);
        bus.setBusExtension(busExtension);
        return bus;
    }

    public static List<Branch> mapLines(List<Map<String, Object>> linesData) {
        Objects.requireNonNull(linesData);
        return linesData
            .stream()
            .filter(busMap -> busMap.get("network_id") != null)
            .map(lineMap -> mapLine(lineMap))
            .sorted(Comparator.comparing(Branch::getFbus))
            .collect(Collectors.toList());
    }

    public static Branch mapLine(Map<String, Object> lineMap) {
        Objects.requireNonNull(lineMap);
        Branch branch = new Branch();
        branch.setFbus(Long.parseLong((String) lineMap.get("fbus")));
        branch.setTbus(Long.parseLong((String) lineMap.get("tbus")));
        branch.setR(lineMap.get("r") == null ? 0D : (Double) lineMap.get("r"));
        branch.setX(lineMap.get("x") == null ? 0D : (Double) lineMap.get("x"));
        branch.setB(lineMap.get("b") == null ? 0D : (Double) lineMap.get("b"));
        branch.setRatea(lineMap.get("ratemvaa") == null ? 0D : (Double) lineMap.get("ratemvaa"));
        branch.setRateb(lineMap.get("ratemvab") == null ? 0D : (Double) lineMap.get("ratemvab"));
        branch.setRatec(lineMap.get("ratemvac") == null ? 0D : (Double) lineMap.get("ratemvac"));
        branch.setTapRatio(0D);
        branch.setAngle(0D);
        int status = 1;
        if (lineMap.get("fbus_connected") != null) {
            if (Boolean.parseBoolean((String) lineMap.get("fbus_connected")) == false) {
                status = 0;
            }
        }
        if (lineMap.get("tbus_connected") != null) {
            if (Boolean.parseBoolean((String) lineMap.get("tbus_connected")) == false) {
                status = 0;
            }
        }
        branch.setStatus(status);
        branch.setAngmin(-360);
        branch.setAngmax(360);
        BranchExtension branchExtension = new BranchExtension();
        branchExtension.setmRid((String) lineMap.get("mridtext"));
        branchExtension.setBranch(branch);
        branch.setBranchExtension(branchExtension);
        return branch;
    }

    public static List<Branch> mapTransformers(List<Map<String, Object>> transformersData) {
        Objects.requireNonNull(transformersData);
        return transformersData
            .stream()
            .filter(busMap -> busMap.get("network_id") != null)
            .map(transformerMap -> mapTransformer(transformerMap))
            .sorted(Comparator.comparing(Branch::getFbus))
            .collect(Collectors.toList());
    }

    public static Branch mapTransformer(Map<String, Object> transformerMap) {
        Objects.requireNonNull(transformerMap);
        Branch branch = new Branch();
        branch.setFbus(Long.parseLong((String) transformerMap.get("fbus")));
        branch.setTbus(Long.parseLong((String) transformerMap.get("tbus")));
        branch.setR(transformerMap.get("r") == null ? 0D : (Double) transformerMap.get("r"));
        branch.setX(transformerMap.get("x") == null ? 0D : (Double) transformerMap.get("x"));
        branch.setB(transformerMap.get("b") == null ? 0D : (Double) transformerMap.get("b"));
        branch.setRatea(transformerMap.get("ratemvaa") == null ? 0D : (Double) transformerMap.get("ratemvaa"));
        branch.setRateb(transformerMap.get("ratemvab") == null ? 0D : (Double) transformerMap.get("ratemvab"));
        branch.setRatec(transformerMap.get("ratemvac") == null ? 0D : (Double) transformerMap.get("ratemvac"));
        branch.setTapRatio(transformerMap.get("nominal_ratio") == null ? 0D : (Double) transformerMap.get("nominal_ratio"));
        branch.setAngle(0D);
        int status = 1;
        if (transformerMap.get("fbus_connected") != null) {
            if (Boolean.parseBoolean((String) transformerMap.get("fbus_connected")) == false) {
                status = 0;
            }
        }
        if (transformerMap.get("tbus_connected") != null) {
            if (Boolean.parseBoolean((String) transformerMap.get("tbus_connected")) == false) {
                status = 0;
            }
        }
        branch.setStatus(status);
        branch.setAngmin(-360);
        branch.setAngmax(360);
        BranchExtension branchExtension = new BranchExtension();
        branchExtension.setStepSize(transformerMap.get("step_size") == null ? 0D : (Double) transformerMap.get("step_size"));
        branchExtension.setActTap(transformerMap.get("act_tap") == null ? 0D : (Double) transformerMap.get("act_tap"));
        branchExtension.setMinTap(transformerMap.get("min_tap") == null ? 0D : (Double) transformerMap.get("min_tap"));
        branchExtension.setMaxTap(transformerMap.get("max_tap") == null ? 0D : (Double) transformerMap.get("max_tap"));
        branchExtension.setNormalTap(transformerMap.get("normal_tap") == null ? 0D : (Double) transformerMap.get("normal_tap"));
        branchExtension.setNominalRatio(transformerMap.get("nominal_ratio") == null ? 0D : (Double) transformerMap.get("nominal_ratio"));
        branchExtension.setmRid((String) transformerMap.get("mridtext"));
        branchExtension.setBranch(branch);
        branch.setBranchExtension(branchExtension);
        return branch;
    }

    public static List<Generator> mapGenerators(List<Map<String, Object>> generatorsData, Double baseMVA) {
        Objects.requireNonNull(generatorsData);
        return generatorsData
            .stream()
            .filter(busMap -> busMap.get("network_id") != null)
            .map(generatorMap -> mapGenerator(generatorMap, baseMVA))
            .sorted(Comparator.comparing(Generator::getBusNum))
            .collect(Collectors.toList());
    }

    public static Generator mapGenerator(Map<String, Object> generatorMap, Double baseMVA) {
        Objects.requireNonNull(generatorMap);
        Generator generator = new Generator();
        generator.setBusNum(Long.parseLong((String) generatorMap.get("bus_num")));
        generator.setPg(generatorMap.get("pg") == null ? 0D : (Double) generatorMap.get("pg"));
        generator.setQg(generatorMap.get("qg") == null ? 0D : (Double) generatorMap.get("qg"));
        generator.setQmin(generatorMap.get("qmin") == null ? 0D : (Double) generatorMap.get("qmin"));
        generator.setQmax(generatorMap.get("qmax") == null ? 0D : (Double) generatorMap.get("qmax"));
        generator.setVg(generatorMap.get("vmpu") == null ? 0D : (Double) generatorMap.get("vmpu"));
        generator.setmBase(baseMVA);
        int status = 1;
        if (generatorMap.get("bus_connected") != null) {
            if (Boolean.parseBoolean((String) generatorMap.get("bus_connected")) == false) {
                status = 0;
            }
        }
        generator.setStatus(status);
        generator.setPmin(generatorMap.get("pmin") == null ? 0D : (Double) generatorMap.get("pmin"));
        generator.setPmax(generatorMap.get("pmax") == null ? 0D : (Double) generatorMap.get("pmax"));
        generator.setPc1(0D);
        generator.setPc2(0D);
        generator.setQc1min(0D);
        generator.setQc1max(0D);
        generator.setQc2min(0D);
        generator.setQc2max(0D);
        generator.setRampAgc(0D);
        generator.setRamp10(0D);
        generator.setRamp30(0D);
        generator.setRampQ(0D);
        generator.setApf(0L);
        GeneratorExtension generatorExtension = new GeneratorExtension();
        generatorExtension.setGenerator(generator);
        generator.setGeneratorExtension(generatorExtension);
        return generator;
    }

    public static List<TopoIsland> mapTopoIslands(List<Map<String, Object>> topoislandsData) {
        Objects.requireNonNull(topoislandsData);
        return topoislandsData
            .stream()
            .map(topoislandMap -> mapTopoIsland(topoislandMap))
            .sorted(Comparator.comparing(TopoIsland::getNumber))
            .collect(Collectors.toList());
    }

    private static TopoIsland mapTopoIsland(Map<String, Object> topoislandMap) {
        Objects.requireNonNull(topoislandMap);
        TopoIsland topoIsland = new TopoIsland();
        topoIsland.setName((String) topoislandMap.get("rname"));
        topoIsland.setNumber(Long.valueOf((Integer) topoislandMap.get("row_number")));
        return topoIsland;
    }
}
