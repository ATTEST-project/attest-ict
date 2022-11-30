package com.attest.ict.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeatherForecastTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeatherForecast.class);
        WeatherForecast weatherForecast1 = new WeatherForecast();
        weatherForecast1.setId(1L);
        WeatherForecast weatherForecast2 = new WeatherForecast();
        weatherForecast2.setId(weatherForecast1.getId());
        assertThat(weatherForecast1).isEqualTo(weatherForecast2);
        weatherForecast2.setId(2L);
        assertThat(weatherForecast1).isNotEqualTo(weatherForecast2);
        weatherForecast1.setId(null);
        assertThat(weatherForecast1).isNotEqualTo(weatherForecast2);
    }
}
