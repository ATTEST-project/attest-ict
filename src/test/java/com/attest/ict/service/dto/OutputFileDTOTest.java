package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutputFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputFileDTO.class);
        OutputFileDTO outputFileDTO1 = new OutputFileDTO();
        outputFileDTO1.setId(1L);
        OutputFileDTO outputFileDTO2 = new OutputFileDTO();
        assertThat(outputFileDTO1).isNotEqualTo(outputFileDTO2);
        outputFileDTO2.setId(outputFileDTO1.getId());
        assertThat(outputFileDTO1).isEqualTo(outputFileDTO2);
        outputFileDTO2.setId(2L);
        assertThat(outputFileDTO1).isNotEqualTo(outputFileDTO2);
        outputFileDTO1.setId(null);
        assertThat(outputFileDTO1).isNotEqualTo(outputFileDTO2);
    }
}
