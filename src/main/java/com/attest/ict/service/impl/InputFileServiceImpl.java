package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.custom.exception.FileStorageException;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Tool;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ProfileInputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.service.mapper.NetworkMapper;
import com.attest.ict.service.mapper.ToolMapper;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link InputFile}.
 */
@Service
@Transactional
public class InputFileServiceImpl implements InputFileService {

    private final Logger log = LoggerFactory.getLogger(InputFileServiceImpl.class);

    private final InputFileRepository inputFileRepository;

    private final InputFileMapper inputFileMapper;

    private final NetworkMapper networkMapper;

    private final ToolMapper toolMapper;

    private final BranchProfileRepository branchProfileRepository;

    private final FlexProfileRepository flexProfileRepository;

    private final GenProfileRepository genProfileRepository;

    private final LoadProfileRepository loadProfileRepository;

    private final TransfProfileRepository transfProfileRepository;

    private final ToolRepository toolRepository;

    public InputFileServiceImpl(
        InputFileRepository inputFileRepository,
        InputFileMapper inputFileMapper,
        NetworkMapper networkMapper,
        ToolMapper toolMapper,
        ToolRepository toolRepository,
        BranchProfileRepository branchProfileRepository,
        FlexProfileRepository flexProfileRepository,
        GenProfileRepository genProfileRepository,
        LoadProfileRepository loadProfileRepository,
        TransfProfileRepository transfProfileRepository
    ) {
        this.inputFileRepository = inputFileRepository;
        this.inputFileMapper = inputFileMapper;
        this.networkMapper = networkMapper;
        this.toolMapper = toolMapper;
        this.toolRepository = toolRepository;
        this.branchProfileRepository = branchProfileRepository;
        this.flexProfileRepository = flexProfileRepository;
        this.genProfileRepository = genProfileRepository;
        this.loadProfileRepository = loadProfileRepository;
        this.transfProfileRepository = transfProfileRepository;
    }

    @Override
    public InputFileDTO save(InputFileDTO inputFileDTO) {
        log.debug("Request to save InputFile : {}", inputFileDTO);
        InputFile inputFile = inputFileMapper.toEntity(inputFileDTO);
        inputFile = inputFileRepository.save(inputFile);
        return inputFileMapper.toDto(inputFile);
    }

