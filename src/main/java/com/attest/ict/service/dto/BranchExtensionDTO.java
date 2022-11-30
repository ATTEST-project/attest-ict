package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BranchExtension} entity.
 */
public class BranchExtensionDTO implements Serializable {

    private Long id;

    private Double stepSize;

    private Double actTap;

    private Double minTap;

    private Double maxTap;

    private Double normalTap;

    private Double nominalRatio;

    private Double rIp;

    private Double rN;

    private Double r0;

    private Double x0;

    private Double b0;

    private Double length;

    private Integer normStat;

    private Double g;

    private String mRid;

    private BranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStepSize() {
        return stepSize;
    }

    public void setStepSize(Double stepSize) {
        this.stepSize = stepSize;
    }

    public Double getActTap() {
        return actTap;
    }

    public void setActTap(Double actTap) {
        this.actTap = actTap;
    }

    public Double getMinTap() {
        return minTap;
    }

    public void setMinTap(Double minTap) {
        this.minTap = minTap;
    }

    public Double getMaxTap() {
        return maxTap;
    }

    public void setMaxTap(Double maxTap) {
        this.maxTap = maxTap;
    }

    public Double getNormalTap() {
        return normalTap;
    }

    public void setNormalTap(Double normalTap) {
        this.normalTap = normalTap;
    }

    public Double getNominalRatio() {
        return nominalRatio;
    }

    public void setNominalRatio(Double nominalRatio) {
        this.nominalRatio = nominalRatio;
    }

    public Double getrIp() {
        return rIp;
    }

    public void setrIp(Double rIp) {
        this.rIp = rIp;
    }

    public Double getrN() {
        return rN;
    }

    public void setrN(Double rN) {
        this.rN = rN;
    }

    public Double getr0() {
        return r0;
    }

    public void setr0(Double r0) {
        this.r0 = r0;
    }

    public Double getx0() {
        return x0;
    }

    public void setx0(Double x0) {
        this.x0 = x0;
    }

    public Double getb0() {
        return b0;
    }

    public void setb0(Double b0) {
        this.b0 = b0;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getNormStat() {
        return normStat;
    }

    public void setNormStat(Integer normStat) {
        this.normStat = normStat;
    }

    public Double getG() {
        return g;
    }

    public void setG(Double g) {
        this.g = g;
    }

    public String getmRid() {
        return mRid;
    }

    public void setmRid(String mRid) {
        this.mRid = mRid;
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
        if (!(o instanceof BranchExtensionDTO)) {
            return false;
        }

        BranchExtensionDTO branchExtensionDTO = (BranchExtensionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchExtensionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchExtensionDTO{" +
            "id=" + getId() +
            ", stepSize=" + getStepSize() +
            ", actTap=" + getActTap() +
            ", minTap=" + getMinTap() +
            ", maxTap=" + getMaxTap() +
            ", normalTap=" + getNormalTap() +
            ", nominalRatio=" + getNominalRatio() +
            ", rIp=" + getrIp() +
            ", rN=" + getrN() +
            ", r0=" + getr0() +
            ", x0=" + getx0() +
            ", b0=" + getb0() +
            ", length=" + getLength() +
            ", normStat=" + getNormStat() +
            ", g=" + getG() +
            ", mRid='" + getmRid() + "'" +
            ", branch=" + getBranch() +
            "}";
    }
}
