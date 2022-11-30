package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetTransformerMapperTest {

    private AssetTransformerMapper assetTransformerMapper;

    @BeforeEach
    public void setUp() {
        assetTransformerMapper = new AssetTransformerMapperImpl();
    }
}
