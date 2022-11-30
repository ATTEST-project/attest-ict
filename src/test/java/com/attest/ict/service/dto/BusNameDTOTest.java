package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusNameDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusNameDTO.class);
        BusNameDTO busNameDTO1 = new BusNameDTO();
        busNameDTO1.setId(1L);
        BusNameDTO busNameDTO2 = new BusNameDTO();
        assertThat(busNameDTO1).isNotEqualTo(busNameDTO2);
        busNameDTO2.setId(busNameDTO1.getId());
        assertThat(busNameDTO1).isEqualTo(busNameDTO2);
        busNameDTO2.setId(2L);
        assertThat(busNameDTO1).isNotEqualTo(busNameDTO2);
        busNameDTO1.setId(null);
        assertThat(busNameDTO1).isNotEqualTo(busNameDTO2);
    }
}
