package com.attest.ict.web.rest;

import com.attest.ict.repository.WeatherForecastRepository;
import com.attest.ict.service.WeatherForecastQueryService;
import com.attest.ict.service.WeatherForecastService;
import com.attest.ict.service.criteria.WeatherForecastCriteria;
import com.attest.ict.service.dto.WeatherForecastDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.WeatherForecast}.
 */
@RestController
@RequestMapping("/api")
public class WeatherForecastResource {

    private final Logger log = LoggerFactory.getLogger(WeatherForecastResource.class);

    private static final String ENTITY_NAME = "weatherForecast";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeatherForecastService weatherForecastService;

    private final WeatherForecastRepository weatherForecastRepository;

    private final WeatherForecastQueryService weatherForecastQueryService;

    public WeatherForecastResource(
        WeatherForecastService weatherForecastService,
        WeatherForecastRepository weatherForecastRepository,
        WeatherForecastQueryService weatherForecastQueryService
    ) {
        this.weatherForecastService = weatherForecastService;
        this.weatherForecastRepository = weatherForecastRepository;
        this.weatherForecastQueryService = weatherForecastQueryService;
    }

    /**
     * {@code POST  /weather-forecasts} : Create a new weatherForecast.
     *
     * @param weatherForecastDTO the weatherForecastDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weatherForecastDTO, or with status {@code 400 (Bad Request)} if the weatherForecast has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weather-forecasts")
    public ResponseEntity<WeatherForecastDTO> createWeatherForecast(@RequestBody WeatherForecastDTO weatherForecastDTO)
        throws URISyntaxException {
        log.debug("REST request to save WeatherForecast : {}", weatherForecastDTO);
        if (weatherForecastDTO.getId() != null) {
            throw new BadRequestAlertException("A new weatherForecast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeatherForecastDTO result = weatherForecastService.save(weatherForecastDTO);
        return ResponseEntity
            .created(new URI("/api/weather-forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weather-forecasts/:id} : Updates an existing weatherForecast.
     *
     * @param id the id of the weatherForecastDTO to save.
     * @param weatherForecastDTO the weatherForecastDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherForecastDTO,
     * or with status {@code 400 (Bad Request)} if the weatherForecastDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weatherForecastDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weather-forecasts/{id}")
    public ResponseEntity<WeatherForecastDTO> updateWeatherForecast(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherForecastDTO weatherForecastDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WeatherForecast : {}, {}", id, weatherForecastDTO);
        if (weatherForecastDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherForecastDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeatherForecastDTO result = weatherForecastService.save(weatherForecastDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weatherForecastDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weather-forecasts/:id} : Partial updates given fields of an existing weatherForecast, field will ignore if it is null
     *
     * @param id the id of the weatherForecastDTO to save.
     * @param weatherForecastDTO the weatherForecastDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherForecastDTO,
     * or with status {@code 400 (Bad Request)} if the weatherForecastDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weatherForecastDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weatherForecastDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weather-forecasts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeatherForecastDTO> partialUpdateWeatherForecast(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherForecastDTO weatherForecastDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeatherForecast partially : {}, {}", id, weatherForecastDTO);
        if (weatherForecastDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherForecastDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeatherForecastDTO> result = weatherForecastService.partialUpdate(weatherForecastDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weatherForecastDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weather-forecasts} : get all the weatherForecasts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weatherForecasts in body.
     */
    @GetMapping("/weather-forecasts")
    public ResponseEntity<List<WeatherForecastDTO>> getAllWeatherForecasts(WeatherForecastCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WeatherForecasts by criteria: {}", criteria);
        Page<WeatherForecastDTO> page = weatherForecastQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weather-forecasts/count} : count all the weatherForecasts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/weather-forecasts/count")
    public ResponseEntity<Long> countWeatherForecasts(WeatherForecastCriteria criteria) {
        log.debug("REST request to count WeatherForecasts by criteria: {}", criteria);
        return ResponseEntity.ok().body(weatherForecastQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /weather-forecasts/:id} : get the "id" weatherForecast.
     *
     * @param id the id of the weatherForecastDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weatherForecastDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weather-forecasts/{id}")
    public ResponseEntity<WeatherForecastDTO> getWeatherForecast(@PathVariable Long id) {
        log.debug("REST request to get WeatherForecast : {}", id);
        Optional<WeatherForecastDTO> weatherForecastDTO = weatherForecastService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weatherForecastDTO);
    }

    /**
     * {@code DELETE  /weather-forecasts/:id} : delete the "id" weatherForecast.
     *
     * @param id the id of the weatherForecastDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weather-forecasts/{id}")
    public ResponseEntity<Void> deleteWeatherForecast(@PathVariable Long id) {
        log.debug("REST request to delete WeatherForecast : {}", id);
        weatherForecastService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
