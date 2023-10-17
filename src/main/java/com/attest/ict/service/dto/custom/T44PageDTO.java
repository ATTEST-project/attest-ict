package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T44PageDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String title;
    private Integer numContingencies;

    private Integer numScenarios;

    public Integer getNumScenarios() {
        return numScenarios;
    }

    public void setNumScenarios(Integer numScenarios) {
        this.numScenarios = numScenarios;
    }

    public Integer getNumContingencies() {
        return numContingencies;
    }

    public void setNumContingencies(Integer numContingencies) {
        this.numContingencies = numContingencies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "T44PageDTO{" + "title='" + title + '\'' + ", numContingency=" + numContingencies + ", numScenario=" + numScenarios + '}';
    }
}
