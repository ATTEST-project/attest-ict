package com.attest.ict.web.rest;

import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.service.ChartGeneratorService;
import com.attest.ict.service.GeneratorService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/chart")
public class ChartGeneratorResource {

    private final Logger log = LoggerFactory.getLogger(ChartGeneratorResource.class);

    @Autowired
    NetworkService networkService;

    @Autowired
    GeneratorService generatorService;

    @Autowired
    ChartGeneratorService chartService;

    @GetMapping("/gen-seasons/{networkId}/{genId}")
    public ResponseEntity<?> allSeasonProfile(@PathVariable("networkId") Long networkId, @PathVariable(value = "genId") Long genId)
        throws URISyntaxException {
        log.info("REST request: /api/chart/gen-seasons/ for networkId: {}, generator genId: {} ", networkId, genId);

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (genId == null) {
            String errMsg = "genId is  missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Optional<Generator> generatorOpt = generatorService.findByIdAndNetworkId(genId, networkId);
        if (!generatorOpt.isPresent()) {
            String errMsg = "Generator with id: " + genId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartService.genGroupedBySeason(networkOpt.get(), generatorOpt.get());
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gen-days/{networkId}/{genId}")
    public ResponseEntity<?> allTypicalDayProfile(@PathVariable("networkId") Long networkId, @PathVariable(value = "genId") Long genId)
        throws URISyntaxException {
        log.info("REST request: /api/chart/gen-days/ for networkId: {}, generator genId: {} ", networkId, genId);

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (genId == null) {
            String errMsg = "genId is  missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Optional<Generator> generatorOpt = generatorService.findByIdAndNetworkId(genId, networkId);
        if (!generatorOpt.isPresent()) {
            String errMsg = "Generator with id: " + genId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartService.genGroupedByTypicalDay(networkOpt.get(), generatorOpt.get());
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gen-seasons-day/{networkId}/{genId}/{typicalDay}")
    public ResponseEntity<?> allSeasonForOneTypicalDay(
        @PathVariable("networkId") Long networkId,
        @PathVariable("genId") Long genId,
        @PathVariable("typicalDay") String typicalDay
    ) throws URISyntaxException {
        log.info(
            "REST request: /api/chart/gen-seasons-day for networkId: {}, generator genId: {}, typicalDay:{} ",
            networkId,
            genId,
            typicalDay
        );
        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (genId == null) {
            String errMsg = "genId is  missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (!ProfileConstants.DAY_SET.contains(typicalDay)) {
            String errMsg = "Typical Day is not valid!  Values allowed are:" + ProfileConstants.DAY_SET.toString();
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Optional<Generator> generatorOpt = generatorService.findByIdAndNetworkId(genId, networkId);
        if (!generatorOpt.isPresent()) {
            String errMsg = "Generator with id: " + genId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartService.genGroupedBySeasonForTypicalDay(networkOpt.get(), generatorOpt.get(), typicalDay);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gen-days-season/{networkId}/{genId}/{season}")
    public ResponseEntity<?> allTypicalDayForOneSeason(
        @PathVariable("networkId") Long networkId,
        @PathVariable(value = "genId") Long genId,
        @PathVariable(value = "season") String season
    ) throws URISyntaxException {
        log.info("REST request: /api/chart/gen-days-season for networkId: {}, generator genId: {}, season: {} ", networkId, genId, season);

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (genId == null) {
            String errMsg = "genId is  missing";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (!ProfileConstants.SEASON_SET.contains(season)) {
            String errMsg = "Season is not valid. Values allowed are:" + ProfileConstants.SEASON_SET.toString();
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Optional<Generator> generatorOpt = generatorService.findByIdAndNetworkId(genId, networkId);
        if (!generatorOpt.isPresent()) {
            String errMsg = "Generator with id: " + genId + " not found!";
            log.warn(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartService.genGroupedByTypicalDayForSeason(networkOpt.get(), generatorOpt.get(), season);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
