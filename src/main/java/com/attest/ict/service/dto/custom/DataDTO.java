package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class DataDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String label;

    private List<Double> values;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Data [label=" + label + ", values=" + values + "]";
    }
}
