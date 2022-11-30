package com.attest.ict.custom.diagrams.converter;

import com.attest.ict.custom.diagrams.util.ContainersMapping;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.powsybl.iidm.network.Line;
import com.powsybl.iidm.network.Load;
import com.powsybl.iidm.network.ShuntCompensator;
import com.powsybl.iidm.network.ShuntCompensatorAdder;
import com.powsybl.iidm.network.Substation;
import com.powsybl.iidm.network.TopologyKind;
import com.powsybl.iidm.network.TwoWindingsTransformer;
import com.powsybl.iidm.network.VoltageLevel;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataConverter {

    // prefixes
    private static final String BUS_PREFIX = "bus";
    private static final String GENERATOR_PREFIX = "gen";
    private static final String LINE_PREFIX = "line";
    private static final String LOAD_PREFIX = "load";
    private static final String SHUNT_PREFIX = "shunt";
    private static final String SUBSTATION_PREFIX = "sub";
    private static final String TRANSFORMER_PREFIX = "twt";
    private static final String VOLTAGE_LEVEL_PREFIX = "vl";

    // use static repository
    static BusRepository busRepository;

    @Autowired
    BusRepository busRepository1;

    @PostConstruct
    private void initBusRepository() {
        busRepository = this.busRepository1;
    }

    static GeneratorRepository generatorRepository;

    @Autowired
    GeneratorRepository generatorRepository1;

    @PostConstruct
    private void initGeneratorRepository() {
        generatorRepository = this.generatorRepository1;
    }

    static BranchRepository branchRepository;

    @Autowired
    BranchRepository branchRepository1;

    @PostConstruct
    private void initBranchRepository() {
        branchRepository = this.branchRepository1;
    }

    // PowSyBl network
    static com.powsybl.iidm.network.Network network1;

    private static final class PerUnitContext {

        private final double baseMva; // base apparent power

        private final boolean ignoreBaseMva;

        private PerUnitContext(double baseMva, boolean ignoreBaseMva) {
            this.baseMva = baseMva;
            this.ignoreBaseMva = ignoreBaseMva;
        }

        public boolean isIgnoreBaseMva() {
            return ignoreBaseMva;
        }

        private double getBaseMva() {
            return baseMva;
        }
    }

    // id with prefix
    private static String getId(String prefix, long num) {
        return prefix + "_" + num;
    }

    private static String getId(String prefix, long from, long to) {
        return prefix + "_" + from + "_" + to;
    }

    // create buses from network
    private static void createBuses(Network network, ContainersMapping containersMapping, PerUnitContext perUnitContext) {
        // get all buses from busRepository
        List<Bus> buses = busRepository.findByNetworkId(network.getId());
        // create PowSyBl network with networkId
        String netIdStr = String.valueOf(network.getId());
        network1 = com.powsybl.iidm.network.Network.create(netIdStr, network.getName());

        for (Bus bus : buses) {
            String voltageLevelId = containersMapping.getVoltageLevelId((long) bus.getBusNum());
            String substationId = containersMapping.getSubstationId(voltageLevelId);

            // new network substation
            Substation substation = createSubstation(network, substationId);

            // new substation voltage level
            VoltageLevel voltageLevel = createVoltageLevel(bus, voltageLevelId, substation, network, perUnitContext);

            // buses
            createBus(bus, voltageLevel);

            // load
            createLoad(bus, voltageLevel);

            // shunt compensator
            createShuntCompensator(bus, voltageLevel, perUnitContext);

            // generators
            createGenerators(network, bus, voltageLevel);
        }

        System.out.println(network1);
    }

    // create shunt compensator for buses
    private static void createShuntCompensator(Bus bus, VoltageLevel voltageLevel, PerUnitContext perUnitContext) {
        if (bus.getSusceptance() != 0.0) {
            String busId = getId(BUS_PREFIX, (long) bus.getBusNum());
            String shuntId = getId(SHUNT_PREFIX, (long) bus.getBusNum());

            // fix this variable -> correct
            double zb = voltageLevel.getNominalV() * voltageLevel.getNominalV() / perUnitContext.getBaseMva();

            ShuntCompensatorAdder adder = voltageLevel
                .newShuntCompensator()
                .setId(shuntId)
                .setConnectableBus(busId)
                .setBus(busId)
                .setSectionCount(1);

            adder.newLinearModel().setBPerSection(bus.getSusceptance() / perUnitContext.getBaseMva() / zb).setMaximumSectionCount(1).add();

            ShuntCompensator newShunt = adder.add();
        }
    }

    // create substation of network
    private static Substation createSubstation(Network network, String substationId) {
        Substation substation = network1.getSubstation(substationId);

        if (substation == null) {
            substation = network1.newSubstation().setId(substationId).add();
        }
        return substation;
    }

    // create voltage levels of substation
    private static VoltageLevel createVoltageLevel(
        Bus bus,
        String voltageLevelId,
        Substation substation,
        Network network,
        PerUnitContext perUnitContext
    ) {
        double nominalV = perUnitContext.isIgnoreBaseMva() || bus.getBaseKv().doubleValue() == 0.0 ? 1.0 : bus.getBaseKv();

        VoltageLevel voltageLevel = network1.getVoltageLevel(voltageLevelId);

        if (voltageLevel == null) {
            voltageLevel =
                substation.newVoltageLevel().setId(voltageLevelId).setNominalV(nominalV).setTopologyKind(TopologyKind.BUS_BREAKER).add();
        }

        return voltageLevel;
    }

    // create a PowSyBl Bua from ATTEST Bus
    private static com.powsybl.iidm.network.Bus createBus(Bus bus, VoltageLevel voltageLevel) {
        String busId = getId(BUS_PREFIX, (long) bus.getBusNum());
        com.powsybl.iidm.network.Bus bus1 = voltageLevel.getBusBreakerView().newBus().setId(busId).add();
        bus1.setV(bus.getVm() * voltageLevel.getNominalV()).setAngle(bus.getVa());
        return bus1;
    }

    // create loads
    private static void createLoad(Bus bus, VoltageLevel voltageLevel) {
        if (bus.getActivePower() != 0.0 || bus.getReactivePower() != 0.0) {
            String busId = getId(BUS_PREFIX, (long) bus.getBusNum());
            String loadId = getId(LOAD_PREFIX, (long) bus.getBusNum());

            Load load = voltageLevel
                .newLoad()
                .setId(loadId)
                .setConnectableBus(busId)
                .setBus(busId)
                .setP0(bus.getActivePower())
                .setQ0(bus.getReactivePower())
                .add();
        }
    }

    // create generators
    private static void createGenerators(Network network, Bus bus, VoltageLevel voltageLevel) {
        List<Generator> generators = generatorRepository.findByNetworkIdOrderByIdAsc(network.getId());

        for (Generator generator : generators) {
            if (generator.getBusNum().equals(bus.getBusNum())) {
                String busId = getId(BUS_PREFIX, (long) bus.getBusNum());
                String genId = getId(GENERATOR_PREFIX, (long) generator.getId());

                com.powsybl.iidm.network.Generator generator1 = voltageLevel
                    .newGenerator()
                    .setId(genId)
                    .setEnsureIdUnicity(true)
                    .setConnectableBus(busId)
                    .setBus(isInService(generator) ? busId : null)
                    .setTargetV(generator.getVg() * voltageLevel.getNominalV())
                    .setTargetP(generator.getPg())
                    .setTargetQ(generator.getQg())
                    .setVoltageRegulatorOn(generator.getVg() != 0.0)
                    .setMaxP(generator.getPmax())
                    .setMinP(generator.getPmin())
                    .add();

                if (generator.getPc1() != 0 || generator.getPc2() != 0) {
                    generator1
                        .newReactiveCapabilityCurve()
                        .beginPoint()
                        .setP(generator.getPc1())
                        .setMaxQ(generator.getQc1max())
                        .setMinQ(generator.getQc1min())
                        .endPoint()
                        .beginPoint()
                        .setP(generator.getPc2())
                        .setMaxQ(generator.getQc2max())
                        .setMinQ(generator.getQc1min())
                        .endPoint()
                        .add();
                } else {
                    generator1.newMinMaxReactiveLimits().setMinQ(generator.getPmin()).setMaxQ(generator.getPmax()).add();
                }
            }
        }
    }

    // check generator status
    private static boolean isInService(Generator generator) {
        return generator.getStatus() > 0;
    }

    // check branch status
    private static boolean isInService(Branch branch) {
        return branch.getStatus() > 0;
    }

    private static boolean isLine(Network network, Branch branch) {
        if (Double.valueOf(0).equals(branch.getTapRatio())) {
            return true;
        }
        Bus from = busRepository.findByBusNumAndNetworkId(branch.getFbus(), network.getId());
        Bus to = busRepository.findByBusNumAndNetworkId(branch.getTbus(), network.getId());
        return Double.valueOf(1).equals(branch.getTapRatio()) && from.getBaseKv().equals(to.getBaseKv());
    }

    // check if it is a transformer
    private static boolean isTransformer(Network network, Branch branch) {
        return !isLine(network, branch);
    }

    // create branches from network
    private static void createBranches(Network network, ContainersMapping containersMapping, PerUnitContext perUnitContext) {
        List<Branch> branches = branchRepository.findByNetworkId(network.getId());

        for (Branch branch : branches) {
            String bus1Id = getId(BUS_PREFIX, branch.getFbus());
            String bus2Id = getId(BUS_PREFIX, branch.getTbus());

            String voltageLevel1Id = containersMapping.getVoltageLevelId((long) branch.getFbus());
            String voltageLevel2Id = containersMapping.getVoltageLevelId((long) branch.getTbus());

            VoltageLevel voltageLevel1 = network1.getVoltageLevel(voltageLevel1Id);
            VoltageLevel voltageLevel2 = network1.getVoltageLevel(voltageLevel2Id);

            // fix this variable -> correct
            double zb = voltageLevel2.getNominalV() * voltageLevel2.getNominalV() / perUnitContext.getBaseMva();

            boolean isInService = isInService(branch);
            String connectedBus1 = isInService ? bus1Id : null;
            String connectedBus2 = isInService ? bus2Id : null;

            if (isTransformer(network, branch)) {
                TwoWindingsTransformer newTwt = voltageLevel2
                    .getSubstation()
                    .newTwoWindingsTransformer()
                    .setId(getId(TRANSFORMER_PREFIX, branch.getFbus(), branch.getTbus()))
                    .setEnsureIdUnicity(true)
                    .setBus1(connectedBus1)
                    .setConnectableBus1(bus1Id)
                    .setVoltageLevel1(voltageLevel1Id)
                    .setBus2(connectedBus2)
                    .setConnectableBus2(bus2Id)
                    .setVoltageLevel2(voltageLevel2Id)
                    .setRatedU1(voltageLevel1.getNominalV() * branch.getTapRatio())
                    .setRatedU2(voltageLevel2.getNominalV())
                    .setR(branch.getR() * zb)
                    .setX(branch.getX() * zb)
                    .setG(0)
                    .setB(branch.getB() / zb)
                    .add();
            } else {
                Line newLine = network1
                    .newLine()
                    .setId(getId(LINE_PREFIX, branch.getFbus(), branch.getTbus()))
                    .setEnsureIdUnicity(true)
                    .setBus1(connectedBus1)
                    .setConnectableBus1(bus1Id)
                    .setVoltageLevel1(voltageLevel1Id)
                    .setBus2(connectedBus2)
                    .setConnectableBus2(bus2Id)
                    .setVoltageLevel2(voltageLevel2Id)
                    .setR(branch.getR() * zb)
                    .setX(branch.getX() * zb)
                    .setG1(0)
                    .setB1(branch.getB() / zb / 2)
                    .setG2(0)
                    .setB2(branch.getB() / zb / 2)
                    .add();
            }
        }
    }

    // create a PowSyBl Network from ATTEST network
    public static com.powsybl.iidm.network.Network createNetwork(Network network) {
        List<Bus> buses = busRepository.findByNetworkId(network.getId());
        List<Branch> branches = branchRepository.findByNetworkId(network.getId());

        ContainersMapping containersMapping = ContainersMapping.create(
            buses,
            branches,
            Bus::getBusNum,
            branch -> (long) branch.getFbus(),
            branch -> (long) branch.getTbus(),
            branch -> 0,
            Branch::getR,
            Branch::getX,
            branch -> isTransformer(network, branch),
            busNums -> getId(VOLTAGE_LEVEL_PREFIX, busNums.iterator().next()),
            substationNum -> getId(SUBSTATION_PREFIX, substationNum)
        );

        Double baseMva = (network.getBaseMVA().getBaseMva().doubleValue() > 0.0) ? network.getBaseMVA().getBaseMva() : 1.0;
        boolean ignoreBaseMva = baseMva.doubleValue() == 1.0;

        PerUnitContext perUnitContext = new PerUnitContext(baseMva, ignoreBaseMva);

        createBuses(network, containersMapping, perUnitContext);

        createBranches(network, containersMapping, perUnitContext);

        return network1;
    }
}
