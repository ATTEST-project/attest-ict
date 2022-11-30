package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformerDTO.class);
        TransformerDTO transformerDTO1 = new TransformerDTO();
        transformerDTO1.setId(1L);
        TransformerDTO transformerDTO2 = new TransformerDTO();
        assertThat(transformerDTO1).isNotEqualTo(transformerDTO2);
        transformerDTO2.setId(transformerDTO1.getId());
        assertThat(transformerDTO1).isEqualTo(transformerDTO2);
        transformerDTO2.setId(2L);
        assertThat(transformerDTO1).isNotEqualTo(transformerDTO2);
        transformerDTO1.setId(null);
        assertThat(transformerDTO1).isNotEqualTo(transformerDTO2);
    }
}
