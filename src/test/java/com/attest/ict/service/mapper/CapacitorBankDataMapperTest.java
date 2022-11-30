package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CapacitorBankDataMapperTest {

    private CapacitorBankDataMapper capacitorBankDataMapper;

    @BeforeEach
    public void setUp() {
        capacitorBankDataMapper = new CapacitorBankDataMapperImpl();
    }
}
