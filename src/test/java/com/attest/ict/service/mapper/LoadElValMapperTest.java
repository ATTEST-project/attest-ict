package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoadElValMapperTest {

    private LoadElValMapper loadElValMapper;

    @BeforeEach
    public void setUp() {
        loadElValMapper = new LoadElValMapperImpl();
    }
}
