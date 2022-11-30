package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolMapperTest {

    private ToolMapper toolMapper;

    @BeforeEach
    public void setUp() {
        toolMapper = new ToolMapperImpl();
    }
}
