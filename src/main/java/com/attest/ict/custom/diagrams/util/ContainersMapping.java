package com.attest.ict.custom.diagrams.util;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.powsybl.commons.PowsyblException;
import java.util.*;
import java.util.function.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.Pseudograph;

public class ContainersMapping {

    private final Map<Long, String> busNumToVoltageLevelId = new HashMap<>();

    private final Map<String, Set<Long>> voltageLevelIdToBusNums = new HashMap<>();

    private final Map<String, String> voltageLevelIdToSubstationId = new HashMap<>();

    // create voltage levels id
    public String getVoltageLevelId(long num) {
        String voltageLevelId = (busNumToVoltageLevelId.get(num));
        if (voltageLevelId == null) {
            throw new PowsyblException("Bus " + num + " not found");
        }
        return voltageLevelId;
    }

    // create substations id
    public String getSubstationId(String voltageLevelId) {
        String substationId = voltageLevelIdToSubstationId.get(voltageLevelId);
        if (substationId == null) {
            throw new PowsyblException("Voltage level '" + voltageLevelId + "' not found");
        }
        return substationId;
    }

    // ContainersMapping to map a specific Bus, Branch, Generator model to what PowSyBl expects
    public static ContainersMapping create(
        List<Bus> buses,
        List<Branch> branches,
        ToLongFunction<Bus> busToNum,
        ToLongFunction<Branch> branchToNum1,
        ToLongFunction<Branch> branchToNum2,
        ToLongFunction<Branch> branchToNum3,
        ToDoubleFunction<Branch> branchToResistance,
        ToDoubleFunction<Branch> branchToReactance,
        Predicate<Branch> branchToIsTransformer,
        Function<Set<Long>, String> busesToVoltageLevelId,
        IntFunction<String> substationNumToId
    ) {
        Objects.requireNonNull(buses);
        Objects.requireNonNull(branches);
        Objects.requireNonNull(busToNum);
        Objects.requireNonNull(branchToNum1);
        Objects.requireNonNull(branchToNum2);
        Objects.requireNonNull(branchToResistance);
        Objects.requireNonNull(branchToReactance);
        Objects.requireNonNull(branchToIsTransformer);
        Objects.requireNonNull(busesToVoltageLevelId);
        Objects.requireNonNull(substationNumToId);

        ContainersMapping containersMapping = new ContainersMapping();

        // group buses connected to non impedant lines to voltage levels
        createVoltageLevelMapping(
            buses,
            branches,
            busToNum,
            branchToNum1,
            branchToNum2,
            branchToResistance,
            branchToReactance,
            busesToVoltageLevelId,
            containersMapping
        );

        // group voltage levels connected by transformers to substations
        createSubstationMapping(
            branches,
            branchToNum1,
            branchToNum2,
            branchToNum3,
            branchToIsTransformer,
            substationNumToId,
            containersMapping
        );

        return containersMapping;
    }

    // createVoltageLevelMapping to create voltage level PowSyBl model using data from other sources
    // for example our Bus, Branch and Generator model
    private static void createVoltageLevelMapping(
        List<Bus> buses,
        List<Branch> branches,
        ToLongFunction<Bus> busToNum,
        ToLongFunction<Branch> branchToNum1,
        ToLongFunction<Branch> branchToNum2,
        ToDoubleFunction<Branch> branchToResistance,
        ToDoubleFunction<Branch> branchToReactance,
        Function<Set<Long>, String> busesToVoltageLevelId,
        ContainersMapping containersMapping
    ) {
        Graph<Long, Object> vlGraph = new Pseudograph<>(Object.class);
        for (Bus bus : buses) {
            vlGraph.addVertex((long) busToNum.applyAsLong(bus));
        }
        for (Branch branch : branches) {
            if (branchToResistance.applyAsDouble(branch) == 0 && branchToReactance.applyAsDouble(branch) == 0) {
                vlGraph.addEdge(branchToNum1.applyAsLong(branch), branchToNum2.applyAsLong(branch));
            }
        }
        for (Set<Long> busNums : new ConnectivityInspector<>(vlGraph).connectedSets()) {
            String voltageLevelId = busesToVoltageLevelId.apply(busNums);
            containersMapping.voltageLevelIdToBusNums.put(voltageLevelId, busNums);
            for (long busNum : busNums) {
                containersMapping.busNumToVoltageLevelId.put(busNum, voltageLevelId);
            }
        }
    }

    // createSubstationMapping to create substation PowSyBl model using data from other sources
    // for example our Bus, Branch and Generator model
    private static void createSubstationMapping(
        List<Branch> branches,
        ToLongFunction<Branch> branchToNum1,
        ToLongFunction<Branch> branchToNum2,
        ToLongFunction<Branch> branchToNum3,
        Predicate<Branch> branchToIsTransformer,
        IntFunction<String> substationNumToId,
        ContainersMapping containersMapping
    ) {
        Graph<String, Object> sGraph = new Pseudograph<>(Object.class);
        for (String voltageLevelId : containersMapping.voltageLevelIdToBusNums.keySet()) {
            sGraph.addVertex(voltageLevelId);
        }
        for (Branch branch : branches) {
            if (branchToIsTransformer.test(branch)) {
                sGraph.addEdge(
                    containersMapping.busNumToVoltageLevelId.get(branchToNum1.applyAsLong(branch)),
                    containersMapping.busNumToVoltageLevelId.get(branchToNum2.applyAsLong(branch))
                );
                // Three winding Tfo
                if (branchToNum3.applyAsLong(branch) != 0) {
                    sGraph.addEdge(
                        containersMapping.busNumToVoltageLevelId.get(branchToNum1.applyAsLong(branch)),
                        containersMapping.busNumToVoltageLevelId.get(branchToNum3.applyAsLong(branch))
                    );
                }
            }
        }
        int substationNum = 1;
        for (Set<String> voltageLevelIds : new ConnectivityInspector<>(sGraph).connectedSets()) {
            String substationId = "sub_" + String.join("_", voltageLevelIds);
            substationId = substationId.replaceAll("vl_", "");
            for (String voltageLevelId : voltageLevelIds) {
                containersMapping.voltageLevelIdToSubstationId.put(voltageLevelId, substationId);
            }
        }
    }
}
