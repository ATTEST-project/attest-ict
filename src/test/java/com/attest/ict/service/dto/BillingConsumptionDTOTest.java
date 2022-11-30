package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingConsumptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingConsumptionDTO.class);
        BillingConsumptionDTO billingConsumptionDTO1 = new BillingConsumptionDTO();
        billingConsumptionDTO1.setId(1L);
        BillingConsumptionDTO billingConsumptionDTO2 = new BillingConsumptionDTO();
        assertThat(billingConsumptionDTO1).isNotEqualTo(billingConsumptionDTO2);
        billingConsumptionDTO2.setId(billingConsumptionDTO1.getId());
        assertThat(billingConsumptionDTO1).isEqualTo(billingConsumptionDTO2);
        billingConsumptionDTO2.setId(2L);
        assertThat(billingConsumptionDTO1).isNotEqualTo(billingConsumptionDTO2);
        billingConsumptionDTO1.setId(null);
        assertThat(billingConsumptionDTO1).isNotEqualTo(billingConsumptionDTO2);
    }
}