    @Override
    public Optional<InputFileDTO> partialUpdate(InputFileDTO inputFileDTO) {
        log.debug("Request to partially update InputFile : {}", inputFileDTO);

        return inputFileRepository
            .findById(inputFileDTO.getId())
            .map(existingInputFile -> {
                inputFileMapper.partialUpdate(existingInputFile, inputFileDTO);

                return existingInputFile;
            })
            .map(inputFileRepository::save)
            .map(inputFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InputFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InputFiles");
        return inputFileRepository.findAll(pageable).map(inputFileMapper::toDto);
    }

    /**
     *  Get all the inputFiles where GenProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findAllWhereGenProfileIsNull() {
        log.debug("Request to get all inputFiles where GenProfile is null");
        return StreamSupport
            .stream(inputFileRepository.findAll().spliterator(), false)
            .filter(inputFile -> inputFile.getGenProfile() == null)
            .map(inputFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the inputFiles where FlexProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findAllWhereFlexProfileIsNull() {
        log.debug("Request to get all inputFiles where FlexProfile is null");
        return StreamSupport
            .stream(inputFileRepository.findAll().spliterator(), false)
            .filter(inputFile -> inputFile.getFlexProfile() == null)
            .map(inputFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the inputFiles where LoadProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findAllWhereLoadProfileIsNull() {
        log.debug("Request to get all inputFiles where LoadProfile is null");
        return StreamSupport
            .stream(inputFileRepository.findAll().spliterator(), false)
            .filter(inputFile -> inputFile.getLoadProfile() == null)
            .map(inputFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the inputFiles where TransfProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findAllWhereTransfProfileIsNull() {
        log.debug("Request to get all inputFiles where TransfProfile is null");
        return StreamSupport
            .stream(inputFileRepository.findAll().spliterator(), false)
            .filter(inputFile -> inputFile.getTransfProfile() == null)
            .map(inputFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the inputFiles where BranchProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findAllWhereBranchProfileIsNull() {
        log.debug("Request to get all inputFiles where BranchProfile is null");
        return StreamSupport
            .stream(inputFileRepository.findAll().spliterator(), false)
            .filter(inputFile -> inputFile.getBranchProfile() == null)
            .map(inputFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InputFileDTO> findOne(Long id) {
        log.debug("Request to get InputFile : {}", id);
        return inputFileRepository.findById(id).map(inputFileMapper::toDto);
    }

    /*@Override
    public void delete(Long id) {
        log.debug("Request to delete InputFile : {}", id);
        inputFileRepository.deleteById(id);
    }*/

    //Start Custom Methods

    @Override
    public boolean delete(Long id) {
        log.debug("Request to delete InputFile : {}", id);
        Optional<InputFile> fileToDelete = inputFileRepository.findById(id);

        if (fileToDelete.isPresent()) {
            Tool tool = fileToDelete.get().getTool();
            if (tool == null) {
                //-- Remove branchProfile and all branchElVal: CascadeType.REMOVE on relationship
                Optional<BranchProfile> branchProfileOpt = branchProfileRepository.findByInputFileId(fileToDelete.get().getId());
                if (branchProfileOpt.isPresent()) {
                    // BranchElVall will be removed automatically
                    branchProfileRepository.delete(branchProfileOpt.get());
                    log.debug(" Branch Profile: {} deleted succesfully! " + branchProfileOpt.get());
                }

                //-- Remove all generation profiles and generation electrical values linked to inputFile
                Optional<GenProfile> genProfileOpt = genProfileRepository.findByInputFileId(fileToDelete.get().getId());
                if (genProfileOpt.isPresent()) {
                    //--  GenElVall will be removed automatically:: CascadeType.REMOVE on relationship
                    genProfileRepository.delete(genProfileOpt.get());
                    log.debug(" GenProfile: {} deleted succesfully! " + genProfileOpt.get());
                }

                //-- Remove flexProfile and all flexElVal
                Optional<FlexProfile> flexProfileOpt = flexProfileRepository.findByInputFileId(fileToDelete.get().getId());
                if (flexProfileOpt.isPresent()) {
                    //--  FlexElVal will be removed automatically:: CascadeType.REMOVE on relationship
                    flexProfileRepository.delete(flexProfileOpt.get());
                    log.debug(" FlexProfile: {} deleted succesfully! " + flexProfileOpt.get());
                }

                //-- Remove all load profiles and load electrical values linked to inputFile
                Optional<LoadProfile> loadProfileOpt = loadProfileRepository.findByInputFileId(fileToDelete.get().getId());

                if (loadProfileOpt.isPresent()) {
                    //--  LoadElVal will be removed automatically:: CascadeType.REMOVE on relationship
                    loadProfileRepository.delete(loadProfileOpt.get());
                    log.debug(" Load Profile: {} deleted succesfully! " + loadProfileOpt.get());
                }

                //-- Remove all transformer profiles and  electrical values linked to inputFile
                Optional<TransfProfile> transfProfileOpt = transfProfileRepository.findByInputFileId(fileToDelete.get().getId());
                if (transfProfileOpt.isPresent()) {
                    //--  TransfElVal will be removed automatically:: CascadeType.REMOVE on relationship
                    transfProfileRepository.delete(transfProfileOpt.get());
                    log.debug("Transformer Profile: {} deleted succesfully! " + transfProfileOpt.get());
                }

                inputFileRepository.deleteById(id);
                return true;
            } else {
                //TODO it's necessary to review how to manage the relationship between tool and input file: the same input file tools could be used by several tools....
                // inputFile is used by one Tool
                log.debug("InputFile : {} will not be removed, it is using by the tool {} ", id, tool.getName());
                return false;
            }
        }
        return false;
    }

    /**
     * @param networkId
     * @param fileName
     * @param toolName
     */
    @Override
    public Optional<InputFileDTO> findLastFileByNetworkIdAndFileNameAndToolName(Long networkId, String fileName, String toolName) {
        log.debug("Request to get the most recently file: {} uploaded, for networkid: {}, toolName: {}", fileName, networkId, toolName);
        return inputFileRepository
            .findTopByNetworkIdAndFileNameAndToolNameOrderByUploadTimeDesc(networkId, fileName, toolName)
            .map(inputFileMapper::toDto);
    }

    /**
     * @param networkId
     * @param fileName
     */
    @Override
    public Optional<InputFileDTO> findFileByNetworkIdAndFileName(Long networkId, String fileName) {
        log.debug("Request to get the filename: {} for networkid: {}", fileName, networkId);
        return inputFileRepository.findByNetworkIdAndFileName(networkId, fileName).map(inputFileMapper::toDto);
    }

    /**
     * @param networkId
     * @param fileName
     * @param descr
     */
    @Override
    public List<InputFile> findByNetworkIdAndFileNameAndDescription(Long networkId, String fileName, String description) {
        log.debug("Request to get the filename: {} for networkid: {}  and descr {}", fileName, networkId, description);
        return inputFileRepository.findByNetworkIdAndFileNameAndDescription(networkId, fileName, description);
    }

    /**
     * toolDTO is null for matpower network's file
     */
    @Override
    public InputFileDTO saveFileForNetwork(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) {
        log.debug("Request to save file file: {}, for networkName: {}", file.getOriginalFilename(), networkDto.getName());
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            InputFile inputFile = new InputFile();
            inputFile.setFileName(fileName);
            inputFile.setDataContentType(file.getContentType());
            inputFile.setNetwork(networkMapper.toEntity(networkDto));
            inputFile.setData(file.getBytes());
            inputFile.setUploadTime(Instant.now());
            if (toolDto != null) inputFile.setTool(toolMapper.toEntity(toolDto));
            InputFile inputFileSaved = inputFileRepository.save(inputFile);
            return inputFileMapper.toDto(inputFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public InputFileDTO saveFileForNetworkWithDescr(MultipartFile file, NetworkDTO networkDto, String description) {
        log.debug(
            "Request to save file file: {}, for networkName: {} ,description: {}",
            file.getOriginalFilename(),
            networkDto.getName(),
            description
        );
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            //   Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            InputFile inputFile = new InputFile();
            inputFile.setFileName(fileName);
            inputFile.setDataContentType(file.getContentType());
            inputFile.setNetwork(networkMapper.toEntity(networkDto));
            inputFile.setDescription(description);
            inputFile.setData(file.getBytes());
            inputFile.setUploadTime(Instant.now());
            InputFile inputFileSaved = inputFileRepository.save(inputFile);
            return inputFileMapper.toDto(inputFileSaved);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     *
     */
    @Override
    public List<ProfileInputFileDTO> findFilesLoadedForNetwork(Long networkId) {
        log.debug("Request to get files loaded for networkId: {} ", networkId);
        List<Tuple> filesUploaded = inputFileRepository.findAllProfileByNetworkId(networkId);
        List<ProfileInputFileDTO> inputFileProfiles = filesUploaded
            .stream()
            .map(t ->
                new ProfileInputFileDTO(
                    t.get(0, BigInteger.class), // id
                    t.get(1, String.class), //season
                    t.get(2, String.class), //typicalDay
                    t.get(3, Integer.class), //mode
                    t.get(4, Double.class), //typeInterval
                    t.get(5, String.class), //fileName
                    t.get(6, String.class) //description
                )
            )
            .collect(Collectors.toList());

        return inputFileProfiles;
    }

    @Override
    public Boolean isNetworkFileAvailable(Long networkId) {
        List<InputFile> inputFiles = inputFileRepository.findByNetworkIdAndDescription(networkId, AttestConstants.INPUT_FILE_NETWORK_DESCR);
        return (inputFiles != null && !inputFiles.isEmpty() && inputFiles.size() == 1);
    }

    @Override
    public InputFile findNetworkFileByNetworkId(Long networkId) {
        List<InputFile> inputFiles = inputFileRepository.findByNetworkIdAndDescription(networkId, AttestConstants.INPUT_FILE_NETWORK_DESCR);
        if (inputFiles.size() > 1) {
            throw new FileStorageException("Sorry! Network id: " + networkId + " contains more then one network's file ");
        } else {
            return inputFiles.get(0);
        }
    }
}
