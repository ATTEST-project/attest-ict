package com.attest.ict.helper.matpower.network.writer;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.domain.BusName;
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.GenTag;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.domain.Transformer;
import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.helper.matpower.common.util.structure.MatpowerFileStruct;
import com.attest.ict.helper.matpower.common.util.structure.MpcBaseElement;
import com.attest.ict.helper.matpower.common.util.structure.MpcElement;
import com.attest.ict.helper.matpower.network.util.MatpowerAttributesTemplate;
import com.attest.ict.helper.matpower.network.util.MatpowerNetworkSection;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatpowerNetworkWriter {

    public static final Logger LOG = LoggerFactory.getLogger(MatpowerNetworkWriter.class);

    private MatpowerNetworkWriter() {}

    public static MatpowerFileStruct generateMatpowerStructure(MatpowerModel model) {
        MatpowerFileStruct struct = new MatpowerFileStruct(model.getCaseName());

        MpcBaseElement version = new MpcBaseElement(MatpowerNetworkSection.VERSION);
        version.setSimpleContent("'" + model.getVersion() + "'");
        struct.getMpcElements().add(version);

        MpcBaseElement baseMVA = new MpcBaseElement(MatpowerNetworkSection.BASE_MVA);
        baseMVA.setSimpleContent("" + model.getBaseMva());
        struct.getMpcElements().add(baseMVA);

        if (model.getvLevels() != null) {
            MpcElement vLevels = new MpcElement(MatpowerNetworkSection.V_LEVELS);
            vLevels.addContent(convertMpcVLevels(model.getvLevels()));
            struct.getMpcElements().add(vLevels);
        }

        MpcElement bus = new MpcElement(MatpowerNetworkSection.BUS);
        if (!model.getBusExtensions().isEmpty()) {
            List<String> newComments = new ArrayList<>();
            newComments.add("%% bus data");
            newComments.add("% " + String.join("\t", MatpowerAttributesTemplate.BUS_TOTAL));
            bus.setComments(newComments);
            bus.addContent(convertMpcBus(model.getBuses(), model.getBusExtensions()));
        } else {
            bus.addContent(convertMpcBus(model.getBuses()));
        }
        struct.getMpcElements().add(bus);

        if (!model.getCaps().isEmpty()) {
            MpcElement caps = new MpcElement(MatpowerNetworkSection.CAP_BANK_DATA);
            caps.addContent(convertMpcCapBankData(model.getCaps()));
            struct.getMpcElements().add(caps);
        }

        MpcElement generator = new MpcElement(MatpowerNetworkSection.GENERATOR);
        if (!model.getGeneratorExtensions().isEmpty()) {
            List<String> newComments = new ArrayList<>();
            newComments.add("%% generator data");
            newComments.add("% " + String.join("\t", MatpowerAttributesTemplate.GENERATOR_TOTAL));
            generator.setComments(newComments);
            generator.addContent(convertMpcGenerator(model.getGenerators(), model.getGeneratorExtensions()));
        } else {
            generator.addContent(convertMpcGenerator(model.getGenerators()));
        }
        struct.getMpcElements().add(generator);

        MpcElement branch = new MpcElement(MatpowerNetworkSection.BRANCH);
        if (!model.getBranchExtensions().isEmpty()) {
            List<String> newComments = new ArrayList<>();
            newComments.add("%% branch data");
            newComments.add("% " + String.join("\t", MatpowerAttributesTemplate.BRANCH_TOTAL));
            branch.setComments(newComments);
            branch.addContent(convertMpcBranch(model.getBranches(), model.getBranchExtensions()));
        } else {
            branch.addContent(convertMpcBranch(model.getBranches()));
        }
        struct.getMpcElements().add(branch);

        if (!model.getGenCosts().isEmpty()) {
            MpcElement genCosts = new MpcElement(MatpowerNetworkSection.GEN_COST);
            genCosts.addContent(convertMpcGenCosts(model.getGenCosts()));
            struct.getMpcElements().add(genCosts);
        }

        if (!model.getGenTags().isEmpty()) {
            MpcElement genTags = new MpcElement(MatpowerNetworkSection.GEN_TAGS);
            genTags.addContent(convertMpcGenTags(model.getGenTags()));
            struct.getMpcElements().add(genTags);
        }

        if (!model.getBusNames().isEmpty()) {
            MpcElement busNames = new MpcElement(MatpowerNetworkSection.BUS_NAMES);
            busNames.addContent(convertMpcBusNames(model.getBusNames()));
            struct.getMpcElements().add(busNames);
        }

        if (!model.getTransformers().isEmpty()) {
            MpcElement transformers = new MpcElement(MatpowerNetworkSection.TRANSFORMER);
            transformers.addContent(convertMpcTransformers(model.getTransformers()));
            struct.getMpcElements().add(transformers);
        }

        if (!model.getBusCoordinates().isEmpty()) {
            MpcElement busCoordinates = new MpcElement(MatpowerNetworkSection.BUS_COORDINATES);
            busCoordinates.addContent(convertMpcCoordinates(model.getBusCoordinates()));
            struct.getMpcElements().add(busCoordinates);
        }

        return struct;
    }

    private static List<String> convertMpcVLevels(VoltageLevel vLevels) {
        List<String> vLevelsList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        if (vLevels.getv1() != null) builder.append("\t").append(vLevels.getv1());

        if (vLevels.getv2() != null) builder.append("\t").append(vLevels.getv2());

        if (vLevels.getv3() != null && vLevels.getv3() != 0.0) {
            builder.append("\t").append(vLevels.getv3());
        }
        builder.append("\n");
        vLevelsList.add(builder.toString());

        return vLevelsList;
    }

    private static List<String> convertMpcBus(List<Bus> buses) {
        List<String> busesList = new ArrayList<>();
        for (Bus bus : buses) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(bus.getBusNum())
                .append("\t")
                .append(bus.getType())
                .append("\t")
                .append(bus.getActivePower())
                .append("\t")
                .append(bus.getReactivePower())
                .append("\t")
                .append(bus.getConductance())
                .append("\t")
                .append(bus.getSusceptance())
                .append("\t")
                .append(bus.getArea())
                .append("\t")
                .append(bus.getVm())
                .append("\t")
                .append(bus.getVa())
                .append("\t")
                .append(bus.getBaseKv())
                .append("\t")
                .append(bus.getZone())
                .append("\t")
                .append(bus.getVmax())
                .append("\t")
                .append(bus.getVmin())
                .append(";\n");
            busesList.add(builder.toString());
        }
        return busesList;
    }

    private static List<String> convertMpcBus(List<Bus> buses, List<BusExtension> busExtensions) {
        List<String> busesList = new ArrayList<>();
        for (int i = 0; i < buses.size(); ++i) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(buses.get(i).getBusNum())
                .append("\t")
                .append(buses.get(i).getType())
                .append("\t")
                .append(buses.get(i).getActivePower())
                .append("\t")
                .append(buses.get(i).getReactivePower())
                .append("\t")
                .append(buses.get(i).getConductance())
                .append("\t")
                .append(buses.get(i).getSusceptance())
                .append("\t")
                .append(buses.get(i).getArea())
                .append("\t")
                .append(buses.get(i).getVm())
                .append("\t")
                .append(buses.get(i).getVa())
                .append("\t")
                .append(buses.get(i).getBaseKv())
                .append("\t")
                .append(buses.get(i).getZone())
                .append("\t")
                .append(buses.get(i).getVmax())
                .append("\t")
                .append(buses.get(i).getVmin())
                // bus extension values
                .append("\t")
                .append(busExtensions.get(i).getHasGen())
                .append("\t")
                .append(busExtensions.get(i).getIsLoad())
                .append("\t")
                .append(busExtensions.get(i).getSnomMva())
                .append("\t")
                .append(busExtensions.get(i).getSx())
                .append("\t")
                .append(busExtensions.get(i).getSy())
                .append("\t")
                .append(busExtensions.get(i).getGx())
                .append("\t")
                .append(busExtensions.get(i).getGy())
                .append("\t")
                .append(busExtensions.get(i).getStatus())
                .append("\t")
                .append(busExtensions.get(i).getIncrementCost())
                .append("\t")
                .append(busExtensions.get(i).getDecrementCost())
                .append(";\n");
            busesList.add(builder.toString());
        }
        return busesList;
    }

    private static List<String> convertMpcGenerator(List<Generator> generators) {
        List<String> generatorsList = new ArrayList<>();
        for (Generator generator : generators) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(generator.getBusNum())
                .append("\t")
                .append(generator.getPg())
                .append("\t")
                .append(generator.getQg())
                .append("\t")
                .append(generator.getQmax())
                .append("\t")
                .append(generator.getQmin())
                .append("\t")
                .append(generator.getVg())
                .append("\t")
                .append(generator.getmBase())
                .append("\t")
                .append(generator.getStatus())
                .append("\t")
                .append(generator.getPmax())
                .append("\t")
                .append(generator.getPmin())
                .append("\t")
                .append(generator.getPc1())
                .append("\t")
                .append(generator.getPc2())
                .append("\t")
                .append(generator.getQc1min())
                .append("\t")
                .append(generator.getQc1max())
                .append("\t")
                .append(generator.getQc2min())
                .append("\t")
                .append(generator.getQc2max())
                .append("\t")
                .append(generator.getRampAgc())
                .append("\t")
                .append(generator.getRamp10())
                .append("\t")
                .append(generator.getRamp30())
                .append("\t")
                .append(generator.getRampQ())
                .append("\t")
                .append(generator.getApf())
                .append(";\n");
            generatorsList.add(builder.toString());
        }
        return generatorsList;
    }

    private static List<String> convertMpcGenerator(List<Generator> generators, List<GeneratorExtension> generatorExtensions) {
        List<String> generatorsList = new ArrayList<>();
        for (int i = 0; i < generators.size(); ++i) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(generators.get(i).getBusNum())
                .append("\t")
                .append(generators.get(i).getPg())
                .append("\t")
                .append(generators.get(i).getQg())
                .append("\t")
                .append(generators.get(i).getQmax())
                .append("\t")
                .append(generators.get(i).getQmin())
                .append("\t")
                .append(generators.get(i).getVg())
                .append("\t")
                .append(generators.get(i).getmBase())
                .append("\t")
                .append(generators.get(i).getStatus())
                .append("\t")
                .append(generators.get(i).getPmax())
                .append("\t")
                .append(generators.get(i).getPmin())
                .append("\t")
                .append(generators.get(i).getPc1())
                .append("\t")
                .append(generators.get(i).getPc2())
                .append("\t")
                .append(generators.get(i).getQc1min())
                .append("\t")
                .append(generators.get(i).getQc1max())
                .append("\t")
                .append(generators.get(i).getQc2min())
                .append("\t")
                .append(generators.get(i).getQc2max())
                .append("\t")
                .append(generators.get(i).getRampAgc())
                .append("\t")
                .append(generators.get(i).getRamp10())
                .append("\t")
                .append(generators.get(i).getRamp30())
                .append("\t")
                .append(generators.get(i).getRampQ())
                .append("\t")
                .append(generators.get(i).getApf())
                .append("\t")
                .append(generatorExtensions.get(i).getId())
                .append("\t")
                .append(generatorExtensions.get(i).getStatusCurt())
                .append("\t")
                .append(generatorExtensions.get(i).getDgType())
                .append(";\n");
            generatorsList.add(builder.toString());
        }
        return generatorsList;
    }

    private static List<String> convertMpcBranch(List<Branch> branches) {
        List<String> branchesList = new ArrayList<>();
        for (Branch branch : branches) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(branch.getFbus())
                .append("\t")
                .append(branch.getTbus())
                .append("\t")
                .append(branch.getR())
                .append("\t")
                .append(branch.getX())
                .append("\t")
                .append(branch.getB())
                .append("\t")
                .append(branch.getRatea())
                .append("\t")
                .append(branch.getRateb())
                .append("\t")
                .append(branch.getRatec())
                .append("\t")
                .append(branch.getTapRatio())
                .append("\t")
                .append(branch.getAngle())
                .append("\t")
                .append(branch.getStatus())
                .append("\t")
                .append(branch.getAngmin())
                .append("\t")
                .append(branch.getAngmax())
                .append(";\n");
            branchesList.add(builder.toString());
        }
        return branchesList;
    }

    private static List<String> convertMpcBranch(List<Branch> branches, List<BranchExtension> branchExtensions) {
        List<String> branchesList = new ArrayList<>();
        for (int i = 0; i < branches.size(); ++i) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(branches.get(i).getFbus())
                .append("\t")
                .append(branches.get(i).getTbus())
                .append("\t")
                .append(branches.get(i).getR())
                .append("\t")
                .append(branches.get(i).getX())
                .append("\t")
                .append(branches.get(i).getB())
                .append("\t")
                .append(branches.get(i).getRatea())
                .append("\t")
                .append(branches.get(i).getRateb())
                .append("\t")
                .append(branches.get(i).getRatec())
                .append("\t")
                .append(branches.get(i).getTapRatio())
                .append("\t")
                .append(branches.get(i).getAngle())
                .append("\t")
                .append(branches.get(i).getStatus())
                .append("\t")
                .append(branches.get(i).getAngmin())
                .append("\t")
                .append(branches.get(i).getAngmax())
                .append("\t")
                .append(branchExtensions.get(i).getStepSize())
                .append("\t")
                .append(branchExtensions.get(i).getActTap())
                .append("\t")
                .append(branchExtensions.get(i).getMinTap())
                .append("\t")
                .append(branchExtensions.get(i).getMaxTap())
                .append("\t")
                .append(branchExtensions.get(i).getNormalTap())
                .append("\t")
                .append(branchExtensions.get(i).getNominalRatio())
                .append("\t")
                .append(branchExtensions.get(i).getrIp())
                .append("\t")
                .append(branchExtensions.get(i).getrN())
                .append("\t")
                .append(branchExtensions.get(i).getr0())
                .append("\t")
                .append(branchExtensions.get(i).getx0())
                .append("\t")
                .append(branchExtensions.get(i).getb0())
                .append("\t")
                .append(branchExtensions.get(i).getLength())
                .append("\t")
                .append(branchExtensions.get(i).getNormStat())
                .append("\t")
                .append(branchExtensions.get(i).getG())
                .append(";\n");
            branchesList.add(builder.toString());
        }
        return branchesList;
    }

    private static List<String> convertMpcCapBankData(List<CapacitorBankData> capacitorBankData) {
        List<String> capacitorBankDataList = new ArrayList<>();
        for (CapacitorBankData cap : capacitorBankData) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(cap.getBusNum())
                .append("\t'")
                .append(cap.getNodeId())
                .append("'")
                .append("\t'")
                .append(cap.getBankId())
                .append("'")
                .append("\t")
                .append(cap.getQnom())
                .append(";\n");
            capacitorBankDataList.add(builder.toString());
        }
        return capacitorBankDataList;
    }

    private static List<String> convertMpcGenTags(List<GenTag> genTags) {
        List<String> genTagsList = new ArrayList<>();
        for (GenTag genTag : genTags) {
            StringBuilder builder = new StringBuilder();
            builder.append("\t'").append(genTag.getGenTag()).append("';\n");
            genTagsList.add(builder.toString());
        }
        return genTagsList;
    }

    private static List<String> convertMpcBusNames(List<BusName> busNames) {
        List<String> busNamesList = new ArrayList<>();
        for (BusName busName : busNames) {
            StringBuilder builder = new StringBuilder();
            builder.append("\t'").append(busName.getBusName()).append("';\n");
            busNamesList.add(builder.toString());
        }
        return busNamesList;
    }

    private static List<String> convertMpcGenCosts(List<GenCost> genCosts) {
        List<String> genCostList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        /* If gen has ng rows, then the first ng rows of gencost contain the costs for active power produced by the corresponding
            generators. If gencost has 2ng rows, then rows ng + 1 through 2ng contain the reactive power costs in the same  form
            Cit. Matpower Manual page 151  Table B-4 Generator Cost Data
         */

        for (GenCost cost : genCosts) {
            builder.append("\t").append(cost.getModel());
            builder.append("\t").append(cost.getStartup());
            builder.append("\t").append(cost.getShutdown());
            builder.append("\t").append(cost.getnCost());
            builder.append("\t").append(ConverterUtils.convertPipeToTab(cost.getCostPF()));
            builder.append(";\n");
        }

        for (GenCost cost : genCosts) {
            if (cost.getCostQF() != null) {
                builder.append("\t").append(cost.getModel());
                builder.append("\t").append(cost.getStartup());
                builder.append("\t").append(cost.getShutdown());
                builder.append("\t").append(cost.getnCost());
                builder.append("\t").append(ConverterUtils.convertPipeToTab(cost.getCostQF()));
                builder.append(";\n");
            }
        }
        genCostList.add(builder.toString());
        return genCostList;
    }

    private static List<String> convertMpcTransformers(List<Transformer> transformers) {
        List<String> transformersList = new ArrayList<>();
        for (Transformer transformer : transformers) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("\t")
                .append(transformer.getFbus())
                .append("\t")
                .append(transformer.getTbus())
                .append("\t")
                .append(transformer.getMin())
                .append("\t")
                .append(transformer.getMax())
                .append("\t")
                .append(transformer.getTotalTaps())
                .append("\t")
                .append(transformer.getTap())
                .append(";\n");
            transformersList.add(builder.toString());
        }
        return transformersList;
    }

    private static List<String> convertMpcCoordinates(List<BusCoordinate> busCoordinates) {
        List<String> coordinatesList = new ArrayList<>();
        for (BusCoordinate bc : busCoordinates) {
            StringBuilder builder = new StringBuilder();
            builder.append("\t").append(bc.getX()).append("\t").append(bc.getY()).append(";\n");
            coordinatesList.add(builder.toString());
        }
        return coordinatesList;
    }
}
