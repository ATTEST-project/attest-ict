package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.math.BigInteger;

public class RowDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String groupedByField;
    BigInteger busNum;
    Integer hh;
    Integer mm;
    Double p;
    Double q;

    public RowDTO(String groupedByField, BigInteger busNum, Integer hh, Integer mm, Double p, Double q) {
        this.groupedByField = groupedByField;
        this.busNum = busNum;
        this.hh = hh;
        this.mm = mm;
        this.p = p;
        this.q = q;
    }

    public String getGroupedByField() {
        return groupedByField;
    }

    public void setGroupedByField(String groupedByField) {
        this.groupedByField = groupedByField;
    }

    public BigInteger getBusNum() {
        return busNum;
    }

    public void setBusNum(BigInteger busNum) {
        this.busNum = busNum;
    }

    public Integer getHh() {
        return hh;
    }

    public void setHh(Integer hh) {
        this.hh = hh;
    }

    public Integer getMm() {
        return mm;
    }

    public void setMm(Integer mm) {
        this.mm = mm;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "Row [groupedByField=" + groupedByField + ", busNum=" + busNum + ", hh=" + hh + ", mm=" + mm + ", p=" + p + ", q=" + q + "]";
    }
}
