package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BaseMVA} entity.
 */
public class BaseMVADTO implements Serializable {

    private Long id;

    private Double baseMva;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBaseMva() {
        return baseMva;
    }

    public void setBaseMva(Double baseMva) {
        this.baseMva = baseMva;
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
        if (!(o instanceof BaseMVADTO)) {
            return false;
        }

        BaseMVADTO baseMVADTO = (BaseMVADTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, baseMVADTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaseMVADTO{" +
            "id=" + getId() +
            ", baseMva=" + getBaseMva() +
            ", network=" + getNetwork() +
            "}";
    }
}
