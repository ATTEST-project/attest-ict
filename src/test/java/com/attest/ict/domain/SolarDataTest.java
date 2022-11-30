package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolarDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolarData.class);
        SolarData solarData1 = new SolarData();
        solarData1.setId(1L);
        SolarData solarData2 = new SolarData();
        solarData2.setId(solarData1.getId());
        assertThat(solarData1).isEqualTo(solarData2);
        solarData2.setId(2L);
        assertThat(solarData1).isNotEqualTo(solarData2);
        solarData1.setId(null);
        assertThat(solarData1).isNotEqualTo(solarData2);
    }
}
