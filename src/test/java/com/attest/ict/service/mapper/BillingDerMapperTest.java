package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillingDerMapperTest {

    private BillingDerMapper billingDerMapper;

    @BeforeEach
    public void setUp() {
        billingDerMapper = new BillingDerMapperImpl();
    }
}
