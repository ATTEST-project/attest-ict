package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetTransformerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetTransformer.class);
        AssetTransformer assetTransformer1 = new AssetTransformer();
        assetTransformer1.setId(1L);
        AssetTransformer assetTransformer2 = new AssetTransformer();
        assetTransformer2.setId(assetTransformer1.getId());
        assertThat(assetTransformer1).isEqualTo(assetTransformer2);
        assetTransformer2.setId(2L);
        assertThat(assetTransformer1).isNotEqualTo(assetTransformer2);
        assetTransformer1.setId(null);
        assertThat(assetTransformer1).isNotEqualTo(assetTransformer2);
    }
}
