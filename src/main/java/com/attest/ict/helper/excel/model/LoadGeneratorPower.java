package com.attest.ict.helper.excel.model;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadGeneratorPower {

    public final Logger log = LoggerFactory.getLogger(LoadGeneratorPower.class);

    private Long busNum;

    private String powerType;

    private List<Double> values;

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "LoadDemand [ busNum=" + busNum + ", powerType=" + powerType + ", values=" + values + "]";
    }
}
