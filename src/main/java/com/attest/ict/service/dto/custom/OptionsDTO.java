package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.List;

public class OptionsDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private String title;

    private YAxisDTO yAxis;

    private XAxisDTO xAxis;

    private List<String> legend;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public YAxisDTO getYAxis() {
        return yAxis;
    }

    public void setYAxis(YAxisDTO hAxis) {
        this.yAxis = hAxis;
    }

    public XAxisDTO getXAxis() {
        return xAxis;
    }

    public void setXAxis(XAxisDTO xAxis) {
        this.xAxis = xAxis;
    }

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    @Override
    public String toString() {
        return "Options [title=" + title + ", yAxis=" + yAxis + ", xAxis=" + xAxis + ", legend=" + legend + "]";
    }
}
