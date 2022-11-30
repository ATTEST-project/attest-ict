package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Bus} entity.
 */
public class BusDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Integer type;

    private Double activePower;

    private Double reactivePower;

    private Double conductance;

    private Double susceptance;

    private Long area;

    private Double vm;

    private Double va;

    private Double baseKv;

    private Long zone;

    private Double vmax;

    private Double vmin;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getActivePower() {
        return activePower;
    }

    public void setActivePower(Double activePower) {
        this.activePower = activePower;
    }

    public Double getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(Double reactivePower) {
        this.reactivePower = reactivePower;
    }

    public Double getConductance() {
        return conductance;
    }

    public void setConductance(Double conductance) {
        this.conductance = conductance;
    }

    public Double getSusceptance() {
        return susceptance;
    }

    public void setSusceptance(Double susceptance) {
        this.susceptance = susceptance;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Double getVm() {
        return vm;
    }

    public void setVm(Double vm) {
        this.vm = vm;
    }

    public Double getVa() {
        return va;
    }

    public void setVa(Double va) {
        this.va = va;
    }

    public Double getBaseKv() {
        return baseKv;
    }

    public void setBaseKv(Double baseKv) {
        this.baseKv = baseKv;
    }

    public Long getZone() {
        return zone;
    }

    public void setZone(Long zone) {
        this.zone = zone;
    }

    public Double getVmax() {
        return vmax;
    }

    public void setVmax(Double vmax) {
        this.vmax = vmax;
    }

    public Double getVmin() {
        return vmin;
    }

    public void setVmin(Double vmin) {
        this.vmin = vmin;
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
        if (!(o instanceof BusDTO)) {
            return false;
        }

        BusDTO busDTO = (BusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, busDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", type=" + getType() +
            ", activePower=" + getActivePower() +
            ", reactivePower=" + getReactivePower() +
            ", conductance=" + getConductance() +
            ", susceptance=" + getSusceptance() +
            ", area=" + getArea() +
            ", vm=" + getVm() +
            ", va=" + getVa() +
            ", baseKv=" + getBaseKv() +
            ", zone=" + getZone() +
            ", vmax=" + getVmax() +
            ", vmin=" + getVmin() +
            ", network=" + getNetwork() +
            "}";
    }
}
