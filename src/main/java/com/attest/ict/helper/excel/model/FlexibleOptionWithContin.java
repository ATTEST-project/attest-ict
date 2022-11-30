package com.attest.ict.helper.excel.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleOptionWithContin extends FlexibleOption {

    //Number of contingencies
    //Number of scenarios
    //Number of Flexible
    //Number of Storages
    private Map<String, Integer> flexibleCountMap = new HashMap<String, Integer>();

    private List<FlexibleCost> flexCosts;

    public Map<String, Integer> getFlexibleCountMap() {
        return flexibleCountMap;
    }

    public void setFlexibleCountMap(Map<String, Integer> flexibleCountMap) {
        this.flexibleCountMap = flexibleCountMap;
    }

    public List<FlexibleCost> getFlexCosts() {
        return flexCosts;
    }

    public void setFlexCosts(List<FlexibleCost> flexCosts) {
        this.flexCosts = flexCosts;
    }

    @Override
    public String toString() {
        return (
            "FlexibleOptionWithContin [flexibleCountMap=" +
            flexibleCountMap +
            ", flexCosts=" +
            flexCosts +
            ", getBusNum()=" +
            getBusNum() +
            ", getCost()=" +
            getCost() +
            ", getValues()=" +
            getValues() +
            "]"
        );
    }
}
