package com.attest.ict.chart.utils;

import com.attest.ict.chart.constants.ToolsResultsChartConstants;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.service.dto.custom.ChartDataAggregators;
import com.attest.ict.service.dto.custom.DataDTO;
import com.attest.ict.service.dto.custom.OptionsDTO;
import com.attest.ict.service.dto.custom.T41FlexResultsDTO;
import com.attest.ict.service.dto.custom.XAxisDTO;
import com.attest.ict.service.dto.custom.YAxisDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class T41ResultsChartsDataSet {

    //-- Curtailed active power
    public ChartDataAggregators resultsAPC(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "APC";
        String group = "PV and Wind Renewable energy Resources (RES)";
        String title = "Curtailed Active Power (MW) and provisioning  of reactive power from RES";
        String sheetName = section.concat("_MW");
        String yAxisTitle = "Active Power, [MW]";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, yAxisTitle); else return null;
    }

    //-- Energy storages
    public ChartDataAggregators resultsEESCH(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_CH";
        String group = "Electrical Energy Storages (EESs)";
        String title = "Active power charging (MW) of each EES unit";
        String sheetName = section.concat("_MW");
        String yAxisTitle = "Active Power, [MW]";

        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, yAxisTitle); else return null;
    }

    public ChartDataAggregators resultsEESDCH(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_DCH";
        String group = "Electrical Energy Storages (EESs)";
        String title = "Active power discharging (MW) of each EES unit";
        String sheetName = section.concat("_MW");
        String yAxisTitle = "Active Power, [MW]";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, yAxisTitle); else return null;
    }

    //-- Flexible loads
    public ChartDataAggregators resultsFLOD(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_OD";
        String group = "Flexible Loads (FLs)";
        String title = "Active Power Over-Demand (MW) of each FL";
        String sheetName = section.concat("_MW");
        String yAxisTitle = "Active Power, [MW]";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, yAxisTitle); else return null;
    }

    public ChartDataAggregators resultsFLUD(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_UD";
        String group = "Flexible Loads (FLs)";
        String title = "Active Power Under-Demand (MW) of each FL";
        String sheetName = section.concat("_MW");
        String yAxisTitle = "Active Power, [MW]";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, yAxisTitle); else return null;
    }

    //-- Cost
    public Double resultsCOST(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String sheetName = "COST";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions.size() == 1) {
            return flexibleOptions.get(0).getCost();
        } else return null;
    }

    public ChartDataAggregators chartResults(
        List<FlexibleOption> flexibleOptions,
        String section,
        String group,
        String title,
        String yAxisTitle
    ) {
        YAxisDTO yAxis = new YAxisDTO();
        yAxis.setTitle(yAxisTitle);

        XAxisDTO xAxis = new XAxisDTO();
        xAxis.setTitle(ToolsResultsChartConstants.X_TITLE_TIME_HH);
        //xAxis.setLabels(ToolsResultsChartConstants.X_LABELS_HH_MM);
        xAxis.setLabels(getXAxisLabel(flexibleOptions.get(0)));

        OptionsDTO opt = new OptionsDTO();
        opt.setTitle(title);
        opt.setXAxis(xAxis);
        opt.setYAxis(yAxis);

        List<String> legend = new ArrayList<String>();
        List<Object> datasets = new ArrayList<Object>();

        ChartDataAggregators results = new ChartDataAggregators();
        results.setSection(section);
        results.setGroup(group);

        for (FlexibleOption flexOpt : flexibleOptions) {
            Long busNum = flexOpt.getBusNum();
            String bus = "BUS_".concat(String.valueOf(busNum));
            legend.add(bus);
            // -- prepare time series
            DataDTO data = new DataDTO();
            data.setLabel(bus);
            data.setValues(flexOpt.getValues());
            datasets.add(data);
        }

        // -- (bus1, bus2, ...,busN)
        opt.setLegend(legend);

        results.setOptions(opt);
        results.setDatasets(datasets);
        return results;
    }

    public T41FlexResultsDTO t41PrepareChartsDataSet(Map<String, List<FlexibleOption>> mapDataForSheet) {
        T41FlexResultsDTO results = new T41FlexResultsDTO();

        List<ChartDataAggregators> resultsCharts = new ArrayList<ChartDataAggregators>();

        Double cost = this.resultsCOST(mapDataForSheet);
        if (cost != null) results.setCost(cost);

        ChartDataAggregators resultApc = this.resultsAPC(mapDataForSheet);
        if (resultApc != null) resultsCharts.add(resultApc);

        ChartDataAggregators resultEESCH = this.resultsEESCH(mapDataForSheet);
        if (resultEESCH != null) resultsCharts.add(resultEESCH);

        ChartDataAggregators resultEESDCH = this.resultsEESDCH(mapDataForSheet);
        if (resultEESDCH != null) resultsCharts.add(resultEESDCH);

        ChartDataAggregators resultFLOD = this.resultsFLOD(mapDataForSheet);
        if (resultFLOD != null) resultsCharts.add(resultFLOD);

        ChartDataAggregators resultFLUD = this.resultsFLUD(mapDataForSheet);
        if (resultFLUD != null) resultsCharts.add(resultFLUD);

        results.setCharts(resultsCharts);

        return results;
    }

    private List<String> getXAxisLabelHourMin(FlexibleOption flexOpt) {
        List<String> xlabels = new ArrayList<String>();
        List<Double> values = flexOpt.getValues();
        String label;
        int size;
        if (values != null) {
            size = values.size();
            for (int i = 0; i < size; i++) {
                if (i < 10) {
                    label = "0" + i + ":00";
                } else {
                    label = i + ":00";
                }
                xlabels.add(label);
            }
        }
        return xlabels;
    }

    private List<String> getXAxisLabel(FlexibleOption flexOpt) {
        List<String> xlabels = new ArrayList<String>();
        List<Double> values = flexOpt.getValues();
        String label = "";
        int size = 0;
        if (values != null) {
            size = values.size();
            for (int i = 1; i <= size; i++) {
                label = "" + i;
                xlabels.add(label);
            }
        }
        return xlabels;
    }
}
