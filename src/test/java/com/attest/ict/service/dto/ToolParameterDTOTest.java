package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolParameterDTO.class);
        ToolParameterDTO toolParameterDTO1 = new ToolParameterDTO();
        toolParameterDTO1.setId(1L);
        ToolParameterDTO toolParameterDTO2 = new ToolParameterDTO();
        assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
        toolParameterDTO2.setId(toolParameterDTO1.getId());
        assertThat(toolParameterDTO1).isEqualTo(toolParameterDTO2);
        toolParameterDTO2.setId(2L);
        assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
        toolParameterDTO1.setId(null);
        assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
    }
}
