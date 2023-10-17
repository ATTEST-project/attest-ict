package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class T33MainInfoDTOV1 implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Info")
    private String info;

    @JsonProperty("Description")
    private String description;

    public T33MainInfoDTOV1(String info, String description) {
        this.info = info;
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
