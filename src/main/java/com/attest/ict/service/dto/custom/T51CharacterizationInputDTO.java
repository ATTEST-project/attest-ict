package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T51CharacterizationInputDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<T51CharacterizationConfigDTO> configs;

    public List<T51CharacterizationConfigDTO> getConfigs() {
        return configs;
    }

    public void setConfigs(List<T51CharacterizationConfigDTO> configs) {
        this.configs = configs;
    }
}
