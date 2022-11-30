package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusCoordinateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusCoordinateDTO.class);
        BusCoordinateDTO busCoordinateDTO1 = new BusCoordinateDTO();
        busCoordinateDTO1.setId(1L);
        BusCoordinateDTO busCoordinateDTO2 = new BusCoordinateDTO();
        assertThat(busCoordinateDTO1).isNotEqualTo(busCoordinateDTO2);
        busCoordinateDTO2.setId(busCoordinateDTO1.getId());
        assertThat(busCoordinateDTO1).isEqualTo(busCoordinateDTO2);
        busCoordinateDTO2.setId(2L);
        assertThat(busCoordinateDTO1).isNotEqualTo(busCoordinateDTO2);
        busCoordinateDTO1.setId(null);
        assertThat(busCoordinateDTO1).isNotEqualTo(busCoordinateDTO2);
    }
}
