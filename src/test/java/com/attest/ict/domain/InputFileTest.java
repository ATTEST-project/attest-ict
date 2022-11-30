package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InputFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputFile.class);
        InputFile inputFile1 = new InputFile();
        inputFile1.setId(1L);
        InputFile inputFile2 = new InputFile();
        inputFile2.setId(inputFile1.getId());
        assertThat(inputFile1).isEqualTo(inputFile2);
        inputFile2.setId(2L);
        assertThat(inputFile1).isNotEqualTo(inputFile2);
        inputFile1.setId(null);
        assertThat(inputFile1).isNotEqualTo(inputFile2);
    }
}
