package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class T53FirstConfigDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String path;

    private Integer nScenarios;

    private String assetsName;

    @JsonProperty("output_dir")
    private String outputDir;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getnScenarios() {
        return nScenarios;
    }

    public void setnScenarios(Integer nScenarios) {
        this.nScenarios = nScenarios;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}
