package com.attest.ict.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeatherForecastMapperTest {

    private WeatherForecastMapper weatherForecastMapper;

    @BeforeEach
    public void setUp() {
        weatherForecastMapper = new WeatherForecastMapperImpl();
    }
}
