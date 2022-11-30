package com.attest.ict.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeatherForecastDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeatherForecastDTO.class);
        WeatherForecastDTO weatherForecastDTO1 = new WeatherForecastDTO();
        weatherForecastDTO1.setId(1L);
        WeatherForecastDTO weatherForecastDTO2 = new WeatherForecastDTO();
        assertThat(weatherForecastDTO1).isNotEqualTo(weatherForecastDTO2);
        weatherForecastDTO2.setId(weatherForecastDTO1.getId());
        assertThat(weatherForecastDTO1).isEqualTo(weatherForecastDTO2);
        weatherForecastDTO2.setId(2L);
        assertThat(weatherForecastDTO1).isNotEqualTo(weatherForecastDTO2);
        weatherForecastDTO1.setId(null);
        assertThat(weatherForecastDTO1).isNotEqualTo(weatherForecastDTO2);
    }
}
