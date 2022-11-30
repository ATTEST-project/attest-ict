package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WindDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WindData.class);
        WindData windData1 = new WindData();
        windData1.setId(1L);
        WindData windData2 = new WindData();
        windData2.setId(windData1.getId());
        assertThat(windData1).isEqualTo(windData2);
        windData2.setId(2L);
        assertThat(windData1).isNotEqualTo(windData2);
        windData1.setId(null);
        assertThat(windData1).isNotEqualTo(windData2);
    }
}
