package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolParameter.class);
        ToolParameter toolParameter1 = new ToolParameter();
        toolParameter1.setId(1L);
        ToolParameter toolParameter2 = new ToolParameter();
        toolParameter2.setId(toolParameter1.getId());
        assertThat(toolParameter1).isEqualTo(toolParameter2);
        toolParameter2.setId(2L);
        assertThat(toolParameter1).isNotEqualTo(toolParameter2);
        toolParameter1.setId(null);
        assertThat(toolParameter1).isNotEqualTo(toolParameter2);
    }
}
