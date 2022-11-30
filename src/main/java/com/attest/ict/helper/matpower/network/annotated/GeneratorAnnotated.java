package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.Generator;
import com.univocity.parsers.annotations.Parsed;

public class GeneratorAnnotated extends Generator {

    /*@Parsed(index = 0)
    public long getId() {
        return super.getId();
    }

    @Parsed(index = 0, field = "id")
    public void setId(long id) {
        super.setId(id);
    }*/

    @Parsed(index = 0)
    public long getBus() {
        return super.getBusNum();
    }

    @Parsed(index = 0, field = "bus")
    public void setBus(long bus) {
        super.setBusNum(bus);
    }

    @Parsed(index = 1)
    public Double getPg() {
        return super.getPg();
    }

    @Parsed(index = 1, field = "pg")
    public void setPg(Double pg) {
        super.setPg(pg);
    }

    @Parsed(index = 2)
    public Double getQg() {
        return super.getQg();
    }

    @Parsed(index = 2, field = "qg")
    public void setQg(Double qg) {
        super.setQg(qg);
    }

    @Parsed(index = 3)
    public Double getQmax() {
        return super.getQmax();
    }

    @Parsed(index = 3, field = "qmax")
    public void setQmax(Double qmax) {
        super.setQmax(qmax);
    }

    @Parsed(index = 4)
    public Double getQmin() {
        return super.getQmin();
    }

    @Parsed(index = 4, field = "qmin")
    public void setQmin(Double qmin) {
        super.setQmin(qmin);
    }

    @Parsed(index = 5)
    public Double getVg() {
        return super.getVg();
    }

    @Parsed(index = 5, field = "vg")
    public void setVg(Double vg) {
        super.setVg(vg);
    }

    @Parsed(index = 6)
    public Double getmBase() {
        return super.getmBase();
    }

    @Parsed(index = 6, field = "m_base")
    public void setmBase(Double mBase) {
        super.setmBase(mBase);
    }

    @Parsed(index = 7)
    public Integer getStatus() {
        return super.getStatus();
    }

    @Parsed(index = 7, field = "status")
    public void setStatus(Integer status) {
        super.setStatus(status);
    }

    @Parsed(index = 8)
    public Double getPmax() {
        return super.getPmax();
    }

    @Parsed(index = 8, field = "p_max")
    public void setPmax(Double pMax) {
        super.setPmax(pMax);
    }

    @Parsed(index = 9)
    public Double getPmin() {
        return super.getPmin();
    }

    @Parsed(index = 9, field = "p_min")
    public void setPmin(Double pMin) {
        super.setPmin(pMin);
    }

    @Parsed(index = 10)
    public Double getPc1() {
        return super.getPc1();
    }

    @Parsed(index = 10, field = "pc1")
    public void setPc1(Double pc1) {
        super.setPc1(pc1);
    }

    @Parsed(index = 11)
    public Double getPc2() {
        return super.getPc2();
    }

    @Parsed(index = 11, field = "pc2")
    public void setPc2(Double pc2) {
        super.setPc2(pc2);
    }

    @Parsed(index = 12)
    public Double getQc1min() {
        return super.getQc1min();
    }

    @Parsed(index = 12, field = "qc1min")
    public void setQc1min(Double qc1min) {
        super.setQc1min(qc1min);
    }

    @Parsed(index = 13)
    public Double getQc1max() {
        return super.getQc1max();
    }

    @Parsed(index = 13, field = "qc1max")
    public void setQc1max(Double qc1max) {
        super.setQc1max(qc1max);
    }

    @Parsed(index = 14)
    public Double getQc2min() {
        return super.getQc2min();
    }

    @Parsed(index = 14, field = "qc2min")
    public void setQc2min(Double qc2min) {
        super.setQc2min(qc2min);
    }

    @Parsed(index = 15)
    public Double getQc2max() {
        return super.getQc2max();
    }

    @Parsed(index = 15, field = "qc2max")
    public void setQc2max(Double qc2max) {
        super.setQc2max(qc2max);
    }

    @Parsed(index = 16)
    public Double getRampAgc() {
        return super.getRampAgc();
    }

    @Parsed(index = 16, field = "ramp_agc")
    public void setRampAgc(Double ramp_agc) {
        super.setRampAgc(ramp_agc);
    }

    @Parsed(index = 17)
    public Double getRamp_10() {
        return super.getRamp10();
    }

    @Parsed(index = 17, field = "ramp_10")
    public void setRamp_10(Double ramp_10) {
        super.setRamp10(ramp_10);
    }

    @Parsed(index = 18)
    public Double getRamp_30() {
        return super.getRamp30();
    }

    @Parsed(index = 18, field = "ramp_30")
    public void setRamp_30(Double ramp_30) {
        super.setRamp30(ramp_30);
    }

    @Parsed(index = 19)
    public Double getRampQ() {
        return super.getRampQ();
    }

    @Parsed(index = 19, field = "ramp_q")
    public void setRampQ(Double ramp_q) {
        super.setRampQ(ramp_q);
    }

    @Parsed(index = 20)
    public Long getApf() {
        return super.getApf();
    }

    @Parsed(index = 20, field = "apf")
    public void setApf(Long apf) {
        super.setApf(apf);
    }
}
