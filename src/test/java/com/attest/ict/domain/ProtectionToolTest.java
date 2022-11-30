package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProtectionToolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProtectionTool.class);
        ProtectionTool protectionTool1 = new ProtectionTool();
        protectionTool1.setId(1L);
        ProtectionTool protectionTool2 = new ProtectionTool();
        protectionTool2.setId(protectionTool1.getId());
        assertThat(protectionTool1).isEqualTo(protectionTool2);
        protectionTool2.setId(2L);
        assertThat(protectionTool1).isNotEqualTo(protectionTool2);
        protectionTool1.setId(null);
        assertThat(protectionTool1).isNotEqualTo(protectionTool2);
    }
}
