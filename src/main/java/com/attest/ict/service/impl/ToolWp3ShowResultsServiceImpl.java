package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.reader.T33ResultsReader;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.SimulationService;
import com.attest.ict.service.ToolWp3ShowResultsService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T33ResultsPagesDTO;
import com.attest.ict.service.dto.custom.TableDataDTO;
import com.attest.ict.tools.constants.T31FileFormat;
import com.attest.ict.tools.constants.T32FileFormat;
import com.attest.ict.tools.constants.T33FileFormat;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolWp3ShowResultsServiceImpl implements ToolWp3ShowResultsService {

    private final Logger log = LoggerFactory.getLogger(ToolWp3ShowResultsServiceImpl.class);

    private String attestToolsDir;

    private String toolsPathSimulation;

    @Autowired
    SimulationService simulationServiceImpl;

    @Autowired
    OutputFileService outputFileServiceImpl;

    public ToolWp3ShowResultsServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("ToolWp3ShowResultsServiceImpl() - attestToolsDir {}", attestToolsDir);

        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("ToolWp3ShowResultsServiceImpl() - toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public File getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        String toolName = toolDto.getName();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // eg: /ATSIM/WP3/T3x/0af054ac-d7ad-4c3f-a4f3-8c31a0da67test
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP3/T3x/0af054ac-d7ad-4c3f-a4f3-8c31a0da67test/output_data

        String outputDir = findOutputDir(toolSimulationRef, toolWorkPackage, uuid, toolName, toolNum);

        List<String> extension = findExtensionOutputFileForTool(toolName);

        log.info("getOutputFile() - Read file present in outputDir: {} ", outputDir);
        log.info("getOutputFile() - Filter files with extension: {} ", extension.toString());

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found!");
        }

        File[] files = dir.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    for (String ext : extension) {
                        if (fileName.endsWith(ext) && !fileName.startsWith("~$")) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        );

        int numFile = files.length;
        if (numFile == 0) {
            String errorMsg = "The directory: " + outputDir + " doesn't contain any expected output files for tool: " + toolDto.getName();
            log.warn("getOutputFile() - " + errorMsg);
            throw new FileNotFoundException(errorMsg);
        }

        if (numFile == 1) {
            log.info("getOutputFile() - Return  Output file {}:  ", files[0].getPath());
            return files[0];
        } else {
            //t32 produce _pt1 and _pt2 File return _pt2
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (name.contains(T32FileFormat.OUTPUT_SUFFIX.get(1))) {
                    log.info("getOutputFile() - Return Output file {}:  ", files[i].getPath());
                    return files[i];
                }
            }
        }

        String errorMsg = " Output file not found! for tool: " + toolDto.getName();
        log.info("getOutputFile() - " + errorMsg);
        throw new FileNotFoundException(errorMsg);
    }

    private String findOutputDir(
        ToolSimulationReferencies toolSimulationRef,
        String toolWorkPackage,
        String uuid,
        String toolName,
        String toolNum
    ) {
        String emptyDir = "";
        // eg: /ATSIM/WP3/T3x/0af054ac-d7ad-4c3f-a4f3-8c31a0da67test
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        switch (toolName) {
            case ToolVarName.T31_OPT_TOOL_DX:
                return simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
            case ToolVarName.T32_OPT_TOOL_TX:
                return simulationWorkingDir.concat(File.separator).concat(T32FileFormat.OUTPUT_DIR);
            case ToolVarName.T33_OPT_TOOL_PLAN_TSO_DSO:
                File dir = FileUtils.searchDir(simulationWorkingDir, T33FileFormat.OUTPUT_RESULTS_DIR);
                return (dir != null) ? dir.getPath() : emptyDir;
        }
        return emptyDir;
    }

    private List<String> findExtensionOutputFileForTool(String toolName) {
        switch (toolName) {
            case ToolVarName.T31_OPT_TOOL_DX:
                return Stream.of(T31FileFormat.OUTPUT_SUFFIX).collect(Collectors.toList());
            case ToolVarName.T32_OPT_TOOL_TX:
                return T32FileFormat.OUTPUT_SUFFIX;
            case ToolVarName.T33_OPT_TOOL_PLAN_TSO_DSO:
                return T33FileFormat.FILE_OUTPUT_SUFFIX;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public T33ResultsPagesDTO getPagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception {
        // --- search simulation's produced by Tool during simulation uuid from FS
        File resultsFile = this.getOutputFile(networkDto, toolDto, uuid);
        T33ResultsReader reader = new T33ResultsReader();
        return reader.getPagesToShow(resultsFile);
    }

    @Override
    public TableDataDTO getTablesData(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String node, String day, String title)
        throws Exception {
        // --- search simulation's produced by Tool during simulation uuid from FS
        File resultsFile = this.getOutputFile(networkDto, toolDto, uuid);
        T33ResultsReader reader = new T33ResultsReader();
        return reader.getTableData(resultsFile, title);
    }
}
