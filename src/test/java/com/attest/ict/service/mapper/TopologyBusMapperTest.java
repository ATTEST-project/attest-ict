package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopologyBusMapperTest {

    private TopologyBusMapper topologyBusMapper;

    @BeforeEach
    public void setUp() {
        topologyBusMapper = new TopologyBusMapperImpl();
    }
}
