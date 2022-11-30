package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolParameterMapperTest {

    private ToolParameterMapper toolParameterMapper;

    @BeforeEach
    public void setUp() {
        toolParameterMapper = new ToolParameterMapperImpl();
    }
}
