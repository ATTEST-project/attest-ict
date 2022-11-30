package com.attest.ict.service.impl;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.BaseMVA;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.domain.BusName;
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.GenTag;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Transformer;
import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.helper.ExcelHelper;
import com.attest.ict.helper.MatHelper;
import com.attest.ict.helper.matpower.common.reader.MatpowerReader;
import com.attest.ict.helper.matpower.network.exporter.MatpowerNetworkExporter;
import com.attest.ict.helper.matpower.network.importer.MatpowerNetworkImporter;
import com.attest.ict.helper.matpower.network.reader.MatpowerNetworkReader;
import com.attest.ict.helper.ods.reader.OdsFileReader;
import com.attest.ict.repository.BaseMVARepository;
import com.attest.ict.repository.BranchExtensionRepository;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.repository.BusCoordinateRepository;
import com.attest.ict.repository.BusExtensionRepository;
import com.attest.ict.repository.BusNameRepository;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.CapacitorBankDataRepository;
import com.attest.ict.repository.GenCostRepository;
import com.attest.ict.repository.GenTagRepository;
import com.attest.ict.repository.GeneratorExtensionRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.repository.ProtectionToolRepository;
import com.attest.ict.repository.StorageRepository;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.repository.TopologyRepository;
import com.attest.ict.repository.TransformerRepository;
import com.attest.ict.repository.VoltageLevelRepository;
import com.attest.ict.service.FileService;
import com.attest.ict.service.OdsNetworkService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link DsoTsoConnection}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    BusRepository busRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    BaseMVARepository baseMVARepository;

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
    LoadElValRepository loadElValRepository;

    @Autowired
    TopologyRepository topologyRepository;

    @Autowired
    TopologyBusRepository topologyBusesRepository;

    @Autowired
    ProtectionToolRepository protectionToolsRepository;

    @Autowired
    BusExtensionRepository busExtensionRepository;

    @Autowired
    BranchExtensionRepository branchExtensionRepository;

    @Autowired
    GeneratorExtensionRepository generatorExtensionRepository;

    @Autowired
    OdsNetworkService odsNetworkService;

    @Override
    public ByteArrayInputStream load(Date startDate, Date endDate) {
        List<Bus> buses = busRepository.findBusByNetworkTime(startDate, endDate);
        List<Branch> branches = branchRepository.findBranchByNetworkTime(startDate, endDate);
        List<Generator> generators = generatorRepository.findGeneratorByNetworkTime(startDate, endDate);
        ByteArrayInputStream in = ExcelHelper.appToExcel(buses, branches, generators);
        return in;
    }

    @Override
    public InputStream getNetworkData(String networkName) throws IOException {
        // Network network = networkRepository.findByName(networkName);
        Optional<Network> networkOpt = networkRepository.findByName(networkName);
        if (!networkOpt.isPresent()) throw new RuntimeException("network : " + networkName + " not found");

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
        if (genCosts != null) log.debug("Gen_cost record found: {}", genCosts.size()); else log.debug("No gen_cost data found");

        // transformers
        List<Transformer> transformers = transformerRepository.findByNetworkId(networkId);

        // bus names
        List<BusName> busNames = busNameRepository.getBusNamesByNetworkId(networkId);

        // bus coordinates
        List<BusCoordinate> busCoordinates = busCoordinatesRepository.findByNetworkId(networkId);

        // demandP
        //  List<LoadElVal> loadElPVals = loadElValRepository.findByTypeByLoadProfileNetworkId(networkId, "P");

        // demandQ
        // List<LoadElVal> loadElQVals = loadElValRepository.findByTypeByLoadProfileNetworkId(networkId, "Q");

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

        return MatpowerNetworkExporter.exportMatpowerData(model);
    }

    /**TODO REMOVE????
    @Override
    public void save(MultipartFile file, String networkName, Long networkId) throws IOException, ParseException {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<Bus> buses = ExcelHelper.excelToBuses(file.getInputStream(), networkName, networkId);
                List<Branch> branches = ExcelHelper.excelToBranches(file.getInputStream(), networkName, networkId);
                List<Generator> generators = ExcelHelper.excelToGenerators(file.getInputStream(), networkName, networkId);

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);
            } catch (IOException e) {
                throw new RuntimeException("fail to store excel data: " + e.getMessage());
            }
        }
        if (MatpowerReader.isDotMFile(file)) {
            try {
                MatpowerModel model = MatpowerNetworkReader.read(file.getInputStream());

                Optional<Network> networkOpt = networkRepository.findByName(networkName);
                if (!networkOpt.isPresent()) throw new RuntimeException("fail to store .mat data network: " + networkName + " not found");

                networkOpt.get().setMpcName(model.getCaseName());
                networkRepository.save(networkOpt.get());

                BaseMVA baseMVA = MatpowerNetworkImporter.createBaseMVA(model, networkName, networkId);
                baseMVARepository.save(baseMVA);

                if (model.getvLevels() != null) {
                    VoltageLevel vLevels = MatpowerNetworkImporter.createVLevels(model, networkName, networkId);
                    vLevelsRepository.save(vLevels);
                }

                List<Bus> buses = MatpowerNetworkImporter.createBuses(model, networkName, networkId);
                List<Branch> branches = MatpowerNetworkImporter.createBranches(model, networkName, networkId);
                List<Generator> generators = MatpowerNetworkImporter.createGenerators(model, networkName, networkId);

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);

                if (!model.getBusExtensions().isEmpty()) {
                    List<BusExtension> busExtensions = MatpowerNetworkImporter.createBusExtensions(model, buses);
                    busExtensionRepository.saveAll(busExtensions);
                }

                if (!model.getBranchExtensions().isEmpty()) {
                    List<BranchExtension> branchExtensions = MatpowerNetworkImporter.createBranchExtensions(model, branches);
                    branchExtensionRepository.saveAll(branchExtensions);
                }

                if (!model.getGeneratorExtensions().isEmpty()) {
                    List<GeneratorExtension> generatorExtensions = MatpowerNetworkImporter.createGeneratorExtensions(model, generators);
                    generatorExtensionRepository.saveAll(generatorExtensions);
                }

                if (!model.getBusNames().isEmpty()) {
                    List<BusName> busNames = MatpowerNetworkImporter.createBusNames(model, buses);
                    busNameRepository.saveAll(busNames);
                }

                if (!model.getGenTags().isEmpty()) {
                    List<GenTag> genTags = MatpowerNetworkImporter.createGenTags(model, generators);
                    genTagRepository.saveAll(genTags);
                }

                if (!model.getTransformers().isEmpty()) {
                    List<Transformer> trans = MatpowerNetworkImporter.createTransformers(model, networkName, networkId);
                    transformerRepository.saveAll(trans);
                }

                if (!model.getBusCoordinates().isEmpty()) {
                    List<BusCoordinate> coords = MatpowerNetworkImporter.createBusCoords(model, buses);
                    busCoordinatesRepository.saveAll(coords);
                }

                if (!model.getGenCosts().isEmpty()) {
                    List<GenCost> genCosts = MatpowerNetworkImporter.createGenCosts(model, generators);
                    genCostRepository.saveAll(genCosts);
                }

                if (!model.getCaps().isEmpty()) {
                    List<CapacitorBankData> caps = MatpowerNetworkImporter.createCapsBankData(model, networkName, networkId);
                    capacitorBankDataRepository.saveAll(caps);
                }
                /*if (!model.getLoadElVarsP().isEmpty()) {
                    List<LoadElVar> loadElVarsP = MatpowerNetworkImporter.createLoadElVarsP(model, buses);
                    loadElVarRepository.saveAll(loadElVarsP);
                }

                if (!model.getLoadElVarsQ().isEmpty()) {
                    List<LoadElVar> loadElVarsQ = MatpowerNetworkImporter.createLoadElVarsQ(model, buses);
                    loadElVarRepository.saveAll(loadElVarsQ);
                }*/
    /*
            } catch (IOException e) {
                throw new RuntimeException("fail to store matpower data: " + e.getMessage());
            }
        }
        if (MatHelper.hasMatlabFormat(file) && !MatpowerReader.isDotMFile(file)) {
            // MultipartFile shall be converted to File before using it in Mat5File
            File convFile = null;
            convFile = File.createTempFile(file.getOriginalFilename(), "");
            file.transferTo(convFile);

            try {
                List<Bus> buses = MatHelper.matToBus(convFile, networkName, networkId);
                List<Branch> branches = MatHelper.matToBranch(convFile, networkName, networkId);
                List<Generator> generators = MatHelper.matToGenerator(convFile, networkName, networkId);

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);
            } catch (IOException e) {
                throw new RuntimeException("fail to store matlab data: " + e.getMessage());
            }
        }
        
        //
        //20220823 For t4.1 V2 network data test cases are generated by developper in .ods format 
        if (OdsFileReader.hasOdsFormat(file)) {
        	odsNetworkService.importNetworkFile(networkId, file);
        }
    }
    **/

    @Override
    public void save(MultipartFile file, Network network) throws IOException, ParseException {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<Bus> buses = ExcelHelper.excelToBuses(file.getInputStream(), network.getName(), network.getId());
                List<Branch> branches = ExcelHelper.excelToBranches(file.getInputStream(), network.getName(), network.getId());
                List<Generator> generators = ExcelHelper.excelToGenerators(file.getInputStream(), network.getName(), network.getId());

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);
            } catch (IOException e) {
                throw new RuntimeException("Fail to import network file from .excel data: " + e.getMessage());
            }
        }
        if (MatpowerReader.isDotMFile(file)) {
            try {
                MatpowerModel model = MatpowerNetworkReader.read(file.getInputStream());

                //Optional<Network> networkOpt = networkRepository.findByName(networkName);
                //if (!networkOpt.isPresent()) throw new RuntimeException("fail to store .mat data network: " + networkName + " not found");

                network.setMpcName(model.getCaseName());
                networkRepository.save(network);

                BaseMVA baseMVA = MatpowerNetworkImporter.createBaseMVA(model, network.getName(), network.getId());
                baseMVARepository.save(baseMVA);

                if (model.getvLevels() != null) {
                    VoltageLevel vLevels = MatpowerNetworkImporter.createVLevels(model, network.getName(), network.getId());
                    vLevelsRepository.save(vLevels);
                }

                List<Bus> buses = MatpowerNetworkImporter.createBuses(model, network.getName(), network.getId());
                List<Branch> branches = MatpowerNetworkImporter.createBranches(model, network.getName(), network.getId());
                List<Generator> generators = MatpowerNetworkImporter.createGenerators(model, network.getName(), network.getId());

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);

                if (!model.getBusExtensions().isEmpty()) {
                    List<BusExtension> busExtensions = MatpowerNetworkImporter.createBusExtensions(model, buses);
                    busExtensionRepository.saveAll(busExtensions);
                }

                if (!model.getBranchExtensions().isEmpty()) {
                    List<BranchExtension> branchExtensions = MatpowerNetworkImporter.createBranchExtensions(model, branches);
                    branchExtensionRepository.saveAll(branchExtensions);
                }

                if (!model.getGeneratorExtensions().isEmpty()) {
                    List<GeneratorExtension> generatorExtensions = MatpowerNetworkImporter.createGeneratorExtensions(model, generators);
                    generatorExtensionRepository.saveAll(generatorExtensions);
                }

                if (!model.getBusNames().isEmpty()) {
                    List<BusName> busNames = MatpowerNetworkImporter.createBusNames(model, buses);
                    busNameRepository.saveAll(busNames);
                }

                if (!model.getGenTags().isEmpty()) {
                    List<GenTag> genTags = MatpowerNetworkImporter.createGenTags(model, generators);
                    genTagRepository.saveAll(genTags);
                }

                if (!model.getTransformers().isEmpty()) {
                    List<Transformer> trans = MatpowerNetworkImporter.createTransformers(model, network.getName(), network.getId());
                    transformerRepository.saveAll(trans);
                }

                if (!model.getBusCoordinates().isEmpty()) {
                    List<BusCoordinate> coords = MatpowerNetworkImporter.createBusCoords(model, buses);
                    busCoordinatesRepository.saveAll(coords);
                }

                if (!model.getGenCosts().isEmpty()) {
                    List<GenCost> genCosts = MatpowerNetworkImporter.createGenCosts(model, generators);
                    genCostRepository.saveAll(genCosts);
                }

                if (!model.getCaps().isEmpty()) {
                    List<CapacitorBankData> caps = MatpowerNetworkImporter.createCapsBankData(model, network.getName(), network.getId());
                    capacitorBankDataRepository.saveAll(caps);
                }
                /*if (!model.getLoadElVarsP().isEmpty()) {
                    List<LoadElVar> loadElVarsP = MatpowerNetworkImporter.createLoadElVarsP(model, buses);
                    loadElVarRepository.saveAll(loadElVarsP);
                }

                if (!model.getLoadElVarsQ().isEmpty()) {
                    List<LoadElVar> loadElVarsQ = MatpowerNetworkImporter.createLoadElVarsQ(model, buses);
                    loadElVarRepository.saveAll(loadElVarsQ);
                }*/

            } catch (IOException e) {
                throw new RuntimeException("Fail to import network file from .matpower file: " + e.getMessage());
            }
        }
        if (MatHelper.hasMatlabFormat(file) && !MatpowerReader.isDotMFile(file)) {
            // MultipartFile shall be converted to File before using it in Mat5File
            File convFile = null;
            convFile = File.createTempFile(file.getOriginalFilename(), "");
            file.transferTo(convFile);

            try {
                List<Bus> buses = MatHelper.matToBus(convFile, network.getName(), network.getId());
                List<Branch> branches = MatHelper.matToBranch(convFile, network.getName(), network.getId());
                List<Generator> generators = MatHelper.matToGenerator(convFile, network.getName(), network.getId());

                busRepository.saveAll(buses);
                branchRepository.saveAll(branches);
                generatorRepository.saveAll(generators);
            } catch (IOException e) {
                throw new RuntimeException("Fail to import network file from .matlab file: " + e.getMessage());
            }
        }

        //
        //20220823 For t4.1 V2 network data test cases are generated by developper in .ods format
        if (OdsFileReader.hasOdsFormat(file)) {
            try {
                odsNetworkService.importNetworkFromOdsFile(network.getId(), file);
            } catch (Exception e) {
                throw new RuntimeException("Fail to import network file from .ods file: " + e.getMessage());
            }
        }
    }
}
