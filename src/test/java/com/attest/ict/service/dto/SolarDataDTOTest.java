package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolarDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolarDataDTO.class);
        SolarDataDTO solarDataDTO1 = new SolarDataDTO();
        solarDataDTO1.setId(1L);
        SolarDataDTO solarDataDTO2 = new SolarDataDTO();
        assertThat(solarDataDTO1).isNotEqualTo(solarDataDTO2);
        solarDataDTO2.setId(solarDataDTO1.getId());
        assertThat(solarDataDTO1).isEqualTo(solarDataDTO2);
        solarDataDTO2.setId(2L);
        assertThat(solarDataDTO1).isNotEqualTo(solarDataDTO2);
        solarDataDTO1.setId(null);
        assertThat(solarDataDTO1).isNotEqualTo(solarDataDTO2);
    }
}
