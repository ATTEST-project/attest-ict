package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputFileMapperTest {

    private InputFileMapper inputFileMapper;

    @BeforeEach
    public void setUp() {
        inputFileMapper = new InputFileMapperImpl();
    }
}
