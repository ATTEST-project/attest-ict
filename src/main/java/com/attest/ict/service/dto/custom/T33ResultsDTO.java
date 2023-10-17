package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class T33ResultsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private TableDataDTO table;

    public T33ResultsDTO(TableDataDTO table) {
        this.table = table;
    }

    public TableDataDTO getTable() {
        return table;
    }

    public void setTable(TableDataDTO table) {
        this.table = table;
    }
}
