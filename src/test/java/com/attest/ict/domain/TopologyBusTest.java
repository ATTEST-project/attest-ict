package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopologyBusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopologyBus.class);
        TopologyBus topologyBus1 = new TopologyBus();
        topologyBus1.setId(1L);
        TopologyBus topologyBus2 = new TopologyBus();
        topologyBus2.setId(topologyBus1.getId());
        assertThat(topologyBus1).isEqualTo(topologyBus2);
        topologyBus2.setId(2L);
        assertThat(topologyBus1).isNotEqualTo(topologyBus2);
        topologyBus1.setId(null);
        assertThat(topologyBus1).isNotEqualTo(topologyBus2);
    }
}
