package com.attest.ict.service;

import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Network;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ChartLoadService {
    ChartDataDTO loadGroupedBySeason(Network network, Bus bus);

    ChartDataDTO loadGroupedBySeasonForTypicalDay(Network network, Bus bus, String typicalDay);

    ChartDataDTO loadGroupedByTypicalDay(Network network, Bus bus);

    ChartDataDTO loadGroupedByTypicalDayForSeason(Network network, Bus bus, String season);
}
