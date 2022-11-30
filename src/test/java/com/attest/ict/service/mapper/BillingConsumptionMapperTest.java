package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillingConsumptionMapperTest {

    private BillingConsumptionMapper billingConsumptionMapper;

    @BeforeEach
    public void setUp() {
        billingConsumptionMapper = new BillingConsumptionMapperImpl();
    }
}
