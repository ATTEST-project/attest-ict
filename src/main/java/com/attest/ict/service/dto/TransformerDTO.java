package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Transformer} entity.
 */
public class TransformerDTO implements Serializable {

    private Long id;

    private Long fbus;

    private Long tbus;

    private Double min;

    private Double max;

    private Integer totalTaps;

    private Integer tap;

    private Integer manufactureYear;

    private Integer commissioningYear;

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

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Integer getTotalTaps() {
        return totalTaps;
    }

    public void setTotalTaps(Integer totalTaps) {
        this.totalTaps = totalTaps;
    }

    public Integer getTap() {
        return tap;
    }

    public void setTap(Integer tap) {
        this.tap = tap;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Integer getCommissioningYear() {
        return commissioningYear;
    }

    public void setCommissioningYear(Integer commissioningYear) {
        this.commissioningYear = commissioningYear;
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
        if (!(o instanceof TransformerDTO)) {
            return false;
        }

        TransformerDTO transformerDTO = (TransformerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transformerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformerDTO{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", min=" + getMin() +
            ", max=" + getMax() +
            ", totalTaps=" + getTotalTaps() +
            ", tap=" + getTap() +
            ", manufactureYear=" + getManufactureYear() +
            ", commissioningYear=" + getCommissioningYear() +
            ", network=" + getNetwork() +
            "}";
    }
}
