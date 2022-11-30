package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransfElValMapperTest {

    private TransfElValMapper transfElValMapper;

    @BeforeEach
    public void setUp() {
        transfElValMapper = new TransfElValMapperImpl();
    }
}
