package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class T33QuantityFromNodeToNodeDTO extends T33QuantityForNodeAndOperatorDTO {

    @JsonProperty("Connection Node ID")
    private String connectionNodeId;

    @JsonProperty("From Node ID")
    private Integer fromNodeId;

    @JsonProperty("To Node ID")
    private Integer toNodeId;

    public T33QuantityFromNodeToNodeDTO(
        String operator,
        String connectionNodeId,
        Integer fromNodeId,
        Integer toNodeId,
        Integer year,
        String day,
        String quantity,
        String marketScenario,
        String operationScenario,
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
        String t23,
        String t24
    ) {
        super(
            null,
            operator,
            year,
            day,
            quantity,
            marketScenario,
            operationScenario,
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
            t23,
            t24
        );
        this.connectionNodeId = connectionNodeId;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
    }

    public String getConnectionNodeId() {
        return connectionNodeId;
    }

    public void setConnectionNodeId(String connectionNodeId) {
        this.connectionNodeId = connectionNodeId;
    }

    public Integer getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(Integer fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public Integer getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(Integer toNodeId) {
        this.toNodeId = toNodeId;
    }
}
