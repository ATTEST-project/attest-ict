package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T33QuantityForNodeAndOperatorDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    TimeSeriesHourDTO timeSeriesHour;

    @JsonProperty("Node ID")
    Integer nodeId;

    @JsonProperty("Operator")
    private String operator;

    @JsonProperty("Quantity")
    private String quantity;

    @JsonProperty("Market Scenario")
    String marketScenario;

    @JsonProperty("Operation Scenario")
    String operationScenario;

    public T33QuantityForNodeAndOperatorDTO(
        Integer nodeId,
        String operator,
        Integer year,
        String day,
        String quantity,
        String marketScenario,
        String operationScenario,
        String t0,
        String t1,
        String t2,
        String t3,
        String t4,
        String t5,
        String t6,
        String t7,
        String t8,
        String t9,
        String t10,
        String t11,
        String t12,
        String t13,
        String t14,
        String t15,
        String t16,
        String t17,
        String t18,
        String t19,
        String t20,
        String t21,
        String t22,
        String t23
    ) {
        this.nodeId = nodeId;
        this.operator = operator;
        this.quantity = quantity;
        this.marketScenario = marketScenario;
        this.operationScenario = operationScenario;
        this.timeSeriesHour =
            new TimeSeriesHourDTO(
                year,
                day,
                t0,
                t1,
                t2,
                t3,
                t4,
                t5,
                t6,
                t7,
                t8,
                t9,
                t10,
                t11,
                t12,
                t13,
                t14,
                t15,
                t16,
                t17,
                t18,
                t19,
                t20,
                t21,
                t22,
                t23
            );
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getMarketScenario() {
        return marketScenario;
    }

    public void setMarketScenario(String marketScenario) {
        this.marketScenario = marketScenario;
    }

    public TimeSeriesHourDTO getTimeSeriesHour() {
        return timeSeriesHour;
    }

    public void setTimeSeriesHour(TimeSeriesHourDTO timeSeriesHour) {
        this.timeSeriesHour = timeSeriesHour;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOperationScenario() {
        return operationScenario;
    }

    public void setOperationScenario(String operationScenario) {
        this.operationScenario = operationScenario;
    }
}
