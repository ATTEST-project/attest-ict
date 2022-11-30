package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexElValTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexElVal.class);
        FlexElVal flexElVal1 = new FlexElVal();
        flexElVal1.setId(1L);
        FlexElVal flexElVal2 = new FlexElVal();
        flexElVal2.setId(flexElVal1.getId());
        assertThat(flexElVal1).isEqualTo(flexElVal2);
        flexElVal2.setId(2L);
        assertThat(flexElVal1).isNotEqualTo(flexElVal2);
        flexElVal1.setId(null);
        assertThat(flexElVal1).isNotEqualTo(flexElVal2);
    }
}
