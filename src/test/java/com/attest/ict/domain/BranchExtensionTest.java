package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchExtensionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchExtension.class);
        BranchExtension branchExtension1 = new BranchExtension();
        branchExtension1.setId(1L);
        BranchExtension branchExtension2 = new BranchExtension();
        branchExtension2.setId(branchExtension1.getId());
        assertThat(branchExtension1).isEqualTo(branchExtension2);
        branchExtension2.setId(2L);
        assertThat(branchExtension1).isNotEqualTo(branchExtension2);
        branchExtension1.setId(null);
        assertThat(branchExtension1).isNotEqualTo(branchExtension2);
    }
}
