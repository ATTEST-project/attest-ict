package com.attest.ict.service;

import com.attest.ict.helper.TaskStatus.Status;
import com.attest.ict.service.dto.*;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

/**
 * Tool Service Interface for managing Attest Tool Page
 */
public interface ToolExecutionService {
    Optional<ToolDTO> findToolByName(String name);

    String getToolPath(String toolRootDir, ToolDTO toolDto);

    TaskDTO createTask(ToolDTO toolDto) throws Exception;

    void uploadToolLogFileToDB(Path f, TaskDTO taskDto, boolean delete);

    TaskDTO updateTaskStatus(TaskDTO taskDto, Status status);

    TaskDTO updateTaskToolLogFile(TaskDTO taskDto, ToolLogFileDTO toolLogFileDTO);

    TaskDTO updateTaskSimulation(TaskDTO taskDto, SimulationDTO simulationDto, Status status);

    ToolLogFileDTO uploadToolLogFileToDB(Path f, boolean delete);

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

    void saveOutputFilesOnDB(
        File outputPath,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        List<String> extension,
        List<OutputFileDTO> outputFileDtoSavedList,
        SimulationDTO simulationDto
    ) throws IOException;

    void saveAllOutputDirFilesOnDB(
        File outputPath,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        List<String> extension,
        List<OutputFileDTO> outputFileDtoSavedList,
        SimulationDTO simulationDto
    ) throws IOException;

    ByteArrayOutputStream zipOutputResults(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException;

    Set<InputFileDTO> getInputFilesDtoSetFromList(List<InputFileDTO> filesList);

    List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        String inputDir,
        List<String> fileContentTypes
    ) throws Exception;

    CompletableFuture<String> asyncRun(NetworkDTO networkDto, ToolDTO toolDto, Map<String, String> toolConfigMap, List<String> suffixList)
        throws RunningToolException, Exception;

    SimulationDTO initSimulation(
        ToolDTO toolDto,
        NetworkDTO networkDto,
        UUID uuid,
        File jsonConfigFile,
        TaskDTO taskDto,
        Set<InputFileDTO> inputFileDtoSet,
        String description
    ) throws IOException;
}
