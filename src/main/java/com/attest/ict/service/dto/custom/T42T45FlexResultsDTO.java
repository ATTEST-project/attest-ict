package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T42T45FlexResultsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    // Request for DSO flexibility service activation in Rial Time at TSO-DSO interface
    private List<T42T45ActivationDTO> activations;
    private List<ChartDataAggregators> charts;

    public List<ChartDataAggregators> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDataAggregators> charts) {
        this.charts = charts;
    }

    public List<T42T45ActivationDTO> getActivations() {
        return activations;
    }

    public void setActivations(List<T42T45ActivationDTO> activations) {
        this.activations = activations;
    }

    @Override
    public String toString() {
        return "T42T45FlexResultsDTO {" + "activations=" + activations + ", charts=" + charts + '}';
    }
}
