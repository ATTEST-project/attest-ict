package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingConsumptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingConsumption.class);
        BillingConsumption billingConsumption1 = new BillingConsumption();
        billingConsumption1.setId(1L);
        BillingConsumption billingConsumption2 = new BillingConsumption();
        billingConsumption2.setId(billingConsumption1.getId());
        assertThat(billingConsumption1).isEqualTo(billingConsumption2);
        billingConsumption2.setId(2L);
        assertThat(billingConsumption1).isNotEqualTo(billingConsumption2);
        billingConsumption1.setId(null);
        assertThat(billingConsumption1).isNotEqualTo(billingConsumption2);
    }
}
