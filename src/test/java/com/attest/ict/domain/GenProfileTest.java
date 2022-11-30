package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenProfile.class);
        GenProfile genProfile1 = new GenProfile();
        genProfile1.setId(1L);
        GenProfile genProfile2 = new GenProfile();
        genProfile2.setId(genProfile1.getId());
        assertThat(genProfile1).isEqualTo(genProfile2);
        genProfile2.setId(2L);
        assertThat(genProfile1).isNotEqualTo(genProfile2);
        genProfile1.setId(null);
        assertThat(genProfile1).isNotEqualTo(genProfile2);
    }
}
