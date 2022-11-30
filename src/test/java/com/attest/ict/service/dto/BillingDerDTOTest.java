package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingDerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingDerDTO.class);
        BillingDerDTO billingDerDTO1 = new BillingDerDTO();
        billingDerDTO1.setId(1L);
        BillingDerDTO billingDerDTO2 = new BillingDerDTO();
        assertThat(billingDerDTO1).isNotEqualTo(billingDerDTO2);
        billingDerDTO2.setId(billingDerDTO1.getId());
        assertThat(billingDerDTO1).isEqualTo(billingDerDTO2);
        billingDerDTO2.setId(2L);
        assertThat(billingDerDTO1).isNotEqualTo(billingDerDTO2);
        billingDerDTO1.setId(null);
        assertThat(billingDerDTO1).isNotEqualTo(billingDerDTO2);
    }
}
