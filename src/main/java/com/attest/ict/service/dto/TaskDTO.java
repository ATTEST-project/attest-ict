package com.attest.ict.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.attest.ict.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private String taskStatus;

    private String info;

    private Instant dateTimeStart;

    private Instant dateTimeEnd;

    @JsonIgnore
    private ToolLogFileDTO toolLogFile;

    private Long toolLogFileId;

    private String toolLogFileName;

    private Long networkId;

    private Long simulationId;

    private UUID simulationUuid;

    @Lob
    @JsonIgnore
    private byte[] simulationConfigFile;

    @JsonIgnore
    private SimulationDTO simulation;

    private ToolDTO tool;

    private UserDTO user;

    //20230526 add new field to show year, season, with or without flexibility
    private String simulationDescr;

    private String networkName;

    //20231003 toolNum for sort
    private String toolNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public ToolLogFileDTO getToolLogFile() {
        return toolLogFile;
    }

    public void setToolLogFile(ToolLogFileDTO toolLogFile) {
        this.toolLogFile = toolLogFile;
    }

    public SimulationDTO getSimulation() {
        return simulation;
    }

    public void setSimulation(SimulationDTO simulation) {
        this.simulation = simulation;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public void setTool(ToolDTO tool) {
        this.tool = tool;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Long getToolLogFileId() {
        return toolLogFileId;
    }

    public void setToolLogFileId(Long toolLogFileId) {
        this.toolLogFileId = toolLogFileId;
    }

    public String getToolLogFileName() {
        return toolLogFileName;
    }

    public void setToolLogFileName(String toolLogFileName) {
        this.toolLogFileName = toolLogFileName;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
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

    public byte[] getSimulationConfigFile() {
        return simulationConfigFile;
    }

    public void setSimulationConfigFile(byte[] simulationConfigFile) {
        this.simulationConfigFile = simulationConfigFile;
    }

    public String getSimulationDescr() {
        return simulationDescr;
    }

    public void setSimulationDescr(String simulationDescr) {
        this.simulationDescr = simulationDescr;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getToolNum() {
        return toolNum;
    }

    public void setToolNum(String toolNum) {
        this.toolNum = toolNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "TaskDTO{" +
            "id=" +
            id +
            ", taskStatus='" +
            taskStatus +
            '\'' +
            ", info='" +
            info +
            '\'' +
            ", dateTimeStart=" +
            dateTimeStart +
            ", dateTimeEnd=" +
            dateTimeEnd +
            ", toolLogFile=" +
            toolLogFile +
            ", toolLogFileId=" +
            toolLogFileId +
            ", toolLogFileName='" +
            toolLogFileName +
            '\'' +
            ", networkId=" +
            networkId +
            ", simulationId=" +
            simulationId +
            ", simulationUuid=" +
            simulationUuid +
            //  ", simulationConfigFile=" + Arrays.toString(simulationConfigFile) +
            ", simulation=" +
            simulation +
            ", tool=" +
            tool +
            ", user=" +
            user +
            ", simulationDescr='" +
            simulationDescr +
            '\'' +
            ", networkName='" +
            networkName +
            '\'' +
            ", toolNum='" +
            toolNum +
            '\'' +
            '}'
        );
    }
    // prettier-ignore

}
