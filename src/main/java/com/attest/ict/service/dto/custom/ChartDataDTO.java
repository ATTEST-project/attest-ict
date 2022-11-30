package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class ChartDataDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private OptionsDTO options;

    private List<Object> datasets;

    public OptionsDTO getOptions() {
        return options;
    }

    public void setOptions(OptionsDTO options) {
        this.options = options;
    }

    public List<Object> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<Object> datasets) {
        this.datasets = datasets;
    }

    @Override
    public String toString() {
        return "ChartData [options=" + options + ", datasets=" + datasets + "]";
    }
}
