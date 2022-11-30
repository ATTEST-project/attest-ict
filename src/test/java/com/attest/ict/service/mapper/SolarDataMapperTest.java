package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolarDataMapperTest {

    private SolarDataMapper solarDataMapper;

    @BeforeEach
    public void setUp() {
        solarDataMapper = new SolarDataMapperImpl();
    }
}
