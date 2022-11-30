package com.attest.ict.helper.excel.model;

import java.util.List;

public class FlexibleOption {

    private Long busNum;
    private List<Double> values;

    //for T41
    private Double cost;

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "FlexibleOption [busNum=" + busNum + ", values=" + values + ", cost=" + cost + "]";
    }
}
