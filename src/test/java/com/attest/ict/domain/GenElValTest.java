package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenElValTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenElVal.class);
        GenElVal genElVal1 = new GenElVal();
        genElVal1.setId(1L);
        GenElVal genElVal2 = new GenElVal();
        genElVal2.setId(genElVal1.getId());
        assertThat(genElVal1).isEqualTo(genElVal2);
        genElVal2.setId(2L);
        assertThat(genElVal1).isNotEqualTo(genElVal2);
        genElVal1.setId(null);
        assertThat(genElVal1).isNotEqualTo(genElVal2);
    }
}
