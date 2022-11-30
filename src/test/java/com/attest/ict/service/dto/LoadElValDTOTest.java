package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoadElValDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadElValDTO.class);
        LoadElValDTO loadElValDTO1 = new LoadElValDTO();
        loadElValDTO1.setId(1L);
        LoadElValDTO loadElValDTO2 = new LoadElValDTO();
        assertThat(loadElValDTO1).isNotEqualTo(loadElValDTO2);
        loadElValDTO2.setId(loadElValDTO1.getId());
        assertThat(loadElValDTO1).isEqualTo(loadElValDTO2);
        loadElValDTO2.setId(2L);
        assertThat(loadElValDTO1).isNotEqualTo(loadElValDTO2);
        loadElValDTO1.setId(null);
        assertThat(loadElValDTO1).isNotEqualTo(loadElValDTO2);
    }
}
