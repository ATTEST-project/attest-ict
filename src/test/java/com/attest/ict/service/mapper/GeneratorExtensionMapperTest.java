package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneratorExtensionMapperTest {

    private GeneratorExtensionMapper generatorExtensionMapper;

    @BeforeEach
    public void setUp() {
        generatorExtensionMapper = new GeneratorExtensionMapperImpl();
    }
}
