package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapacitorBankDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapacitorBankDataDTO.class);
        CapacitorBankDataDTO capacitorBankDataDTO1 = new CapacitorBankDataDTO();
        capacitorBankDataDTO1.setId(1L);
        CapacitorBankDataDTO capacitorBankDataDTO2 = new CapacitorBankDataDTO();
        assertThat(capacitorBankDataDTO1).isNotEqualTo(capacitorBankDataDTO2);
        capacitorBankDataDTO2.setId(capacitorBankDataDTO1.getId());
        assertThat(capacitorBankDataDTO1).isEqualTo(capacitorBankDataDTO2);
        capacitorBankDataDTO2.setId(2L);
        assertThat(capacitorBankDataDTO1).isNotEqualTo(capacitorBankDataDTO2);
        capacitorBankDataDTO1.setId(null);
        assertThat(capacitorBankDataDTO1).isNotEqualTo(capacitorBankDataDTO2);
    }
}
