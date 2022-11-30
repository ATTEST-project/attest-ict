package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusNameMapperTest {

    private BusNameMapper busNameMapper;

    @BeforeEach
    public void setUp() {
        busNameMapper = new BusNameMapperImpl();
    }
}
