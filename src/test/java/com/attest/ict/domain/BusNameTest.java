package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusNameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusName.class);
        BusName busName1 = new BusName();
        busName1.setId(1L);
        BusName busName2 = new BusName();
        busName2.setId(busName1.getId());
        assertThat(busName1).isEqualTo(busName2);
        busName2.setId(2L);
        assertThat(busName1).isNotEqualTo(busName2);
        busName1.setId(null);
        assertThat(busName1).isNotEqualTo(busName2);
    }
}
