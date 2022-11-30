package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WindDataMapperTest {

    private WindDataMapper windDataMapper;

    @BeforeEach
    public void setUp() {
        windDataMapper = new WindDataMapperImpl();
    }
}
