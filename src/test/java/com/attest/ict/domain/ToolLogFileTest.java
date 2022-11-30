package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolLogFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolLogFile.class);
        ToolLogFile toolLogFile1 = new ToolLogFile();
        toolLogFile1.setId(1L);
        ToolLogFile toolLogFile2 = new ToolLogFile();
        toolLogFile2.setId(toolLogFile1.getId());
        assertThat(toolLogFile1).isEqualTo(toolLogFile2);
        toolLogFile2.setId(2L);
        assertThat(toolLogFile1).isNotEqualTo(toolLogFile2);
        toolLogFile1.setId(null);
        assertThat(toolLogFile1).isNotEqualTo(toolLogFile2);
    }
}
