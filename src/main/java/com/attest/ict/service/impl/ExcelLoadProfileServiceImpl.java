package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import com.attest.ict.helper.excel.reader.ExcelProfileReader;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.ExcelLoadProfileService;
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
public class ExcelLoadProfileServiceImpl implements ExcelLoadProfileService {

    public final Logger log = LoggerFactory.getLogger(ExcelLoadProfileServiceImpl.class);

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

        String[] profile = ProfileUtil.getProfile(fileNameFull);
        String season = ProfileUtil.getSeason(profile);
        log.debug("loadProfile() - season: {} ", season);

        String typicalDay = ProfileUtil.getTypicalDay(profile);
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

        // Parse file with one sheet
        // busNum;type;val1,.........val_24; if time frame = 1hour
        // busNum;type;val1,.........val_48; if time frame = 30 min
        // busNum;type;val1,.........val_96: if time frame = 15 min
        ExcelProfileReader reader = new ExcelProfileReader();
        Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelLoadProfileFile(file, isHeaderEnabled);
        for (String sheetName : mapSheet.keySet()) {
            List<LoadGeneratorPower> loadsData = mapSheet.get(sheetName);
            int numColMax = ProfileUtil.getMaxNumCol(loadsData);
            double timeInterval = ProfileUtil.getTimeInterval(numColMax);
            log.info("loadProfile() - reading SheetName: {},  numCols:{} , Time Interval: {} ", sheetName, numColMax, timeInterval);
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
            LinkedHashMap<String, LoadElVal> mapLoadElVal = new LinkedHashMap<String, LoadElVal>();
            LoadElVal loadElVal;
            Long precBusNum = Long.valueOf(-1);
            Bus bus = null;
            for (LoadGeneratorPower demand : loadsData) {
                Long busNum = demand.getBusNum();
                String type = demand.getPowerType();

                if (type.equals("P")) {
                    if (!precBusNum.equals(busNum)) {
                        bus = busRepository.findByBusNumAndNetworkId(busNum, network.getId());
                        if (bus == null) {
                            String errMsg = " BusNum: " + busNum + " not present for Network id: " + network.getId();
                            log.error(errMsg);
                            throw new ExcelnvalidDataException(errMsg);
                        }
                        precBusNum = busNum;
                    }
                }

                List<Double> values = demand.getValues();
                int size = values.size();
                int colMissing = numColMax - size;
                if (size < numColMax) {
                    log.warn(" Not all colums are filled correctlty:  Size: " + size + " MissingValue: " + colMissing);
                    for (int k = 0; k < colMissing; k++) {
                        values.add(null);
                    }
                }
                int countHour = 0;
                int countStep = 1;
                int countMin = 0;
                for (Double value : values) {
                    String mapKey = busNum + "_" + countHour + "_" + countMin;
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
            }

            // SavefIle in table: inputFile
            InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
                file,
                networkMapper.toDto(network),
                AttestConstants.INPUT_FILE_LOAD_DESCR
            );
            log.info("New File: {}, saved in InputFile ", inputFileDto.getFileName());

            LoadProfile lp = saveLoadProfile(mode, timeInterval, season, typicalDay, networkOpt, inputFileDto);
            log.info("New LoadProfile saved successfully: {} ", lp);

            Instant start = Instant.now();
            List<LoadElVal> loadVals = saveLoadProfileVals(mapLoadElVal, lp);
            Duration d = Duration.between(start, Instant.now());
            log.debug("Insert into LoadElVal takes: {} seconds", d.toSeconds());
        }
    }

    public LoadProfile saveLoadProfile(
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

    public List<LoadElVal> saveLoadProfileVals(LinkedHashMap<String, LoadElVal> mapLoadElVal, LoadProfile lp) {
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
                log.info("Input File: {} removed successfully! " + fileName);
            } else {
                log.warn("Input File: {} not removed! " + fileName);
            }
        }
    }
}
