package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchElValTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchElVal.class);
        BranchElVal branchElVal1 = new BranchElVal();
        branchElVal1.setId(1L);
        BranchElVal branchElVal2 = new BranchElVal();
        branchElVal2.setId(branchElVal1.getId());
        assertThat(branchElVal1).isEqualTo(branchElVal2);
        branchElVal2.setId(2L);
        assertThat(branchElVal1).isNotEqualTo(branchElVal2);
        branchElVal1.setId(null);
        assertThat(branchElVal1).isNotEqualTo(branchElVal2);
    }
}
