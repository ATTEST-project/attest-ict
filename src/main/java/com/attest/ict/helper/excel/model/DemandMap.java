package com.attest.ict.helper.excel.model;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemandMap {

    public final Logger log = LoggerFactory.getLogger(DemandMap.class);

    Map<String, List<LoadGeneratorPower>> sheetDemandMap;

    public Map<String, List<LoadGeneratorPower>> getSheetDemandMap() {
        return sheetDemandMap;
    }

    public void setSheetDemandMap(Map<String, List<LoadGeneratorPower>> sheetDemandMap) {
        this.sheetDemandMap = sheetDemandMap;
    }

    @Override
    public String toString() {
        return "DemandMap [sheetDemandMap=" + sheetDemandMap + "]";
    }
}
