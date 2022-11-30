package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusExtensionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusExtensionDTO.class);
        BusExtensionDTO busExtensionDTO1 = new BusExtensionDTO();
        busExtensionDTO1.setId(1L);
        BusExtensionDTO busExtensionDTO2 = new BusExtensionDTO();
        assertThat(busExtensionDTO1).isNotEqualTo(busExtensionDTO2);
        busExtensionDTO2.setId(busExtensionDTO1.getId());
        assertThat(busExtensionDTO1).isEqualTo(busExtensionDTO2);
        busExtensionDTO2.setId(2L);
        assertThat(busExtensionDTO1).isNotEqualTo(busExtensionDTO2);
        busExtensionDTO1.setId(null);
        assertThat(busExtensionDTO1).isNotEqualTo(busExtensionDTO2);
    }
}
