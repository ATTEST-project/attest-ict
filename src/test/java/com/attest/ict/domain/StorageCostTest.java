package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageCostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageCost.class);
        StorageCost storageCost1 = new StorageCost();
        storageCost1.setId(1L);
        StorageCost storageCost2 = new StorageCost();
        storageCost2.setId(storageCost1.getId());
        assertThat(storageCost1).isEqualTo(storageCost2);
        storageCost2.setId(2L);
        assertThat(storageCost1).isNotEqualTo(storageCost2);
        storageCost1.setId(null);
        assertThat(storageCost1).isNotEqualTo(storageCost2);
    }
}
