package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineCableMapperTest {

    private LineCableMapper lineCableMapper;

    @BeforeEach
    public void setUp() {
        lineCableMapper = new LineCableMapperImpl();
    }
}
