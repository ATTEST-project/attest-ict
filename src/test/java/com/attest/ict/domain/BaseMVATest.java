package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaseMVATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseMVA.class);
        BaseMVA baseMVA1 = new BaseMVA();
        baseMVA1.setId(1L);
        BaseMVA baseMVA2 = new BaseMVA();
        baseMVA2.setId(baseMVA1.getId());
        assertThat(baseMVA1).isEqualTo(baseMVA2);
        baseMVA2.setId(2L);
        assertThat(baseMVA1).isNotEqualTo(baseMVA2);
        baseMVA1.setId(null);
        assertThat(baseMVA1).isNotEqualTo(baseMVA2);
    }
}
