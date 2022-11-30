package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfProfile.class);
        TransfProfile transfProfile1 = new TransfProfile();
        transfProfile1.setId(1L);
        TransfProfile transfProfile2 = new TransfProfile();
        transfProfile2.setId(transfProfile1.getId());
        assertThat(transfProfile1).isEqualTo(transfProfile2);
        transfProfile2.setId(2L);
        assertThat(transfProfile1).isNotEqualTo(transfProfile2);
        transfProfile1.setId(null);
        assertThat(transfProfile1).isNotEqualTo(transfProfile2);
    }
}
