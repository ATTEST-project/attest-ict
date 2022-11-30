package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusExtensionMapperTest {

    private BusExtensionMapper busExtensionMapper;

    @BeforeEach
    public void setUp() {
        busExtensionMapper = new BusExtensionMapperImpl();
    }
}
