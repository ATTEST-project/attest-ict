package com.attest.ict.service;

import com.attest.ict.custom.exception.ImportToolCsvFileException;
import com.attest.ict.service.dto.ToolDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Tool}.
 */
public interface ToolService {
    /**
     * Save a tool.
     *
     * @param toolDTO the entity to save.
     * @return the persisted entity.
     */
    ToolDTO save(ToolDTO toolDTO);

    /**
     * Partially updates a tool.
     *
     * @param toolDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ToolDTO> partialUpdate(ToolDTO toolDTO);

    /**
     * Get all the tools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tool.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToolDTO> findOne(Long id);

    /**
     * Delete the "id" tool.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    // -- Start Custom Methods
    /**
     *
     * @param file
     */
    public void firstImportToolFromCsv(MultipartFile file) throws ImportToolCsvFileException;
}
