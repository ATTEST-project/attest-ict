package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T41TableDataDTO extends TableDataDTO {

    private static final long serialVersionUID = 1234567L;
    List<Integer> scenarios;

    public List<Integer> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Integer> scenarios) {
        this.scenarios = scenarios;
    }
}
