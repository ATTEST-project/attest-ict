package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetUGCableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetUGCableDTO.class);
        AssetUGCableDTO assetUGCableDTO1 = new AssetUGCableDTO();
        assetUGCableDTO1.setId(1L);
        AssetUGCableDTO assetUGCableDTO2 = new AssetUGCableDTO();
        assertThat(assetUGCableDTO1).isNotEqualTo(assetUGCableDTO2);
        assetUGCableDTO2.setId(assetUGCableDTO1.getId());
        assertThat(assetUGCableDTO1).isEqualTo(assetUGCableDTO2);
        assetUGCableDTO2.setId(2L);
        assertThat(assetUGCableDTO1).isNotEqualTo(assetUGCableDTO2);
        assetUGCableDTO1.setId(null);
        assertThat(assetUGCableDTO1).isNotEqualTo(assetUGCableDTO2);
    }
}
