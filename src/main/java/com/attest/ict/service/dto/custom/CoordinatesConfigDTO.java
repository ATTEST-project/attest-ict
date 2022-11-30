package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class CoordinatesConfigDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String coordinatesFilePath; //": "C:/Users/Davide Longo/Documents/software_factory_acotel/wp5/Assest-WP-5.2-Tools-master/dataset/Cordinate.csv",

    private String coordinatesIdentifier; //": "additional_id",

    private String latColumn; //": "x",

    private String longColumn; //": "y",

    private String regionCB; //": "Europe"

    public String getCoordinatesFilePath() {
        return coordinatesFilePath;
    }

    public void setCoordinatesFilePath(String coordinatesFilePath) {
        this.coordinatesFilePath = coordinatesFilePath;
    }

    public String getCoordinatesIdentifier() {
        return coordinatesIdentifier;
    }

    public void setCoordinatesIdentifier(String coordinatesIdentifier) {
        this.coordinatesIdentifier = coordinatesIdentifier;
    }

    public String getLatColumn() {
        return latColumn;
    }

    public void setLatColumn(String latColumn) {
        this.latColumn = latColumn;
    }

    public String getLongColumn() {
        return longColumn;
    }

    public void setLongColumn(String longColumn) {
        this.longColumn = longColumn;
    }

    public String getRegionCB() {
        return regionCB;
    }

    public void setRegionCB(String regionCB) {
        this.regionCB = regionCB;
    }

    @Override
    public String toString() {
        return (
            "CoordinatesConfigDTO [coordinatesFilePath=" +
            coordinatesFilePath +
            ", coordinatesIdentifier=" +
            coordinatesIdentifier +
            ", latColumn=" +
            latColumn +
            ", longColumn=" +
            longColumn +
            ", regionCB=" +
            regionCB +
            "]"
        );
    }
}
