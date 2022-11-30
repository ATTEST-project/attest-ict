package com.attest.ict.service.mapper;

import com.attest.ict.domain.WeatherForecast;
import com.attest.ict.service.dto.WeatherForecastDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WeatherForecast} and its DTO {@link WeatherForecastDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeatherForecastMapper extends EntityMapper<WeatherForecastDTO, WeatherForecast> {}
