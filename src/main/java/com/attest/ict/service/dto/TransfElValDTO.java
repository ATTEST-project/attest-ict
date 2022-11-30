package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.TransfElVal} entity.
 */
public class TransfElValDTO implements Serializable {

    private Long id;

    private Integer hour;

    private Integer min;

    private Double tapRatio;

    private Integer status;

    private Long trasfIdOnSubst;

    private String nominalVoltage;

    private TransfProfileDTO transfProfile;

    private BranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getTapRatio() {
        return tapRatio;
    }

    public void setTapRatio(Double tapRatio) {
        this.tapRatio = tapRatio;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTrasfIdOnSubst() {
        return trasfIdOnSubst;
    }

    public void setTrasfIdOnSubst(Long trasfIdOnSubst) {
        this.trasfIdOnSubst = trasfIdOnSubst;
    }

    public String getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public TransfProfileDTO getTransfProfile() {
        return transfProfile;
    }

    public void setTransfProfile(TransfProfileDTO transfProfile) {
        this.transfProfile = transfProfile;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfElValDTO)) {
            return false;
        }

        TransfElValDTO transfElValDTO = (TransfElValDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transfElValDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfElValDTO{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", tapRatio=" + getTapRatio() +
            ", status=" + getStatus() +
            ", trasfIdOnSubst=" + getTrasfIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            ", transfProfile=" + getTransfProfile() +
            ", branch=" + getBranch() +
            "}";
    }
}
