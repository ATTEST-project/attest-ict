package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T52InputParamDTO extends T5InputParamDTO {

    private static final long serialVersionUID = 1234567L;

    private CoordinatesConfigDTO coordinatesConfig;

    public CoordinatesConfigDTO getCoordinatesConfig() {
        return coordinatesConfig;
    }

    public void setCoordinatesConfig(CoordinatesConfigDTO coordinatesConfig) {
        this.coordinatesConfig = coordinatesConfig;
    }
}
