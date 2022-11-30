package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusMapperTest {

    private BusMapper busMapper;

    @BeforeEach
    public void setUp() {
        busMapper = new BusMapperImpl();
    }
}
