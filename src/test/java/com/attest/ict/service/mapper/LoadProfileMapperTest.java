package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoadProfileMapperTest {

    private LoadProfileMapper loadProfileMapper;

    @BeforeEach
    public void setUp() {
        loadProfileMapper = new LoadProfileMapperImpl();
    }
}
