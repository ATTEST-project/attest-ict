package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.service.ToolWp3ShowResultsService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.tools.constants.T31FileFormat;
import com.attest.ict.tools.constants.T32FileFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ToolWp3ShowResultsServiceImpl implements ToolWp3ShowResultsService {

    private final Logger log = LoggerFactory.getLogger(ToolWp3ShowResultsServiceImpl.class);

    private String attestToolsDir;

    private String toolsPathSimulation;

    public ToolWp3ShowResultsServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);

        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public File getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // eg: /ATSIM/WP3/T3x/0af054ac-d7ad-4c3f-a4f3-8c31a0da67test
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP3/T3x/0af054ac-d7ad-4c3f-a4f3-8c31a0da67test/output_data
        String outputDir = toolDto.getName().equals(ToolVarName.T31_OPT_TOOL_DX)
            ? simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData())
            : simulationWorkingDir.concat(File.separator).concat(T32FileFormat.OUTPUT_DIR);

        List<String> extension = new ArrayList<String>();
        if (toolDto.getName().equals(ToolVarName.T31_OPT_TOOL_DX)) {
            extension.add(T31FileFormat.OUTPUT_SUFFIX);
        } else {
            extension.addAll(T32FileFormat.OUTPUT_SUFFIX);
        }

        log.info("Read file present in outputDir: {} ", outputDir);
        log.info("Filter files with extension: {} ", extension.toString());

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found!");
        }

        File[] files = dir.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    for (String ext : extension) {
                        if (fileName.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        );

        int numFile = files.length;
        if (numFile == 0) {
            log.warn("The directory: " + outputDir + " doesn't contain any output files for tool {} " + toolDto.getName());
            return null;
        }

        if (numFile == 1) {
            log.info("Return  Output file {}:  ", files[0].getPath());
            return files[0];
        } else {
            //t32 produce _pt1 and _pt2 File return _pt2
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (name.contains(T32FileFormat.OUTPUT_SUFFIX.get(1))) {
                    log.info("Return Output file {}:  ", files[i].getPath());
                    return files[i];
                }
            }
        }

        log.info(" Output file not found! for tool: {}" + toolDto.getName());
        return null;
    }
}
