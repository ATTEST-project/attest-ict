package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class T51MonitoringLaunchParamDTO extends T51MonitoringInputDTO {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("output_dir")
    private String outputDir;

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}
