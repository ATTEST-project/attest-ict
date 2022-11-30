package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class T51MonitoringInputDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("modelpath1")
    private String modelPath1;

    @JsonProperty("modelpath2")
    private String modelPath2;

    @JsonProperty("filepath2")
    private String filePath2;

    private String item1;

    private String item2;

    private String item3;

    public String getModelPath1() {
        return modelPath1;
    }

    public void setModelPath1(String modelPath1) {
        this.modelPath1 = modelPath1;
    }

    public String getModelPath2() {
        return modelPath2;
    }

    public void setModelPath2(String modelPath2) {
        this.modelPath2 = modelPath2;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    @Override
    public String toString() {
        return (
            "T51MonitoringConfigDTO [modelPath1=" +
            modelPath1 +
            ", modelPath2=" +
            modelPath2 +
            ", filePath2=" +
            filePath2 +
            ", item1=" +
            item1 +
            ", item2=" +
            item2 +
            ", item3=" +
            item3 +
            "]"
        );
    }
}
