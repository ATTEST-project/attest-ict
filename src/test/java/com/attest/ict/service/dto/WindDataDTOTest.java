package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WindDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WindDataDTO.class);
        WindDataDTO windDataDTO1 = new WindDataDTO();
        windDataDTO1.setId(1L);
        WindDataDTO windDataDTO2 = new WindDataDTO();
        assertThat(windDataDTO1).isNotEqualTo(windDataDTO2);
        windDataDTO2.setId(windDataDTO1.getId());
        assertThat(windDataDTO1).isEqualTo(windDataDTO2);
        windDataDTO2.setId(2L);
        assertThat(windDataDTO1).isNotEqualTo(windDataDTO2);
        windDataDTO1.setId(null);
        assertThat(windDataDTO1).isNotEqualTo(windDataDTO2);
    }
}
