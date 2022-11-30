package com.attest.ict.custom.tools.utils;

import com.attest.ict.service.dto.ToolDTO;
import java.io.File;

public class ToolSimulationReferencies {

    private String attestToolsDir;
    private String toolsPathSimulation;

    private String outputData = "output_data";

    public ToolSimulationReferencies(String attestToolDir, String attestToolSimulationDir) {
        this.attestToolsDir = attestToolDir;
        this.toolsPathSimulation = attestToolSimulationDir;
    }

    public String getToolWorkingDir(ToolDTO toolDto) {
        String num = (toolDto.getNum().equals("T511") || toolDto.getNum().equals("T512")) ? "T51" : toolDto.getNum();

        return this.attestToolsDir.concat(File.separator).concat(toolDto.getWorkPackage()).concat(File.separator).concat(num);
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
