package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopologyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopologyDTO.class);
        TopologyDTO topologyDTO1 = new TopologyDTO();
        topologyDTO1.setId(1L);
        TopologyDTO topologyDTO2 = new TopologyDTO();
        assertThat(topologyDTO1).isNotEqualTo(topologyDTO2);
        topologyDTO2.setId(topologyDTO1.getId());
        assertThat(topologyDTO1).isEqualTo(topologyDTO2);
        topologyDTO2.setId(2L);
        assertThat(topologyDTO1).isNotEqualTo(topologyDTO2);
        topologyDTO1.setId(null);
        assertThat(topologyDTO1).isNotEqualTo(topologyDTO2);
    }
}
