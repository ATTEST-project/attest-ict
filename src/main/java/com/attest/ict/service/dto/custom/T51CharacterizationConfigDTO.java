package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class T51CharacterizationConfigDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String config;

    String path;

    String selectedVariables;

    List<String> variables;

    @JsonProperty("component1_field")
    String component1Field;

    List<String> components;

    String method;
    List<String> results;

    //Optional configuration params
    String path2;

    String variables2;

    List<String> components2;

    @JsonProperty("component2_field")
    String component2Field;

    // simulation name seleted by the user
    String assestsType;

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

    public String getSelectedVariables() {
        return selectedVariables;
    }

    public void setSelectedVariables(String selectedVariables) {
        this.selectedVariables = selectedVariables;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public String getComponent1Field() {
        return component1Field;
    }

    public void setComponent1Field(String component1Field) {
        this.component1Field = component1Field;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }

    public String getVariables2() {
        return variables2;
    }

    public void setVariables2(String variables2) {
        this.variables2 = variables2;
    }

    public List<String> getComponents2() {
        return components2;
    }

    public void setComponents2(List<String> components2) {
        this.components2 = components2;
    }

    public String getComponent2Field() {
        return component2Field;
    }

    public void setComponent2Field(String component2Field) {
        this.component2Field = component2Field;
    }

    public String getAssestsType() {
        return assestsType;
    }

    public void setAssestsType(String assestsType) {
        this.assestsType = assestsType;
    }

    @Override
    public String toString() {
        return (
            "T51CharacterizationConfigDTO [config=" +
            config +
            ", path=" +
            path +
            ", selectedVariables=" +
            selectedVariables +
            ", variables=" +
            variables +
            ", component1Field=" +
            component1Field +
            ", components=" +
            components +
            ", method=" +
            method +
            ", results=" +
            results +
            ", path2=" +
            path2 +
            ", variables2=" +
            variables2 +
            ", components2=" +
            components2 +
            ", component2Field=" +
            component2Field +
            ", assestsType=" +
            assestsType +
            "]"
        );
    }
}
