package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneratorDTO.class);
        GeneratorDTO generatorDTO1 = new GeneratorDTO();
        generatorDTO1.setId(1L);
        GeneratorDTO generatorDTO2 = new GeneratorDTO();
        assertThat(generatorDTO1).isNotEqualTo(generatorDTO2);
        generatorDTO2.setId(generatorDTO1.getId());
        assertThat(generatorDTO1).isEqualTo(generatorDTO2);
        generatorDTO2.setId(2L);
        assertThat(generatorDTO1).isNotEqualTo(generatorDTO2);
        generatorDTO1.setId(null);
        assertThat(generatorDTO1).isNotEqualTo(generatorDTO2);
    }
}
