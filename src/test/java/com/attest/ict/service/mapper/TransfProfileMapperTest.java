package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransfProfileMapperTest {

    private TransfProfileMapper transfProfileMapper;

    @BeforeEach
    public void setUp() {
        transfProfileMapper = new TransfProfileMapperImpl();
    }
}
