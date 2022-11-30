package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenTagMapperTest {

    private GenTagMapper genTagMapper;

    @BeforeEach
    public void setUp() {
        genTagMapper = new GenTagMapperImpl();
    }
}
