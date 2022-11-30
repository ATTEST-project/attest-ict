package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolLogFileMapperTest {

    private ToolLogFileMapper toolLogFileMapper;

    @BeforeEach
    public void setUp() {
        toolLogFileMapper = new ToolLogFileMapperImpl();
    }
}
