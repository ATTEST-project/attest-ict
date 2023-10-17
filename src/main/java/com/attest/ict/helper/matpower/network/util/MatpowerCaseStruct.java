package com.attest.ict.helper.matpower.network.util;

import java.util.ArrayList;
import java.util.List;

public class MatpowerCaseStruct {

    private MatpowerNetworkSection type;
    private List<String> attributes;
    private List<String> matrix;

    //2023/03 Fix Attribute extension
    private List<String> origFileAttributes;

    public MatpowerCaseStruct() {
        attributes = new ArrayList<>();
        matrix = new ArrayList<>();
        origFileAttributes = new ArrayList<>();
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

    //2023/03 fix
    public List<String> getOrigFileAttributes() {
        return origFileAttributes;
    }

    public void setOrigFileAttributes(List<String> origFileAttributes) {
        this.origFileAttributes = origFileAttributes;
    }
}
