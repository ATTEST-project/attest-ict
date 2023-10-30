package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.model.projection.NetworkProjection;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.NetworkQueryService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.criteria.NetworkCriteria;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.Network}.
 */
@RestController
@RequestMapping("/api")
public class NetworkResource {

    private final Logger log = LoggerFactory.getLogger(NetworkResource.class);

    private static final String ENTITY_NAME = "network";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NetworkService networkService;

    private final NetworkRepository networkRepository;

    private final NetworkQueryService networkQueryService;

    public NetworkResource(NetworkService networkService, NetworkRepository networkRepository, NetworkQueryService networkQueryService) {
        this.networkService = networkService;
        this.networkRepository = networkRepository;
        this.networkQueryService = networkQueryService;
    }

    /**
     * {@code POST  /networks} : Create a new network.
     *
     * @param networkDTO the networkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new networkDTO, or with status {@code 400 (Bad Request)} if the network has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/networks")
    public ResponseEntity<NetworkDTO> createNetwork(@Valid @RequestBody NetworkDTO networkDTO) throws URISyntaxException {
        log.debug("REST request to save Network : {}", networkDTO);
        if (networkDTO.getId() != null) {
            throw new BadRequestAlertException("A new network cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (networkDTO.getNetworkDate() == null) {
            networkDTO.setNetworkDate(Instant.now());
        }

        if (networkDTO.getCreationDateTime() == null) {
            networkDTO.setCreationDateTime(Instant.now());
        }

        if (networkDTO.getUpdateDateTime() == null) {
            networkDTO.setUpdateDateTime(Instant.now());
        }
        NetworkDTO result = networkService.save(networkDTO);
        return ResponseEntity
            .created(new URI("/api/networks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /networks/:id} : Updates an existing network.
     *
     * @param id the id of the networkDTO to save.
     * @param networkDTO the networkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated networkDTO,
     * or with status {@code 400 (Bad Request)} if the networkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the networkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/networks/{id}")
    public ResponseEntity<NetworkDTO> updateNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NetworkDTO networkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Network : {}, {}", id, networkDTO);
        if (networkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, networkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!networkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NetworkDTO result = networkService.save(networkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, networkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /networks/:id} : Partial updates given fields of an existing network, field will ignore if it is null
     *
     * @param id the id of the networkDTO to save.
     * @param networkDTO the networkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated networkDTO,
     * or with status {@code 400 (Bad Request)} if the networkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the networkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the networkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/networks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NetworkDTO> partialUpdateNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NetworkDTO networkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Network partially : {}, {}", id, networkDTO);
        if (networkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, networkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!networkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NetworkDTO> result = networkService.partialUpdate(networkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, networkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /networks} : get all the networks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of networks in body.
     */
    @GetMapping("/networks")
    public ResponseEntity<List<NetworkDTO>> getAllNetworks(NetworkCriteria criteria, Pageable pageable) {
        //public ResponseEntity<List<NetworkDTO>> getAllNetworks(NetworkCriteria criteria,@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size =10000) Pageable pageable) {

        log.debug("REST request to get Networks by criteria: {}", criteria);
        Page<NetworkDTO> page = networkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /networks/count} : count all the networks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/networks/count")
    public ResponseEntity<Long> countNetworks(NetworkCriteria criteria) {
        log.debug("REST request to count Networks by criteria: {}", criteria);
        return ResponseEntity.ok().body(networkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /networks/:id} : get the "id" network.
     *
     * @param id the id of the networkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the networkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/networks/{id}")
    public ResponseEntity<NetworkDTO> getNetwork(@PathVariable Long id) {
        log.debug("REST request to get Network : {}", id);
        Optional<NetworkDTO> networkDTO = networkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(networkDTO);
    }

    /**
     * {@code DELETE  /networks/:id} : delete the "id" network.
     *
     * @param id the id of the networkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/networks/{id}")
    public ResponseEntity<Void> deleteNetwork(@PathVariable Long id) {
        log.debug("REST request to delete Network : {}", id);
        networkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //======= Start Custom Methods

    // EX @PostMapping("/addNewVersion/{id}")
    /**
     * {@code POST  /networks//new-version:id} : save a new version of a network from network with specific networkId "id"
     * @param id the id of the networkDTO
     */
    @PostMapping("/networks/new-version/{id}")
    public ResponseEntity<?> addNewNetworkVersion(@PathVariable("id") Long id) {
        log.debug("REST request to add new version of a network,  from a network with id: {} ", id);
        String message = "";
        //Optional<Network> optNet = networkServiceCustom.findById(networkId);
        Optional<NetworkDTO> optNet = networkService.findOne(id);
        if (!optNet.isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        NetworkDTO fromNetwork = optNet.get();
        NetworkDTO networkNewVersion = new NetworkDTO();
        networkNewVersion.setName(fromNetwork.getName());
        networkNewVersion.setMpcName(fromNetwork.getMpcName());
        networkNewVersion.setType(fromNetwork.getType());
        networkNewVersion.setCountry(fromNetwork.getCountry());
        networkNewVersion.setVersion(fromNetwork.getVersion() + 1);
        networkNewVersion.setNetworkDate(fromNetwork.getNetworkDate());
        networkNewVersion.setCreationDateTime(Instant.now());
        networkNewVersion.setUpdateDateTime(Instant.now());
        try {
            //networkServiceCustom.saveNetworkWithVersion(network1);
            NetworkDTO newVersion = networkService.save(networkNewVersion);
            message = "Saved the new version of network successfully id: " + newVersion.getId();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not save the version of network";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/networks-names")
    public ResponseEntity<List<String>> getAllNetworkNames() {
        List<String> networkNames = networkService.getAllNetworkNames();
        if (networkNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(networkNames, HttpStatus.OK);
    }

    //old /networkId/{networkName}/{startDate}/{endDate}
    @GetMapping("/networks-id/{networkName}/{startDate}/{endDate}")
    public ResponseEntity<List<Network>> getNetworkId(
        @PathVariable String networkName,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        try {
            List<Long> networkIdList = networkService.getNetworkId(networkName, startDate, endDate);
            if (networkIdList.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(networkIdList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* returns the networkId of a network by the network name
     */
    @GetMapping("/network-id/{networkName}")
    public ResponseEntity<Long> getNetworkIdByName(@PathVariable String networkName) {
        try {
            Long networkId = networkService.getNetworkIdByName(networkName);
            if (networkId == null) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(networkId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* getNetworkVersion method
     * return a version of network with specific id
     */
    @GetMapping("/network-version/{networkId}")
    public ResponseEntity<String> getNetworkVersion(@PathVariable("networkId") Long networkId) {
        String message = "";
        Optional<Network> networkList = networkService.findById(networkId);

        try {
            if (!networkList.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // networkRepository.deleteNetworksById(networkId);
            message = "Network Version is: " + networkList.get().getVersion();

            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* getAllNetworksPlain method
     * return a list of all networks plain
     */
    @GetMapping("/networks-plain")
    public ResponseEntity<List<Network>> getAllNetworksPlain() {
        try {
            List<Object> networkList = networkService.getAllNetworksPlain();
            if (networkList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity(networkList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v1/networks")
    public ResponseEntity<List<NetworkProjection>> getNetworks() {
        try {
            List<NetworkProjection> networkList = networkService.getNetworks();
            if (networkList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(networkList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* getAllNetworksIds method
     * return a list of all networks ids
     */
    @GetMapping("/networks-all-id")
    public ResponseEntity<List<Long>> getAllNetworkIds() {
        try {
            List<Long> networkIdList = networkService.getNetworkIds();

            if (networkIdList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(networkIdList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Exception handler for handling ConstraintViolationException from Hibernate.
     * @param e The ConstraintViolationException that was thrown.
     * @return A ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        String constraintName = e.getConstraintName();
        String errorMessage = "Constraint '" + constraintName + "' violation: ";
        if (constraintName.contains("network.UC_NETWORKNAME_COL")) {
            errorMessage = "A network with the same name already exists!";
        }
        log.debug("handleConstraintViolationException() return message: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
