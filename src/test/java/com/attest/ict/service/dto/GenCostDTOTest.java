package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenCostDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenCostDTO.class);
        GenCostDTO genCostDTO1 = new GenCostDTO();
        genCostDTO1.setId(1L);
        GenCostDTO genCostDTO2 = new GenCostDTO();
        assertThat(genCostDTO1).isNotEqualTo(genCostDTO2);
        genCostDTO2.setId(genCostDTO1.getId());
        assertThat(genCostDTO1).isEqualTo(genCostDTO2);
        genCostDTO2.setId(2L);
        assertThat(genCostDTO1).isNotEqualTo(genCostDTO2);
        genCostDTO1.setId(null);
        assertThat(genCostDTO1).isNotEqualTo(genCostDTO2);
    }
}
