package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoadElValTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadElVal.class);
        LoadElVal loadElVal1 = new LoadElVal();
        loadElVal1.setId(1L);
        LoadElVal loadElVal2 = new LoadElVal();
        loadElVal2.setId(loadElVal1.getId());
        assertThat(loadElVal1).isEqualTo(loadElVal2);
        loadElVal2.setId(2L);
        assertThat(loadElVal1).isNotEqualTo(loadElVal2);
        loadElVal1.setId(null);
        assertThat(loadElVal1).isNotEqualTo(loadElVal2);
    }
}
