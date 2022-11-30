package com.attest.ict.service.impl;

import com.attest.ict.custom.exception.ImportToolCsvFileException;
import com.attest.ict.domain.Tool;
import com.attest.ict.helper.csv.exception.CsvReaderFileException;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.csv.reader.annotation.ToolByFieldName;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.ToolService;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.mapper.ToolMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Tool}.
 */
@Service
@Transactional
public class ToolServiceImpl implements ToolService {

    private final Logger log = LoggerFactory.getLogger(ToolServiceImpl.class);

    private final ToolRepository toolRepository;

    private final ToolMapper toolMapper;

    public ToolServiceImpl(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }

    @Override
    public ToolDTO save(ToolDTO toolDTO) {
        log.debug("Request to save Tool : {}", toolDTO);
        Tool tool = toolMapper.toEntity(toolDTO);
        tool = toolRepository.save(tool);
        return toolMapper.toDto(tool);
    }

    @Override
    public Optional<ToolDTO> partialUpdate(ToolDTO toolDTO) {
        log.debug("Request to partially update Tool : {}", toolDTO);

        return toolRepository
            .findById(toolDTO.getId())
            .map(existingTool -> {
                toolMapper.partialUpdate(existingTool, toolDTO);

                return existingTool;
            })
            .map(toolRepository::save)
            .map(toolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tools");
        return toolRepository.findAll(pageable).map(toolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ToolDTO> findOne(Long id) {
        log.debug("Request to get Tool : {}", id);
        return toolRepository.findById(id).map(toolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tool : {}", id);
        toolRepository.deleteById(id);
    }

    // Start Custom
    //First import of data present in toolCsv
    public void firstImportToolFromCsv(MultipartFile file) throws ImportToolCsvFileException, CsvReaderFileException {
        CsvFileReader csvFileReader = new CsvFileReader();
        List<ToolByFieldName> data = csvFileReader.parseToolCsvMultiPartFile(file);
        long tableSize = toolRepository.count();
        if (tableSize == 0) {
            log.debug("Tool is empty, first import is allowed");
            for (ToolByFieldName tfn : data) {
                Tool tool = new Tool();
                tool.setName(tfn.getName());
                tool.setDescription(tfn.getDescription());
                tool.setNum(tfn.getNum());
                tool.setPath(tfn.getPath());
                tool.setWorkPackage(tfn.getWorkPackage());
                toolRepository.save(tool);
            }
        } else {
            String msg = "Database table 'Tool'  is NOT empty, sorry but this operation is not allowed!";
            log.error(msg);
            throw new ImportToolCsvFileException(msg);
        }
    }
}
