package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.StorageCost} entity.
 */
public class StorageCostDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Double costA;

    private Double costB;

    private Double costC;

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

    public Double getCostA() {
        return costA;
    }

    public void setCostA(Double costA) {
        this.costA = costA;
    }

    public Double getCostB() {
        return costB;
    }

    public void setCostB(Double costB) {
        this.costB = costB;
    }

    public Double getCostC() {
        return costC;
    }

    public void setCostC(Double costC) {
        this.costC = costC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageCostDTO)) {
            return false;
        }

        StorageCostDTO storageCostDTO = (StorageCostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storageCostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageCostDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", costA=" + getCostA() +
            ", costB=" + getCostB() +
            ", costC=" + getCostC() +
            "}";
    }
}
