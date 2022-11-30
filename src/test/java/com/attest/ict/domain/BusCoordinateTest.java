package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusCoordinateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusCoordinate.class);
        BusCoordinate busCoordinate1 = new BusCoordinate();
        busCoordinate1.setId(1L);
        BusCoordinate busCoordinate2 = new BusCoordinate();
        busCoordinate2.setId(busCoordinate1.getId());
        assertThat(busCoordinate1).isEqualTo(busCoordinate2);
        busCoordinate2.setId(2L);
        assertThat(busCoordinate1).isNotEqualTo(busCoordinate2);
        busCoordinate1.setId(null);
        assertThat(busCoordinate1).isNotEqualTo(busCoordinate2);
    }
}
