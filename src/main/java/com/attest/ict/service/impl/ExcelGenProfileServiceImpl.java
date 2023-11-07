package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import com.attest.ict.helper.excel.reader.ExcelProfileReader;
import com.attest.ict.helper.utils.GeneratorUtil;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.ExcelGenProfileService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.service.mapper.NetworkMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ExcelGenProfileServiceImpl implements ExcelGenProfileService {

    public final Logger log = LoggerFactory.getLogger(ExcelGenProfileServiceImpl.class);

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    InputFileMapper inputFileMapper;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    GenElValRepository genElValRepository;

    @Autowired
    GenProfileRepository genProfileRepository;

    @Autowired
    InputFileService inputFileServiceImpl;

    @Autowired
    InputFileRepository inputFileRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    @Override
    public void genProfile(MultipartFile file, Optional<Network> networkOpt, Boolean headerEnabled) {
        String fileNameFull = file.getOriginalFilename();
        log.debug("genProfile() - fileName: {} ", fileNameFull);

        String[] profile = ProfileUtil.getProfile(fileNameFull);
        String season = ProfileUtil.getSeason(profile);
        log.debug("genProfile() - season: {} ", season);

        String typicalDay = ProfileUtil.getTypicalDay(profile);
        log.debug("genProfile() - typicalDay: {} ", typicalDay);

        int mode = ProfileUtil.getMode(season, typicalDay);
        log.debug("genProfile() - mode: {} ", mode);
        genProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
    }

    @Override
    public void genProfile(
        MultipartFile file,
        Optional<Network> networkOpt,
        Integer mode,
        String season,
        String typicalDay,
        Boolean headerEnabled
    ) {
        // By default header is not present in file
        boolean isHeaderEnabled = Boolean.FALSE.booleanValue();
        if (headerEnabled != null) isHeaderEnabled = headerEnabled.booleanValue();

        Network network = networkOpt.get();
        log.info(
            "genProfile() - parse the auxiliary file: {} for networkId: {}, mode: {}, season: {}, typicalDay: {} ",
            file.getOriginalFilename(),
            network.getId(),
            mode,
            season,
            typicalDay
        );
        List<Generator> generators = generatorRepository.findByNetworkIdOrderByIdAsc(network.getId());

        // Parse file with one sheet
        // busNum;type;val1,.........val_24; if time frame = 1hour
        // busNum;type;val1,.........val_48; if time frame = 30 min
        // busNum;type;val1,.........val_96: if time frame = 15 min
        ExcelProfileReader reader = new ExcelProfileReader();
        Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelGenProfileFile(file, isHeaderEnabled);
        for (String sheetName : mapSheet.keySet()) {
            List<LoadGeneratorPower> gensData = mapSheet.get(sheetName);
            int numColMax = ProfileUtil.getMaxNumCol(gensData);
            double timeInterval = ProfileUtil.getTimeInterval(numColMax);
            log.info("genProfile() - reading SheetName: {},  numCols:{} , Time Interval: {} ", sheetName, numColMax, timeInterval);
            // time Interval
            //1.0 = 1 hour
            // 0.5 = 30 mins
            // 0.25 = 15 mins
            if (timeInterval != 1.0 && timeInterval != 0.5 && timeInterval != 0.25) {
                String errMsg =
                    " Time frame: " +
                    timeInterval +
                    ", numCols: " +
                    numColMax +
                    ". The excel file should consists of 26 or 50 or 98 columns";
                log.error(errMsg);
                throw new ExcelnvalidDataException(errMsg);
            }

            int step = (int) (1 / timeInterval);
            int increment = ProfileConstants.MINS_FOR_HOUR / step;
            LinkedHashMap<String, GenElVal> mapGenElVal = new LinkedHashMap<String, GenElVal>();
            GenElVal genElVal;

            int genOrderForBus = 0;
            Long precBusNum = Long.valueOf(-1);
            Generator gen = null;
            for (LoadGeneratorPower demand : gensData) {
                Long busNum = demand.getBusNum();
                String type = demand.getPowerType();
                // On the same bus, you can find multiple generators. To retrieve the correct 'gen_id', we maintain the order of reading,
                // assuming that the values entered follow the same order as the structure 'mcp.gen' in Matpower
                if (type.equals("P")) {
                    if (!precBusNum.equals(busNum)) {
                        genOrderForBus = 1;
                        precBusNum = busNum;
                        gen = GeneratorUtil.getGeneratorForBus(genOrderForBus, busNum, generators);
                    } else {
                        genOrderForBus++;
                        gen = GeneratorUtil.getGeneratorForBus(genOrderForBus, busNum, generators);
                    }
                }

                List<Double> values = demand.getValues();
                int size = values.size();
                int colMissing = numColMax - size;
                if (size < numColMax) {
                    //log.warn(" Not all columns are filled correctlty:  Size: " + size + " MissingValue: " + colMissing);
                    for (int k = 0; k < colMissing; k++) {
                        values.add(null);
                    }
                }
                int countHour = 0;
                int countStep = 1;
                int countMin = 0;

                for (Double value : values) {
                    //2022/07/05 there may be several generators on the same bus
                    //String mapKey = busNum + "_" + countHour + "_" + countMin;
                    String mapKey = busNum + "_" + genOrderForBus + "_" + countHour + "_" + countMin;
                    if (!mapGenElVal.containsKey(mapKey)) {
                        genElVal = new GenElVal();
                        genElVal.setGenerator(gen);
                        genElVal.setHour(countHour);
                        genElVal.setMin(countMin);
                    } else {
                        genElVal = mapGenElVal.get(mapKey);
                    }
                    if (type.equals("P")) {
                        genElVal.setP(value);
                    } else {
                        genElVal.setQ(value);
                    }
                    mapGenElVal.put(mapKey, genElVal);
                    if (countStep < step) {
                        countStep++;
                        countMin = countMin + increment;
                    } else {
                        countStep = 1;
                        countMin = 0;
                        countHour++;
                    }
                }
            }

            // SavefIle in table: inputFile
            InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
                file,
                networkMapper.toDto(network),
                AttestConstants.INPUT_FILE_GEN_DESCR
            );
            log.info("New File: {}, saved in InputFile ", inputFileDto.getFileName());

            GenProfile lp = saveGenProfile(mode, timeInterval, season, typicalDay, networkOpt, inputFileDto);
            log.info("New GenProfile saved successfully: {} ", lp);

            Instant start = Instant.now();
            List<GenElVal> genVals = saveGenProfileVals(mapGenElVal, lp);
            Duration d = Duration.between(start, Instant.now());
            log.debug("Insert into GenElVal takes: {} seconds", d.toSeconds());
        }
    }

    private GenProfile saveGenProfile(
        Integer mode,
        Double timeInterval,
        String season,
        String typicalDay,
        Optional<Network> networkOpt,
        InputFileDTO inputFileDto
    ) {
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        GenProfile genProfile = new GenProfile();
        genProfile.setSeason(season);
        genProfile.setTypicalDay(typicalDay);
        genProfile.setMode(mode);
        genProfile.setTimeInterval(timeInterval);
        genProfile.setUploadDateTime(Instant.now());
        genProfile.setNetwork(networkOpt.get());
        genProfile.setInputFile(inputFile);
        return genProfileRepository.save(genProfile);
    }

    public List<GenElVal> saveGenProfileVals(LinkedHashMap<String, GenElVal> mapLoadElVal, GenProfile lp) {
        List<GenElVal> genVals = new ArrayList<GenElVal>();
        for (String key : mapLoadElVal.keySet()) {
            GenElVal lev = mapLoadElVal.get(key);
            lev.setGenProfile(lp);
            GenElVal newLoadElVal = genElValRepository.save(mapLoadElVal.get(key));
            genVals.add(newLoadElVal);
        }
        log.info("Number of GenElVal saved: {}", genVals.size());
        return genVals;
    }

    private void cleanOldValues(
        MultipartFile file,
        Integer mode,
        Double timeInterval,
        String season,
        String typicalDay,
        Optional<Network> networkOpt
    ) {
        String fileName = file.getOriginalFilename();
        Optional<InputFileDTO> inputFileDto = inputFileServiceImpl.findFileByNetworkIdAndFileName(networkOpt.get().getId(), fileName);

        if (inputFileDto.isPresent()) {
            log.info(
                "The file: {}, for networkId: {} is already present on db;  inputFile, profile and electrical value will be removed! ",
                fileName,
                networkOpt.get().getId()
            );

            boolean isRemoved = inputFileServiceImpl.delete(inputFileDto.get().getId());
            if (isRemoved) {
                log.info("Input File: {} removed successfully! " + fileName);
            } else {
                log.warn("Input File: {} not removed! " + fileName);
            }
        }
    }
}
