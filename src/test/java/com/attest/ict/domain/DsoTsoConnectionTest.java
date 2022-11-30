package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DsoTsoConnectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DsoTsoConnection.class);
        DsoTsoConnection dsoTsoConnection1 = new DsoTsoConnection();
        dsoTsoConnection1.setId(1L);
        DsoTsoConnection dsoTsoConnection2 = new DsoTsoConnection();
        dsoTsoConnection2.setId(dsoTsoConnection1.getId());
        assertThat(dsoTsoConnection1).isEqualTo(dsoTsoConnection2);
        dsoTsoConnection2.setId(2L);
        assertThat(dsoTsoConnection1).isNotEqualTo(dsoTsoConnection2);
        dsoTsoConnection1.setId(null);
        assertThat(dsoTsoConnection1).isNotEqualTo(dsoTsoConnection2);
    }
}
