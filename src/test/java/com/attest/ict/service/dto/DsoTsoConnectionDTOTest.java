package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DsoTsoConnectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DsoTsoConnectionDTO.class);
        DsoTsoConnectionDTO dsoTsoConnectionDTO1 = new DsoTsoConnectionDTO();
        dsoTsoConnectionDTO1.setId(1L);
        DsoTsoConnectionDTO dsoTsoConnectionDTO2 = new DsoTsoConnectionDTO();
        assertThat(dsoTsoConnectionDTO1).isNotEqualTo(dsoTsoConnectionDTO2);
        dsoTsoConnectionDTO2.setId(dsoTsoConnectionDTO1.getId());
        assertThat(dsoTsoConnectionDTO1).isEqualTo(dsoTsoConnectionDTO2);
        dsoTsoConnectionDTO2.setId(2L);
        assertThat(dsoTsoConnectionDTO1).isNotEqualTo(dsoTsoConnectionDTO2);
        dsoTsoConnectionDTO1.setId(null);
        assertThat(dsoTsoConnectionDTO1).isNotEqualTo(dsoTsoConnectionDTO2);
    }
}
