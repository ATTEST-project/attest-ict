package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T5ConfigDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String config;

    private String path;

    private String index;

    private List<String> variables;

    private List<String> weights;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public List<String> getWeights() {
        return weights;
    }

    public void setWeights(List<String> weights) {
        this.weights = weights;
    }

    @Override
    public String toString() {
        return (
            "T5ConfigDTO [config=" +
            config +
            ", path=" +
            path +
            ", index=" +
            index +
            ", variables=" +
            variables +
            ", weights=" +
            weights +
            "]"
        );
    }
}
