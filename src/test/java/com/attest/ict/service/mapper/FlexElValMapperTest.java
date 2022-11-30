package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlexElValMapperTest {

    private FlexElValMapper flexElValMapper;

    @BeforeEach
    public void setUp() {
        flexElValMapper = new FlexElValMapperImpl();
    }
}
