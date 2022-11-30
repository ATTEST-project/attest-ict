package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchProfileMapperTest {

    private BranchProfileMapper branchProfileMapper;

    @BeforeEach
    public void setUp() {
        branchProfileMapper = new BranchProfileMapperImpl();
    }
}
