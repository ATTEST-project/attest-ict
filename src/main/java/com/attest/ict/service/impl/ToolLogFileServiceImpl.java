package com.attest.ict.service.impl;

import com.attest.ict.custom.exception.FileStorageException;
import com.attest.ict.domain.Task;
import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.repository.ToolLogFileRepository;
import com.attest.ict.service.ToolLogFileService;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolLogFileDTO;
import com.attest.ict.service.mapper.TaskMapper;
import com.attest.ict.service.mapper.ToolLogFileMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link ToolLogFile}.
 */
@Service
@Transactional
public class ToolLogFileServiceImpl implements ToolLogFileService {

    private final Logger log = LoggerFactory.getLogger(ToolLogFileServiceImpl.class);

    private final ToolLogFileRepository toolLogFileRepository;

    private final ToolLogFileMapper toolLogFileMapper;

    private final TaskMapper taskMapper;

    public ToolLogFileServiceImpl(ToolLogFileRepository toolLogFileRepository, ToolLogFileMapper toolLogFileMapper, TaskMapper taskMapper) {
        this.toolLogFileRepository = toolLogFileRepository;
        this.toolLogFileMapper = toolLogFileMapper;
        this.taskMapper = taskMapper;
    }

    @Override
    public ToolLogFileDTO save(ToolLogFileDTO toolLogFileDTO) {
        log.debug("Request to save ToolLogFile : {}", toolLogFileDTO);
        ToolLogFile toolLogFile = toolLogFileMapper.toEntity(toolLogFileDTO);
        toolLogFile = toolLogFileRepository.save(toolLogFile);
        return toolLogFileMapper.toDto(toolLogFile);
    }

    @Override
    public Optional<ToolLogFileDTO> partialUpdate(ToolLogFileDTO toolLogFileDTO) {
        log.debug("Request to partially update ToolLogFile : {}", toolLogFileDTO);

        return toolLogFileRepository
            .findById(toolLogFileDTO.getId())
            .map(existingToolLogFile -> {
                toolLogFileMapper.partialUpdate(existingToolLogFile, toolLogFileDTO);

                return existingToolLogFile;
            })
            .map(toolLogFileRepository::save)
            .map(toolLogFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToolLogFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToolLogFiles");
        return toolLogFileRepository.findAll(pageable).map(toolLogFileMapper::toDto);
    }

    /**
     *  Get all the toolLogFiles where Task is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ToolLogFileDTO> findAllWhereTaskIsNull() {
        log.debug("Request to get all toolLogFiles where Task is null");
        return StreamSupport
            .stream(toolLogFileRepository.findAll().spliterator(), false)
            .filter(toolLogFile -> toolLogFile.getTask() == null)
            .map(toolLogFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ToolLogFileDTO> findOne(Long id) {
        log.debug("Request to get ToolLogFile : {}", id);
        return toolLogFileRepository.findById(id).map(toolLogFileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToolLogFile : {}", id);
        toolLogFileRepository.deleteById(id);
    }

    //======  Start Custom Methods
    @Override
    public ToolLogFileDTO saveFileByTask(MultipartFile file, TaskDTO taskDto) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            ToolLogFile toolLogFile = new ToolLogFile();
            toolLogFile.setFileName(fileName);
            toolLogFile.setDataContentType(file.getContentType());
            Task task = this.taskMapper.toEntity(taskDto);
            toolLogFile.setTask(task);
            toolLogFile.setData(file.getBytes());
            toolLogFile.setUploadTime(Instant.now());
            ToolLogFile logFileSaved = toolLogFileRepository.save(toolLogFile);
            return toolLogFileMapper.toDto(logFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Optional<ToolLogFile> findByTaskId(Long taskId) {
        return toolLogFileRepository.findByTaskId(taskId);
    }
}
