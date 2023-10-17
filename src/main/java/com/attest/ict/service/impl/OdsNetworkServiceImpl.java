package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.BaseMVA;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.exception.OdsWriterFileException;
import com.attest.ict.helper.ods.reader.OdsNetworkFileReader;
import com.attest.ict.helper.ods.reader.model.Load;
import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.attest.ict.helper.ods.writer.NetworkOdsExporter;
import com.attest.ict.service.BaseMVAService;
import com.attest.ict.service.BranchService;
import com.attest.ict.service.BusService;
import com.attest.ict.service.GeneratorService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OdsNetworkService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import com.github.miachm.sods.Sheet;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class OdsNetworkServiceImpl implements OdsNetworkService {

    public final Logger log = LoggerFactory.getLogger(OdsNetworkServiceImpl.class);

    @Autowired
    NetworkService networkService;

    @Autowired
    BusService busService;

    @Autowired
    BranchService branchService;

    @Autowired
    BaseMVAService baseMVAService;

    @Autowired
    GeneratorService generatorService;

    @Autowired
    InputFileService inputFileInputServiceImpl;

    @Autowired
    NetworkMapper networkMapper;

    /**
     * @param networkId network identifier
     * @return ODS file with network data one component for each sheet:
     *  (buses, branches, loads, generators, baseMVA)
     */
    @Override
    public ByteArrayOutputStream exportNetworkToOdsFile(Long networkId) throws OdsWriterFileException {
        NetworkOdsExporter networkOdsExporter = new NetworkOdsExporter();
        try {
            return networkOdsExporter.writeData(
                // filePath,
                this.getBuses(networkId),
                this.getBranches(networkId), // Lines
                this.getLoads(networkId),
                this.getGenerators(networkId), //
                this.getBaseMVA(networkId)
            );
        } catch (Exception e) {
            String msg = "Error exporting network with id: " + networkId + " into .ods file file format! ";
            log.error(msg);
            throw new OdsWriterFileException(msg + " " + e.getMessage());
        }
    }

    /**
     * Import network data from '.ods' file format
     * @param networkId network unique identifier
     * @param multiPartFile file to upload
     */
    @Override
    public void importNetworkFromOdsFile(Long networkId, MultipartFile multiPartFile) throws OdsReaderFileException {
        log.info("File Ods: {} import start ...", multiPartFile.getOriginalFilename());
        String origFileName = StringUtils.cleanPath(multiPartFile.getOriginalFilename());

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String msg = "Network with id: " + networkId + " not found!";
            log.error(msg);
            throw new OdsReaderFileException(msg);
        }

        Network network = networkOpt.get();
        NetworkDTO networkDTO = networkMapper.toDto(network);
        if (origFileName != null) {
            String fileName = FileUtils.getFileLessExtension(origFileName);
            log.info("Network fileName: {}", fileName);
            network.setMpcName(fileName);
            Optional<NetworkDTO> networkDtoOptSaved = networkService.partialUpdate(networkMapper.toDto(network));
            if (networkDtoOptSaved.isPresent()) {
                network = networkMapper.toEntity(networkDtoOptSaved.get());
                log.info("Network partialUpdate: {}", network);
            }
        }

        OdsNetworkFileReader reader = new OdsNetworkFileReader(multiPartFile);
        List<Bus> buses = new ArrayList<Bus>();
        List<Branch> branches = new ArrayList<Branch>();
        List<Load> loads = new ArrayList<Load>();
        List<Generator> gens = new ArrayList<Generator>();
        List<BaseMVA> baseMvas = new ArrayList<BaseMVA>();
        List<Sheet> sheets;

        // Get file's sheets
        try {
            sheets = reader.parseOdsNetworkFile();
        } catch (Exception e) {
            throw new OdsReaderFileException(e.getMessage());
        }

        // Parsing sheets
        try {
            for (Sheet sheet : sheets) {
                String name = sheet.getName();
                log.info("Reading... sheet: " + name);
                if (name.equals(T41FileInputFormat.networkSheetHeaders.get(0))) {
                    buses = reader.parseSheetBuses(sheet, network);
                    if (buses != null) log.debug("sheet 'Buses' contain: {} rows ", buses.size());
                }
                if (name.equals(T41FileInputFormat.networkSheetHeaders.get(1))) {
                    branches = reader.parseSheetLines(sheet, network);
                    if (branches != null) log.debug("sheet 'Branches' contain: {} rows", branches.size());
                }
                if (name.equals(T41FileInputFormat.networkSheetHeaders.get(2))) {
                    loads = reader.parseSheetLoads(sheet);
                    if (loads != null) log.debug("sheet 'Load' contain: {} row ", loads.size());
                }
                if (name.equals(T41FileInputFormat.networkSheetHeaders.get(3))) {
                    gens = reader.parseSheetGens(sheet, network);
                    if (gens != null) log.debug("sheet 'Gens' contain: {} rows ", gens.size());
                }
                if (name.equals(T41FileInputFormat.networkSheetHeaders.get(4))) {
                    baseMvas = reader.parseSheetBaseMVA(sheet, network);
                    if (baseMvas != null) log.debug("sheet Base_MVA contains: {} rows ", baseMvas.size());
                }
            }
            // -- merge buses and loads
            mergeBusAndLoad(buses, loads);
        } catch (Exception e) {
            String errMsg = "Error parsing file: {} " + multiPartFile.getOriginalFilename() + " " + e.getMessage();
            log.error(errMsg);
            throw new OdsReaderFileException(errMsg);
        }

        try {
            // -- store data on DB
            List<Bus> busesSaved = this.busService.saveAll(buses);
            busesSaved.forEach(elem -> log.debug("busesSaved saved: {}", elem));

            List<Branch> branchSaved = this.branchService.saveAll(branches);
            branchSaved.forEach(elem -> log.debug("branchSaved saved: {}", elem));

            List<Generator> gensSaved = this.generatorService.saveAll(gens);
            gensSaved.forEach(elem -> log.debug("Genearator saved: {}", elem));

            List<BaseMVA> baseMVASaved = this.baseMVAService.saveAll(baseMvas);
            baseMVASaved.forEach(elem -> log.debug("  BaseMVA saved: {}", elem));
        } catch (Exception e) {
            String errMsg = "Error saving data in the data base";
            log.error(errMsg);
            throw new OdsReaderFileException(errMsg, e);
        }

        InputFileDTO inputFileSavedDTO = inputFileInputServiceImpl.saveFileForNetworkWithDescr(
            multiPartFile,
            networkDTO,
            AttestConstants.INPUT_FILE_NETWORK_DESCR
        );
        log.info(
            "File Ods: {}, imported successfully, inputFileSavedDTO saved: {} ",
            multiPartFile.getOriginalFilename(),
            inputFileSavedDTO
        );
    }

    private Object[][] getBuses(Long networkId) {
        // Buses
        String sheetName = T41FileInputFormat.networkSheetHeaders.get(0);

        // "bus_i", "type", "area", "Vm", "Va", "baseKV", "zone", "Vmax", "Vmin"
        Object[] header = T41FileInputFormat.netwrokSheetMap.get(sheetName).toArray();
        int numFields = header.length;

        List<Bus> buses = busService.getBusesByNetworkId(networkId);
        int countBuses = buses.size();
        log.info("Found {} buses, for networkID: {}", countBuses, networkId);

        Object[][] busArray = new Object[countBuses][numFields];
        for (int i = 0; i < countBuses; i++) {
            Bus bus = buses.get(i);
            busArray[i] =
                new Object[] {
                    bus.getBusNum(),
                    bus.getType(),
                    bus.getArea(),
                    bus.getVm(),
                    bus.getVa(),
                    bus.getBaseKv(),
                    bus.getZone(),
                    bus.getVmax(),
                    bus.getVmin(),
                };
        }

        return busArray;
    }

    private Object[][] getBranches(Long networkId) {
        // Lines
        String sheetName = T41FileInputFormat.networkSheetHeaders.get(1);

        // "fbus","tbus","r","x", "b","rateA", "rateB","rateC","ratio",
        // "angle","status","angmin","angmax"
        Object[] header = T41FileInputFormat.netwrokSheetMap.get(sheetName).toArray();

        List<Branch> elements = branchService.getBranchesByNetworkId(networkId);
        int count = elements.size();
        log.info("Found {} Branches, for networkID: {}", count, networkId);
        int numFields = header.length;

        Object[][] valuesArray = new Object[count][numFields];
        for (int i = 0; i < count; i++) {
            Branch elem = elements.get(i);
            valuesArray[i] =
                new Object[] {
                    elem.getFbus(),
                    elem.getTbus(),
                    elem.getR(),
                    elem.getX(),
                    elem.getB(),
                    elem.getRatea(),
                    elem.getRateb(),
                    elem.getRatec(),
                    elem.getTapRatio(),
                    elem.getAngle(),
                    elem.getStatus(),
                    elem.getAngmin(),
                    elem.getAngmax(),
                };
        }

        return valuesArray;
    }

    private Object[][] getGenerators(Long networkId, List<Generator> elements) {
        // Gens
        String sheetName = T41FileInputFormat.networkSheetHeaders.get(3);

        // bus_i Pg Qg Qmax Qmin Vg mBase status Pmax Pmin Pc1 Pc2 Qc1min Qc1max Qc2min
        // Qc2max ramp_agc ramp_10 ramp_30 ramp_q apf
        Object[] header = T41FileInputFormat.netwrokSheetMap.get(sheetName).toArray();

        int count = elements.size();
        log.info("Found num: {} Generators, for networkID: {}", count, networkId);
        int numFields = header.length;

        Object[][] valuesArray = new Object[count][numFields];
        for (int i = 0; i < count; i++) {
            Generator elem = elements.get(i);
            valuesArray[i] =
                new Object[] {
                    elem.getBusNum(),
                    elem.getPg(),
                    elem.getQg(),
                    elem.getQmax(),
                    elem.getQmin(),
                    elem.getVg(),
                    elem.getmBase(),
                    elem.getStatus(),
                    elem.getPmax(),
                    elem.getPmin(),
                    elem.getPc1(),
                    elem.getPc2(),
                    elem.getQc1min(),
                    elem.getQc1max(),
                    elem.getQc2min(),
                    elem.getQc2max(),
                    elem.getRampAgc(),
                    elem.getRamp10(),
                    elem.getRamp30(),
                    elem.getRampQ(),
                    elem.getApf(),
                };
        }
        return valuesArray;
    }

    private Object[][] getGenerators(Long networkId, Double pg) {
        if (pg == null) {
            return getGenerators(networkId);
        }
        List<Generator> elements = generatorService.findByNetworkIdAndPg(networkId, pg);
        return getGenerators(networkId, elements);
    }

    private Object[][] getGenerators(Long networkId) {
        List<Generator> elements = generatorService.findByNetworkId(networkId);
        return getGenerators(networkId, elements);
    }

    private Object[][] getLoads(Long networkId) {
        // bus_i Pd Qd Gs Bs
        String loadSheetName = T41FileInputFormat.networkSheetHeaders.get(2);
        Object[] header = T41FileInputFormat.netwrokSheetMap.get(loadSheetName).toArray();
        int numFields = header.length;

        // List<Bus> elements = busService.getLoadsByNetworkId(networkId, 0.0, 0.0);
        List<Bus> elements = busService.getBusesByNetworkId(networkId);
        int count = elements.size();
        log.info("Found {} Loads, for networkID: {}", count, networkId);

        Object[][] valuesArray = new Object[count][numFields];
        for (int i = 0; i < count; i++) {
            Bus elem = elements.get(i);
            valuesArray[i] =
                new Object[] {
                    elem.getBusNum(),
                    elem.getActivePower(),
                    elem.getReactivePower(),
                    elem.getConductance(),
                    elem.getSusceptance(),
                };
        }

        return valuesArray;
    }

    private Object[][] getBaseMVA(Long networkId) {
        // Base_MVA
        BaseMVA baseMVA = baseMVAService.getBaseMVAByNetworkId(networkId);
        if (baseMVA != null) {
            log.info("Found {} BaseMVA, for networkID: {}", baseMVA.getBaseMva(), networkId);
            Object[][] valuesArray = new Object[1][1];
            valuesArray[0] = new Object[] { baseMVA.getBaseMva() };
            return valuesArray;
        } else {
            log.warn("BaseMVA, for networkID: {} is null", networkId);
            return null;
        }
    }

    private void mergeBusAndLoad(List<Bus> buses, List<Load> loads) {
        for (Bus bus : buses) {
            Long busNum = bus.getBusNum();
            for (Load load : loads) {
                Long loadBusNum = load.getBusNum();
                if (busNum.equals(loadBusNum)) {
                    bus.setActivePower(load.getPd());
                    bus.setReactivePower(load.getQd());
                    bus.setConductance(load.getBs());
                    bus.setSusceptance(load.getGs());
                }
            }
        }
    }
}
