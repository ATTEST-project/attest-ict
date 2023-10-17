package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T33CapacityDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Node")
    private Integer node;

    @JsonProperty("Quantity")
    private String quantity;

    @JsonProperty("2020")
    private Double year2020;

    @JsonProperty("2030")
    private Double year2030;

    @JsonProperty("2040")
    private Double year2040;

    @JsonProperty("2050")
    private Double year2050;

    public T33CapacityDTO(Integer node, String quantity, Double year2020, Double year2030, Double year2040, Double year2050) {
        this.node = node;
        this.quantity = quantity;
        this.year2020 = year2020;
        this.year2030 = year2030;
        this.year2040 = year2040;
        this.year2050 = year2050;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getYear2020() {
        return year2020;
    }

    public void setYear2020(Double year2020) {
        this.year2020 = year2020;
    }

    public Double getYear2030() {
        return year2030;
    }

    public void setYear2030(Double year2030) {
        this.year2030 = year2030;
    }

    public Double getYear2040() {
        return year2040;
    }

    public void setYear2040(Double year2040) {
        this.year2040 = year2040;
    }

    public Double getYear2050() {
        return year2050;
    }

    public void setYear2050(Double year2050) {
        this.year2050 = year2050;
    }
}
