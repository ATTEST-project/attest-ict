package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NetworkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetworkDTO.class);
        NetworkDTO networkDTO1 = new NetworkDTO();
        networkDTO1.setId(1L);
        NetworkDTO networkDTO2 = new NetworkDTO();
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
        networkDTO2.setId(networkDTO1.getId());
        assertThat(networkDTO1).isEqualTo(networkDTO2);
        networkDTO2.setId(2L);
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
        networkDTO1.setId(null);
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
    }
}
