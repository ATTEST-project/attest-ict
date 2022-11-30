package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexCostDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexCostDTO.class);
        FlexCostDTO flexCostDTO1 = new FlexCostDTO();
        flexCostDTO1.setId(1L);
        FlexCostDTO flexCostDTO2 = new FlexCostDTO();
        assertThat(flexCostDTO1).isNotEqualTo(flexCostDTO2);
        flexCostDTO2.setId(flexCostDTO1.getId());
        assertThat(flexCostDTO1).isEqualTo(flexCostDTO2);
        flexCostDTO2.setId(2L);
        assertThat(flexCostDTO1).isNotEqualTo(flexCostDTO2);
        flexCostDTO1.setId(null);
        assertThat(flexCostDTO1).isNotEqualTo(flexCostDTO2);
    }
}
