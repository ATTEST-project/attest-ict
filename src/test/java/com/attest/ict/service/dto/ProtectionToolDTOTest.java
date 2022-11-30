package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProtectionToolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProtectionToolDTO.class);
        ProtectionToolDTO protectionToolDTO1 = new ProtectionToolDTO();
        protectionToolDTO1.setId(1L);
        ProtectionToolDTO protectionToolDTO2 = new ProtectionToolDTO();
        assertThat(protectionToolDTO1).isNotEqualTo(protectionToolDTO2);
        protectionToolDTO2.setId(protectionToolDTO1.getId());
        assertThat(protectionToolDTO1).isEqualTo(protectionToolDTO2);
        protectionToolDTO2.setId(2L);
        assertThat(protectionToolDTO1).isNotEqualTo(protectionToolDTO2);
        protectionToolDTO1.setId(null);
        assertThat(protectionToolDTO1).isNotEqualTo(protectionToolDTO2);
    }
}
