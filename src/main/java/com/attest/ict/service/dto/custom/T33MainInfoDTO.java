package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T33MainInfoDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Agent")
    private String agent;

    @JsonProperty("Node ID") // add New column in T33V3
    private String nodeId;

    @JsonProperty("Value") // add New column in T33V3
    private String value;

    @JsonProperty("2020 Summer")
    private Double year2020Summer;

    @JsonProperty("2020 Winter")
    private Double year2020Winter;

    @JsonProperty("2030 Summer")
    private Double year2030Summer;

    @JsonProperty("2030 Winter")
    private Double year2030Winter;

    @JsonProperty("2040 Summer")
    private Double year2040Summer;

    @JsonProperty("2040 Winter")
    private Double year2040Winter;

    @JsonProperty("2050 Summer")
    private Double year2050Summer;

    @JsonProperty("2050 Winter")
    private Double year2050Winter;

    public T33MainInfoDTO(
        String agent,
        String nodeId,
        String value,
        Double year2020Summer,
        Double year2020Winter,
        Double year2030Summer,
        Double year2030Winter,
        Double year2040Summer,
        Double year2040Winter,
        Double year2050Summer,
        Double year2050Winter
    ) {
        this.agent = agent;
        this.nodeId = nodeId;
        this.value = value;
        this.year2020Summer = year2020Summer;
        this.year2020Winter = year2020Winter;
        this.year2030Summer = year2030Summer;
        this.year2030Winter = year2030Winter;
        this.year2040Summer = year2040Summer;
        this.year2040Winter = year2040Winter;
        this.year2050Summer = year2050Summer;
        this.year2050Winter = year2050Winter;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getYear2020Summer() {
        return year2020Summer;
    }

    public void setYear2020Summer(Double year2020Summer) {
        this.year2020Summer = year2020Summer;
    }

    public Double getYear2020Winter() {
        return year2020Winter;
    }

    public void setYear2020Winter(Double year2020Winter) {
        this.year2020Winter = year2020Winter;
    }

    public Double getYear2030Summer() {
        return year2030Summer;
    }

    public void setYear2030Summer(Double year2030Summer) {
        this.year2030Summer = year2030Summer;
    }

    public Double getYear2030Winter() {
        return year2030Winter;
    }

    public void setYear2030Winter(Double year2030Winter) {
        this.year2030Winter = year2030Winter;
    }

    public Double getYear2040Summer() {
        return year2040Summer;
    }

    public void setYear2040Summer(Double year2040Summer) {
        this.year2040Summer = year2040Summer;
    }

    public Double getYear2040Winter() {
        return year2040Winter;
    }

    public void setYear2040Winter(Double year2040Winter) {
        this.year2040Winter = year2040Winter;
    }

    public Double getYear2050Summer() {
        return year2050Summer;
    }

    public void setYear2050Summer(Double year2050Summer) {
        this.year2050Summer = year2050Summer;
    }

    public Double getYear2050Winter() {
        return year2050Winter;
    }

    public void setYear2050Winter(Double year2050Winter) {
        this.year2050Winter = year2050Winter;
    }

    @Override
    public String toString() {
        return (
            "T33OfValueDTO{" +
            "agent='" +
            agent +
            '\'' +
            ", nodeId='" +
            nodeId +
            '\'' +
            ", value='" +
            value +
            '\'' +
            ", year2020Summer=" +
            year2020Summer +
            ", year2020Winter=" +
            year2020Winter +
            ", year2030Summer=" +
            year2030Summer +
            ", year2030Winter=" +
            year2030Winter +
            ", year2040Summer=" +
            year2040Summer +
            ", year2040Winter=" +
            year2040Winter +
            ", year2050Summer=" +
            year2050Summer +
            ", year2050Winter=" +
            year2050Winter +
            '}'
        );
    }
}
