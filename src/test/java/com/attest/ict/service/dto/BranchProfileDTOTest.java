package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchProfileDTO.class);
        BranchProfileDTO branchProfileDTO1 = new BranchProfileDTO();
        branchProfileDTO1.setId(1L);
        BranchProfileDTO branchProfileDTO2 = new BranchProfileDTO();
        assertThat(branchProfileDTO1).isNotEqualTo(branchProfileDTO2);
        branchProfileDTO2.setId(branchProfileDTO1.getId());
        assertThat(branchProfileDTO1).isEqualTo(branchProfileDTO2);
        branchProfileDTO2.setId(2L);
        assertThat(branchProfileDTO1).isNotEqualTo(branchProfileDTO2);
        branchProfileDTO1.setId(null);
        assertThat(branchProfileDTO1).isNotEqualTo(branchProfileDTO2);
    }
}
