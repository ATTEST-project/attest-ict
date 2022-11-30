package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class YAxisDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "YAxis [title=" + title + "]";
    }
}
