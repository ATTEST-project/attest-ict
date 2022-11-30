package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.attest.ict.domain.Simulation} entity.
 */
public class SimulationDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    private String description;

    @Lob
    private byte[] configFile;

    private String configFileContentType;
    private NetworkDTO network;

    private Set<InputFileDTO> inputFiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getConfigFile() {
        return configFile;
    }

    public void setConfigFile(byte[] configFile) {
        this.configFile = configFile;
    }

    public String getConfigFileContentType() {
        return configFileContentType;
    }

    public void setConfigFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    public Set<InputFileDTO> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(Set<InputFileDTO> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimulationDTO)) {
            return false;
        }

        SimulationDTO simulationDTO = (SimulationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, simulationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimulationDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", description='" + getDescription() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", network=" + getNetwork() +
            ", inputFiles=" + getInputFiles() +
            "}";
    }
}
