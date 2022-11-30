package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchElValMapperTest {

    private BranchElValMapper branchElValMapper;

    @BeforeEach
    public void setUp() {
        branchElValMapper = new BranchElValMapperImpl();
    }
}
