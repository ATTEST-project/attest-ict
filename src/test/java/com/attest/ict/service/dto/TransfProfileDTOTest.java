package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfProfileDTO.class);
        TransfProfileDTO transfProfileDTO1 = new TransfProfileDTO();
        transfProfileDTO1.setId(1L);
        TransfProfileDTO transfProfileDTO2 = new TransfProfileDTO();
        assertThat(transfProfileDTO1).isNotEqualTo(transfProfileDTO2);
        transfProfileDTO2.setId(transfProfileDTO1.getId());
        assertThat(transfProfileDTO1).isEqualTo(transfProfileDTO2);
        transfProfileDTO2.setId(2L);
        assertThat(transfProfileDTO1).isNotEqualTo(transfProfileDTO2);
        transfProfileDTO1.setId(null);
        assertThat(transfProfileDTO1).isNotEqualTo(transfProfileDTO2);
    }
}
