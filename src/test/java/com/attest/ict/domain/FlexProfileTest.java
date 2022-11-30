package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexProfile.class);
        FlexProfile flexProfile1 = new FlexProfile();
        flexProfile1.setId(1L);
        FlexProfile flexProfile2 = new FlexProfile();
        flexProfile2.setId(flexProfile1.getId());
        assertThat(flexProfile1).isEqualTo(flexProfile2);
        flexProfile2.setId(2L);
        assertThat(flexProfile1).isNotEqualTo(flexProfile2);
        flexProfile1.setId(null);
        assertThat(flexProfile1).isNotEqualTo(flexProfile2);
    }
}
