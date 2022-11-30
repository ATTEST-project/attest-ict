package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoltageLevelMapperTest {

    private VoltageLevelMapper voltageLevelMapper;

    @BeforeEach
    public void setUp() {
        voltageLevelMapper = new VoltageLevelMapperImpl();
    }
}
