package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetUGCableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetUGCable.class);
        AssetUGCable assetUGCable1 = new AssetUGCable();
        assetUGCable1.setId(1L);
        AssetUGCable assetUGCable2 = new AssetUGCable();
        assetUGCable2.setId(assetUGCable1.getId());
        assertThat(assetUGCable1).isEqualTo(assetUGCable2);
        assetUGCable2.setId(2L);
        assertThat(assetUGCable1).isNotEqualTo(assetUGCable2);
        assetUGCable1.setId(null);
        assertThat(assetUGCable1).isNotEqualTo(assetUGCable2);
    }
}
