package com.attest.ict.service;

import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ChartGeneratorService {
    ChartDataDTO genGroupedBySeason(Network network, Generator generator);

    ChartDataDTO genGroupedBySeasonForTypicalDay(Network network, Generator generator, String typicalDay);

    ChartDataDTO genGroupedByTypicalDay(Network network, Generator generator);

    ChartDataDTO genGroupedByTypicalDayForSeason(Network network, Generator generator, String season);
}
