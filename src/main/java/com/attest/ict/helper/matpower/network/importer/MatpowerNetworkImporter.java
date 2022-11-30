package com.attest.ict.helper.matpower.network.importer;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.*;
import com.attest.ict.repository.NetworkRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatpowerNetworkImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatpowerNetworkImporter.class);

    static NetworkRepository networkRepository;

    // to call repository in a static function
    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initStaticNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    private MatpowerNetworkImporter() {}

    private static Network findNetwork(String networkName, Long networkId) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }
        return network;
    }

    /* createBaseMVA method
     * return a BaseMVA (made up of value and networkId)
     */
    public static BaseMVA createBaseMVA(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        BaseMVA baseMVA = new BaseMVA();
        baseMVA.setBaseMva(model.getBaseMva());
        baseMVA.setNetwork(network);

        return baseMVA;
    }

    /* createVLevels method
     * return a VLevels (made up of three values and networkId)
     */
    public static VoltageLevel createVLevels(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        VoltageLevel vLevels = new VoltageLevel();
        vLevels.setv1(model.getvLevels().getv1());
        vLevels.setv2(model.getvLevels().getv2());
        vLevels.setv3(model.getvLevels().getv3());
        vLevels.setNetwork(network);

        return vLevels;
    }

    /* createBuses method
     * return a list of buses
     * data is taken from MatpowerModel
     *
     *
     */
    public static List<Bus> createBuses(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        List<Bus> buses = new ArrayList<>();
        for (int i = 0; i < model.getBuses().size(); ++i) {
            Bus bus1 = new Bus();
            bus1.setBusNum(model.getBuses().get(i).getBusNum());
            bus1.setType(model.getBuses().get(i).getType());
            bus1.setActivePower(model.getBuses().get(i).getActivePower());
            bus1.setReactivePower(model.getBuses().get(i).getReactivePower());
            bus1.setConductance(model.getBuses().get(i).getConductance());
            bus1.setSusceptance(model.getBuses().get(i).getSusceptance());
            bus1.setArea(model.getBuses().get(i).getArea());
            bus1.setVm(model.getBuses().get(i).getVm());
            bus1.setVa(model.getBuses().get(i).getVa());
            bus1.setBaseKv(model.getBuses().get(i).getBaseKv());
            bus1.setZone(model.getBuses().get(i).getZone());
            bus1.setVmax(model.getBuses().get(i).getVmax());
            bus1.setVmin(model.getBuses().get(i).getVmin());
            bus1.setNetwork(network);
            buses.add(bus1);
        }

        return buses;
    }

    public static List<BusExtension> createBusExtensions(MatpowerModel model, List<Bus> buses) {
        List<BusExtension> busExtensions = new ArrayList<>();
        for (int i = 0; i < model.getBusExtensions().size(); ++i) {
            BusExtension busExtension = new BusExtension();
            busExtension.setHasGen(model.getBusExtensions().get(i).getHasGen());
            busExtension.setIsLoad(model.getBusExtensions().get(i).getIsLoad());
            busExtension.setSnomMva(model.getBusExtensions().get(i).getSnomMva());
            busExtension.setSx(model.getBusExtensions().get(i).getSx());
            busExtension.setSy(model.getBusExtensions().get(i).getSy());
            busExtension.setGx(model.getBusExtensions().get(i).getGx());
            busExtension.setGy(model.getBusExtensions().get(i).getGy());
            busExtension.setStatus(model.getBusExtensions().get(i).getStatus());
            busExtension.setIncrementCost(model.getBusExtensions().get(i).getIncrementCost());
            busExtension.setDecrementCost(model.getBusExtensions().get(i).getDecrementCost());
            busExtension.setBus(buses.get(i));
            busExtensions.add(busExtension);
        }

        return busExtensions;
    }

    /* createBranches method
     * return a list of branches
     * data is taken from MatpowerModel
     */
    public static List<Branch> createBranches(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < model.getBranches().size(); ++i) {
            Branch branch = new Branch();
            branch.setFbus(model.getBranches().get(i).getFbus());
            branch.setTbus(model.getBranches().get(i).getTbus());
            branch.setR(model.getBranches().get(i).getR());
            branch.setX(model.getBranches().get(i).getX());
            branch.setB(model.getBranches().get(i).getB());
            branch.setRatea(model.getBranches().get(i).getRatea());
            branch.setRateb(model.getBranches().get(i).getRateb());
            branch.setRatec(model.getBranches().get(i).getRatec());
            branch.setTapRatio(model.getBranches().get(i).getTapRatio());
            branch.setAngle(model.getBranches().get(i).getAngle());
            branch.setStatus(model.getBranches().get(i).getStatus());
            branch.setAngmin(model.getBranches().get(i).getAngmin());
            branch.setAngmax(model.getBranches().get(i).getAngmax());
            branch.setNetwork(network);
            branches.add(branch);
        }

        return branches;
    }

    public static List<BranchExtension> createBranchExtensions(MatpowerModel model, List<Branch> branches) {
        List<BranchExtension> branchExtensions = new ArrayList<>();
        for (int i = 0; i < model.getBranchExtensions().size(); ++i) {
            BranchExtension branchExtension = new BranchExtension();
            branchExtension.setStepSize(model.getBranchExtensions().get(i).getStepSize());
            branchExtension.setActTap(model.getBranchExtensions().get(i).getActTap());
            branchExtension.setMinTap(model.getBranchExtensions().get(i).getMinTap());
            branchExtension.setMaxTap(model.getBranchExtensions().get(i).getMaxTap());
            branchExtension.setNormalTap(model.getBranchExtensions().get(i).getNormalTap());
            branchExtension.setNominalRatio(model.getBranchExtensions().get(i).getNominalRatio());
            branchExtension.setrIp(model.getBranchExtensions().get(i).getrIp());
            branchExtension.setrN(model.getBranchExtensions().get(i).getrN());
            branchExtension.setr0(model.getBranchExtensions().get(i).getr0());
            branchExtension.setx0(model.getBranchExtensions().get(i).getx0());
            branchExtension.setb0(model.getBranchExtensions().get(i).getb0());
            branchExtension.setLength(model.getBranchExtensions().get(i).getLength());
            branchExtension.setNormStat(model.getBranchExtensions().get(i).getNormStat());
            branchExtension.setG(model.getBranchExtensions().get(i).getG());
            branchExtension.setBranch(branches.get(i));
            branchExtensions.add(branchExtension);
        }
        return branchExtensions;
    }

    /* createGenerators method
     * return a list of generators
     * data is taken from MatpowerModel
     */
    public static List<Generator> createGenerators(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        List<Generator> generators = new ArrayList<>();
        for (int i = 0; i < model.getGenerators().size(); ++i) {
            Generator generator = new Generator();
            generator.setBusNum(model.getGenerators().get(i).getBusNum());
            generator.setPg(model.getGenerators().get(i).getPg());
            generator.setQg(model.getGenerators().get(i).getQg());
            generator.setQmax(model.getGenerators().get(i).getQmax());
            generator.setQmin(model.getGenerators().get(i).getQmin());
            generator.setVg(model.getGenerators().get(i).getVg());
            generator.setmBase(model.getGenerators().get(i).getmBase());
            generator.setStatus(model.getGenerators().get(i).getStatus());
            generator.setPmax(model.getGenerators().get(i).getPmax());
            generator.setPmin(model.getGenerators().get(i).getPmin());
            generator.setPc1(model.getGenerators().get(i).getPc1());
            generator.setPc2(model.getGenerators().get(i).getPc2());
            generator.setQc1min(model.getGenerators().get(i).getQc1min());
            generator.setQc1max(model.getGenerators().get(i).getQc1max());
            generator.setQc2max(model.getGenerators().get(i).getQc2max());
            generator.setQc2min(model.getGenerators().get(i).getQc2min());
            generator.setRampAgc(model.getGenerators().get(i).getRampAgc());
            generator.setRamp10(model.getGenerators().get(i).getRamp10());
            generator.setRamp30(model.getGenerators().get(i).getRamp30());
            generator.setRampQ(model.getGenerators().get(i).getRampQ());
            generator.setApf(model.getGenerators().get(i).getApf());
            generator.setNetwork(network);
            generators.add(generator);
        }

        return generators;
    }

    public static List<GeneratorExtension> createGeneratorExtensions(MatpowerModel model, List<Generator> generators) {
        List<GeneratorExtension> generatorExtensions = new ArrayList<>();
        for (int i = 0; i < model.getGeneratorExtensions().size(); ++i) {
            GeneratorExtension generatorExtension = new GeneratorExtension();
            generatorExtension.setId(model.getGeneratorExtensions().get(i).getId());
            generatorExtension.setStatusCurt(model.getGeneratorExtensions().get(i).getStatusCurt());
            generatorExtension.setDgType(model.getGeneratorExtensions().get(i).getDgType());
            generatorExtension.setGenerator(generators.get(i));
            generatorExtensions.add(generatorExtension);
        }
        return generatorExtensions;
    }

    /* createGenTags method
     * return a list of gen tags (genTag and genId)
     * data is taken from MatpowerModel
     */
    public static List<GenTag> createGenTags(MatpowerModel model, List<Generator> gens) {
        List<GenTag> genTags = new ArrayList<>();
        for (int i = 0; i < model.getGenTags().size(); ++i) {
            GenTag genTag = new GenTag();
            genTag.setGenerator(gens.get(i));
            genTag.setGenTag(model.getGenTags().get(i).getGenTag());
            genTags.add(genTag);
        }

        return genTags;
    }

    /* createBusNames method
     * return a list of bus names (busName and busId)
     * data is taken from MatpowerModel
     */
    public static List<BusName> createBusNames(MatpowerModel model, List<Bus> buses) {
        List<BusName> busNames = new ArrayList<>();
        for (int i = 0; i < model.getBusNames().size(); ++i) {
            BusName busName = new BusName();
            busName.setBus(buses.get(i));
            busName.setBusName(model.getBusNames().get(i).getBusName());
            busNames.add(busName);
        }

        return busNames;
    }

    /* createTransformers method
     * return a list of transformers
     * data is taken from MatpowerModel
     */
    public static List<Transformer> createTransformers(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        List<Transformer> trans = new ArrayList<>();
        for (int i = 0; i < model.getTransformers().size(); ++i) {
            Transformer tran = new Transformer();
            tran.setFbus(model.getTransformers().get(i).getFbus());
            tran.setTbus(model.getTransformers().get(i).getTbus());
            tran.setMin(model.getTransformers().get(i).getMin());
            tran.setMax(model.getTransformers().get(i).getMax());
            tran.setTotalTaps(model.getTransformers().get(i).getTotalTaps());
            tran.setTap(model.getTransformers().get(i).getTap());
            tran.setNetwork(network);
            trans.add(tran);
        }

        return trans;
    }

    /* createBusCoords method
     * return a list of bus coordinates
     * data is taken from MatpowerModel
     */
    public static List<BusCoordinate> createBusCoords(MatpowerModel model, List<Bus> buses) {
        List<BusCoordinate> busCoordinates = new ArrayList<>();
        for (int i = 0; i < model.getBusCoordinates().size(); ++i) {
            BusCoordinate bc = new BusCoordinate();
            bc.setBus(buses.get(i));
            bc.setX(model.getBusCoordinates().get(i).getX());
            bc.setY(model.getBusCoordinates().get(i).getY());
            busCoordinates.add(bc);
        }

        return busCoordinates;
    }

    /* createGenCosts method
     * return a list of gen costs
     * data is taken from MatpowerModel
     */
    public static List<GenCost> createGenCosts(MatpowerModel model, List<Generator> generators) {
        List<GenCost> genCosts = new ArrayList<>();
        for (int i = 0; i < model.getGenCosts().size(); ++i) {
            GenCost genCost = new GenCost();
            genCost.setGenerator(generators.get(i));
            genCost.setModel(model.getGenCosts().get(i).getModel());
            genCost.setStartup(model.getGenCosts().get(i).getStartup());
            genCost.setShutdown(model.getGenCosts().get(i).getShutdown());
            genCost.setnCost(model.getGenCosts().get(i).getnCost());

            genCost.setCostPF(model.getGenCosts().get(i).getCostPF());
            genCost.setCostQF(model.getGenCosts().get(i).getCostQF());

            genCosts.add(genCost);
        }

        return genCosts;
    }

    /* createCapsBankData method
     * return a list of capacitors bank data
     * data is taken from MatpowerModel
     */
    public static List<CapacitorBankData> createCapsBankData(MatpowerModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        List<CapacitorBankData> caps = new ArrayList<>();
        for (int i = 0; i < model.getCaps().size(); ++i) {
            CapacitorBankData cap = new CapacitorBankData();
            cap.setBusNum(model.getCaps().get(i).getBusNum());
            cap.setNodeId(model.getCaps().get(i).getNodeId());
            cap.setBankId(model.getCaps().get(i).getBankId());
            cap.setQnom(model.getCaps().get(i).getQnom());
            cap.setNetwork(network);
            caps.add(cap);
        }

        return caps;
    }
    /* createLoadElVarsP method
     * return a list of load P values
     * data is taken from MatpowerModel
 
    public static List<LoadElVar> createLoadElVarsP(MatpowerModel model, List<Bus> buses) {

        List<LoadElVar> loadElVars = new ArrayList<>();
        for (int i = 0; i < model.getLoadElVarsP().size(); ++i) {
            LoadElVar loadElVar = new LoadElVar();
            loadElVar.setBus(buses.get(i));
            loadElVar.setType("P");
            loadElVar.setT0(model.getLoadElVarsP().get(i).getT0());
            loadElVar.setT1(model.getLoadElVarsP().get(i).getT1());
            loadElVar.setT2(model.getLoadElVarsP().get(i).getT2());
            loadElVar.setT3(model.getLoadElVarsP().get(i).getT3());
            loadElVar.setT4(model.getLoadElVarsP().get(i).getT4());
            loadElVar.setT5(model.getLoadElVarsP().get(i).getT5());
            loadElVar.setT6(model.getLoadElVarsP().get(i).getT6());
            loadElVar.setT7(model.getLoadElVarsP().get(i).getT7());
            loadElVar.setT8(model.getLoadElVarsP().get(i).getT8());
            loadElVar.setT9(model.getLoadElVarsP().get(i).getT9());
            loadElVar.setT10(model.getLoadElVarsP().get(i).getT10());
            loadElVar.setT11(model.getLoadElVarsP().get(i).getT11());
            loadElVar.setT12(model.getLoadElVarsP().get(i).getT12());
            loadElVar.setT13(model.getLoadElVarsP().get(i).getT13());
            loadElVar.setT14(model.getLoadElVarsP().get(i).getT14());
            loadElVar.setT15(model.getLoadElVarsP().get(i).getT15());
            loadElVar.setT16(model.getLoadElVarsP().get(i).getT16());
            loadElVar.setT17(model.getLoadElVarsP().get(i).getT17());
            loadElVar.setT18(model.getLoadElVarsP().get(i).getT18());
            loadElVar.setT19(model.getLoadElVarsP().get(i).getT19());
            loadElVar.setT20(model.getLoadElVarsP().get(i).getT20());
            loadElVar.setT21(model.getLoadElVarsP().get(i).getT21());
            loadElVar.setT22(model.getLoadElVarsP().get(i).getT22());
            loadElVar.setT23(model.getLoadElVarsP().get(i).getT23());
            loadElVars.add(loadElVar);
        }

        return loadElVars;

    }

    public static List<LoadElVal> createLoadElVal(MatpowerModel model, List<Bus> buses, LoadProfile loadProfile) throws Exception {
        List<LoadElVal> loadElValues = new ArrayList<>();
        for (Bus bus : buses) {
            for (int j = 0; j < model.getLoadElVarsP().size(); ++j) {
                LoadElVal loadElVal = new LoadElVal();
                loadElVal.setBusNum(bus.getBusNum());
                loadElVal.setTime(new SimpleDateFormat("HH:mm").parse(j + ":00"));
                loadElVal.setP(model.getLoadElVarsP().get(j).getT0());
                loadElVal.setQ(model.getLoadElVarsQ().get(j).getT0());
                loadElVal.setLoadProfile(loadProfile);
                loadElValues.add(loadElVal);
            }
        }
        return loadElValues;
    }

    /* createLoadElVarsQ method
     * return a list of load Q values
     * data is taken from MatpowerModel
    
    public static List<LoadElVar> createLoadElVarsQ(MatpowerModel model, List<Bus> buses) {

        List<LoadElVar> loadElVars = new ArrayList<>();
        for (int i = 0; i < model.getLoadElVarsQ().size(); ++i) {
            LoadElVar loadElVar = new LoadElVar();
            loadElVar.setBus(buses.get(i));
            loadElVar.setType("Q");
            loadElVar.setT0(model.getLoadElVarsQ().get(i).getT0());
            loadElVar.setT1(model.getLoadElVarsQ().get(i).getT1());
            loadElVar.setT2(model.getLoadElVarsQ().get(i).getT2());
            loadElVar.setT3(model.getLoadElVarsQ().get(i).getT3());
            loadElVar.setT4(model.getLoadElVarsQ().get(i).getT4());
            loadElVar.setT5(model.getLoadElVarsQ().get(i).getT5());
            loadElVar.setT6(model.getLoadElVarsQ().get(i).getT6());
            loadElVar.setT7(model.getLoadElVarsQ().get(i).getT7());
            loadElVar.setT8(model.getLoadElVarsQ().get(i).getT8());
            loadElVar.setT9(model.getLoadElVarsQ().get(i).getT9());
            loadElVar.setT10(model.getLoadElVarsQ().get(i).getT10());
            loadElVar.setT11(model.getLoadElVarsQ().get(i).getT11());
            loadElVar.setT12(model.getLoadElVarsQ().get(i).getT12());
            loadElVar.setT13(model.getLoadElVarsQ().get(i).getT13());
            loadElVar.setT14(model.getLoadElVarsQ().get(i).getT14());
            loadElVar.setT15(model.getLoadElVarsQ().get(i).getT15());
            loadElVar.setT16(model.getLoadElVarsQ().get(i).getT16());
            loadElVar.setT17(model.getLoadElVarsQ().get(i).getT17());
            loadElVar.setT18(model.getLoadElVarsQ().get(i).getT18());
            loadElVar.setT19(model.getLoadElVarsQ().get(i).getT19());
            loadElVar.setT20(model.getLoadElVarsQ().get(i).getT20());
            loadElVar.setT21(model.getLoadElVarsQ().get(i).getT21());
            loadElVar.setT22(model.getLoadElVarsQ().get(i).getT22());
            loadElVar.setT23(model.getLoadElVarsQ().get(i).getT23());
            loadElVars.add(loadElVar);
        }

        return loadElVars;

    }
*/
}
