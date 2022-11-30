package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.LineCable} entity.
 */
public class LineCableDTO implements Serializable {

    private Long id;

    private Long fbus;

    private Long tbus;

    private Double lengthKm;

    private String typeOfInstallation;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFbus() {
        return fbus;
    }

    public void setFbus(Long fbus) {
        this.fbus = fbus;
    }

    public Long getTbus() {
        return tbus;
    }

    public void setTbus(Long tbus) {
        this.tbus = tbus;
    }

    public Double getLengthKm() {
        return lengthKm;
    }

    public void setLengthKm(Double lengthKm) {
        this.lengthKm = lengthKm;
    }

    public String getTypeOfInstallation() {
        return typeOfInstallation;
    }

    public void setTypeOfInstallation(String typeOfInstallation) {
        this.typeOfInstallation = typeOfInstallation;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineCableDTO)) {
            return false;
        }

        LineCableDTO lineCableDTO = (LineCableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lineCableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineCableDTO{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", lengthKm=" + getLengthKm() +
            ", typeOfInstallation='" + getTypeOfInstallation() + "'" +
            ", network=" + getNetwork() +
            "}";
    }
}
