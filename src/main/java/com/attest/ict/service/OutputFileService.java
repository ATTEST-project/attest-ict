package com.attest.ict.service;

import com.attest.ict.domain.OutputFile;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolResultsOutputFileDTO;
import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.OutputFile}.
 */
public interface OutputFileService {
    /**
     * Save a outputFile.
     *
     * @param outputFileDTO the entity to save.
     * @return the persisted entity.
     */
    OutputFileDTO save(OutputFileDTO outputFileDTO);

    /**
     * Partially updates a outputFile.
     *
     * @param outputFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OutputFileDTO> partialUpdate(OutputFileDTO outputFileDTO);

    /**
     * Get all the outputFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutputFileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" outputFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutputFileDTO> findOne(Long id);

    /**
     * Delete the "id" outputFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //Start Custom Methods
    // OutputFileDTO saveFileForNetworkAndTool(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto);

    Optional<OutputFileDTO> findLastFileByNetworkIdAndFileNameAndToolName(Long networkId, String fileName, String toolName);

    List<OutputFile> findFromSimulationId(Long simulationId);

    //  OutputFileDTO saveFileForNetworkAndToolAndSimulation(File file, NetworkDTO networkDto, ToolDTO toolDto, SimulationDTO simulationDTO);
    OutputFileDTO saveFileForNetworkAndToolAndSimulation(
        File file,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        SimulationDTO simulationDTO,
        String fileNameModified
    );

    List<ToolResultsOutputFileDTO> findToolResults(Long networkId, Long toolId, String fileName, Instant dateTimeEnd);
}
