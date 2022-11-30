package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopologyMapperTest {

    private TopologyMapper topologyMapper;

    @BeforeEach
    public void setUp() {
        topologyMapper = new TopologyMapperImpl();
    }
}
