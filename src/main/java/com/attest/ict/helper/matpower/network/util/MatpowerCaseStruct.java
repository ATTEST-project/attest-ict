package com.attest.ict.helper.matpower.network.util;

import java.util.ArrayList;
import java.util.List;

public class MatpowerCaseStruct {

    private MatpowerNetworkSection type;
    private List<String> attributes;
    private List<String> matrix;

    public MatpowerCaseStruct() {
        attributes = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    public MatpowerCaseStruct(MatpowerNetworkSection type) {
        this();
        this.type = type;
    }

    public MatpowerNetworkSection getType() {
        return type;
    }

    public void setType(MatpowerNetworkSection type) {
        this.type = type;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<String> matrix) {
        this.matrix = matrix;
    }
}
