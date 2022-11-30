package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchMapperTest {

    private BranchMapper branchMapper;

    @BeforeEach
    public void setUp() {
        branchMapper = new BranchMapperImpl();
    }
}
