package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InputFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputFileDTO.class);
        InputFileDTO inputFileDTO1 = new InputFileDTO();
        inputFileDTO1.setId(1L);
        InputFileDTO inputFileDTO2 = new InputFileDTO();
        assertThat(inputFileDTO1).isNotEqualTo(inputFileDTO2);
        inputFileDTO2.setId(inputFileDTO1.getId());
        assertThat(inputFileDTO1).isEqualTo(inputFileDTO2);
        inputFileDTO2.setId(2L);
        assertThat(inputFileDTO1).isNotEqualTo(inputFileDTO2);
        inputFileDTO1.setId(null);
        assertThat(inputFileDTO1).isNotEqualTo(inputFileDTO2);
    }
}
