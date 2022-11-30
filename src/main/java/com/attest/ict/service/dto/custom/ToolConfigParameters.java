package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ToolConfigParameters implements Serializable {

    private static final long serialVersionUID = 1234567L;

    LinkedHashMap<String, Object> parameters;

    public LinkedHashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ToolConfigParameters [parameters=" + parameters + "]";
    }
}
