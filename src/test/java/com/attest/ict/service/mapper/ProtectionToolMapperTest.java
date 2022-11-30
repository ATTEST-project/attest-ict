package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtectionToolMapperTest {

    private ProtectionToolMapper protectionToolMapper;

    @BeforeEach
    public void setUp() {
        protectionToolMapper = new ProtectionToolMapperImpl();
    }
}
