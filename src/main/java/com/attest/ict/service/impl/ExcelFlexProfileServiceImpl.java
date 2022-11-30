package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import com.attest.ict.helper.excel.reader.ExcelProfileReader;
import com.attest.ict.helper.excel.util.ExcelProfilesFormat;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.repository.FlexElValRepository;
import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.ExcelFlexProfileService;
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
public class ExcelFlexProfileServiceImpl implements ExcelFlexProfileService {

    public final Logger log = LoggerFactory.getLogger(ExcelFlexProfileServiceImpl.class);

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    InputFileMapper inputFileMapper;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    FlexElValRepository flexElValRepository;

    @Autowired
    FlexProfileRepository flexProfileRepository;

    @Autowired
    InputFileService inputFileServiceImpl;

    @Autowired
    InputFileRepository inputFileRepository;

    @Override
    public void flexProfile(MultipartFile file, Optional<Network> networkOpt, Boolean headerEnabled) {
        String fileNameFull = file.getOriginalFilename();
        log.debug("fileName: {} ", fileNameFull);

        //int pos = fileNameFull.lastIndexOf(".");
        //String fileName = fileNameFull.substring(0, pos);
        //String[] loadProfile = fileName.split("_");

        String[] profile = ProfileUtil.getProfile(fileNameFull);
        String season = ProfileUtil.getSeason(profile);
        log.debug("season: {} ", season);

        String typicalDay = ProfileUtil.getTypicalDay(profile);
        log.debug("typicalDay: {} ", typicalDay);

        int mode = ProfileUtil.getMode(season, typicalDay);
        log.debug("mode: {} ", mode);
        flexProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
    }

    @Override
    public void flexProfile(
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

        // Parse file with one sheet
        // busNum;type;val1,.........val_24; if time frame = 1hour
        // busNum;type;val1,.........val_48; if time frame = 30 min
        // busNum;type;val1,.........val_96: if time frame = 15 min
        ExcelProfileReader reader = new ExcelProfileReader();
        Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelFlexProfileFile(file, isHeaderEnabled);
        //First sheet is Flex_decrease
        //Second sheet is Flex_increase
        double timeInterval = 0;
        LinkedHashMap<String, FlexElVal> mapFlexElVal = new LinkedHashMap<String, FlexElVal>();

        for (String sheetName : mapSheet.keySet()) {
            List<LoadGeneratorPower> loadsData = mapSheet.get(sheetName);
            int numColMax = ProfileUtil.getMaxNumCol(loadsData);
            timeInterval = ProfileUtil.getTimeInterval(numColMax);
            log.info("SheetName: {},  numCols:{} , Time Interval: {} ", sheetName, numColMax, timeInterval);
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

            FlexElVal flexElVal;
            for (LoadGeneratorPower demand : loadsData) {
                Long busNum = demand.getBusNum();
                String type = demand.getPowerType();
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
                    String mapKey = busNum + "_" + countHour + "_" + countMin;
                    //log.debug("SheetName: {} MapKey: ", sheetName, mapKey);
                    if (!mapFlexElVal.containsKey(mapKey)) {
                        flexElVal = new FlexElVal();
                        flexElVal.setBusNum(busNum);
                        flexElVal.setHour(countHour);
                        flexElVal.setMin(countMin);
                    } else {
                        flexElVal = mapFlexElVal.get(mapKey);
                    }
                    switch (type) {
                        case "Q":
                            if (sheetName.equalsIgnoreCase(ExcelProfilesFormat.FLEXIBILITY_SHEET_NAMES[0])) {
                                //log.debug(" decrease : Type: {}: ", type);
                                // Flex_decrease
                                flexElVal.setQfmaxDn(value);
                            } else {
                                //log.debug(" increase Type: {}: ", type);
                                // Flex increase
                                flexElVal.setQfmaxUp(value);
                            }
                            break;
                        default:
                            // as Default we assume that type is ="P"
                            if (sheetName.equalsIgnoreCase(ExcelProfilesFormat.FLEXIBILITY_SHEET_NAMES[0])) {
                                // Flex_decrease
                                //log.debug(" decrease : Type: {}: ", type);
                                flexElVal.setPfmaxDn(value);
                            } else {
                                // Flex increase
                                //log.debug(" decrease : Type: {}: ", type);
                                flexElVal.setPfmaxUp(value);
                            }
                            break;
                    }

                    mapFlexElVal.put(mapKey, flexElVal);
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
        }

        //clean old value loaded precedentily, if there are.
        //20221130 comment
        //cleanOldValues(file, mode, timeInterval, season, typicalDay, networkOpt);

        // SavefIle in table: inputFile
        InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
            file,
            networkMapper.toDto(network),
            AttestConstants.INPUT_FILE_FLEXIBILITY_DESCR
        );
        //log.debug("New File: {}, saved in InputFile ", inputFileDto.getFileName());

        FlexProfile lp = saveFlexProfile(mode, timeInterval, season, typicalDay, networkOpt, inputFileDto);
        //log.debug("New Flex Profile: {} saved in FlexProfile" + lp);

        Instant start = Instant.now();
        List<FlexElVal> genVals = saveFlexProfileVals(mapFlexElVal, lp);
        Duration d = Duration.between(start, Instant.now());
        //log.info(" Insert into FlexElVal takes: {} seconds", d.toSeconds());

        log.info(
            "New Flex Profile values saved for network: {}, mode: {} , timeInterval: {} season: {} , typicalDay: {}  ",
            network,
            mode,
            timeInterval,
            season,
            typicalDay
        );
    }

    private FlexProfile saveFlexProfile(
        Integer mode,
        Double timeInterval,
        String season,
        String typicalDay,
        Optional<Network> networkOpt,
        InputFileDTO inputFileDto
    ) {
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        FlexProfile flexProfile = new FlexProfile();
        flexProfile.setSeason(season);
        flexProfile.setTypicalDay(typicalDay);
        flexProfile.setMode(mode);
        flexProfile.setTimeInterval(timeInterval);
        flexProfile.setUploadDateTime(Instant.now());
        flexProfile.setNetwork(networkOpt.get());
        flexProfile.setInputFile(inputFile);
        return flexProfileRepository.save(flexProfile);
    }

    private List<FlexElVal> saveFlexProfileVals(LinkedHashMap<String, FlexElVal> mapLoadElVal, FlexProfile lp) {
        List<FlexElVal> flexVals = new ArrayList<FlexElVal>();
        for (String key : mapLoadElVal.keySet()) {
            FlexElVal lev = mapLoadElVal.get(key);
            lev.setFlexProfile(lp);
            FlexElVal newLoadElVal = flexElValRepository.save(mapLoadElVal.get(key));
            flexVals.add(newLoadElVal);
        }
        return flexVals;
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
