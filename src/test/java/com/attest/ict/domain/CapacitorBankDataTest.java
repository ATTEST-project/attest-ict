package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapacitorBankDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapacitorBankData.class);
        CapacitorBankData capacitorBankData1 = new CapacitorBankData();
        capacitorBankData1.setId(1L);
        CapacitorBankData capacitorBankData2 = new CapacitorBankData();
        capacitorBankData2.setId(capacitorBankData1.getId());
        assertThat(capacitorBankData1).isEqualTo(capacitorBankData2);
        capacitorBankData2.setId(2L);
        assertThat(capacitorBankData1).isNotEqualTo(capacitorBankData2);
        capacitorBankData1.setId(null);
        assertThat(capacitorBankData1).isNotEqualTo(capacitorBankData2);
    }
}
