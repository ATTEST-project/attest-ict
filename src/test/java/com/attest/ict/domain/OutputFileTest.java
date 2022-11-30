package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutputFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputFile.class);
        OutputFile outputFile1 = new OutputFile();
        outputFile1.setId(1L);
        OutputFile outputFile2 = new OutputFile();
        outputFile2.setId(outputFile1.getId());
        assertThat(outputFile1).isEqualTo(outputFile2);
        outputFile2.setId(2L);
        assertThat(outputFile1).isNotEqualTo(outputFile2);
        outputFile1.setId(null);
        assertThat(outputFile1).isNotEqualTo(outputFile2);
    }
}
