package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class T53AllConfigDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("input_dir")
    private String inputDir;

    @JsonProperty("output_dir")
    private String outputDir;

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    @Override
    public String toString() {
        return "T53AllConfigDTO [inputDir=" + inputDir + ", outputDir=" + outputDir + "]";
    }
}
