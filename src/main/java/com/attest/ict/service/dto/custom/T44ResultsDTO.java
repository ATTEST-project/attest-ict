package com.attest.ict.service.dto.custom;

import com.attest.ict.helper.excel.model.FlexibleCost;
import java.io.Serializable;
import java.util.List;

public class T44ResultsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<FlexibleCost> flexCosts;

    private List<ChartDataAggregators> charts;

    public List<ChartDataAggregators> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDataAggregators> charts) {
        this.charts = charts;
    }

    public List<FlexibleCost> getFlexCosts() {
        return flexCosts;
    }

    public void setFlexCosts(List<FlexibleCost> flexCosts) {
        this.flexCosts = flexCosts;
    }

    @Override
    public String toString() {
        return "T44Results [flexCosts=" + flexCosts + ", charts=" + charts + "]";
    }
}
