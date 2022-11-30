package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformerMapperTest {

    private TransformerMapper transformerMapper;

    @BeforeEach
    public void setUp() {
        transformerMapper = new TransformerMapperImpl();
    }
}
