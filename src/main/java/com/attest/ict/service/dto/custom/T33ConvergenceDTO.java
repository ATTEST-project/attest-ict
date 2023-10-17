package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T33ConvergenceDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Iteration")
    private Integer iteration;

    @JsonProperty("Lower Bound")
    private Double lowerBound;

    @JsonProperty("Upper Bound")
    private Double upperBound;

    public T33ConvergenceDTO(Integer iteration, Double lowerBound, Double upperBound) {
        this.iteration = iteration;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }
}
