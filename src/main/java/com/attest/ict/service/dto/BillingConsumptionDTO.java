package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BillingConsumption} entity.
 */
public class BillingConsumptionDTO implements Serializable {

    private Long id;

    private Long busNum;

    private String type;

    private Long totalEnergyConsumption;

    private String unitOfMeasure;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption(Long totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
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
        if (!(o instanceof BillingConsumptionDTO)) {
            return false;
        }

        BillingConsumptionDTO billingConsumptionDTO = (BillingConsumptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billingConsumptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingConsumptionDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", type='" + getType() + "'" +
            ", totalEnergyConsumption=" + getTotalEnergyConsumption() +
            ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
            ", network=" + getNetwork() +
            "}";
    }
}
