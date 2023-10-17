package com.attest.ict.custom.tools.utils;

import com.attest.ict.service.dto.ToolDTO;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolSimulationReferencies {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToolSimulationReferencies.class);

    private String attestToolsDir;
    private String toolsPathSimulation;

    private String t44WithConv;

    private String outputData = "output_data";

    public ToolSimulationReferencies(String attestToolDir, String attestToolSimulationDir) {
        this.attestToolsDir = attestToolDir;
        this.toolsPathSimulation = attestToolSimulationDir;
    }

    /**
     * Retrieves the working directory for a tool.
     *
     * @param toolDto The ToolDTO object representing the tool.
     * @return The path to the working directory of the tool.
     * @throws IOException If there is an issue with file operations.
     */
    public String getToolWorkingDir(ToolDTO toolDto) throws IOException {
        String toolFullPath = this.attestToolsDir.concat(toolDto.getPath());
        File toolWorkingDir = new File(toolFullPath);
        if (!toolWorkingDir.exists()) {
            String msg = "Unable to find directory: " + toolFullPath;
            LOGGER.error(msg);
            throw new IOException(msg);
        }
        return toolFullPath;
    }

    /**
     * Retrieves the working directory for a tool with an optional demo path.
     *
     * @param toolDto The ToolDTO object representing the tool.
     * @param toolInstallDir The optional demo path suffix for the working directory.
     * @return The path to the working directory of the tool.
     * @throws IOException If there is an issue with file operations.
     */
    public String getToolWorkingDir(ToolDTO toolDto, String toolInstallDir) throws IOException {
        String toolPath = this.attestToolsDir.concat(toolDto.getPath());
        LOGGER.debug("toolPath: {} ", toolPath);
        LOGGER.debug("toolInstallDir: {} ", toolInstallDir);
        if (toolInstallDir != null && !toolInstallDir.isEmpty()) {
            int pos = toolPath.lastIndexOf(File.separator);
            if (pos > 0) {
                // If the tool is a demo tool, create a working directory with the given demoPath.
                String prefix = toolPath.substring(0, pos + 1);
                String newToolPath = prefix.concat(toolInstallDir);
                LOGGER.debug("newToolPath: {} ", newToolPath);
                File toolWorkingDir = new File(newToolPath);
                if (!toolWorkingDir.exists()) {
                    String msg = "Unable to find the directory: " + newToolPath;
                    LOGGER.error(msg);
                    throw new IOException(msg);
                }
                return newToolPath;
            }
        }
        return (getToolWorkingDir(toolDto));
    }

    public String getSimulationWorkingDir(String toolWorkPackage, String toolNum, String uuid) {
        String num = (toolNum.equals("T511") || toolNum.equals("T512")) ? "T51" : toolNum;
        return this.toolsPathSimulation.concat(File.separator)
            .concat(toolWorkPackage)
            .concat(File.separator)
            .concat(num)
            .concat(File.separator)
            .concat(uuid);
    }

    public String getOutputData() {
        return this.outputData;
    }
}
