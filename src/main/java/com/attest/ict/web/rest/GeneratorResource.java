package com.attest.ict.web.rest;

import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.service.GeneratorQueryService;
import com.attest.ict.service.GeneratorService;
import com.attest.ict.service.criteria.GeneratorCriteria;
import com.attest.ict.service.dto.GeneratorDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.Generator}.
 */
@RestController
@RequestMapping("/api")
public class GeneratorResource {

    private final Logger log = LoggerFactory.getLogger(GeneratorResource.class);

    private static final String ENTITY_NAME = "generator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneratorService generatorService;

    private final GeneratorRepository generatorRepository;

    private final GeneratorQueryService generatorQueryService;

    public GeneratorResource(
        GeneratorService generatorService,
        GeneratorRepository generatorRepository,
        GeneratorQueryService generatorQueryService
    ) {
        this.generatorService = generatorService;
        this.generatorRepository = generatorRepository;
        this.generatorQueryService = generatorQueryService;
    }

    /**
     * {@code POST  /generators} : Create a new generator.
     *
     * @param generatorDTO the generatorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generatorDTO, or with status {@code 400 (Bad Request)} if the generator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generators")
    public ResponseEntity<GeneratorDTO> createGenerator(@RequestBody GeneratorDTO generatorDTO) throws URISyntaxException {
        log.debug("REST request to save Generator : {}", generatorDTO);
        if (generatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new generator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneratorDTO result = generatorService.save(generatorDTO);
        return ResponseEntity
            .created(new URI("/api/generators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generators/:id} : Updates an existing generator.
     *
     * @param id the id of the generatorDTO to save.
     * @param generatorDTO the generatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generatorDTO,
     * or with status {@code 400 (Bad Request)} if the generatorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generators/{id}")
    public ResponseEntity<GeneratorDTO> updateGenerator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneratorDTO generatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Generator : {}, {}", id, generatorDTO);
        if (generatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GeneratorDTO result = generatorService.save(generatorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, generatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generators/:id} : Partial updates given fields of an existing generator, field will ignore if it is null
     *
     * @param id the id of the generatorDTO to save.
     * @param generatorDTO the generatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generatorDTO,
     * or with status {@code 400 (Bad Request)} if the generatorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the generatorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the generatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeneratorDTO> partialUpdateGenerator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneratorDTO generatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Generator partially : {}, {}", id, generatorDTO);
        if (generatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeneratorDTO> result = generatorService.partialUpdate(generatorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, generatorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /generators} : get all the generators.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generators in body.
     */
    @GetMapping("/generators")
    public ResponseEntity<List<GeneratorDTO>> getAllGenerators(GeneratorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Generators by criteria: {}", criteria);
        Page<GeneratorDTO> page = generatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /generators/count} : count all the generators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/generators/count")
    public ResponseEntity<Long> countGenerators(GeneratorCriteria criteria) {
        log.debug("REST request to count Generators by criteria: {}", criteria);
        return ResponseEntity.ok().body(generatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /generators/:id} : get the "id" generator.
     *
     * @param id the id of the generatorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generatorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generators/{id}")
    public ResponseEntity<GeneratorDTO> getGenerator(@PathVariable Long id) {
        log.debug("REST request to get Generator : {}", id);
        Optional<GeneratorDTO> generatorDTO = generatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generatorDTO);
    }

    /**
     * {@code DELETE  /generators/:id} : delete the "id" generator.
     *
     * @param id the id of the generatorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generators/{id}")
    public ResponseEntity<Void> deleteGenerator(@PathVariable Long id) {
        log.debug("REST request to delete Generator : {}", id);
        generatorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //====== Start Custom Methods

    @GetMapping("/getGeneratorsByNetworkName/{networkName}")
    public ResponseEntity<List<Generator>> getGeneratorsByNetworkName(@PathVariable String networkName) throws ParseException {
        try {
            List<Generator> generatorList = generatorService.findByNetworkName(networkName);

            if (generatorList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(generatorList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* get generators by network id to which they belong
     * return some generators from network response
     */
    @GetMapping("/getGeneratorsByNetworkId/{networkId}")
    public ResponseEntity<List<Generator>> getGeneratorsByNetworkId(@PathVariable Long networkId) throws ParseException {
        try {
            List<Generator> generatorList = generatorService.findByNetworkId(networkId);

            if (generatorList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(generatorList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
