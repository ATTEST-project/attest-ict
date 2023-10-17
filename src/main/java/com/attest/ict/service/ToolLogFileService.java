package com.attest.ict.service;

import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolLogFileDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.attest.ict.domain.ToolLogFile}.
 */
public interface ToolLogFileService {
    /**
     * Save a toolLogFile.
     *
     * @param toolLogFileDTO the entity to save.
     * @return the persisted entity.
     */
    ToolLogFileDTO save(ToolLogFileDTO toolLogFileDTO);

    /**
     * Partially updates a toolLogFile.
     *
     * @param toolLogFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ToolLogFileDTO> partialUpdate(ToolLogFileDTO toolLogFileDTO);

    /**
     * Get all the toolLogFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToolLogFileDTO> findAll(Pageable pageable);
    /**
     * Get all the ToolLogFileDTO where Task is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ToolLogFileDTO> findAllWhereTaskIsNull();

    /**
     * Get the "id" toolLogFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToolLogFileDTO> findOne(Long id);

    /**
     * Delete the "id" toolLogFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //==== Start Custom Method
    ToolLogFileDTO saveFileByTask(MultipartFile file, TaskDTO taskDto);

    ToolLogFileDTO saveFile(MultipartFile file);

    Optional<ToolLogFile> findByTaskId(Long taskId);
}
