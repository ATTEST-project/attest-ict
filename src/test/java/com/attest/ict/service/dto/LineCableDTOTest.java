package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineCableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineCableDTO.class);
        LineCableDTO lineCableDTO1 = new LineCableDTO();
        lineCableDTO1.setId(1L);
        LineCableDTO lineCableDTO2 = new LineCableDTO();
        assertThat(lineCableDTO1).isNotEqualTo(lineCableDTO2);
        lineCableDTO2.setId(lineCableDTO1.getId());
        assertThat(lineCableDTO1).isEqualTo(lineCableDTO2);
        lineCableDTO2.setId(2L);
        assertThat(lineCableDTO1).isNotEqualTo(lineCableDTO2);
        lineCableDTO1.setId(null);
        assertThat(lineCableDTO1).isNotEqualTo(lineCableDTO2);
    }
}
