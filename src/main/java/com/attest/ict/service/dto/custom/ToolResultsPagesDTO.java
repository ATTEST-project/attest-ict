package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class ToolResultsPagesDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<ToolPageDTO> pages;

    private ToolConfigParameters toolConfigParameters;

    public List<ToolPageDTO> getPages() {
        return pages;
    }

    public void setPages(List<ToolPageDTO> pages) {
        this.pages = pages;
    }

    public ToolConfigParameters getToolConfigParameters() {
        return toolConfigParameters;
    }

    public void setToolConfigParameters(ToolConfigParameters toolConfigParameters) {
        this.toolConfigParameters = toolConfigParameters;
    }

    @Override
    public String toString() {
        return "ToolResultsPagesDTO{" + "pages=" + pages + ", toolConfigParameters=" + toolConfigParameters + '}';
    }
}
