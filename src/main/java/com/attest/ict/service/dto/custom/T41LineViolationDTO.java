package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T41LineViolationDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Itr")
    private String itr;

    @JsonProperty("Num")
    private String num;

    @JsonProperty("Scen")
    private Integer scen;

    @JsonProperty("T")
    private Integer t;

    @JsonProperty("F_Bus")
    private Integer fBus;

    @JsonProperty("T_Bus")
    private Integer tBus;

    @JsonProperty("S")
    private Double s;

    @JsonProperty("S_limit")
    private Double sLimit;

    @JsonProperty("Viol(S)")
    private Double violation;

    public T41LineViolationDTO(
        String itr,
        String num,
        Integer scen,
        Integer t,
        Integer fBus,
        Integer tBus,
        Double s,
        Double sLimit,
        Double violation
    ) {
        this.itr = itr;
        this.num = num;
        this.scen = scen;
        this.t = t;
        this.fBus = fBus;
        this.tBus = tBus;
        this.s = s;
        this.sLimit = sLimit;
        this.violation = violation;
    }

    public String getItr() {
        return itr;
    }

    public void setItr(String itr) {
        this.itr = itr;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Integer getScen() {
        return scen;
    }

    public void setScen(Integer scen) {
        this.scen = scen;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Integer getfBus() {
        return fBus;
    }

    public void setfBus(Integer fBus) {
        this.fBus = fBus;
    }

    public Integer gettBus() {
        return tBus;
    }

    public void settBus(Integer tBus) {
        this.tBus = tBus;
    }

    public Double getS() {
        return s;
    }

    public void setS(Double s) {
        this.s = s;
    }

    public Double getsLimit() {
        return sLimit;
    }

    public void setsLimit(Double sLimit) {
        this.sLimit = sLimit;
    }

    public Double getViolation() {
        return violation;
    }

    public void setViolation(Double violation) {
        this.violation = violation;
    }

    @Override
    public String toString() {
        return (
            "T41LineViolationDTO{" +
            "itr=" +
            itr +
            ", num=" +
            num +
            ", scen=" +
            scen +
            ", t=" +
            t +
            ", fBus=" +
            fBus +
            ", tBus=" +
            tBus +
            ", s=" +
            s +
            ", sLimit=" +
            sLimit +
            ", violation=" +
            violation +
            '}'
        );
    }
}
