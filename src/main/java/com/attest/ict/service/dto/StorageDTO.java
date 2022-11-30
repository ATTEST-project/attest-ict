package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Storage} entity.
 */
public class StorageDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Double ps;

    private Double qs;

    private Double energy;

    private Double eRating;

    private Double chargeRating;

    private Double dischargeRating;

    private Double chargeEfficiency;

    private Double thermalRating;

    private Double qmin;

    private Double qmax;

    private Double r;

    private Double x;

    private Double pLoss;

    private Double qLoss;

    private Integer status;

    private Double socInitial;

    private Double socMin;

    private Double socMax;

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

    public Double getPs() {
        return ps;
    }

    public void setPs(Double ps) {
        this.ps = ps;
    }

    public Double getQs() {
        return qs;
    }

    public void setQs(Double qs) {
        this.qs = qs;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double geteRating() {
        return eRating;
    }

    public void seteRating(Double eRating) {
        this.eRating = eRating;
    }

    public Double getChargeRating() {
        return chargeRating;
    }

    public void setChargeRating(Double chargeRating) {
        this.chargeRating = chargeRating;
    }

    public Double getDischargeRating() {
        return dischargeRating;
    }

    public void setDischargeRating(Double dischargeRating) {
        this.dischargeRating = dischargeRating;
    }

    public Double getChargeEfficiency() {
        return chargeEfficiency;
    }

    public void setChargeEfficiency(Double chargeEfficiency) {
        this.chargeEfficiency = chargeEfficiency;
    }

    public Double getThermalRating() {
        return thermalRating;
    }

    public void setThermalRating(Double thermalRating) {
        this.thermalRating = thermalRating;
    }

    public Double getQmin() {
        return qmin;
    }

    public void setQmin(Double qmin) {
        this.qmin = qmin;
    }

    public Double getQmax() {
        return qmax;
    }

    public void setQmax(Double qmax) {
        this.qmax = qmax;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getpLoss() {
        return pLoss;
    }

    public void setpLoss(Double pLoss) {
        this.pLoss = pLoss;
    }

    public Double getqLoss() {
        return qLoss;
    }

    public void setqLoss(Double qLoss) {
        this.qLoss = qLoss;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getSocInitial() {
        return socInitial;
    }

    public void setSocInitial(Double socInitial) {
        this.socInitial = socInitial;
    }

    public Double getSocMin() {
        return socMin;
    }

    public void setSocMin(Double socMin) {
        this.socMin = socMin;
    }

    public Double getSocMax() {
        return socMax;
    }

    public void setSocMax(Double socMax) {
        this.socMax = socMax;
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
        if (!(o instanceof StorageDTO)) {
            return false;
        }

        StorageDTO storageDTO = (StorageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", ps=" + getPs() +
            ", qs=" + getQs() +
            ", energy=" + getEnergy() +
            ", eRating=" + geteRating() +
            ", chargeRating=" + getChargeRating() +
            ", dischargeRating=" + getDischargeRating() +
            ", chargeEfficiency=" + getChargeEfficiency() +
            ", thermalRating=" + getThermalRating() +
            ", qmin=" + getQmin() +
            ", qmax=" + getQmax() +
            ", r=" + getR() +
            ", x=" + getX() +
            ", pLoss=" + getpLoss() +
            ", qLoss=" + getqLoss() +
            ", status=" + getStatus() +
            ", socInitial=" + getSocInitial() +
            ", socMin=" + getSocMin() +
            ", socMax=" + getSocMax() +
            ", network=" + getNetwork() +
            "}";
    }
}
