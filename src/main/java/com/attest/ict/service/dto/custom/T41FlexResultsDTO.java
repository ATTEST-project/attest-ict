package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T41FlexResultsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private Double cost;

    private List<ChartDataAggregators> charts;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<ChartDataAggregators> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDataAggregators> charts) {
        this.charts = charts;
    }

    @Override
    public String toString() {
        return "T41FlexResultsDTO{" + "cost=" + cost + ", charts=" + charts + '}';
    }
}
