package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BillingDer} entity.
 */
public class BillingDerDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Long maxPowerKw;

    private String type;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Long getMaxPowerKw() {
        return maxPowerKw;
    }

    public void setMaxPowerKw(Long maxPowerKw) {
        this.maxPowerKw = maxPowerKw;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(o instanceof BillingDerDTO)) {
            return false;
        }

        BillingDerDTO billingDerDTO = (BillingDerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billingDerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingDerDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", maxPowerKw=" + getMaxPowerKw() +
            ", type='" + getType() + "'" +
            ", network=" + getNetwork() +
            "}";
    }
}
