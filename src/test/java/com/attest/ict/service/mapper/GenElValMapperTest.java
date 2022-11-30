package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenElValMapperTest {

    private GenElValMapper genElValMapper;

    @BeforeEach
    public void setUp() {
        genElValMapper = new GenElValMapperImpl();
    }
}
