package com.attest.ict.service.impl;

import com.attest.ict.custom.exception.FileStorageException;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import com.attest.ict.service.mapper.OutputFileMapper;
import com.attest.ict.service.mapper.ToolMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

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

    public OutputFileServiceImpl(
        OutputFileRepository outputFileRepository,
        OutputFileMapper outputFileMapper,
        NetworkMapper networkMapper,
        ToolMapper toolMapper
    ) {
        this.outputFileRepository = outputFileRepository;
        this.outputFileMapper = outputFileMapper;
        this.networkMapper = networkMapper;
        this.toolMapper = toolMapper;
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
    @Override
    public OutputFileDTO saveFileForNetworkAndTool(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) {
        log.debug("Request to save file file: {}, for networkName: {}", file.getOriginalFilename(), networkDto.getName());
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            OutputFile outputFile = new OutputFile();
            outputFile.setFileName(fileName);
            outputFile.setTool(toolMapper.toEntity(toolDto));
            outputFile.setDataContentType(file.getContentType());
            outputFile.setNetwork(networkMapper.toEntity(networkDto));
            outputFile.setData(file.getBytes());
            outputFile.setUploadTime(Instant.now());
            OutputFile outputFileSaved = outputFileRepository.save(outputFile);
            return outputFileMapper.toDto(outputFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    //Start Custom Methods
    @Override
    public Optional<OutputFileDTO> findLastFileByNetworkIdAndFileNameAndToolName(Long networkId, String fileName, String toolName) {
        log.debug("Request to get the most recently file: {} uploaded, for networkid: {}", fileName, networkId);
        //return inputFileRepository.getLastFileByNetworkIdAndFileName(networkId, fileName);
        return outputFileRepository
            .findTopByNetworkIdAndFileNameAndToolNameOrderByUploadTimeDesc(networkId, fileName, toolName)
            .map(outputFileMapper::toDto);
    }

    @Override
    public List<OutputFile> findFromSimulationId(Long simulationId) {
        log.debug("REST request to get Tool Simulation  results simulationId: {}", simulationId);
        return outputFileRepository.findBySimulationId(simulationId);
    }
}
