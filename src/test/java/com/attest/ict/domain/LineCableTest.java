package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineCableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineCable.class);
        LineCable lineCable1 = new LineCable();
        lineCable1.setId(1L);
        LineCable lineCable2 = new LineCable();
        lineCable2.setId(lineCable1.getId());
        assertThat(lineCable1).isEqualTo(lineCable2);
        lineCable2.setId(2L);
        assertThat(lineCable1).isNotEqualTo(lineCable2);
        lineCable1.setId(null);
        assertThat(lineCable1).isNotEqualTo(lineCable2);
    }
}
