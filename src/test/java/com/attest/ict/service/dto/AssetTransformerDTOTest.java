package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetTransformerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetTransformerDTO.class);
        AssetTransformerDTO assetTransformerDTO1 = new AssetTransformerDTO();
        assetTransformerDTO1.setId(1L);
        AssetTransformerDTO assetTransformerDTO2 = new AssetTransformerDTO();
        assertThat(assetTransformerDTO1).isNotEqualTo(assetTransformerDTO2);
        assetTransformerDTO2.setId(assetTransformerDTO1.getId());
        assertThat(assetTransformerDTO1).isEqualTo(assetTransformerDTO2);
        assetTransformerDTO2.setId(2L);
        assertThat(assetTransformerDTO1).isNotEqualTo(assetTransformerDTO2);
        assetTransformerDTO1.setId(null);
        assertThat(assetTransformerDTO1).isNotEqualTo(assetTransformerDTO2);
    }
}
