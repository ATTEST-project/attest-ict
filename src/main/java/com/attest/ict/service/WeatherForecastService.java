package com.attest.ict.service;

import com.attest.ict.service.dto.WeatherForecastDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.WeatherForecast}.
 */
public interface WeatherForecastService {
    /**
     * Save a weatherForecast.
     *
     * @param weatherForecastDTO the entity to save.
     * @return the persisted entity.
     */
    WeatherForecastDTO save(WeatherForecastDTO weatherForecastDTO);

    /**
     * Partially updates a weatherForecast.
     *
     * @param weatherForecastDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WeatherForecastDTO> partialUpdate(WeatherForecastDTO weatherForecastDTO);

    /**
     * Get all the weatherForecasts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WeatherForecastDTO> findAll(Pageable pageable);

    /**
     * Get the "id" weatherForecast.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WeatherForecastDTO> findOne(Long id);

    /**
     * Delete the "id" weatherForecast.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
