package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transformer.class);
        Transformer transformer1 = new Transformer();
        transformer1.setId(1L);
        Transformer transformer2 = new Transformer();
        transformer2.setId(transformer1.getId());
        assertThat(transformer1).isEqualTo(transformer2);
        transformer2.setId(2L);
        assertThat(transformer1).isNotEqualTo(transformer2);
        transformer1.setId(null);
        assertThat(transformer1).isNotEqualTo(transformer2);
    }
}
