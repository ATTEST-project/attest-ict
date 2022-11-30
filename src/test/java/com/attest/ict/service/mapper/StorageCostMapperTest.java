package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageCostMapperTest {

    private StorageCostMapper storageCostMapper;

    @BeforeEach
    public void setUp() {
        storageCostMapper = new StorageCostMapperImpl();
    }
}
