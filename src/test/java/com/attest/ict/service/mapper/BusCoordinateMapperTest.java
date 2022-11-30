package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusCoordinateMapperTest {

    private BusCoordinateMapper busCoordinateMapper;

    @BeforeEach
    public void setUp() {
        busCoordinateMapper = new BusCoordinateMapperImpl();
    }
}
