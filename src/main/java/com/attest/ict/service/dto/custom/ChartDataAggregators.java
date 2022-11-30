package com.attest.ict.service.dto.custom;

public class ChartDataAggregators extends ChartDataDTO {

    private static final long serialVersionUID = 1234567L;

    private String group;

    private String section;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return (
            "ChartDataAggregators [group=" +
            group +
            ", section=" +
            section +
            ", getOptions()=" +
            getOptions() +
            ", getDatasets()=" +
            getDatasets() +
            "]"
        );
    }
}
