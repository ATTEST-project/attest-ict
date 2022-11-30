package com.attest.ict.helper.excel.model;

public class FlexibleCost {

    String costType;
    Double value;
    String description;

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FlexibleCost [costType=" + costType + ", value=" + value + ", description=" + description + "]";
    }
}
