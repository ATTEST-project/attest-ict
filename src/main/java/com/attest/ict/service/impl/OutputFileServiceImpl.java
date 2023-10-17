package com.attest.ict.service.impl;

import com.attest.ict.custom.exception.FileStorageException;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolResultsOutputFileDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import com.attest.ict.service.mapper.OutputFileMapper;
import com.attest.ict.service.mapper.SimulationMapper;
import com.attest.ict.service.mapper.ToolMapper;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing {@link OutputFile}.
 */
@Service
@Transactional
public class OutputFileServiceImpl implements OutputFileService {

    private final Logger log = LoggerFactory.getLogger(OutputFileServiceImpl.class);

    private final OutputFileRepository outputFileRepository;

    private final OutputFileMapper outputFileMapper;

    private final NetworkMapper networkMapper;

    private final ToolMapper toolMapper;

    private final SimulationMapper simulationMapper;

    public OutputFileServiceImpl(
        OutputFileRepository outputFileRepository,
        OutputFileMapper outputFileMapper,
        NetworkMapper networkMapper,
        ToolMapper toolMapper,
        SimulationMapper simulationMapper
    ) {
        this.outputFileRepository = outputFileRepository;
        this.outputFileMapper = outputFileMapper;
        this.networkMapper = networkMapper;
        this.toolMapper = toolMapper;
        this.simulationMapper = simulationMapper;
    }

    @Override
    public OutputFileDTO save(OutputFileDTO outputFileDTO) {
        log.debug("Request to save OutputFile : {}", outputFileDTO);
        OutputFile outputFile = outputFileMapper.toEntity(outputFileDTO);
        outputFile = outputFileRepository.save(outputFile);
        return outputFileMapper.toDto(outputFile);
    }

    @Override
    public Optional<OutputFileDTO> partialUpdate(OutputFileDTO outputFileDTO) {
        log.debug("Request to partially update OutputFile : {}", outputFileDTO);

        return outputFileRepository
            .findById(outputFileDTO.getId())
            .map(existingOutputFile -> {
                outputFileMapper.partialUpdate(existingOutputFile, outputFileDTO);

                return existingOutputFile;
            })
            .map(outputFileRepository::save)
            .map(outputFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutputFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutputFiles");
        return outputFileRepository.findAll(pageable).map(outputFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutputFileDTO> findOne(Long id) {
        log.debug("Request to get OutputFile : {}", id);
        return outputFileRepository.findById(id).map(outputFileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutputFile : {}", id);
        outputFileRepository.deleteById(id);
    }

    // Start Custom Methods
    /*
    @Override
    public OutputFileDTO saveFileForNetworkAndTool(MultipartFile mpFile, NetworkDTO networkDto, ToolDTO toolDto) {
        log.debug("Request to save file file: {}, for networkName: {}", mpFile.getOriginalFilename(), networkDto.getName());
        // Normalize file name
        String fileName = StringUtils.cleanPath(mpFile.getOriginalFilename());
        String contentType = MimeUtils.detect(mpFile);
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            OutputFile outputFile = new OutputFile();
            outputFile.setFileName(fileName);
            outputFile.setTool(toolMapper.toEntity(toolDto));
            // outputFile.setDataContentType(mpFile.getContentType());
            outputFile.setDataContentType(contentType);
            outputFile.setNetwork(networkMapper.toEntity(networkDto));
            outputFile.setData(mpFile.getBytes());
            outputFile.setUploadTime(Instant.now());
            OutputFile outputFileSaved = outputFileRepository.save(outputFile);
            return outputFileMapper.toDto(outputFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    */

    // Start Custom Methods
    @Override
    public OutputFileDTO saveFileForNetworkAndToolAndSimulation(
        File file,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        SimulationDTO simulationDTO,
        String fileNameModified
    ) {
        log.debug("Save File : {}, for networkName: {}, for fileNameModified {}", file.getName(), networkDto.getName(), fileNameModified);
        // Normalize file name
        String fileName = org.apache.commons.lang3.StringUtils.isBlank(fileNameModified)
            ? StringUtils.cleanPath(file.getName())
            : StringUtils.cleanPath(fileNameModified);
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            OutputFile outputFile = new OutputFile();
            outputFile.setFileName(fileName);
            outputFile.setTool(toolMapper.toEntity(toolDto));
            String contentType = FileUtils.probeContentType(file.toPath());
            byte[] fileByteArray = Files.readAllBytes(file.toPath());
            outputFile.setDataContentType(contentType);
            outputFile.setNetwork(networkMapper.toEntity(networkDto));
            outputFile.setData(fileByteArray);
            outputFile.setUploadTime(Instant.now());
            outputFile.setSimulation(simulationMapper.toEntity(simulationDTO));
            OutputFile outputFileSaved = outputFileRepository.save(outputFile);
            return outputFileMapper.toDto(outputFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Error saving file " + fileName + ".", ex);
        }
    }

    @Override
    public Optional<OutputFileDTO> findLastFileByNetworkIdAndFileNameAndToolName(Long networkId, String fileName, String toolName) {
        log.debug("Find the most recently file: {} uploaded, by networkId: {}", fileName, networkId);
        //return inputFileRepository.getLastFileByNetworkIdAndFileName(networkId, fileName);
        return outputFileRepository
            .findTopByNetworkIdAndFileNameAndToolNameOrderByUploadTimeDesc(networkId, fileName, toolName)
            .map(outputFileMapper::toDto);
    }

    @Override
    public List<OutputFile> findFromSimulationId(Long simulationId) {
        log.debug("Find outputFile by simulationId: {}", simulationId);
        return outputFileRepository.findBySimulationId(simulationId);
    }

    /**
     * Service method to retrieve a list of tool results' output files based on the provided filters.
     *
     * @param networkId    The ID of the network associated with the tool results.
     * @param toolId       The ID of the tool.
     * @param fileName     The name (or part) of the name of the output file to filter by.
     * @param dateTimeEnd  The running task end date and time to filter by.
     * @return A list of {@link ToolResultsOutputFileDTO} objects containing the filtered tool results' output files.
     */
    @Override
    public List<ToolResultsOutputFileDTO> findToolResults(Long networkId, Long toolId, String fileName, Instant dateTimeEnd) {
        log.debug(
            "Search tool's'results filtered by networkId: {}, toolId: {}, fileName: {}, dateTimeEnd: {}",
            networkId,
            toolId,
            fileName,
            dateTimeEnd
        );

        // Query the repository to get filtered tool results
        List<Tuple> toolResultsFiltered = outputFileRepository.findToolResults(networkId, toolId, fileName, dateTimeEnd);

        // Convert the query results into ToolResultsOutputFileDTO objects
        List<ToolResultsOutputFileDTO> toolResults = toolResultsFiltered
            .stream()
            .map(t ->
                new ToolResultsOutputFileDTO(
                    t.get(0, BigInteger.class), // toolId
                    t.get(1, BigInteger.class), // networkId
                    t.get(2, BigInteger.class), // outputFileId
                    t.get(3, String.class), // fileName
                    t.get(4, BigInteger.class), // simulationId
                    t.get(5, String.class), // simulationUUID
                    t.get(6, String.class), // simulationDescription
                    t.get(7, BigInteger.class), // taskId
                    t.get(8, Timestamp.class), // dateTimeStart
                    t.get(9, Timestamp.class), // dateTimeEnd
                    t.get(10, String.class), // userId
                    t.get(11, String.class) // Login
                )
            )
            .collect(Collectors.toList());

        return toolResults;
    }
}
