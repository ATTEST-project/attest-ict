package com.attest.ict.service;

import com.attest.ict.service.dto.NodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Node}.
 */
public interface NodeService {
    /**
     * Save a node.
     *
     * @param nodeDTO the entity to save.
     * @return the persisted entity.
     */
    NodeDTO save(NodeDTO nodeDTO);

    /**
     * Partially updates a node.
     *
     * @param nodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NodeDTO> partialUpdate(NodeDTO nodeDTO);

    /**
     * Get all the nodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NodeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" node.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NodeDTO> findOne(Long id);

    /**
     * Delete the "id" node.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
