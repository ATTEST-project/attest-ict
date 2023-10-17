package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class T33PageDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String title;

    private String sheetName;

    private boolean filterDay;

    private boolean filterNode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public boolean getFilterDay() {
        return filterDay;
    }

    public void setFilterDay(boolean filterDay) {
        this.filterDay = filterDay;
    }

    public boolean getFilterNode() {
        return filterNode;
    }

    public void setFilterNode(boolean filterNode) {
        this.filterNode = filterNode;
    }
}
