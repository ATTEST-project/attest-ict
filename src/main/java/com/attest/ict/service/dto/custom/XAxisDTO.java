package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class XAxisDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String title;

    List<String> labels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "XAxis [title=" + title + ", labels=" + labels + "]";
    }
}
