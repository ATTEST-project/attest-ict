package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.excel.reader.ExcelProfileReader;
import com.attest.ict.helper.excel.util.ExcelFileFormat;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.helper.utils.ProfileUtil;
import com.attest.ict.service.ExcelAllProfileService;
import com.attest.ict.service.ExcelFlexProfileService;
import com.attest.ict.service.ExcelGenProfileService;
import com.attest.ict.service.ExcelLoadProfileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelResource {

    private final Logger log = LoggerFactory.getLogger(ExcelResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    ExcelLoadProfileService excelLoadProfileService;

    @Autowired
    ExcelGenProfileService excelGenProfileService;

    @Autowired
    ExcelFlexProfileService excelFlexProfileService;

    @Autowired
    ExcelAllProfileService excelProfileService;

    @Autowired
    NetworkService networkService;

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
            "REST request to upload load profile from '.xlsx' file, with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a load ''.xlsx' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!ExcelFileUtils.hasExcelFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                throw new BadRequestAlertException("Invalid id", null, "idnull");
            }

            if (!networkOpt.isPresent()) {
                log.debug("- Network not found: {}", networkId);
                throw new BadRequestAlertException("Sorry network not found", null, "idnotfound");
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    throw new BadRequestAlertException(
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateSeason(season)) {
                    throw new BadRequestAlertException(
                        "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    throw new BadRequestAlertException(
                        "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET,
                        null,
                        null
                    );
                }
                excelLoadProfileService.loadProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
            } else {
                // WARNING loadfile name should be composed as:
                // networkName_season_typicalDay.csv
                excelLoadProfileService.loadProfile(file, networkOpt, headerEnabled);
            }

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    // ---- Generator profile
    @PostMapping("/gen-profile")
    public ResponseEntity<ResponseMessage> uploadGenProfile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("networkId") Long networkId,
        @RequestParam("mode") Integer mode,
        @RequestParam("season") String season,
        @RequestParam("typicalDay") String typicalDay,
        @RequestParam(value = "headerEnabled", required = false) Boolean headerEnabled
    ) {
        log.info(
            "REST request to upload gen profile from '.xlsx' file,  with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a gen profile ''.xlsx' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!ExcelFileUtils.hasExcelFormat(file)) {
                message = "-Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                throw new BadRequestAlertException("Invalid id", null, "idnull");
            }

            if (!networkOpt.isPresent()) {
                log.debug("- Network not found: {}", networkId);
                throw new BadRequestAlertException("Sorry network not found", null, "idnotfound");
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    throw new BadRequestAlertException(
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateSeason(season)) {
                    throw new BadRequestAlertException(
                        "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    throw new BadRequestAlertException(
                        "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET,
                        null,
                        null
                    );
                }

                excelGenProfileService.genProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
            } else {
                // WARNING loadfile name should be composed as:
                // networkName_season_typicalDay.csv
                excelGenProfileService.genProfile(file, networkOpt, headerEnabled);
            }

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    // ---- Flexibility Profile
    @PostMapping("/flex-profile")
    public ResponseEntity<ResponseMessage> uploadFlexProfile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("networkId") Long networkId,
        @RequestParam("mode") Integer mode,
        @RequestParam("season") String season,
        @RequestParam("typicalDay") String typicalDay,
        @RequestParam(value = "headerEnabled", required = false) Boolean headerEnabled
    ) {
        log.info(
            "REST request to upload Flex profile from '.xlsx' file,  with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload a flex profile ''.xlsx' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!ExcelFileUtils.hasExcelFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                throw new BadRequestAlertException("Invalid id", null, "idnull");
            }

            if (!networkOpt.isPresent()) {
                log.debug("Network not found: {}", networkId);
                throw new BadRequestAlertException("Sorry network not found", null, "idnotfound");
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    throw new BadRequestAlertException(
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateSeason(season)) {
                    throw new BadRequestAlertException(
                        "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    throw new BadRequestAlertException(
                        "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET,
                        null,
                        null
                    );
                }

                excelFlexProfileService.flexProfile(file, networkOpt, mode, season, typicalDay, headerEnabled);
            } else {
                // WARNING loadfile name should be composed as:
                // networkName_season_typicalDay.csv
                excelFlexProfileService.flexProfile(file, networkOpt, headerEnabled);
            }

            message = "Uploaded the file succesfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    /**
     * In case of TX system, test cases are composed from one .xlsx file with
     * different sheets containing: Load, Gen, Transformer, Branch, Flexibility
     * information like P, Q, voltage magnitude, status, tap ratio
     *
     * @param file
     * @param networkId
     * @param mode
     * @param season
     * @param typicalDay
     * @param headerEnabled
     * @return
     */
    @PostMapping("/all-profile")
    public ResponseEntity<ResponseMessage> uploadAllProfile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("networkId") Long networkId,
        @RequestParam("mode") Integer mode,
        @RequestParam("season") String season,
        @RequestParam("typicalDay") String typicalDay,
        @RequestParam(value = "headerEnabled", required = false) Boolean headerEnabled
    ) {
        log.info(
            "REST request to upload upload AllProfile profile from '.xlsx' file with ARGS: networkId: {}, mode: {}, season: {}, typicalDay: {}, headerEnabled: {} ",
            networkId,
            mode,
            season,
            typicalDay,
            headerEnabled
        );
        String message = "";

        try {
            if (file.isEmpty()) {
                message = "- Please upload an auxiliary ''.xlsx' file!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            if (!ExcelFileUtils.hasExcelFormat(file)) {
                message = "- Sorry, file format is not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
            }

            Optional<Network> networkOpt = networkService.findById(networkId);
            if (networkId == null) {
                throw new BadRequestAlertException("Invalid id", null, "idnull");
            }

            if (!networkOpt.isPresent()) {
                log.debug("- Network not found: {}", networkId);
                throw new BadRequestAlertException("Sorry network not found", null, "idnotfound");
            }

            if (mode != null && season != null && typicalDay != null) {
                if (!ProfileUtil.validateMode(mode)) {
                    throw new BadRequestAlertException(
                        "- Mode: " +
                        mode +
                        " is not valid! Accepted values are: Min:" +
                        ProfileConstants.MODE_MIN +
                        ", Max: " +
                        ProfileConstants.MODE_MAX,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateSeason(season)) {
                    throw new BadRequestAlertException(
                        "- Season: " + season + " is not valid! Accepted values are: " + ProfileConstants.SEASON_SET,
                        null,
                        null
                    );
                }

                if (!ProfileUtil.validateTypicalDay(typicalDay)) {
                    throw new BadRequestAlertException(
                        "- Typical Day: " + typicalDay + " is not valid! Accepted values are: " + ProfileConstants.DAY_SET,
                        null,
                        null
                    );
                }
            } else {
                // non info available
                season = null;
                typicalDay = null;
                mode = 1; // 1 hour
            }

            excelProfileService.allProfile(file, networkOpt.get(), mode, season, typicalDay);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }
}
