package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;

public class T26LaunchParamDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    Map<String, String> network;
    Map<String, String> energy;
    Map<String, String> secondary;
    Map<String, String> tertiary;

    @JsonProperty("output_dir")
    Map<String, String> outputDir;

    @JsonProperty("run_energy")
    Boolean runEnergy;

    @JsonProperty("run_secondary")
    Boolean runSecondary;

    @JsonProperty("run_tertiary")
    Boolean runTertiary;

    public Map<String, String> getNetwork() {
        return network;
    }

    public void setNetwork(Map<String, String> network) {
        this.network = network;
    }

    public Map<String, String> getEnergy() {
        return energy;
    }

    public void setEnergy(Map<String, String> energy) {
        this.energy = energy;
    }

    public Map<String, String> getSecondary() {
        return secondary;
    }

    public void setSecondary(Map<String, String> secondary) {
        this.secondary = secondary;
    }

    public Map<String, String> getTertiary() {
        return tertiary;
    }

    public void setTertiary(Map<String, String> tertiary) {
        this.tertiary = tertiary;
    }

    public Map<String, String> getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(Map<String, String> outputDir) {
        this.outputDir = outputDir;
    }

    public Boolean getRunEnergy() {
        return runEnergy;
    }

    public void setRunEnergy(Boolean runEnergy) {
        this.runEnergy = runEnergy;
    }

    public Boolean getRunSecondary() {
        return runSecondary;
    }

    public void setRunSecondary(Boolean runSecondary) {
        this.runSecondary = runSecondary;
    }

    public Boolean getRunTertiary() {
        return runTertiary;
    }

    public void setRunTertiary(Boolean runTertiary) {
        this.runTertiary = runTertiary;
    }
}
