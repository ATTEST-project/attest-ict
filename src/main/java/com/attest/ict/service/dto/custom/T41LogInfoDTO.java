package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.time.LocalDateTime;

public class T41LogInfoDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    Integer index;
    String caseName;
    String inputFile;
    String outputFile;
    LocalDateTime startTime;
    String intPf;
    String nViolations;
    String optStarted;
    String optFound;

    public T41LogInfoDTO(
        Integer index,
        String caseName,
        String inputFile,
        String outputFile,
        LocalDateTime startTime,
        String intPf,
        String nViolation,
        String optStarted,
        String optFound
    ) {
        this.index = index;
        this.caseName = caseName;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.startTime = startTime;
        this.intPf = intPf;
        this.nViolations = nViolation;
        this.optStarted = optStarted;
        this.optFound = optFound;
    }

    public T41LogInfoDTO(String caseName, LocalDateTime startTime, String intPf, String nViolation, String optStarted, String optFound) {
        this.caseName = caseName;
        this.startTime = startTime;
        this.intPf = intPf;
        this.nViolations = nViolation;
        this.optStarted = optStarted;
        this.optFound = optFound;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getIntPf() {
        return intPf;
    }

    public void setIntPf(String intPf) {
        this.intPf = intPf;
    }

    public String getnViolations() {
        return nViolations;
    }

    public void setnViolations(String nViolations) {
        this.nViolations = nViolations;
    }

    public String getOptStarted() {
        return optStarted;
    }

    public void setOptStarted(String optStarted) {
        this.optStarted = optStarted;
    }

    public String getOptFound() {
        return optFound;
    }

    public void setOptFound(String optFound) {
        this.optFound = optFound;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
