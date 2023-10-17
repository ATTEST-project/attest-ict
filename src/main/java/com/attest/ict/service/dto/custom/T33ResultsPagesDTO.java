package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T33ResultsPagesDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private List<T33PageDTO> pages;

    public List<T33PageDTO> getPages() {
        return pages;
    }

    public void setPages(List<T33PageDTO> pages) {
        this.pages = pages;
    }
}
