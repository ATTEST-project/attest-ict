package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlexProfileMapperTest {

    private FlexProfileMapper flexProfileMapper;

    @BeforeEach
    public void setUp() {
        flexProfileMapper = new FlexProfileMapperImpl();
    }
}
