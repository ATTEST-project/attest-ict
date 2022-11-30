package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetUGCableMapperTest {

    private AssetUGCableMapper assetUGCableMapper;

    @BeforeEach
    public void setUp() {
        assetUGCableMapper = new AssetUGCableMapperImpl();
    }
}
