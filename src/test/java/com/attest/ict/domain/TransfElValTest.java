package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfElValTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfElVal.class);
        TransfElVal transfElVal1 = new TransfElVal();
        transfElVal1.setId(1L);
        TransfElVal transfElVal2 = new TransfElVal();
        transfElVal2.setId(transfElVal1.getId());
        assertThat(transfElVal1).isEqualTo(transfElVal2);
        transfElVal2.setId(2L);
        assertThat(transfElVal1).isNotEqualTo(transfElVal2);
        transfElVal1.setId(null);
        assertThat(transfElVal1).isNotEqualTo(transfElVal2);
    }
}
