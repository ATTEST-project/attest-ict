package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenProfileMapperTest {

    private GenProfileMapper genProfileMapper;

    @BeforeEach
    public void setUp() {
        genProfileMapper = new GenProfileMapperImpl();
    }
}
