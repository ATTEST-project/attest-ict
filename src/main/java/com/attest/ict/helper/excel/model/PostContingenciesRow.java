package com.attest.ict.helper.excel.model;

public class PostContingenciesRow {

    private Long contingency;
    private Long scenario;
    private Long time;
    private Long assetUnits;
    private Double value;

    public Long getContingency() {
        return contingency;
    }

    public void setContingency(Long contingency) {
        this.contingency = contingency;
    }

    public Long getScenario() {
        return scenario;
    }

    public void setScenario(Long scenario) {
        this.scenario = scenario;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getAssetUnits() {
        return assetUnits;
    }

    public void setAssetUnits(Long assetUnits) {
        this.assetUnits = assetUnits;
    }

    public Double getValue() {
        return value;
    }

    public void setValues(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (
            "PostContingenciesRow{" +
            "contingency=" +
            contingency +
            ", scenario=" +
            scenario +
            ", time=" +
            time +
            ", assetUnits=" +
            assetUnits +
            ", value=" +
            value +
            '}'
        );
    }
}
