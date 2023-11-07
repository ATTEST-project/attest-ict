package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.csv.exception.CsvInvalidDataException;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.CsvLoadProfileService;
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
public class CsvLoadProfileServiceImpl implements CsvLoadProfileService {

    public final Logger log = LoggerFactory.getLogger(CsvLoadProfileServiceImpl.class);

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    InputFileMapper inputFileMapper;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    LoadElValRepository loadElValRepository;

    @Autowired
    LoadProfileRepository loadProfileRepository;

    @Autowired
    InputFileService inputFileServiceImpl;

    @Autowired
    InputFileRepository inputFileRepository;

    @Autowired
    BusRepository busRepository;

    @Override
    public void loadProfile(MultipartFile file, Optional<Network> networkOpt, Boolean headerEnabled) {
        String fileNameFull = file.getOriginalFilename();
        log.debug("loadProfile() - fileName: {} ", fileNameFull);

        int pos = fileNameFull.lastIndexOf(".");
        String fileName = fileNameFull.substring(0, pos);
        String[] loadProfile = fileName.split("_");

        String season = ProfileUtil.getSeason(loadProfile);
        log.debug("loadProfile() - season: {} ", season);

        String typicalDay = ProfileUtil.getTypicalDay(loadProfile);
        log.debug("loadProfile() - typicalDay: {} ", typicalDay);

        int mode = ProfileUtil.getMode(season, typicalDay);
        log.debug("loadProfile() - mode: {} ", mode);
        loadProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
    }

    @Override
    public void loadProfile(
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
            "loadProfile() - parse the auxiliary file: {} for networkId: {}, mode: {}, season: {}, typicalDay: {} ",
            file.getOriginalFilename(),
            network.getId(),
            mode,
            season,
            typicalDay
        );
        // Parse file
        // busNum;type;val1,.........val_24; if time frame = 1hour
        // busNum;type;val1,.........val_48; if time frame = 30 min
        // busNum;type;val1,.........val_96: if time frame = 15 min
        CsvFileReader csvFileReader = new CsvFileReader();
        List<String[]> loadsData = csvFileReader.parseCsvMultiPartFile(file, isHeaderEnabled);

        int numSkipColumns = 2; // first column = busNum , second columns type (P or Q)
        int numColMax = ProfileUtil.getMaxNumCol(loadsData, numSkipColumns);
        double timeInterval = ProfileUtil.getTimeInterval(numColMax);
        log.info("loadProfile() -  numCols:{} , Time Interval: {} ", numColMax, timeInterval);

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
        LinkedHashMap<String, LoadElVal> mapLoadElVal = new LinkedHashMap<String, LoadElVal>();
        LoadElVal loadElVal;

        Long precBusNum = Long.valueOf(-1);
        Bus bus = null;
        Long busNum = null;

        int rowNum = isHeaderEnabled ? 2 : 1;
        for (String[] col : loadsData) {
            //-- Column 0: Valudate BusNum: Some HR TEST CASES contains  fromBus-ToBus (BRANCH) instead OF BusNum
            try {
                busNum = Long.valueOf(col[0]);
            } catch (NumberFormatException ne) {
                String errMsg = " Row: " + rowNum + ", BusNum " + col[0] + " is not valid.";
                log.error(errMsg);
                throw new CsvInvalidDataException(errMsg);
            }

            String type = col[1];
            if (type.equals("P")) {
                if (!precBusNum.equals(busNum)) {
                    bus = busRepository.findByBusNumAndNetworkId(busNum, network.getId());
                    if (bus == null) {
                        String errMsg = " BusNum: " + busNum + " not present for Network id: " + network.getId();
                        log.error(errMsg);
                        throw new CsvInvalidDataException(errMsg);
                    }
                    precBusNum = busNum;
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
                String mapKey = col[0] + "_" + countHour + "_" + countMin;
                Double value;
                int pos = i + numSkipColumns;
                try {
                    value = (col[pos] != null) ? Double.valueOf(col[pos].trim()) : null;
                } catch (NumberFormatException ne) {
                    String errMsg = " Row: " + rowNum + ", Col " + pos + " Value: " + col[pos] + " is not valid.";
                    log.error(errMsg);
                    throw new CsvInvalidDataException(errMsg);
                }
                if (!mapLoadElVal.containsKey(mapKey)) {
                    loadElVal = new LoadElVal();
                    loadElVal.setBus(bus);
                    loadElVal.setHour(countHour);
                    loadElVal.setMin(countMin);
                } else {
                    loadElVal = mapLoadElVal.get(mapKey);
                }
                if (type.equals("P")) {
                    loadElVal.setP(value);
                } else {
                    loadElVal.setQ(value);
                }
                mapLoadElVal.put(mapKey, loadElVal);
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

        // Save file in table: inputFile
        InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
            file,
            networkMapper.toDto(network),
            AttestConstants.INPUT_FILE_LOAD_DESCR
        );
        log.info("New File: {}, saved in InputFile ", inputFileDto.getFileName());

        LoadProfile lp = saveLoadProfile(mode, timeInterval, season, typicalDay, networkOpt, inputFileDto);
        log.info("New LoadProfile saved successfully: {} ", lp);

        List<LoadElVal> loadVals = saveLoadProfileVals(mapLoadElVal, lp);
    }

    private LoadProfile saveLoadProfile(
        Integer mode,
        Double timeInterval,
        String season,
        String typicalDay,
        Optional<Network> networkOpt,
        InputFileDTO inputFileDto
    ) {
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        LoadProfile loadProfile = new LoadProfile();
        loadProfile.setSeason(season);
        loadProfile.setTypicalDay(typicalDay);
        loadProfile.setMode(mode);
        loadProfile.setTimeInterval(timeInterval);
        loadProfile.setUploadDateTime(Instant.now());
        loadProfile.setNetwork(networkOpt.get());
        loadProfile.setInputFile(inputFile);
        return loadProfileRepository.save(loadProfile);
    }

    private List<LoadElVal> saveLoadProfileVals(LinkedHashMap<String, LoadElVal> mapLoadElVal, LoadProfile lp) {
        List<LoadElVal> loadVals = new ArrayList<LoadElVal>();
        for (String key : mapLoadElVal.keySet()) {
            LoadElVal lev = mapLoadElVal.get(key);
            lev.setLoadProfile(lp);
            LoadElVal newLoadElVal = loadElValRepository.save(mapLoadElVal.get(key));
            loadVals.add(newLoadElVal);
        }
        log.info("Number of LoadElVal saved: {}", loadVals.size());
        return loadVals;
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
                log.info("Input File: {} removed succesfully! " + fileName);
            } else {
                log.warn("Input File: {} not removed! " + fileName);
            }
        }
    }
}
