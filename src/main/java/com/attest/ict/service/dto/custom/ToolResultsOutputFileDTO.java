package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ToolResultsOutputFileDTO implements Serializable {

    //--- outputFile attributes
    private Long networkId;

    private Long toolId;

    private Long outputFileId;
    private String fileName;

    //--- simulation attributes
    private Long simulationId;
    private UUID simulationUuid;
    private String simulationDescription;

    //--- task attributes
    private Long taskId;
    private Instant dateTimeStart;
    private Instant dateTimeEnd;

    //--- user attributes
    private String userId;
    private String login;

    public ToolResultsOutputFileDTO(
        BigInteger networkId,
        BigInteger toolId,
        BigInteger outputFileId,
        String fileName,
        BigInteger simulationId,
        String simulationUuid,
        String simulationDescription,
        BigInteger taskId,
        Timestamp dateTimeStart,
        Timestamp dateTimeEnd,
        String userId,
        String login
    ) {
        this.networkId = networkId.longValue();
        this.toolId = toolId.longValue();
        this.outputFileId = outputFileId.longValue();
        this.fileName = fileName;
        this.simulationId = simulationId.longValue();
        this.simulationUuid = UUID.fromString(simulationUuid);
        this.simulationDescription = simulationDescription;
        this.taskId = taskId.longValue();
        this.dateTimeStart = dateTimeStart.toInstant();
        this.dateTimeEnd = dateTimeEnd.toInstant();
        this.userId = userId;
        this.login = login;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public Long getOutputFileId() {
        return outputFileId;
    }

    public void setOutputFileId(Long outputFileId) {
        this.outputFileId = outputFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(Long simulationId) {
        this.simulationId = simulationId;
    }

    public UUID getSimulationUuid() {
        return simulationUuid;
    }

    public void setSimulationUuid(UUID simulationUuid) {
        this.simulationUuid = simulationUuid;
    }

    public String getSimulationDescription() {
        return simulationDescription;
    }

    public void setSimulationDescription(String simulationDescription) {
        this.simulationDescription = simulationDescription;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Instant getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Instant dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public Instant getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(Instant dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolResultsOutputFileDTO that = (ToolResultsOutputFileDTO) o;
        return (
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(toolId, that.toolId) &&
            Objects.equals(outputFileId, that.outputFileId) &&
            Objects.equals(simulationId, that.simulationId) &&
            Objects.equals(taskId, that.taskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, toolId, outputFileId, simulationId, taskId);
    }

    @Override
    public String toString() {
        return (
            "ToolResultsOutputFileDTO{" +
            "networkId=" +
            networkId +
            ", toolId=" +
            toolId +
            ", outputFileId=" +
            outputFileId +
            ", fileName='" +
            fileName +
            '\'' +
            ", simulationId=" +
            simulationId +
            ", simulationUuid=" +
            simulationUuid +
            ", simulationDescription='" +
            simulationDescription +
            '\'' +
            ", taskId=" +
            taskId +
            ", dateTimeStart=" +
            dateTimeStart +
            ", dateTimeEnd=" +
            dateTimeEnd +
            ", userId='" +
            userId +
            '\'' +
            ", login='" +
            login +
            '\'' +
            '}'
        );
    }
}
