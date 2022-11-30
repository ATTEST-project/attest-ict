package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoltageLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoltageLevelDTO.class);
        VoltageLevelDTO voltageLevelDTO1 = new VoltageLevelDTO();
        voltageLevelDTO1.setId(1L);
        VoltageLevelDTO voltageLevelDTO2 = new VoltageLevelDTO();
        assertThat(voltageLevelDTO1).isNotEqualTo(voltageLevelDTO2);
        voltageLevelDTO2.setId(voltageLevelDTO1.getId());
        assertThat(voltageLevelDTO1).isEqualTo(voltageLevelDTO2);
        voltageLevelDTO2.setId(2L);
        assertThat(voltageLevelDTO1).isNotEqualTo(voltageLevelDTO2);
        voltageLevelDTO1.setId(null);
        assertThat(voltageLevelDTO1).isNotEqualTo(voltageLevelDTO2);
    }
}
