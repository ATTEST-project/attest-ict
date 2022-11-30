package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageCostDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageCostDTO.class);
        StorageCostDTO storageCostDTO1 = new StorageCostDTO();
        storageCostDTO1.setId(1L);
        StorageCostDTO storageCostDTO2 = new StorageCostDTO();
        assertThat(storageCostDTO1).isNotEqualTo(storageCostDTO2);
        storageCostDTO2.setId(storageCostDTO1.getId());
        assertThat(storageCostDTO1).isEqualTo(storageCostDTO2);
        storageCostDTO2.setId(2L);
        assertThat(storageCostDTO1).isNotEqualTo(storageCostDTO2);
        storageCostDTO1.setId(null);
        assertThat(storageCostDTO1).isNotEqualTo(storageCostDTO2);
    }
}
