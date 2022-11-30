package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenTagDTO.class);
        GenTagDTO genTagDTO1 = new GenTagDTO();
        genTagDTO1.setId(1L);
        GenTagDTO genTagDTO2 = new GenTagDTO();
        assertThat(genTagDTO1).isNotEqualTo(genTagDTO2);
        genTagDTO2.setId(genTagDTO1.getId());
        assertThat(genTagDTO1).isEqualTo(genTagDTO2);
        genTagDTO2.setId(2L);
        assertThat(genTagDTO1).isNotEqualTo(genTagDTO2);
        genTagDTO1.setId(null);
        assertThat(genTagDTO1).isNotEqualTo(genTagDTO2);
    }
}
