package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopologyBusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopologyBusDTO.class);
        TopologyBusDTO topologyBusDTO1 = new TopologyBusDTO();
        topologyBusDTO1.setId(1L);
        TopologyBusDTO topologyBusDTO2 = new TopologyBusDTO();
        assertThat(topologyBusDTO1).isNotEqualTo(topologyBusDTO2);
        topologyBusDTO2.setId(topologyBusDTO1.getId());
        assertThat(topologyBusDTO1).isEqualTo(topologyBusDTO2);
        topologyBusDTO2.setId(2L);
        assertThat(topologyBusDTO1).isNotEqualTo(topologyBusDTO2);
        topologyBusDTO1.setId(null);
        assertThat(topologyBusDTO1).isNotEqualTo(topologyBusDTO2);
    }
}
