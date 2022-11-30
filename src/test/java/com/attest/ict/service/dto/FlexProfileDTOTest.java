package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexProfileDTO.class);
        FlexProfileDTO flexProfileDTO1 = new FlexProfileDTO();
        flexProfileDTO1.setId(1L);
        FlexProfileDTO flexProfileDTO2 = new FlexProfileDTO();
        assertThat(flexProfileDTO1).isNotEqualTo(flexProfileDTO2);
        flexProfileDTO2.setId(flexProfileDTO1.getId());
        assertThat(flexProfileDTO1).isEqualTo(flexProfileDTO2);
        flexProfileDTO2.setId(2L);
        assertThat(flexProfileDTO1).isNotEqualTo(flexProfileDTO2);
        flexProfileDTO1.setId(null);
        assertThat(flexProfileDTO1).isNotEqualTo(flexProfileDTO2);
    }
}
