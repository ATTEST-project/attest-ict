package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BusExtension} entity.
 */
public class BusExtensionDTO implements Serializable {

    private Long id;

    private Integer hasGen;

    private Integer isLoad;

    private Double snomMva;

    private Double sx;

    private Double sy;

    private Double gx;

    private Double gy;

    private Integer status;

    private Integer incrementCost;

    private Integer decrementCost;

    private String mRid;

    private BusDTO bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHasGen() {
        return hasGen;
    }

    public void setHasGen(Integer hasGen) {
        this.hasGen = hasGen;
    }

    public Integer getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(Integer isLoad) {
        this.isLoad = isLoad;
    }

    public Double getSnomMva() {
        return snomMva;
    }

    public void setSnomMva(Double snomMva) {
        this.snomMva = snomMva;
    }

    public Double getSx() {
        return sx;
    }

    public void setSx(Double sx) {
        this.sx = sx;
    }

    public Double getSy() {
        return sy;
    }

    public void setSy(Double sy) {
        this.sy = sy;
    }

    public Double getGx() {
        return gx;
    }

    public void setGx(Double gx) {
        this.gx = gx;
    }

    public Double getGy() {
        return gy;
    }

    public void setGy(Double gy) {
        this.gy = gy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIncrementCost() {
        return incrementCost;
    }

    public void setIncrementCost(Integer incrementCost) {
        this.incrementCost = incrementCost;
    }

    public Integer getDecrementCost() {
        return decrementCost;
    }

    public void setDecrementCost(Integer decrementCost) {
        this.decrementCost = decrementCost;
    }

    public String getmRid() {
        return mRid;
    }

    public void setmRid(String mRid) {
        this.mRid = mRid;
    }

    public BusDTO getBus() {
        return bus;
    }

    public void setBus(BusDTO bus) {
        this.bus = bus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusExtensionDTO)) {
            return false;
        }

        BusExtensionDTO busExtensionDTO = (BusExtensionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, busExtensionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusExtensionDTO{" +
            "id=" + getId() +
            ", hasGen=" + getHasGen() +
            ", isLoad=" + getIsLoad() +
            ", snomMva=" + getSnomMva() +
            ", sx=" + getSx() +
            ", sy=" + getSy() +
            ", gx=" + getGx() +
            ", gy=" + getGy() +
            ", status=" + getStatus() +
            ", incrementCost=" + getIncrementCost() +
            ", decrementCost=" + getDecrementCost() +
            ", mRid='" + getmRid() + "'" +
            ", bus=" + getBus() +
            "}";
    }
}
