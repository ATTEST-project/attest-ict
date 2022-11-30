package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutputFileMapperTest {

    private OutputFileMapper outputFileMapper;

    @BeforeEach
    public void setUp() {
        outputFileMapper = new OutputFileMapperImpl();
    }
}
