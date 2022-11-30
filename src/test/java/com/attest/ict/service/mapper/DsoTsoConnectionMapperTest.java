package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DsoTsoConnectionMapperTest {

    private DsoTsoConnectionMapper dsoTsoConnectionMapper;

    @BeforeEach
    public void setUp() {
        dsoTsoConnectionMapper = new DsoTsoConnectionMapperImpl();
    }
}
