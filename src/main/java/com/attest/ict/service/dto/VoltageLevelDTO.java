package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.VoltageLevel} entity.
 */
public class VoltageLevelDTO implements Serializable {

    private Long id;

    private Double v1;

    private Double v2;

    private Double v3;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getv1() {
        return v1;
    }

    public void setv1(Double v1) {
        this.v1 = v1;
    }

    public Double getv2() {
        return v2;
    }

    public void setv2(Double v2) {
        this.v2 = v2;
    }

    public Double getv3() {
        return v3;
    }

    public void setv3(Double v3) {
        this.v3 = v3;
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
        if (!(o instanceof VoltageLevelDTO)) {
            return false;
        }

        VoltageLevelDTO voltageLevelDTO = (VoltageLevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, voltageLevelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoltageLevelDTO{" +
            "id=" + getId() +
            ", v1=" + getv1() +
            ", v2=" + getv2() +
            ", v3=" + getv3() +
            ", network=" + getNetwork() +
            "}";
    }
}
