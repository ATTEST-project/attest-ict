package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.FlexCost} entity.
 */
public class FlexCostDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Integer model;

    private Long nCost;

    private Double costPr;

    private Double costQr;

    private String costPf;

    private String costQf;

    private FlexProfileDTO flexProfile;

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

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Long getnCost() {
        return nCost;
    }

    public void setnCost(Long nCost) {
        this.nCost = nCost;
    }

    public Double getCostPr() {
        return costPr;
    }

    public void setCostPr(Double costPr) {
        this.costPr = costPr;
    }

    public Double getCostQr() {
        return costQr;
    }

    public void setCostQr(Double costQr) {
        this.costQr = costQr;
    }

    public String getCostPf() {
        return costPf;
    }

    public void setCostPf(String costPf) {
        this.costPf = costPf;
    }

    public String getCostQf() {
        return costQf;
    }

    public void setCostQf(String costQf) {
        this.costQf = costQf;
    }

    public FlexProfileDTO getFlexProfile() {
        return flexProfile;
    }

    public void setFlexProfile(FlexProfileDTO flexProfile) {
        this.flexProfile = flexProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexCostDTO)) {
            return false;
        }

        FlexCostDTO flexCostDTO = (FlexCostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flexCostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexCostDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", model=" + getModel() +
            ", nCost=" + getnCost() +
            ", costPr=" + getCostPr() +
            ", costQr=" + getCostQr() +
            ", costPf='" + getCostPf() + "'" +
            ", costQf='" + getCostQf() + "'" +
            ", flexProfile=" + getFlexProfile() +
            "}";
    }
}
