package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class TableDataDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String title;

    String titleLongDescription;

    String yAxisTitle;

    List<?> rowData;

    List<ColumnDefDTO> columnDefs;

    List<Integer> connectionNodesId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<?> getRowData() {
        return rowData;
    }

    public void setRowData(List<?> rowData) {
        this.rowData = rowData;
    }

    public List<ColumnDefDTO> getColumnDefs() {
        return this.columnDefs;
    }

    public void setColumnDefs(List<ColumnDefDTO> columnDef) {
        this.columnDefs = columnDef;
    }

    public List<Integer> getConnectionNodesId() {
        return connectionNodesId;
    }

    public void setConnectionNodesId(List<Integer> connectionNodesId) {
        this.connectionNodesId = connectionNodesId;
    }

    public String getTitleLongDescription() {
        return titleLongDescription;
    }

    public void setTitleLongDescription(String titleLongDescription) {
        this.titleLongDescription = titleLongDescription;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }
}
