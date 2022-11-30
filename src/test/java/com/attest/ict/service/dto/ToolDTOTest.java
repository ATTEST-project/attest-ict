package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolDTO.class);
        ToolDTO toolDTO1 = new ToolDTO();
        toolDTO1.setId(1L);
        ToolDTO toolDTO2 = new ToolDTO();
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
        toolDTO2.setId(toolDTO1.getId());
        assertThat(toolDTO1).isEqualTo(toolDTO2);
        toolDTO2.setId(2L);
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
        toolDTO1.setId(null);
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
    }
}
