package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NetworkMapperTest {

    private NetworkMapper networkMapper;

    @BeforeEach
    public void setUp() {
        networkMapper = new NetworkMapperImpl();
    }
}
