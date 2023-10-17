package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class ColumnDefDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private String field;

    private boolean filter;

    private String headerName;

    public ColumnDefDTO(String field, boolean filter, String headerName) {
        this.field = field;
        this.filter = filter;
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean getFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }
}
