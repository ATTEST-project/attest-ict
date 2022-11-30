package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaseMVAMapperTest {

    private BaseMVAMapper baseMVAMapper;

    @BeforeEach
    public void setUp() {
        baseMVAMapper = new BaseMVAMapperImpl();
    }
}
