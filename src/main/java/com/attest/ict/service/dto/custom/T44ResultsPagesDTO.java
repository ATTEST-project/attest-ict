package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T44ResultsPagesDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private List<T44PageDTO> pages;

    private ToolConfigParameters toolConfigParameters;

    private List<Integer> contingencies;

    private List<Integer> scenarios;

    public List<Integer> getContingencies() {
        return contingencies;
    }

    public void setContingencies(List<Integer> contingencies) {
        this.contingencies = contingencies;
    }

    public List<Integer> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Integer> scenarios) {
        this.scenarios = scenarios;
    }

    public List<T44PageDTO> getPages() {
        return pages;
    }

    public void setPages(List<T44PageDTO> pages) {
        this.pages = pages;
    }

    public ToolConfigParameters getToolConfigParameters() {
        return toolConfigParameters;
    }

    public void setToolConfigParameters(ToolConfigParameters toolConfigParameters) {
        this.toolConfigParameters = toolConfigParameters;
    }
}
