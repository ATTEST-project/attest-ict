package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Generator.class);
        Generator generator1 = new Generator();
        generator1.setId(1L);
        Generator generator2 = new Generator();
        generator2.setId(generator1.getId());
        assertThat(generator1).isEqualTo(generator2);
        generator2.setId(2L);
        assertThat(generator1).isNotEqualTo(generator2);
        generator1.setId(null);
        assertThat(generator1).isNotEqualTo(generator2);
    }
}
