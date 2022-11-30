package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopologyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topology.class);
        Topology topology1 = new Topology();
        topology1.setId(1L);
        Topology topology2 = new Topology();
        topology2.setId(topology1.getId());
        assertThat(topology1).isEqualTo(topology2);
        topology2.setId(2L);
        assertThat(topology1).isNotEqualTo(topology2);
        topology1.setId(null);
        assertThat(topology1).isNotEqualTo(topology2);
    }
}
