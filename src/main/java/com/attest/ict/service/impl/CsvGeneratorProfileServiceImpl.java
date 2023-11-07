package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.csv.exception.CsvInvalidDataException;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.utils.GeneratorUtil;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.CsvGeneratorProfileService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.service.mapper.NetworkMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
public class CsvGeneratorProfileServiceImpl implements CsvGeneratorProfileService {

    public final Logger log = LoggerFactory.getLogger(CsvGeneratorProfileServiceImpl.class);

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
    public void generatorProfile(MultipartFile file, Optional<Network> networkOpt, Boolean headerEnabled) {
        String fileNameFull = file.getOriginalFilename();
        log.debug("generatorProfile() - fileName: {} ", fileNameFull);

        int pos = fileNameFull.lastIndexOf(".");
        String fileName = fileNameFull.substring(0, pos);
        String[] profile = fileName.split("_");

        String season = ProfileUtil.getSeason(profile);
        log.debug("generatorProfile() - season: {} ", season);

        String typicalDay = ProfileUtil.getTypicalDay(profile);
        log.debug("generatorProfile() - typicalDay: {} ", typicalDay);

        int mode = ProfileUtil.getMode(season, typicalDay);
        log.debug("generatorProfile() - mode: {} ", mode);
        generatorProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
    }

    @Override
    public void generatorProfile(
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
            "generatorProfile() - parse the auxiliary file: {} for networkId: {}, mode: {}, season: {}, typicalDay: {} ",
            file.getOriginalFilename(),
            network.getId(),
            mode,
            season,
            typicalDay
        );
        List<Generator> generators = generatorRepository.findByNetworkIdOrderByIdAsc(network.getId());
        log.debug("generatorProfile() - Num Generators: {} " + generators.size());

        // Parse file
        // busNum;type;val1,.........val_24; if time frame = 1hour
        // busNum;type;val1,.........val_48; if time frame = 30 min
        // busNum;type;val1,.........val_96: if time frame = 15 min
        CsvFileReader csvFileReader = new CsvFileReader();
        List<String[]> data = csvFileReader.parseCsvMultiPartFile(file, isHeaderEnabled);

        int numSkipColumns = 2; // first column = busNum , second columns type (P or Q)
        int numColMax = ProfileUtil.getMaxNumCol(data, numSkipColumns);
        double timeInterval = ProfileUtil.getTimeInterval(numColMax);
        log.info("generatorProfile() -  numCols:{} , Time Interval: {} ", numColMax, timeInterval);

        // time Interval
        // 1.0 = 1 hour
        // 0.5 = 30 mins
        // 0.25 = 15 mins
        if (timeInterval != 1.0 && timeInterval != 0.5 && timeInterval != 0.25) {
            String errMsg = "The csv file should consists of num cols: 26 (1 hour)  or 50 (30 min) or 98 (15 min) columns";
            log.error(errMsg);
            throw new CsvInvalidDataException(errMsg);
        }

        int step = (int) (1 / timeInterval);
        int increment = ProfileConstants.MINS_FOR_HOUR / step;
        LinkedHashMap<String, GenElVal> mapGenElVal = new LinkedHashMap<String, GenElVal>();
        GenElVal elVal;
        int rowNum = isHeaderEnabled ? 2 : 1;

        //20220705 fix more than one generator installed on the same bus
        int genOrderForBus = 0;
        Long precBusNum = Long.valueOf(-1);
        Generator gen = null;
        for (String[] col : data) {
            Long busNum = Long.valueOf(col[0]);
            String type = col[1];
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

            int size = col.length;
            int colMissing = numColMax + numSkipColumns - size;
            //log.warn(" Not all colums are filled correctlty:  Size: " + size + " MissingValue: " + colMissing);
            if (size < numColMax + numSkipColumns) {
                col = Arrays.copyOf(col, col.length + colMissing);
                for (int k = 0; k < colMissing; k++) {
                    col[size + k] = null;
                }
            }
            int countHour = 0;
            int countStep = 1;
            int countMin = 0;
            for (int i = 0; i < numColMax; i++) {
                //2022/07/05 there may be several generators on the same bus
                //String mapKey = col[0] + "_" + countHour + "_" + countMin;
                String mapKey = col[0] + "_" + genOrderForBus + "_" + countHour + "_" + countMin;

                Double value;
                int pos = i + numSkipColumns;
                try {
                    value = (col[pos] != null) ? Double.valueOf(col[pos].trim()) : null;
                } catch (NumberFormatException ne) {
                    String errMsg = " Row: " + rowNum + ", Col " + pos + " Value: " + col[pos] + " is not valid.";
                    log.error(errMsg);
                    throw new CsvInvalidDataException(errMsg);
                }
                if (!mapGenElVal.containsKey(mapKey)) {
                    elVal = new GenElVal();

                    elVal.setGenerator(gen);
                    elVal.setHour(countHour);
                    elVal.setMin(countMin);
                } else {
                    elVal = mapGenElVal.get(mapKey);
                }
                if (type.equals("P")) {
                    elVal.setP(value);
                } else {
                    elVal.setQ(value);
                }
                mapGenElVal.put(mapKey, elVal);
                if (countStep < step) {
                    countStep++;
                    countMin = countMin + increment;
                } else {
                    countStep = 1;
                    countMin = 0;
                    countHour++;
                }
            }
            rowNum++;
        }

        // SavefIle in table: inputFile
        InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
            file,
            networkMapper.toDto(network),
            AttestConstants.INPUT_FILE_GEN_DESCR
        );
        log.info("New File: {}, saved in InputFile ", inputFileDto.getFileName());

        GenProfile lp = saveGenProfile(mode, timeInterval, season, typicalDay, networkOpt, inputFileDto);
        log.info("New Generator Profile: {} saved in GenProfile" + lp);

        List<GenElVal> loadVals = saveGenProfileVals(mapGenElVal, lp);
        log.info(
            "New Generator Profile values saved for network: {}, mode: {} , timeInterval: {} season: {} , typicalDay: {}  ",
            network,
            mode,
            timeInterval,
            season,
            typicalDay
        );
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

        GenProfile profile = new GenProfile();
        profile.setSeason(season);
        profile.setTypicalDay(typicalDay);
        profile.setMode(mode);
        profile.setTimeInterval(timeInterval);
        profile.setUploadDateTime(Instant.now());
        profile.setNetwork(networkOpt.get());
        profile.setInputFile(inputFile);
        return genProfileRepository.save(profile);
    }

    private List<GenElVal> saveGenProfileVals(LinkedHashMap<String, GenElVal> mapElVal, GenProfile gp) {
        List<GenElVal> elVals = new ArrayList<GenElVal>();
        for (String key : mapElVal.keySet()) {
            GenElVal gev = mapElVal.get(key);
            gev.setGenProfile(gp);
            GenElVal newLoadElVal = genElValRepository.save(mapElVal.get(key));
            elVals.add(newLoadElVal);
        }
        log.info("Number of GenElVal saved: {}", elVals.size());
        return elVals;
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
