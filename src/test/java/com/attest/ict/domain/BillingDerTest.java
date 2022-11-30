package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingDerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingDer.class);
        BillingDer billingDer1 = new BillingDer();
        billingDer1.setId(1L);
        BillingDer billingDer2 = new BillingDer();
        billingDer2.setId(billingDer1.getId());
        assertThat(billingDer1).isEqualTo(billingDer2);
        billingDer2.setId(2L);
        assertThat(billingDer1).isNotEqualTo(billingDer2);
        billingDer1.setId(null);
        assertThat(billingDer1).isNotEqualTo(billingDer2);
    }
}
