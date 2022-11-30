package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenElValDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenElValDTO.class);
        GenElValDTO genElValDTO1 = new GenElValDTO();
        genElValDTO1.setId(1L);
        GenElValDTO genElValDTO2 = new GenElValDTO();
        assertThat(genElValDTO1).isNotEqualTo(genElValDTO2);
        genElValDTO2.setId(genElValDTO1.getId());
        assertThat(genElValDTO1).isEqualTo(genElValDTO2);
        genElValDTO2.setId(2L);
        assertThat(genElValDTO1).isNotEqualTo(genElValDTO2);
        genElValDTO1.setId(null);
        assertThat(genElValDTO1).isNotEqualTo(genElValDTO2);
    }
}
