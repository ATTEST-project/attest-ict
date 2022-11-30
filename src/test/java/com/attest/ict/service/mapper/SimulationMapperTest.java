package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationMapperTest {

    private SimulationMapper simulationMapper;

    @BeforeEach
    public void setUp() {
        simulationMapper = new SimulationMapperImpl();
    }
}
