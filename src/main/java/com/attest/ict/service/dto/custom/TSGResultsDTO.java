package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class TSGResultsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<ChartDataAggregators> charts;

    public List<ChartDataAggregators> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDataAggregators> charts) {
        this.charts = charts;
    }

    @Override
    public String toString() {
        return "TSGResults [charts=" + charts + "]";
    }
}
