package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchProfile.class);
        BranchProfile branchProfile1 = new BranchProfile();
        branchProfile1.setId(1L);
        BranchProfile branchProfile2 = new BranchProfile();
        branchProfile2.setId(branchProfile1.getId());
        assertThat(branchProfile1).isEqualTo(branchProfile2);
        branchProfile2.setId(2L);
        assertThat(branchProfile1).isNotEqualTo(branchProfile2);
        branchProfile1.setId(null);
        assertThat(branchProfile1).isNotEqualTo(branchProfile2);
    }
}
