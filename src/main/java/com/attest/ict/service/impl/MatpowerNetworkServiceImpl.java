package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.*;
import com.attest.ict.helper.matpower.common.util.structure.MatpowerFileStruct;
import com.attest.ict.helper.matpower.exception.MatpowerReaderFileException;
import com.attest.ict.helper.matpower.exception.MatpowerWriterFileException;
import com.attest.ict.helper.matpower.network.reader.MatpowerNetworkReader;
import com.attest.ict.helper.matpower.network.writer.MatpowerNetworkWriter;
import com.attest.ict.repository.*;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.MatpowerNetworkService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MatpowerNetworkServiceImpl implements MatpowerNetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatpowerNetworkServiceImpl.class);

    @Autowired
    NetworkService networkService;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    BaseMVARepository baseMVARepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    @Autowired
    BusNameRepository busNameRepository;

    @Autowired
    GenTagRepository genTagRepository;

    @Autowired
    TransformerRepository transformerRepository;

    @Autowired
    BusCoordinateRepository busCoordinatesRepository;

    @Autowired
    GenCostRepository genCostRepository;

    @Autowired
    CapacitorBankDataRepository capacitorBankDataRepository;

    @Autowired
    VoltageLevelRepository vLevelsRepository;

    @Autowired
    BusExtensionRepository busExtensionRepository;

    @Autowired
    BranchExtensionRepository branchExtensionRepository;

    @Autowired
    GeneratorExtensionRepository generatorExtensionRepository;

    @Autowired
    InputFileService inputFileInputServiceImpl;

    @Autowired
    NetworkMapper networkMapper;

    @Override
    public InputStream exportToMatpowerFile(String networkName) throws MatpowerWriterFileException {
        Optional<Network> networkOpt = networkRepository.findByName(networkName);
        if (!networkOpt.isPresent()) throw new MatpowerWriterFileException(
            "Unable to export network data to '.m'  file, Network:  '" + networkName + "' not found"
        );

        LOGGER.info("Export Network: {}  START ...", networkName);
        try {
            // get networkId from network
            Long networkId = networkOpt.get().getId();

            // base MVA
            BaseMVA baseMVA = baseMVARepository.findByNetworkId(networkId);

            // Voltage Levels
            VoltageLevel vLevels = vLevelsRepository.findByNetworkId(networkId);

            // buses
            List<Bus> buses = busRepository.getBusesByNetworkId(networkId);

            // buses extension
            List<BusExtension> busExtensions = busExtensionRepository.getBusExtensionsByNetworkId(networkId);

            // branches
            List<Branch> branches = branchRepository.findByNetworkId(networkId);

            // branch extension
            List<BranchExtension> branchExtensions = branchExtensionRepository.getBranchExtensionsByNetworkId(networkId);

            // generators
            List<Generator> generators = generatorRepository.findByNetworkIdOrderByIdAsc(networkId);

            // generator extension
            List<GeneratorExtension> generatorExtensions = generatorExtensionRepository.getGeneratorExtensionsByNetworkId(networkId);

            // gen tags
            List<GenTag> genTags = genTagRepository.getGenTagsByNetworkId(networkId);

            // capacitor bank data
            List<CapacitorBankData> caps = capacitorBankDataRepository.findByNetworkId(networkId);

            // gen costs
            List<GenCost> genCosts = genCostRepository.getGenCostsByNetworkId(networkId);

            // transformers
            List<Transformer> transformers = transformerRepository.findByNetworkId(networkId);

            // bus names
            List<BusName> busNames = busNameRepository.getBusNamesByNetworkId(networkId);

            // bus coordinates
            List<BusCoordinate> busCoordinates = busCoordinatesRepository.findByNetworkId(networkId);

            MatpowerModel model = new MatpowerModel(networkOpt.get().getMpcName());
            model.setVersion("2");
            if (baseMVA != null) {
                model.setBaseMva(baseMVA.getBaseMva());
            }
            model.setvLevels(vLevels);
            model.getCaps().addAll(caps);
            model.getBuses().addAll(buses);
            model.getBusExtensions().addAll(busExtensions);
            model.getBusNames().addAll(busNames);
            model.getBranches().addAll(branches);
            model.getBranchExtensions().addAll(branchExtensions);
            model.getGenerators().addAll(generators);
            model.getGeneratorExtensions().addAll(generatorExtensions);
            model.getGenTags().addAll(genTags);
            model.getGenCosts().addAll(genCosts);
            model.getBusCoordinates().addAll(busCoordinates);
            model.getTransformers().addAll(transformers);

            MatpowerFileStruct struct = MatpowerNetworkWriter.generateMatpowerStructure(model);

            LOGGER.info("Export Network: {}  END ...", networkName);
            return new ByteArrayInputStream(struct.toString().getBytes());
        } catch (Exception ex) {
            String msg = "Failure exporting Network into matpower file format! ";
            LOGGER.error(msg, ex.getMessage());
            throw new MatpowerWriterFileException(msg, ex);
        }
    }

    @Override
    public void importFromMatpowerFile(MultipartFile mpFile, Long networkId) throws IOException, MatpowerReaderFileException {
        try {
            Optional<Network> networkOpt = networkService.findById(networkId);
            if (!networkOpt.isPresent()) {
                String msg = "Network with id: " + networkId + " not found!";
                LOGGER.error(msg);
                throw new MatpowerReaderFileException(msg);
            }

            Network network = networkOpt.get();
            LOGGER.info("Import Network from file: {}, {} START ...", network.getName(), mpFile.getOriginalFilename());

            MatpowerModel model = MatpowerNetworkReader.read(mpFile.getInputStream());

            LOGGER.debug("MatpowerModel: {}", model.toString());

            Network networkSaved = null;
            if (model.getCaseName() != null) {
                network.setMpcName(model.getCaseName());
                Optional<NetworkDTO> networkSavedOpt = networkService.partialUpdate(networkMapper.toDto(network));
                networkSaved = networkMapper.toEntity(networkSavedOpt.get());
                LOGGER.info("Network: {} updated. ", networkSaved.toString());
            }

            if (networkSaved == null) {
                networkSaved = network;
            }

            // -- store baseMVA
            if (model.getBaseMva() != null) {
                BaseMVA baseMVA = this.createBaseMVA(model, networkSaved);
                BaseMVA newBaseMva = baseMVARepository.save(baseMVA);
                LOGGER.info("BaseMVA: {} saved! ", newBaseMva.toString());
            } else {
                LOGGER.warn("BaseMVA is empty, should not be! ");
            }

            // -- store VoltageLevel
            if (model.getvLevels() != null) {
                VoltageLevel vLevels = this.createVLevels(model, networkSaved);
                VoltageLevel newVoltageLevel = vLevelsRepository.save(vLevels);
                LOGGER.info("VLevels: {} saved! ", newVoltageLevel.toString());
            } else {
                LOGGER.info("VoltageLevel is empty ");
            }

            // -- store buses
            if (model.getBuses().isEmpty()) {
                String message = "Bus is empty!";
                LOGGER.info(message);
                throw new MatpowerReaderFileException(message);
            }

            List<Bus> buses = this.createBuses(model, network);
            List<Bus> newBuses = busRepository.saveAll(buses);
            LOGGER.info("Num Buses: {} saved! ", newBuses.size());

            // -- store branches
            if (model.getBranches().isEmpty()) {
                String message = "Branch is empty!";
                LOGGER.info(message);
                throw new MatpowerReaderFileException(message);
            }
            List<Branch> branches = this.createBranches(model, networkSaved);
            List<Branch> newBranches = branchRepository.saveAll(branches);
            LOGGER.info("Num branches: {} saved! ", newBranches.size());

            List<Generator> generators = this.createGenerators(model, networkSaved);
            if (!model.getGenerators().isEmpty()) {
                List<Generator> newGenerators = generatorRepository.saveAll(generators);
                LOGGER.info("Num generators: {} saved! ", newGenerators.size());
            }

            if (!model.getBusExtensions().isEmpty()) {
                List<BusExtension> busExtensions = this.createBusExtensions(model, buses);
                List<BusExtension> newBusExtensions = busExtensionRepository.saveAll(busExtensions);
                LOGGER.info("Num BusExtension {} saved! ", newBusExtensions.size());
            }

            if (!model.getBranchExtensions().isEmpty()) {
                List<BranchExtension> branchExtensions = this.createBranchExtensions(model, branches);
                List<BranchExtension> newBranchExtensions = branchExtensionRepository.saveAll(branchExtensions);
                LOGGER.info("Num BranchExtension {} saved! ", newBranchExtensions.size());
            }

            if (!model.getGeneratorExtensions().isEmpty()) {
                List<GeneratorExtension> generatorExtensions = this.createGeneratorExtensions(model, generators);
                List<GeneratorExtension> newGeneratorExtensions = generatorExtensionRepository.saveAll(generatorExtensions);
                LOGGER.info("Num GeneratorExtension {} saved! ", newGeneratorExtensions.size());
            }

            if (!model.getBusNames().isEmpty()) {
                List<BusName> busNames = this.createBusNames(model, buses);
                List<BusName> newBusNames = busNameRepository.saveAll(busNames);
                LOGGER.info("Num BusName {} saved! ", newBusNames.size());
            }

            if (!model.getGenTags().isEmpty()) {
                List<GenTag> genTags = this.createGenTags(model, generators);
                List<GenTag> newGenTags = genTagRepository.saveAll(genTags);
                LOGGER.info("Num GenTag {} saved! ", newGenTags.size());
            }

            if (!model.getTransformers().isEmpty()) {
                List<Transformer> trans = this.createTransformers(model, networkSaved);
                List<Transformer> newTrans = transformerRepository.saveAll(trans);
                LOGGER.info("Num Transformer {} saved! ", newTrans.size());
            }

            if (!model.getBusCoordinates().isEmpty()) {
                List<BusCoordinate> coords = this.createBusCoords(model, buses);
                List<BusCoordinate> newCoords = busCoordinatesRepository.saveAll(coords);
                LOGGER.info("Num BusCoordinate {} saved! ", newCoords.size());
            }

            if (!model.getGenCosts().isEmpty()) {
                List<GenCost> genCosts = this.createGenCosts(model, generators);
                List<GenCost> newGenCosts = genCostRepository.saveAll(genCosts);
                LOGGER.info("Num GenCost {} saved! ", newGenCosts.size());
            }

            if (!model.getCaps().isEmpty()) {
                List<CapacitorBankData> caps = this.createCapsBankData(model, networkSaved);
                List<CapacitorBankData> newCaps = capacitorBankDataRepository.saveAll(caps);
                LOGGER.info("Num CapacitorBankData {} caps! ", newCaps.size());
            }

            InputFileDTO inputFileSavedDTO = inputFileInputServiceImpl.saveFileForNetworkWithDescr(
                mpFile,
                networkMapper.toDto(networkSaved),
                AttestConstants.INPUT_FILE_NETWORK_DESCR
            );
            LOGGER.info("InputFileSavedDTO saved: {} ", inputFileSavedDTO);

            LOGGER.info("Import Network from file: {}  END ...", mpFile.getOriginalFilename());
        } catch (Exception e) {
            String msg = "Import Network from matpowerFile failure! ";
            LOGGER.error(msg, e.getMessage());
            throw new MatpowerReaderFileException(msg, e);
        }
    }

    private BaseMVA createBaseMVA(MatpowerModel model, Network network) {
        BaseMVA baseMVA = new BaseMVA();
        baseMVA.setBaseMva(model.getBaseMva());
        baseMVA.setNetwork(network);
        LOGGER.info("Return BaseMVA:  {}! ", baseMVA.toString());
        return baseMVA;
    }

    private VoltageLevel createVLevels(MatpowerModel model, Network network) {
        VoltageLevel vLevels = new VoltageLevel();
        vLevels.setv1(model.getvLevels().getv1());
        vLevels.setv2(model.getvLevels().getv2());
        vLevels.setv3(model.getvLevels().getv3());
        vLevels.setNetwork(network);
        LOGGER.info("Return VoltageLevel:  {}! ", vLevels.toString());
        return vLevels;
    }

    private List<Bus> createBuses(MatpowerModel model, Network network) {
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
        LOGGER.info("Return list of buses of size: {}! ", buses.size());
        return buses;
    }

    private List<BusExtension> createBusExtensions(MatpowerModel model, List<Bus> buses) {
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
        LOGGER.info("Return list of busExtensions of size: {}! ", busExtensions.size());
        return busExtensions;
    }

    private List<Branch> createBranches(MatpowerModel model, Network network) {
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
        LOGGER.info("Return list of branches of size: {}! ", branches.size());
        return branches;
    }

    private List<BranchExtension> createBranchExtensions(MatpowerModel model, List<Branch> branches) {
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
        LOGGER.info("Return list of branchExtensions of size: {}! ", branchExtensions.size());
        return branchExtensions;
    }

    private List<Generator> createGenerators(MatpowerModel model, Network network) {
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
        LOGGER.info("Return list of generators of size: {}! ", generators.size());
        return generators;
    }

    private List<GeneratorExtension> createGeneratorExtensions(MatpowerModel model, List<Generator> generators) {
        List<GeneratorExtension> generatorExtensions = new ArrayList<>();
        for (int i = 0; i < model.getGeneratorExtensions().size(); ++i) {
            GeneratorExtension generatorExtension = new GeneratorExtension();
            generatorExtension.setId(model.getGeneratorExtensions().get(i).getId());
            generatorExtension.setStatusCurt(model.getGeneratorExtensions().get(i).getStatusCurt());
            generatorExtension.setDgType(model.getGeneratorExtensions().get(i).getDgType());
            generatorExtension.setGenerator(generators.get(i));
            generatorExtensions.add(generatorExtension);
        }
        LOGGER.info("Return list of generatorExtensions of size: {}! ", generatorExtensions.size());
        return generatorExtensions;
    }

    /* createGenTags method
     * return a list of gen tags (genTag and genId)
     * data is taken from MatpowerModel
     */
    private List<GenTag> createGenTags(MatpowerModel model, List<Generator> gens) {
        List<GenTag> genTags = new ArrayList<>();
        for (int i = 0; i < model.getGenTags().size(); ++i) {
            GenTag genTag = new GenTag();
            genTag.setGenerator(gens.get(i));
            genTag.setGenTag(model.getGenTags().get(i).getGenTag());
            genTags.add(genTag);
        }
        LOGGER.info("Return list of genTags of size: {}! ", genTags.size());
        return genTags;
    }

    /* createBusNames method
     * return a list of bus names (busName and busId)
     * data is taken from MatpowerModel
     */
    private List<BusName> createBusNames(MatpowerModel model, List<Bus> buses) {
        List<BusName> busNames = new ArrayList<>();
        for (int i = 0; i < model.getBusNames().size(); ++i) {
            BusName busName = new BusName();
            busName.setBus(buses.get(i));
            busName.setBusName(model.getBusNames().get(i).getBusName());
            busNames.add(busName);
        }
        LOGGER.info("Return list of busNames of size: {}! ", busNames.size());
        return busNames;
    }

    /* createTransformers method
     * return a list of transformers
     * data is taken from MatpowerModel
     */
    private List<Transformer> createTransformers(MatpowerModel model, Network network) {
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
        LOGGER.info("Return list of transformers of size: {}! ", trans.size());
        return trans;
    }

    /* createBusCoords method
     * return a list of bus coordinates
     * data is taken from MatpowerModel
     */
    private List<BusCoordinate> createBusCoords(MatpowerModel model, List<Bus> buses) {
        List<BusCoordinate> busCoordinates = new ArrayList<>();
        for (int i = 0; i < model.getBusCoordinates().size(); ++i) {
            BusCoordinate bc = new BusCoordinate();
            bc.setBus(buses.get(i));
            bc.setX(model.getBusCoordinates().get(i).getX());
            bc.setY(model.getBusCoordinates().get(i).getY());
            busCoordinates.add(bc);
        }
        LOGGER.info("Return list of busCoordinates of size: {}! ", busCoordinates.size());
        return busCoordinates;
    }

    /* createGenCosts method
     * return a list of gen costs
     * data is taken from MatpowerModel
     */
    private List<GenCost> createGenCosts(MatpowerModel model, List<Generator> generators) {
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
        LOGGER.info("Return list of genCosts of size: {}! ", genCosts.size());
        return genCosts;
    }

    /* createCapsBankData method
     * return a list of capacitors bank data
     * data is taken from MatpowerModel
     */
    private List<CapacitorBankData> createCapsBankData(MatpowerModel model, Network network) {
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
        LOGGER.info("Return list of capacitors bank data of size: {}! ", caps.size());
        return caps;
    }
}
