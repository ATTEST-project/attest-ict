package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratorExtensionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneratorExtension.class);
        GeneratorExtension generatorExtension1 = new GeneratorExtension();
        generatorExtension1.setId(1L);
        GeneratorExtension generatorExtension2 = new GeneratorExtension();
        generatorExtension2.setId(generatorExtension1.getId());
        assertThat(generatorExtension1).isEqualTo(generatorExtension2);
        generatorExtension2.setId(2L);
        assertThat(generatorExtension1).isNotEqualTo(generatorExtension2);
        generatorExtension1.setId(null);
        assertThat(generatorExtension1).isNotEqualTo(generatorExtension2);
    }
}
