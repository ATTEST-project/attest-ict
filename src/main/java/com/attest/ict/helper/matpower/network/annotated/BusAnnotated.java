package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.Bus;
import com.univocity.parsers.annotations.Parsed;

public class BusAnnotated extends Bus {

    @Parsed(index = 0)
    public Long getBusNum() {
        return super.getBusNum();
    }

    @Parsed(index = 0, field = "busId")
    public void setBusNum(Long busNum) {
        super.setBusNum(busNum);
    }

    @Parsed(index = 1)
    public Integer getType() {
        return super.getType();
    }

    @Parsed(index = 1, field = "type")
    public void setType(Integer type) {
        super.setType(type);
    }

    @Parsed(index = 2)
    public Double getActivePower() {
        return super.getActivePower();
    }

    @Parsed(index = 2, field = "activePower")
    public void setActivePower(Double activePower) {
        super.setActivePower(activePower);
    }

    @Parsed(index = 3)
    public Double getReactivePower() {
        return super.getReactivePower();
    }

    @Parsed(index = 3, field = "reactivePower")
    public void setReactivePower(Double reactivePower) {
        super.setReactivePower(reactivePower);
    }

    @Parsed(index = 4)
    public Double getConductance() {
        return super.getConductance();
    }

    @Parsed(index = 4, field = "conductance")
    public void setConductance(Double conductance) {
        super.setConductance(conductance);
    }

    @Parsed(index = 5)
    public Double getSusceptance() {
        return super.getSusceptance();
    }

    @Parsed(index = 5, field = "susceptance")
    public void setSusceptance(Double susceptance) {
        super.setSusceptance(susceptance);
    }

    @Parsed(index = 6)
    public Long getArea() {
        return super.getArea();
    }

    @Parsed(index = 6, field = "area")
    public void setArea(Long area) {
        super.setArea(area);
    }

    @Parsed(index = 7)
    public Double getVm() {
        return super.getVm();
    }

    @Parsed(index = 7, field = "Vm")
    public void setVm(Double vm) {
        super.setVm(vm);
    }

    @Parsed(index = 8)
    public Double getVa() {
        return super.getVa();
    }

    @Parsed(index = 8, field = "Va")
    public void setVa(Double va) {
        super.setVa(va);
    }

    @Parsed(index = 9)
    public Double getBaseKv() {
        return super.getBaseKv();
    }

    @Parsed(index = 9, field = "baseKv")
    public void setBaseKv(Double baseKv) {
        super.setBaseKv(baseKv);
    }

    @Parsed(index = 10)
    public Long getZone() {
        return super.getZone();
    }

    @Parsed(index = 10, field = "zone")
    public void setZone(Long zone) {
        super.setZone(zone);
    }

    @Parsed(index = 11)
    public Double getVmax() {
        return super.getVmax();
    }

    @Parsed(index = 11, field = "Vmax")
    public void setVmax(Double vmax) {
        super.setVmax(vmax);
    }

    @Parsed(index = 12)
    public Double getVmin() {
        return super.getVmin();
    }

    @Parsed(index = 12, field = "Vmin")
    public void setVmin(Double vmin) {
        super.setVmin(vmin);
    }
}
