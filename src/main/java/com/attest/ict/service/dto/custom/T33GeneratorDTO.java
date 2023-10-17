package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class T33GeneratorDTO extends T33ConnectionNodeQuantityDTO {

    @JsonProperty("Generator ID")
    private Integer generatorId;

    @JsonProperty("Type")
    private String type;

    public T33GeneratorDTO(
        String operator,
        String connectionNodeId,
        Integer nodeId,
        Integer generatorId,
        String type,
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
        super(
            operator,
            connectionNodeId,
            nodeId,
            year,
            day,
            quantity,
            marketScenario,
            operationScenario,
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
        this.type = type;
        this.generatorId = generatorId;
    }

    public Integer getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(Integer generatorIdId) {
        this.generatorId = generatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
