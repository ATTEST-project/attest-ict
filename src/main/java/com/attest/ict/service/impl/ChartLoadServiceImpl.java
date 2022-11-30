package com.attest.ict.service.impl;

import com.attest.ict.chart.utils.ChartDataSetUtil;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.service.ChartLoadService;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import java.util.List;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChartLoadServiceImpl implements ChartLoadService {

    private final Logger log = LoggerFactory.getLogger(ChartLoadServiceImpl.class);

    @Autowired
    LoadElValRepository loadElValRepository;

    @Autowired
    LoadProfileRepository loadProfileRepository;

    @Override
    public ChartDataDTO loadGroupedBySeason(Network network, Bus bus) {
        List<Tuple> values = loadElValRepository.findLoadPQGroupBySeason(network.getId(), bus.getId());
        String networkName = network.getName();
        String chartTitle = networkName + " Load(P) on bus_num: " + bus.getBusNum() + ". Considering  all Season";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, true, chartTitle);
    }

    @Override
    public ChartDataDTO loadGroupedBySeasonForTypicalDay(Network network, Bus bus, String typicalDay) {
        List<Tuple> values = loadElValRepository.findLoadPQGroupBySeasonForTypicalDay(network.getId(), bus.getId(), typicalDay);
        String networkName = network.getName();
        String typicalDayExt = ProfileConstants.MAP_TIPICAL_DAY.get(typicalDay);
        String chartTitle = networkName + " Load(P) on bus_num: " + bus.getBusNum() + ". Considering " + typicalDayExt + " of all Season";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, true, chartTitle);
    }

    @Override
    public ChartDataDTO loadGroupedByTypicalDay(Network network, Bus bus) {
        List<Tuple> values = loadElValRepository.findLoadPQGroupByTypicalDay(network.getId(), bus.getId());
        String networkName = network.getName();
        String chartTitle = networkName + " Load(P) on bus_num: " + bus.getBusNum() + ". Considering all Typical Day";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, false, chartTitle);
    }

    @Override
    public ChartDataDTO loadGroupedByTypicalDayForSeason(Network network, Bus bus, String season) {
        List<Tuple> values = loadElValRepository.findLoadPQGroupByTypicalDayForSeason(network.getId(), bus.getId(), season);
        String networkName = network.getName();
        String seasonExt = ProfileConstants.MAP_SEASON.get(season);
        String chartTitle = networkName + " Load(P) on bus_num: " + bus.getBusNum() + ". Considering all Typical Day of " + seasonExt;
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, false, chartTitle);
    }
}
