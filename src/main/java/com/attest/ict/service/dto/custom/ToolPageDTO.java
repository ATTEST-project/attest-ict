package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class ToolPageDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String title;
    private String sheetName;
    private String section;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "ToolPageDTO{" + "title='" + title + '\'' + ", sheetName='" + sheetName + '\'' + ", section='" + section + '}';
    }
}
