package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoadProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadProfileDTO.class);
        LoadProfileDTO loadProfileDTO1 = new LoadProfileDTO();
        loadProfileDTO1.setId(1L);
        LoadProfileDTO loadProfileDTO2 = new LoadProfileDTO();
        assertThat(loadProfileDTO1).isNotEqualTo(loadProfileDTO2);
        loadProfileDTO2.setId(loadProfileDTO1.getId());
        assertThat(loadProfileDTO1).isEqualTo(loadProfileDTO2);
        loadProfileDTO2.setId(2L);
        assertThat(loadProfileDTO1).isNotEqualTo(loadProfileDTO2);
        loadProfileDTO1.setId(null);
        assertThat(loadProfileDTO1).isNotEqualTo(loadProfileDTO2);
    }
}
