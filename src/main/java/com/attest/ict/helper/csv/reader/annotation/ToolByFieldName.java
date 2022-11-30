package com.attest.ict.helper.csv.reader.annotation;

import com.univocity.parsers.annotations.Parsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolByFieldName {

    // Tool.csv
    // work_package;num;name;path;description

    public final Logger log = LoggerFactory.getLogger(ToolByFieldName.class);

    @Parsed(field = "work_package")
    private String workPackage;

    @Parsed
    private String num;

    @Parsed
    private String name;

    @Parsed
    private String path;

    @Parsed
    private String description;

    public String getWorkPackage() {
        return workPackage;
    }

    public void setWorkPackage(String workPackage) {
        this.workPackage = workPackage;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return (
            "ToolByFieldName [workPackage=" +
            workPackage +
            ", num=" +
            num +
            ", name=" +
            name +
            ", path=" +
            path +
            ", description=" +
            description +
            "]"
        );
    }
}
