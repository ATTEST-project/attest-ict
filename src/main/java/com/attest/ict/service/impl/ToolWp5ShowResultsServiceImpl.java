package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.service.ToolWp5ShowResultsService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.tools.constants.T51FileFormat;
import com.attest.ict.tools.constants.T52FileFormat;
import com.attest.ict.tools.constants.T53FileFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ToolWp5ShowResultsServiceImpl implements ToolWp5ShowResultsService {

    private final Logger log = LoggerFactory.getLogger(ToolWp5ShowResultsServiceImpl.class);

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    private String attestToolsDir;

    private String toolsPathSimulation;

    public ToolWp5ShowResultsServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("ToolWp5ShowResultsServiceImpl() - attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("ToolWp5ShowResultsServiceImpl() - toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public List<String> getResultsFileName(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        List<String> extensions = this.getResultsFilesExtensionByToolName(toolDto.getName());

        File[] files = getResultsFile(networkDto, toolDto, uuid, extensions);

        List<String> resultsFileName = new ArrayList<String>();
        for (File f : files) {
            resultsFileName.add(f.getName());
        }

        return resultsFileName;
    }

    @Override
    public File[] getResultsFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid, List<String> fileExtensions)
        throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP5/T5/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP4/T5/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data/action_part or output_data
        String outputDir = (toolDto.getName().equals(ToolVarName.T53_MANAGEMENT))
            ? simulationWorkingDir
                .concat(File.separator)
                .concat(toolSimulationRef.getOutputData())
                .concat(File.separator)
                .concat(T53FileFormat.ACTION_PARTS_OUTPUT_DIR)
            : simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        log.debug(outputDir);
        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found");
        }

        // firter for file extension
        File[] files = dir.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    for (String ext : fileExtensions) {
                        if (fileName.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        );

        return files;
    }

    @Override
    public File[] downloadResultsFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        List<String> fileExtensions = this.getDownloadFilesExtensionByToolName(toolDto.getName());
        return getResultsFile(networkDto, toolDto, uuid, fileExtensions);
    }

    @Override
    public String readFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String fileName) throws IOException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP5/T5x/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP5/T5x/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data/action_part or output_data
        String outputDir = (toolDto.getName().equals(ToolVarName.T53_MANAGEMENT))
            ? simulationWorkingDir
                .concat(File.separator)
                .concat(toolSimulationRef.getOutputData())
                .concat(File.separator)
                .concat(T53FileFormat.ACTION_PARTS_OUTPUT_DIR)
            : simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found!");
        }

        String absoluteFileName = outputDir.concat(File.separator).concat(fileName);

        try {
            return Files.readString(Paths.get(absoluteFileName));
        } catch (IOException ioExc) {
            String errMsg = "Error reading file: " + absoluteFileName;
            log.error("readFile() - " + errMsg, ioExc);
            throw new IOException(errMsg + " " + ioExc.getMessage());
        }
    }

    private List<String> getResultsFilesExtensionByToolName(String toolName) {
        List<String> fileExtension = new ArrayList();
        switch (toolName) {
            case ToolVarName.T52_INDICATOR:
                fileExtension.add(T52FileFormat.RESULTS_FILES_EXTENSION);
                break;
            case ToolVarName.T51_CHARACTERIZATION:
            case ToolVarName.T51_MONITORING:
                fileExtension.add(T51FileFormat.RESULTS_FILES_EXTENSION);
                break;
            case ToolVarName.T53_MANAGEMENT:
                fileExtension.add(T53FileFormat.RESULTS_FILES_EXTENSION);
                break;
            default:
                fileExtension.add(".html");
                break;
        }
        return fileExtension;
    }

    private List<String> getDownloadFilesExtensionByToolName(String toolName) {
        List<String> fileExtension = new ArrayList();
        switch (toolName) {
            case ToolVarName.T52_INDICATOR:
                fileExtension = T52FileFormat.DOWNLOAD_FILES_EXTENSION;
                break;
            case ToolVarName.T51_CHARACTERIZATION:
            case ToolVarName.T51_MONITORING:
                fileExtension = T51FileFormat.DOWNLOAD_FILES_EXTENSION;
                break;
            case ToolVarName.T53_MANAGEMENT:
                fileExtension = T53FileFormat.DOWNLOAD_FILES_EXTENSION;
                break;
            default:
                fileExtension.add(".html");
                break;
        }
        return fileExtension;
    }
}
