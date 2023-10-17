package com.attest.ict.helper.excel.model;

public class Contingency {

    private Integer from;
    private Integer to;

    public Contingency(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Contingency{" + "from=" + from + ", to=" + to + '}';
    }
}
