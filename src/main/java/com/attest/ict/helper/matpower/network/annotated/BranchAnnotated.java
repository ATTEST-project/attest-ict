package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.Branch;
import com.univocity.parsers.annotations.Parsed;

public class BranchAnnotated extends Branch {

    /*public long getBranchId() {
        return super.getBranchId();
    }

    public void setBranchId(long branchId) {
        super.setBranchId(branchId);
    }*/

    @Parsed(index = 0)
    public Long getFbus() {
        return super.getFbus();
    }

    @Parsed(index = 0, field = "fbus")
    public void setFbus(Long fbus) {
        super.setFbus(fbus);
    }

    @Parsed(index = 1)
    public Long getTbus() {
        return super.getTbus();
    }

    @Parsed(index = 1, field = "tbus")
    public void setTbus(Long tbus) {
        super.setTbus(tbus);
    }

    @Parsed(index = 2)
    public Double getR() {
        return super.getR();
    }

    @Parsed(index = 2, field = "r")
    public void setR(Double r) {
        super.setR(r);
    }

    @Parsed(index = 3)
    public Double getX() {
        return super.getX();
    }

    @Parsed(index = 3, field = "x")
    public void setX(Double x) {
        super.setX(x);
    }

    @Parsed(index = 4)
    public Double getB() {
        return super.getB();
    }

    @Parsed(index = 4, field = "b")
    public void setB(Double b) {
        super.setB(b);
    }

    /*public double getR0() {
        return super.getR0();
    }

    public void setR0(double r0) {
        super.setR0(r0);
    }

    public double getX0() {
        return super.getX0();
    }

    public void setX0(double x0) {
        super.setX0(x0);
    }*/

    @Parsed(index = 5)
    public double getRateA() {
        return super.getRatea();
    }

    @Parsed(index = 5, field = "rateA")
    public void setRateA(double ratea) {
        super.setRatea(ratea);
    }

    @Parsed(index = 6)
    public double getRateB() {
        return super.getRateb();
    }

    @Parsed(index = 6, field = "rateB")
    public void setRateB(double rateb) {
        super.setRateb(rateb);
    }

    @Parsed(index = 7)
    public double getRateC() {
        return super.getRatec();
    }

    @Parsed(index = 7, field = "rateC")
    public void setRateC(double ratec) {
        super.setRatec(ratec);
    }

    @Parsed(index = 8)
    public double getRatio() {
        return super.getTapRatio();
    }

    @Parsed(index = 8, field = "ratio")
    public void setRatio(double ratio) {
        super.setTapRatio(ratio);
    }

    @Parsed(index = 9)
    public Double getAngle() {
        return super.getAngle();
    }

    @Parsed(index = 9, field = "angle")
    public void setAngle(Double angle) {
        super.setAngle(angle);
    }

    @Parsed(index = 10)
    public Integer getStatus() {
        return super.getStatus();
    }

    @Parsed(index = 10, field = "status")
    public void setStatus(Integer status) {
        super.setStatus(status);
    }

    @Parsed(index = 11)
    public Integer getAngmin() {
        return super.getAngmin();
    }

    @Parsed(index = 11, field = "angmin")
    public void setAngmin(Integer angmin) {
        super.setAngmin(angmin);
    }

    @Parsed(index = 12)
    public Integer getAngmax() {
        return super.getAngmax();
    }

    @Parsed(index = 12, field = "angmax")
    public void setAngmax(Integer angmax) {
        super.setAngmax(angmax);
    }
}
