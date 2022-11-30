package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusExtensionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusExtension.class);
        BusExtension busExtension1 = new BusExtension();
        busExtension1.setId(1L);
        BusExtension busExtension2 = new BusExtension();
        busExtension2.setId(busExtension1.getId());
        assertThat(busExtension1).isEqualTo(busExtension2);
        busExtension2.setId(2L);
        assertThat(busExtension1).isNotEqualTo(busExtension2);
        busExtension1.setId(null);
        assertThat(busExtension1).isNotEqualTo(busExtension2);
    }
}
