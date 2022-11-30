package com.attest.ict.service.impl;

import com.attest.ict.chart.utils.ChartDataSetUtil;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.service.ChartGeneratorService;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChartGeneratorServiceImpl implements ChartGeneratorService {

    private final Logger log = LoggerFactory.getLogger(ChartGeneratorServiceImpl.class);

    @Autowired
    GenElValRepository genElValRepository;

    @Autowired
    GenProfileRepository genProfileRepository;

    @Override
    public ChartDataDTO genGroupedBySeason(Network network, Generator generator) {
        Long genId = generator.getId();
        //2 = a representative business day for a season, 4 = a representative weekend for a season
        List<String> mode = new ArrayList<String>();
        mode.add("2");
        mode.add("4");
        List<Tuple> values = genElValRepository.findGenPQGroupBySeason(network.getId(), genId, mode);
        String networkName = network.getName();
        String chartTitle = networkName + " Gen(P) on genId: " + generator.getId() + ". Considering  all Season";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, true, chartTitle);
    }

    @Override
    public ChartDataDTO genGroupedBySeasonForTypicalDay(Network network, Generator generator, String typicalDay) {
        Long genId = generator.getId();
        //2 = a representative business day for a season, 4 = a representative weekend for a season
        List<String> mode = new ArrayList<String>();
        mode.add("2");
        mode.add("4");
        List<Tuple> values = genElValRepository.findGenPQGroupBySeasonForTypicalDay(network.getId(), genId, typicalDay, mode);
        String networkName = network.getName();

        String typicalDayExt = ProfileConstants.MAP_TIPICAL_DAY.get(typicalDay);
        String chartTitle = networkName + " Gen(P) on genId: " + generator.getId() + ". Considering " + typicalDayExt + " of all Season";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, true, chartTitle);
    }

    @Override
    public ChartDataDTO genGroupedByTypicalDay(Network network, Generator generator) {
        Long genId = generator.getId();
        //2 = a representative business day for a season, 4 = a representative weekend for a season
        List<String> mode = new ArrayList<String>();
        mode.add("2");
        mode.add("4");
        List<Tuple> values = genElValRepository.findGenPQGroupByTypicalDay(network.getId(), genId, mode);
        String networkName = network.getName();
        String chartTitle = networkName + " Gen(P) on genId: " + generator.getId() + ". Considering all Typical Day";
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, false, chartTitle);
    }

    @Override
    public ChartDataDTO genGroupedByTypicalDayForSeason(Network network, Generator generator, String season) {
        Long genId = generator.getId();
        //2 = a representative business day for a season, 4 = a representative weekend for a season
        List<String> mode = new ArrayList<String>();
        mode.add("2");
        mode.add("4");
        List<Tuple> values = genElValRepository.findGenPQGroupByTypicalDay(network.getId(), genId, mode);
        String networkName = network.getName();

        String seasonExt = ProfileConstants.MAP_SEASON.get(season);
        String chartTitle = networkName + " Gen(P) on genId: " + generator.getId() + ". Considering all Typical Day of " + seasonExt;
        return ChartDataSetUtil.chartDataLoad(network.getName(), values, false, chartTitle);
    }
}
