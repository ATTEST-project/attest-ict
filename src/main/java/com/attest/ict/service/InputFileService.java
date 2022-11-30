package com.attest.ict.service;

import com.attest.ict.domain.InputFile;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ProfileInputFileDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.attest.ict.domain.InputFile}.
 */
public interface InputFileService {
    /**
     * Save a inputFile.
     *
     * @param inputFileDTO the entity to save.
     * @return the persisted entity.
     */
    InputFileDTO save(InputFileDTO inputFileDTO);

    /**
     * Partially updates a inputFile.
     *
     * @param inputFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InputFileDTO> partialUpdate(InputFileDTO inputFileDTO);

    /**
     * Get all the inputFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InputFileDTO> findAll(Pageable pageable);
    /**
     * Get all the InputFileDTO where GenProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InputFileDTO> findAllWhereGenProfileIsNull();
    /**
     * Get all the InputFileDTO where FlexProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InputFileDTO> findAllWhereFlexProfileIsNull();
    /**
     * Get all the InputFileDTO where LoadProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InputFileDTO> findAllWhereLoadProfileIsNull();
    /**
     * Get all the InputFileDTO where TransfProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InputFileDTO> findAllWhereTransfProfileIsNull();
    /**
     * Get all the InputFileDTO where BranchProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InputFileDTO> findAllWhereBranchProfileIsNull();

    /**
     * Get the "id" inputFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InputFileDTO> findOne(Long id);

    /**
     * Delete the "id" inputFile.
     *
     * @param id the id of the entity.
     */
    //void delete(Long id);

    //Start Custom methods

    /**
     * Delete the "id" inputFile.
     *
     * @param id the id of the entity.
     */
    boolean delete(Long id);

    Optional<InputFileDTO> findLastFileByNetworkIdAndFileNameAndToolName(Long networkId, String fileName, String toolName);

    InputFileDTO saveFileForNetwork(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto);

    Optional<InputFileDTO> findFileByNetworkIdAndFileName(Long networkId, String fileName);

    InputFileDTO saveFileForNetworkWithDescr(MultipartFile file, NetworkDTO networkDto, String description);

    List<ProfileInputFileDTO> findFilesLoadedForNetwork(Long networkId);

    Boolean isNetworkFileAvailable(Long networkId);

    /**
     * Get file with name "fileName" for "networkId" of type "description"
     * @param networkId
     * @param fileName
     * @param description
     */
    List<InputFile> findByNetworkIdAndFileNameAndDescription(Long networkId, String fileName, String description);

    InputFile findNetworkFileByNetworkId(Long networkId);
}
