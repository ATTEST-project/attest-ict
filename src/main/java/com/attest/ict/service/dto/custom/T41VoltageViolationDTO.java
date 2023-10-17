package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T41VoltageViolationDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Itr")
    private String itr;

    @JsonProperty("Num")
    private String num;

    @JsonProperty("Scen")
    private Integer scen;

    @JsonProperty("T")
    private Integer t;

    @JsonProperty("Bus")
    private Integer bus;

    @JsonProperty("V")
    private Double v;

    @JsonProperty("Viol(V)")
    private Double violation;

    public T41VoltageViolationDTO(String itr, String num, Integer scen, Integer t, Integer bus, Double v, Double violation) {
        this.itr = itr;
        this.num = num;
        this.scen = scen;
        this.t = t;
        this.bus = bus;
        this.v = v;
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

    public Integer getBus() {
        return bus;
    }

    public void setBus(Integer bus) {
        this.bus = bus;
    }

    public Double getV() {
        return v;
    }

    public void setV(Double v) {
        this.v = v;
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
            "T41VoltageViolationDTO{" +
            "itr=" +
            itr +
            ", num=" +
            num +
            ", scen=" +
            scen +
            ", t=" +
            t +
            ", bus=" +
            bus +
            ", v=" +
            v +
            ", violation=" +
            violation +
            '}'
        );
    }
}
