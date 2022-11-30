package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenCostMapperTest {

    private GenCostMapper genCostMapper;

    @BeforeEach
    public void setUp() {
        genCostMapper = new GenCostMapperImpl();
    }
}
