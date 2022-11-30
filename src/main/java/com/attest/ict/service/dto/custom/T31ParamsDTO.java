package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class T31ParamsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("line_capacities")
    private List<Number> lineCapacities;

    @JsonProperty("TRS_capacities")
    private List<Number> trsCapacities;

    @JsonProperty("line_costs")
    private List<Number> lineCosts;

    @JsonProperty("TRS_costs")
    private List<Number> trsCosts;

    @JsonProperty("cont_list")
    private List<Number> contList;

    @JsonProperty("line_length")
    private List<Number> lineLength;

    private GrowthDTO growth;

    @JsonProperty("DSR")
    private DsrDTO dsr;

    private List<Number> cluster;

    private Integer oversize;

    @JsonProperty("Max_clusters")
    private Integer maxClusters;

    private List<Integer> scenarios;

    @JsonProperty("case_path")
    private String casePath;

    @JsonProperty("output_path")
    private String outputPath;

    @JsonProperty("attest_inputs_path")
    private String attestInputsPath;

    public List<Number> getLineCapacities() {
        return lineCapacities;
    }

    public void setLineCapacities(List<Number> lineCapacities) {
        this.lineCapacities = lineCapacities;
    }

    public List<Number> getTrsCapacities() {
        return trsCapacities;
    }

    public void setTrsCapacities(List<Number> trsCapacities) {
        this.trsCapacities = trsCapacities;
    }

    public List<Number> getLineCosts() {
        return lineCosts;
    }

    public void setLineCosts(List<Number> lineCosts) {
        this.lineCosts = lineCosts;
    }

    public List<Number> getTrsCosts() {
        return trsCosts;
    }

    public void setTrsCosts(List<Number> trsCosts) {
        this.trsCosts = trsCosts;
    }

    public List<Number> getContList() {
        return contList;
    }

    public void setContList(List<Number> contList) {
        this.contList = contList;
    }

    public List<Number> getLineLength() {
        return lineLength;
    }

    public void setLineLength(List<Number> lineLength) {
        this.lineLength = lineLength;
    }

    public GrowthDTO getGrowth() {
        return growth;
    }

    public void setGrowth(GrowthDTO growth) {
        this.growth = growth;
    }

    public DsrDTO getDsr() {
        return dsr;
    }

    public void setDsr(DsrDTO dsr) {
        this.dsr = dsr;
    }

    public List<Number> getCluster() {
        return cluster;
    }

    public void setCluster(List<Number> cluster) {
        this.cluster = cluster;
    }

    public Integer getOversize() {
        return oversize;
    }

    public void setOversize(Integer oversize) {
        this.oversize = oversize;
    }

    public Integer getMaxClusters() {
        return maxClusters;
    }

    public void setMaxClusters(Integer maxClusters) {
        this.maxClusters = maxClusters;
    }

    public List<Integer> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Integer> scenarios) {
        this.scenarios = scenarios;
    }

    public String getCasePath() {
        return casePath;
    }

    public void setCasePath(String casePath) {
        this.casePath = casePath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getAttestInputsPath() {
        return attestInputsPath;
    }

    public void setAttestInputsPath(String attestInputsPath) {
        this.attestInputsPath = attestInputsPath;
    }
}
