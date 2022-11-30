package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenTag.class);
        GenTag genTag1 = new GenTag();
        genTag1.setId(1L);
        GenTag genTag2 = new GenTag();
        genTag2.setId(genTag1.getId());
        assertThat(genTag1).isEqualTo(genTag2);
        genTag2.setId(2L);
        assertThat(genTag1).isNotEqualTo(genTag2);
        genTag1.setId(null);
        assertThat(genTag1).isNotEqualTo(genTag2);
    }
}
