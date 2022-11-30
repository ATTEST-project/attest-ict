package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoadProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadProfile.class);
        LoadProfile loadProfile1 = new LoadProfile();
        loadProfile1.setId(1L);
        LoadProfile loadProfile2 = new LoadProfile();
        loadProfile2.setId(loadProfile1.getId());
        assertThat(loadProfile1).isEqualTo(loadProfile2);
        loadProfile2.setId(2L);
        assertThat(loadProfile1).isNotEqualTo(loadProfile2);
        loadProfile1.setId(null);
        assertThat(loadProfile1).isNotEqualTo(loadProfile2);
    }
}
