package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneratorMapperTest {

    private GeneratorMapper generatorMapper;

    @BeforeEach
    public void setUp() {
        generatorMapper = new GeneratorMapperImpl();
    }
}
