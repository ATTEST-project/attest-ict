package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchElValDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchElValDTO.class);
        BranchElValDTO branchElValDTO1 = new BranchElValDTO();
        branchElValDTO1.setId(1L);
        BranchElValDTO branchElValDTO2 = new BranchElValDTO();
        assertThat(branchElValDTO1).isNotEqualTo(branchElValDTO2);
        branchElValDTO2.setId(branchElValDTO1.getId());
        assertThat(branchElValDTO1).isEqualTo(branchElValDTO2);
        branchElValDTO2.setId(2L);
        assertThat(branchElValDTO1).isNotEqualTo(branchElValDTO2);
        branchElValDTO1.setId(null);
        assertThat(branchElValDTO1).isNotEqualTo(branchElValDTO2);
    }
}
