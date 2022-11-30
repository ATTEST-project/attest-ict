package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaseMVADTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseMVADTO.class);
        BaseMVADTO baseMVADTO1 = new BaseMVADTO();
        baseMVADTO1.setId(1L);
        BaseMVADTO baseMVADTO2 = new BaseMVADTO();
        assertThat(baseMVADTO1).isNotEqualTo(baseMVADTO2);
        baseMVADTO2.setId(baseMVADTO1.getId());
        assertThat(baseMVADTO1).isEqualTo(baseMVADTO2);
        baseMVADTO2.setId(2L);
        assertThat(baseMVADTO1).isNotEqualTo(baseMVADTO2);
        baseMVADTO1.setId(null);
        assertThat(baseMVADTO1).isNotEqualTo(baseMVADTO2);
    }
}
