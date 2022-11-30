package com.attest.ict.repository;

import com.attest.ict.domain.WeatherForecast;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WeatherForecast entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long>, JpaSpecificationExecutor<WeatherForecast> {}
