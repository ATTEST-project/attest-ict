package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexCostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexCost.class);
        FlexCost flexCost1 = new FlexCost();
        flexCost1.setId(1L);
        FlexCost flexCost2 = new FlexCost();
        flexCost2.setId(flexCost1.getId());
        assertThat(flexCost1).isEqualTo(flexCost2);
        flexCost2.setId(2L);
        assertThat(flexCost1).isNotEqualTo(flexCost2);
        flexCost1.setId(null);
        assertThat(flexCost1).isNotEqualTo(flexCost2);
    }
}
