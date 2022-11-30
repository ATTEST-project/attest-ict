package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Generator} entity.
 */
public class GeneratorDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Double pg;

    private Double qg;

    private Double qmax;

    private Double qmin;

    private Double vg;

    private Double mBase;

    private Integer status;

    private Double pmax;

    private Double pmin;

    private Double pc1;

    private Double pc2;

    private Double qc1min;

    private Double qc1max;

    private Double qc2min;

    private Double qc2max;

    private Double rampAgc;

    private Double ramp10;

    private Double ramp30;

    private Double rampQ;

    private Long apf;

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

    public Double getPg() {
        return pg;
    }

    public void setPg(Double pg) {
        this.pg = pg;
    }

    public Double getQg() {
        return qg;
    }

    public void setQg(Double qg) {
        this.qg = qg;
    }

    public Double getQmax() {
        return qmax;
    }

    public void setQmax(Double qmax) {
        this.qmax = qmax;
    }

    public Double getQmin() {
        return qmin;
    }

    public void setQmin(Double qmin) {
        this.qmin = qmin;
    }

    public Double getVg() {
        return vg;
    }

    public void setVg(Double vg) {
        this.vg = vg;
    }

    public Double getmBase() {
        return mBase;
    }

    public void setmBase(Double mBase) {
        this.mBase = mBase;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPmax() {
        return pmax;
    }

    public void setPmax(Double pmax) {
        this.pmax = pmax;
    }

    public Double getPmin() {
        return pmin;
    }

    public void setPmin(Double pmin) {
        this.pmin = pmin;
    }

    public Double getPc1() {
        return pc1;
    }

    public void setPc1(Double pc1) {
        this.pc1 = pc1;
    }

    public Double getPc2() {
        return pc2;
    }

    public void setPc2(Double pc2) {
        this.pc2 = pc2;
    }

    public Double getQc1min() {
        return qc1min;
    }

    public void setQc1min(Double qc1min) {
        this.qc1min = qc1min;
    }

    public Double getQc1max() {
        return qc1max;
    }

    public void setQc1max(Double qc1max) {
        this.qc1max = qc1max;
    }

    public Double getQc2min() {
        return qc2min;
    }

    public void setQc2min(Double qc2min) {
        this.qc2min = qc2min;
    }

    public Double getQc2max() {
        return qc2max;
    }

    public void setQc2max(Double qc2max) {
        this.qc2max = qc2max;
    }

    public Double getRampAgc() {
        return rampAgc;
    }

    public void setRampAgc(Double rampAgc) {
        this.rampAgc = rampAgc;
    }

    public Double getRamp10() {
        return ramp10;
    }

    public void setRamp10(Double ramp10) {
        this.ramp10 = ramp10;
    }

    public Double getRamp30() {
        return ramp30;
    }

    public void setRamp30(Double ramp30) {
        this.ramp30 = ramp30;
    }

    public Double getRampQ() {
        return rampQ;
    }

    public void setRampQ(Double rampQ) {
        this.rampQ = rampQ;
    }

    public Long getApf() {
        return apf;
    }

    public void setApf(Long apf) {
        this.apf = apf;
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
        if (!(o instanceof GeneratorDTO)) {
            return false;
        }

        GeneratorDTO generatorDTO = (GeneratorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, generatorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneratorDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", pg=" + getPg() +
            ", qg=" + getQg() +
            ", qmax=" + getQmax() +
            ", qmin=" + getQmin() +
            ", vg=" + getVg() +
            ", mBase=" + getmBase() +
            ", status=" + getStatus() +
            ", pmax=" + getPmax() +
            ", pmin=" + getPmin() +
            ", pc1=" + getPc1() +
            ", pc2=" + getPc2() +
            ", qc1min=" + getQc1min() +
            ", qc1max=" + getQc1max() +
            ", qc2min=" + getQc2min() +
            ", qc2max=" + getQc2max() +
            ", rampAgc=" + getRampAgc() +
            ", ramp10=" + getRamp10() +
            ", ramp30=" + getRamp30() +
            ", rampQ=" + getRampQ() +
            ", apf=" + getApf() +
            ", network=" + getNetwork() +
            "}";
    }
}
