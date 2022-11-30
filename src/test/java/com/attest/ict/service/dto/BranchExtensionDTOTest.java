package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchExtensionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchExtensionDTO.class);
        BranchExtensionDTO branchExtensionDTO1 = new BranchExtensionDTO();
        branchExtensionDTO1.setId(1L);
        BranchExtensionDTO branchExtensionDTO2 = new BranchExtensionDTO();
        assertThat(branchExtensionDTO1).isNotEqualTo(branchExtensionDTO2);
        branchExtensionDTO2.setId(branchExtensionDTO1.getId());
        assertThat(branchExtensionDTO1).isEqualTo(branchExtensionDTO2);
        branchExtensionDTO2.setId(2L);
        assertThat(branchExtensionDTO1).isNotEqualTo(branchExtensionDTO2);
        branchExtensionDTO1.setId(null);
        assertThat(branchExtensionDTO1).isNotEqualTo(branchExtensionDTO2);
    }
}
