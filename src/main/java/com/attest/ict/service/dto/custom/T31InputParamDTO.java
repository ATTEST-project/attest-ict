package com.attest.ict.service.dto.custom;

public class T31InputParamDTO extends T31ParamsDTO {

    private static final long serialVersionUID = 1234567L;

    Long networkId;

    String toolName;

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
}
