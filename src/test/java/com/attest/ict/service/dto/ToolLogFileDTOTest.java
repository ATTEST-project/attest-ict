package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolLogFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolLogFileDTO.class);
        ToolLogFileDTO toolLogFileDTO1 = new ToolLogFileDTO();
        toolLogFileDTO1.setId(1L);
        ToolLogFileDTO toolLogFileDTO2 = new ToolLogFileDTO();
        assertThat(toolLogFileDTO1).isNotEqualTo(toolLogFileDTO2);
        toolLogFileDTO2.setId(toolLogFileDTO1.getId());
        assertThat(toolLogFileDTO1).isEqualTo(toolLogFileDTO2);
        toolLogFileDTO2.setId(2L);
        assertThat(toolLogFileDTO1).isNotEqualTo(toolLogFileDTO2);
        toolLogFileDTO1.setId(null);
        assertThat(toolLogFileDTO1).isNotEqualTo(toolLogFileDTO2);
    }
}
