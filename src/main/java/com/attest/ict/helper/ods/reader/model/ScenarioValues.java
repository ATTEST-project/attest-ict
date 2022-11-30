package com.attest.ict.helper.ods.reader.model;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioValues {

    public final Logger log = LoggerFactory.getLogger(ScenarioValues.class);

    private String scNum; // scenario Num

    private List<Double> values;

    public String getScNum() {
        return scNum;
    }

    public void setScNum(String scNum) {
        this.scNum = scNum;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ScenarioValues [ scNum=" + scNum + ", values=" + values + "]";
    }
}
