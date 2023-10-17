package com.attest.ict.helper.excel.model;

import java.util.HashMap;
import java.util.Map;

public class ContingencyMap {

    private Map<Integer, Contingency> contingencyMap = new HashMap<Integer, Contingency>();

    public Map<Integer, Contingency> getContingencyMap() {
        return contingencyMap;
    }

    public void setContingencyMap(Map<Integer, Contingency> contingencyMap) {
        this.contingencyMap = contingencyMap;
    }

    @Override
    public String toString() {
        return "ContingencyMap{" + "contingencyMap=" + contingencyMap + '}';
    }
}
