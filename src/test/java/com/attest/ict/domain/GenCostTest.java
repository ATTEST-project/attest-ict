package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenCostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenCost.class);
        GenCost genCost1 = new GenCost();
        genCost1.setId(1L);
        GenCost genCost2 = new GenCost();
        genCost2.setId(genCost1.getId());
        assertThat(genCost1).isEqualTo(genCost2);
        genCost2.setId(2L);
        assertThat(genCost1).isNotEqualTo(genCost2);
        genCost1.setId(null);
        assertThat(genCost1).isNotEqualTo(genCost2);
    }
}
