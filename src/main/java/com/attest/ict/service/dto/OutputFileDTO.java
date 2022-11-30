package com.attest.ict.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.attest.ict.domain.OutputFile} entity.
 */
public class OutputFileDTO implements Serializable {

    private Long id;

    private String fileName;

    private String description;

    @Lob
    private byte[] data;

    private String dataContentType;
    private Instant uploadTime;

    private ToolDTO tool;

    private NetworkDTO network;

    private SimulationDTO simulation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public void setTool(ToolDTO tool) {
        this.tool = tool;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    public SimulationDTO getSimulation() {
        return simulation;
    }

    public void setSimulation(SimulationDTO simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutputFileDTO)) {
            return false;
        }

        OutputFileDTO outputFileDTO = (OutputFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outputFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputFileDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", description='" + getDescription() + "'" +
            ", data='" + getData() + "'" +
            ", uploadTime='" + getUploadTime() + "'" +
            ", tool=" + getTool() +
            ", network=" + getNetwork() +
            ", simulation=" + getSimulation() +
            "}";
    }
}
