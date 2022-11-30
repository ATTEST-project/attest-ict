package com.attest.ict.service.dto.custom;

public class T53InputParamDTO extends T5InputParamDTO {

    private static final long serialVersionUID = 1234567L;

    private Integer nScenarios;

    private String assetsName;

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

    @Override
    public String toString() {
        return "T53InputParamDTO [nScenarios=" + nScenarios + ", assetsName=" + assetsName + "]";
    }
}
