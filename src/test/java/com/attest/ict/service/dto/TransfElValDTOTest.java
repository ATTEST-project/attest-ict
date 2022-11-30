package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfElValDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfElValDTO.class);
        TransfElValDTO transfElValDTO1 = new TransfElValDTO();
        transfElValDTO1.setId(1L);
        TransfElValDTO transfElValDTO2 = new TransfElValDTO();
        assertThat(transfElValDTO1).isNotEqualTo(transfElValDTO2);
        transfElValDTO2.setId(transfElValDTO1.getId());
        assertThat(transfElValDTO1).isEqualTo(transfElValDTO2);
        transfElValDTO2.setId(2L);
        assertThat(transfElValDTO1).isNotEqualTo(transfElValDTO2);
        transfElValDTO1.setId(null);
        assertThat(transfElValDTO1).isNotEqualTo(transfElValDTO2);
    }
}
