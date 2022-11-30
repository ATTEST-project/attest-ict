package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T5InputParamDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<T5ConfigDTO> mainConfig;

    public List<T5ConfigDTO> getMainConfig() {
        return mainConfig;
    }

    public void setMainConfig(List<T5ConfigDTO> mainConfig) {
        this.mainConfig = mainConfig;
    }
}
