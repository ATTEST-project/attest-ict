package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class T26InputParamDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    Map<String, Boolean> parameters;

    List<Map<String, String>> networkFileNames;

    List<Map<String, String>> energyFileNames;

    List<Map<String, String>> secondaryFileNames;

    List<Map<String, String>> tertiaryFileNames;

    public Map<String, Boolean> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Boolean> parameters) {
        this.parameters = parameters;
    }

    public List<Map<String, String>> getNetworkFileNames() {
        return networkFileNames;
    }

    public void setNetworkFileNames(List<Map<String, String>> networkFileNames) {
        this.networkFileNames = networkFileNames;
    }

    public List<Map<String, String>> getEnergyFileNames() {
        return energyFileNames;
    }

    public void setEnergyFileNames(List<Map<String, String>> energyFileNames) {
        this.energyFileNames = energyFileNames;
    }

    public List<Map<String, String>> getSecondaryFileNames() {
        return secondaryFileNames;
    }

    public void setSecondaryFileNames(List<Map<String, String>> secondaryFileNames) {
        this.secondaryFileNames = secondaryFileNames;
    }

    public List<Map<String, String>> getTertiaryFileNames() {
        return tertiaryFileNames;
    }

    public void setTertiaryFileNames(List<Map<String, String>> tertiaryFileNames) {
        this.tertiaryFileNames = tertiaryFileNames;
    }

    @Override
    public String toString() {
        return (
            "T26InputParamDTO{" +
            "parameters=" +
            parameters +
            ", networkFileNames=" +
            networkFileNames +
            ", energyFileNames=" +
            energyFileNames +
            ", secondaryFileNames=" +
            secondaryFileNames +
            ", tertiaryFileNames=" +
            tertiaryFileNames +
            '}'
        );
    }
}
