package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodeMapperTest {

    private NodeMapper nodeMapper;

    @BeforeEach
    public void setUp() {
        nodeMapper = new NodeMapperImpl();
    }
}
