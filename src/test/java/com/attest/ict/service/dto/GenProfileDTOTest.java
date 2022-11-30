package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenProfileDTO.class);
        GenProfileDTO genProfileDTO1 = new GenProfileDTO();
        genProfileDTO1.setId(1L);
        GenProfileDTO genProfileDTO2 = new GenProfileDTO();
        assertThat(genProfileDTO1).isNotEqualTo(genProfileDTO2);
        genProfileDTO2.setId(genProfileDTO1.getId());
        assertThat(genProfileDTO1).isEqualTo(genProfileDTO2);
        genProfileDTO2.setId(2L);
        assertThat(genProfileDTO1).isNotEqualTo(genProfileDTO2);
        genProfileDTO1.setId(null);
        assertThat(genProfileDTO1).isNotEqualTo(genProfileDTO2);
    }
}
