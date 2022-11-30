package com.attest.ict.service.impl;

import com.attest.ict.domain.WeatherForecast;
import com.attest.ict.repository.WeatherForecastRepository;
import com.attest.ict.service.WeatherForecastService;
import com.attest.ict.service.dto.WeatherForecastDTO;
import com.attest.ict.service.mapper.WeatherForecastMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WeatherForecast}.
 */
@Service
@Transactional
public class WeatherForecastServiceImpl implements WeatherForecastService {

    private final Logger log = LoggerFactory.getLogger(WeatherForecastServiceImpl.class);

    private final WeatherForecastRepository weatherForecastRepository;

    private final WeatherForecastMapper weatherForecastMapper;

    public WeatherForecastServiceImpl(WeatherForecastRepository weatherForecastRepository, WeatherForecastMapper weatherForecastMapper) {
        this.weatherForecastRepository = weatherForecastRepository;
        this.weatherForecastMapper = weatherForecastMapper;
    }

    @Override
    public WeatherForecastDTO save(WeatherForecastDTO weatherForecastDTO) {
        log.debug("Request to save WeatherForecast : {}", weatherForecastDTO);
        WeatherForecast weatherForecast = weatherForecastMapper.toEntity(weatherForecastDTO);
        weatherForecast = weatherForecastRepository.save(weatherForecast);
        return weatherForecastMapper.toDto(weatherForecast);
    }

    @Override
    public Optional<WeatherForecastDTO> partialUpdate(WeatherForecastDTO weatherForecastDTO) {
        log.debug("Request to partially update WeatherForecast : {}", weatherForecastDTO);

        return weatherForecastRepository
            .findById(weatherForecastDTO.getId())
            .map(existingWeatherForecast -> {
                weatherForecastMapper.partialUpdate(existingWeatherForecast, weatherForecastDTO);

                return existingWeatherForecast;
            })
            .map(weatherForecastRepository::save)
            .map(weatherForecastMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeatherForecastDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeatherForecasts");
        return weatherForecastRepository.findAll(pageable).map(weatherForecastMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeatherForecastDTO> findOne(Long id) {
        log.debug("Request to get WeatherForecast : {}", id);
        return weatherForecastRepository.findById(id).map(weatherForecastMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WeatherForecast : {}", id);
        weatherForecastRepository.deleteById(id);
    }
}
