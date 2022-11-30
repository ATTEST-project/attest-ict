package com.attest.ict.web.rest;

import com.attest.ict.custom.exception.ImportToolCsvFileException;
import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.service.CsvGeneratorProfileService;
import com.attest.ict.service.CsvLoadProfileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.ToolService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
public class CsvResource {

    private final Logger log = LoggerFactory.getLogger(ExcelResource.class);

    @Autowired
    CsvLoadProfileService csvLoadProfileService;

    @Autowired
    CsvGeneratorProfileService csvGeneratorProfileService;

    @Autowired
    NetworkService networkService;

    @Autowired
    ToolService toolService;

    @PostMapping("/load-profile")
    public ResponseEntity<ResponseMessage> uploadLoadProfile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("networkId") Long networkId,
        @RequestParam("mode") Integer mode,
        @RequestParam("season") String season,
        @RequestParam("typicalDay") String typicalDay,
        @RequestParam(value = "headerEnabled", required = false) Boolean headerEnabled
    ) {
        log.info(
            "REST request to upload load profile from '.csv' file, with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a load ''.csv' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!CsvFileReader.hasCSVFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Missing networkId"));
            }

            if (!networkOpt.isPresent()) {
                message = "- Network " + networkId + " not found ";
                log.info(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    message =
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                if (!ProfileUtil.validateSeason(season)) {
                    message = "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    message = "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                csvLoadProfileService.loadProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
            } else {
                // WARNING loadfile name should be composed as: networkName_season_typicalDay.csv
                csvLoadProfileService.loadProfile(file, networkOpt, headerEnabled);
            }

            message = "File uploaded successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/first-upload-tool")
    public ResponseEntity<ResponseMessage> firstImportTool(@RequestParam("file") MultipartFile file) {
        log.info("REST request to upload tool from '.csv' file");
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a tool.csv file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!CsvFileReader.hasCSVFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            toolService.firstImportToolFromCsv(file);
            message = "- Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (ImportToolCsvFileException importExc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(importExc.getMessage()));
        }
    }

    @PostMapping("/gen-profile")
    public ResponseEntity<ResponseMessage> uploadGeneratorProfile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("networkId") Long networkId,
        @RequestParam("mode") Integer mode,
        @RequestParam("season") String season,
        @RequestParam("typicalDay") String typicalDay,
        @RequestParam(value = "headerEnabled", required = false) Boolean headerEnabled
    ) {
        log.info(
            "REST request to upload generator profile from '.csv' file,  with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a generator ''.csv' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!CsvFileReader.hasCSVFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Missing networkId"));
            }

            if (!networkOpt.isPresent()) {
                message = "- Network " + networkId + " not found ";
                log.info(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    message =
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                if (!ProfileUtil.validateSeason(season)) {
                    message = "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    message = "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
                }

                csvGeneratorProfileService.generatorProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
            } else {
                // WARNING loadfile name should be composed as: networkName_season_typicalDay.csv
                csvGeneratorProfileService.generatorProfile(file, networkOpt, headerEnabled);
            }

            message = "- Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "- Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }
}
