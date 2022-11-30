package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexElValDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexElValDTO.class);
        FlexElValDTO flexElValDTO1 = new FlexElValDTO();
        flexElValDTO1.setId(1L);
        FlexElValDTO flexElValDTO2 = new FlexElValDTO();
        assertThat(flexElValDTO1).isNotEqualTo(flexElValDTO2);
        flexElValDTO2.setId(flexElValDTO1.getId());
        assertThat(flexElValDTO1).isEqualTo(flexElValDTO2);
        flexElValDTO2.setId(2L);
        assertThat(flexElValDTO1).isNotEqualTo(flexElValDTO2);
        flexElValDTO1.setId(null);
        assertThat(flexElValDTO1).isNotEqualTo(flexElValDTO2);
    }
}
