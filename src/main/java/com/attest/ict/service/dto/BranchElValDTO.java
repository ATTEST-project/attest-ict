package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BranchElVal} entity.
 */
public class BranchElValDTO implements Serializable {

    private Long id;

    private Integer hour;

    private Integer min;

    private Double p;

    private Double q;

    private Integer status;

    private Long branchIdOnSubst;

    private String nominalVoltage;

    private BranchDTO branch;

    private BranchProfileDTO branchProfile;

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

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBranchIdOnSubst() {
        return branchIdOnSubst;
    }

    public void setBranchIdOnSubst(Long branchIdOnSubst) {
        this.branchIdOnSubst = branchIdOnSubst;
    }

    public String getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }

    public BranchProfileDTO getBranchProfile() {
        return branchProfile;
    }

    public void setBranchProfile(BranchProfileDTO branchProfile) {
        this.branchProfile = branchProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchElValDTO)) {
            return false;
        }

        BranchElValDTO branchElValDTO = (BranchElValDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchElValDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchElValDTO{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", status=" + getStatus() +
            ", branchIdOnSubst=" + getBranchIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            ", branch=" + getBranch() +
            ", branchProfile=" + getBranchProfile() +
            "}";
    }
}
