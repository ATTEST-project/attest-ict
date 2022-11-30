package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoltageLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoltageLevel.class);
        VoltageLevel voltageLevel1 = new VoltageLevel();
        voltageLevel1.setId(1L);
        VoltageLevel voltageLevel2 = new VoltageLevel();
        voltageLevel2.setId(voltageLevel1.getId());
        assertThat(voltageLevel1).isEqualTo(voltageLevel2);
        voltageLevel2.setId(2L);
        assertThat(voltageLevel1).isNotEqualTo(voltageLevel2);
        voltageLevel1.setId(null);
        assertThat(voltageLevel1).isNotEqualTo(voltageLevel2);
    }
}
