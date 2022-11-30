package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratorExtensionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneratorExtensionDTO.class);
        GeneratorExtensionDTO generatorExtensionDTO1 = new GeneratorExtensionDTO();
        generatorExtensionDTO1.setId(1L);
        GeneratorExtensionDTO generatorExtensionDTO2 = new GeneratorExtensionDTO();
        assertThat(generatorExtensionDTO1).isNotEqualTo(generatorExtensionDTO2);
        generatorExtensionDTO2.setId(generatorExtensionDTO1.getId());
        assertThat(generatorExtensionDTO1).isEqualTo(generatorExtensionDTO2);
        generatorExtensionDTO2.setId(2L);
        assertThat(generatorExtensionDTO1).isNotEqualTo(generatorExtensionDTO2);
        generatorExtensionDTO1.setId(null);
        assertThat(generatorExtensionDTO1).isNotEqualTo(generatorExtensionDTO2);
    }
}
