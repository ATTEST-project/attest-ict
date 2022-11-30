package com.attest.ict.service;

import com.attest.ict.domain.OutputFile;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.helper.TaskStatus.Status;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * Tool Service Interface for managing Attest Tool Page
 */
public interface ToolExecutionService {
    void uploadAndMove(MultipartFile file, NetworkDTO networkDto, String folderPath, ToolDTO toolDto) throws IOException;

    void uploadDATInputs(MultipartFile[] files, NetworkDTO networkDto, ToolDTO toolDto) throws Exception;

    void uploadRTTInputs(MultipartFile[] files, NetworkDTO networkDto, ToolDTO toolDto) throws Exception;

    void uploadWindPVInput(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) throws Exception;

    void uploadStochasticMPInput(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) throws Exception;

    String uploadAssetInput(MultipartFile file, NetworkDTO networkDTO, ToolDTO toolDTO) throws Exception;

    void uploadAssetMonitoringInput(MultipartFile file, NetworkDTO networkDTO, ToolDTO toolDTO) throws Exception;

    void uploadOutputFilesToDB(Path f, NetworkDTO networkDto, ToolDTO toolDto);

    boolean checkInputFileNameDAT(String fileName);

    boolean checkInputFileNameRTT(String fileName);

    void createTmpInputsForTools(List<String> fileNames, Long networkId, String path, String toolName) throws IOException;

    List<OutputFile> getAllPlots(Long networkId);

    OutputFile getFdrPlot(Long networkId, String feeder, String scenario);

    OutputFile getVPlot(Long networkId, String scenario);

    Optional<ToolDTO> findToolByName(String name);

    String getToolPath(String toolRootDir, ToolDTO toolDto);

    TaskDTO createTask(ToolDTO toolDto) throws Exception;

    TaskDTO updateTask(TaskDTO taskDto, TaskStatus.Status status);

    void uploadToolLogFileToDB(Path f, TaskDTO taskDto, boolean delete);

    //    SimulationDTO createSimulation(
    //        ToolDTO toolDto,
    //        NetworkDTO networkDto,
    //        UUID uuid,
    //        String description,
    //        File configFile,
    //        Set<InputFileDTO> inputFiles
    //    ) throws IOException;

    TaskDTO updateTaskSimulation(TaskDTO taskDto, SimulationDTO simulationDto, Status status);

    List<OutputFileDTO> saveOutputFileOnDB(File outputPath, NetworkDTO networkDto, ToolDTO toolDto, String outputSuffix) throws IOException;

    // int runToolWrapper(String toolWorkingDir, String jsonConfig, String logFile, String launchFile);

    SimulationDTO createSimulation(
        ToolDTO toolDto,
        NetworkDTO networkDto,
        UUID uuid,
        File configFile,
        TaskDTO taskDto,
        List<InputFileDTO> inputFileTDOList,
        List<OutputFileDTO> outputFileDTOList,
        Status status,
        String description
    ) throws IOException;
}
