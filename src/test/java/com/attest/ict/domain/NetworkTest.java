package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NetworkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Network.class);
        Network network1 = new Network();
        network1.setId(1L);
        Network network2 = new Network();
        network2.setId(network1.getId());
        assertThat(network1).isEqualTo(network2);
        network2.setId(2L);
        assertThat(network1).isNotEqualTo(network2);
        network1.setId(null);
        assertThat(network1).isNotEqualTo(network2);
    }
}
