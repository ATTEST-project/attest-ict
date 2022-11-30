package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlexCostMapperTest {

    private FlexCostMapper flexCostMapper;

    @BeforeEach
    public void setUp() {
        flexCostMapper = new FlexCostMapperImpl();
    }
}
