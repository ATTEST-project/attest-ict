package com.attest.ict.web.rest;

import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.service.BusService;
import com.attest.ict.service.ChartLoadService;
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
public class ChartLoadResource {

    private final Logger log = LoggerFactory.getLogger(ChartLoadResource.class);

    @Autowired
    NetworkService networkService;

    @Autowired
    BusService busService;

    @Autowired
    ChartLoadService chartLoadService;

    @GetMapping("/load-seasons/{networkId}/{busNum}")
    public ResponseEntity<?> allSeasonProfile(@PathVariable("networkId") Long networkId, @PathVariable(value = "busNum") Long busNum)
        throws URISyntaxException {
        log.info("REST request: /api/chart/load-seasons for networkId: {}, Load busNum: {} ", networkId, busNum);

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (busNum == null) {
            String errMsg = "busNum is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Bus bus = busService.findByBusNumAndNetworkId(busNum, networkId);
        if (bus == null) {
            String errMsg = "Bus: " + busNum + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartLoadService.loadGroupedBySeason(networkOpt.get(), bus);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/load-days/{networkId}/{busNum}")
    public ResponseEntity<?> allTypicalDayProfile(@PathVariable("networkId") Long networkId, @PathVariable(value = "busNum") Long busNum)
        throws URISyntaxException {
        log.info("REST request: /api/chart/load-days for networkId: {}, load busNum: {} ", networkId, busNum);

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (busNum == null) {
            String errMsg = "busNum is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        Optional<Network> networkOpt = networkService.findById(networkId);
        if (!networkOpt.isPresent()) {
            String errMsg = "Network with id: " + networkId + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Bus bus = busService.findByBusNumAndNetworkId(busNum, networkId);
        if (bus == null) {
            String errMsg = "Bus: " + busNum + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartLoadService.loadGroupedByTypicalDay(networkOpt.get(), bus);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/load-seasons-day/{networkId}/{busNum}/{typicalDay}")
    public ResponseEntity<?> allSeasonForOneTypicalDay(
        @PathVariable("networkId") Long networkId,
        @PathVariable(value = "busNum") Long busNum,
        @PathVariable("typicalDay") String typicalDay
    ) throws URISyntaxException {
        log.info(
            " REST request: /api/chart/load-seasons-day for networkId: {}, Load busNum: {}, for one typicalDay:{} ",
            networkId,
            busNum,
            typicalDay
        );

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (busNum == null) {
            String errMsg = "busNum is missing";
            log.error(errMsg);
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
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Bus bus = busService.findByBusNumAndNetworkId(busNum, networkId);
        if (bus == null) {
            String errMsg = "Bus: " + busNum + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartLoadService.loadGroupedBySeasonForTypicalDay(networkOpt.get(), bus, typicalDay);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/load-days-season/{networkId}/{busNum}/{season}")
    public ResponseEntity<?> allTypicalDayForOneSeason(
        @PathVariable("networkId") Long networkId,
        @PathVariable(value = "busNum") Long busNum,
        @PathVariable(value = "season") String season
    ) throws URISyntaxException {
        log.info(
            "REST request: /api/chart/load-days-season for networkId: {}, Load busNum: {},for one season: {} ",
            networkId,
            busNum,
            season
        );

        if (networkId == null) {
            String errMsg = "networkId is missing";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (busNum == null) {
            String errMsg = "busNum is missing";
            log.error(errMsg);
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
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        Bus bus = busService.findByBusNumAndNetworkId(busNum, networkId);
        if (bus == null) {
            String errMsg = "Bus: " + busNum + " not found!";
            log.error(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        try {
            ChartDataDTO chartData = chartLoadService.loadGroupedByTypicalDayForSeason(networkOpt.get(), bus, season);
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
