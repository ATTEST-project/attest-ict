package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimulationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimulationDTO.class);
        SimulationDTO simulationDTO1 = new SimulationDTO();
        simulationDTO1.setId(1L);
        SimulationDTO simulationDTO2 = new SimulationDTO();
        assertThat(simulationDTO1).isNotEqualTo(simulationDTO2);
        simulationDTO2.setId(simulationDTO1.getId());
        assertThat(simulationDTO1).isEqualTo(simulationDTO2);
        simulationDTO2.setId(2L);
        assertThat(simulationDTO1).isNotEqualTo(simulationDTO2);
        simulationDTO1.setId(null);
        assertThat(simulationDTO1).isNotEqualTo(simulationDTO2);
    }
}
