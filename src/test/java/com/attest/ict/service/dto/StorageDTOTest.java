package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageDTO.class);
        StorageDTO storageDTO1 = new StorageDTO();
        storageDTO1.setId(1L);
        StorageDTO storageDTO2 = new StorageDTO();
        assertThat(storageDTO1).isNotEqualTo(storageDTO2);
        storageDTO2.setId(storageDTO1.getId());
        assertThat(storageDTO1).isEqualTo(storageDTO2);
        storageDTO2.setId(2L);
        assertThat(storageDTO1).isNotEqualTo(storageDTO2);
        storageDTO1.setId(null);
        assertThat(storageDTO1).isNotEqualTo(storageDTO2);
    }
}
