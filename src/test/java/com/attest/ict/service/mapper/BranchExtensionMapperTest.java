package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchExtensionMapperTest {

    private BranchExtensionMapper branchExtensionMapper;

    @BeforeEach
    public void setUp() {
        branchExtensionMapper = new BranchExtensionMapperImpl();
    }
}
