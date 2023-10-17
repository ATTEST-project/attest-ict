package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T41ResultsPagesDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;
    List<T41LogInfoDTO> logInfos;

    private List<T41PageDTO> pages;

    private ToolConfigParameters toolConfigParameters;

    public List<T41PageDTO> getPages() {
        return pages;
    }

    public void setPages(List<T41PageDTO> pages) {
        this.pages = pages;
    }

    public List<T41LogInfoDTO> getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(List<T41LogInfoDTO> logInfos) {
        this.logInfos = logInfos;
    }

    public ToolConfigParameters getToolConfigParameters() {
        return toolConfigParameters;
    }

    public void setToolConfigParameters(ToolConfigParameters toolConfigParameters) {
        this.toolConfigParameters = toolConfigParameters;
    }
}
