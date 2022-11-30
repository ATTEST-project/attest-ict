package com.attest.ict.chart.utils;

import com.attest.ict.chart.constants.ToolsResultsChartConstants;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.service.dto.custom.ChartDataAggregators;
import com.attest.ict.service.dto.custom.DataDTO;
import com.attest.ict.service.dto.custom.OptionsDTO;
import com.attest.ict.service.dto.custom.T41ResultsDTO;
import com.attest.ict.service.dto.custom.XAxisDTO;
import com.attest.ict.service.dto.custom.YAxisDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class T41ResultsChartsDataSet {

    //-- Curtailed active power
    public ChartDataAggregators resultsAPC(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "APC";
        String group = "PV and Wind Renewable energy Resources (RES)";
        String title = "Curtailed Active Power (MW) and provisioning  of reactive power from RES";
        String sheetName = section.concat("_MW");

        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, sheetName); else return null;
    }

    //-- Energy storages
    public ChartDataAggregators resultsEESCH(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_CH";
        String group = "Electrical Energy Storages (EESs)";
        String title = "Active power charging (MW) of each EES unit";
        String sheetName = section.concat("_MW");
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, sheetName); else return null;
    }

    public ChartDataAggregators resultsEESDCH(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_DCH";
        String group = "Electrical Energy Storages (EESs)";
        String title = "Active power discharging (MW) of each EES unit";
        String sheetName = section.concat("_MW");
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, sheetName); else return null;
    }

    //-- Flexible loads
    public ChartDataAggregators resultsFLOD(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_OD";
        String group = "Flexible Loads (FLs)";
        String title = "Active power overdemand (MW) of each FL";
        String sheetName = section.concat("_MW");
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, sheetName); else return null;
    }

    public ChartDataAggregators resultsFLUD(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_UD";
        String group = "Flexible Loads (FLs)";
        String title = "Active power under-demand (MW) of each FL";
        String sheetName = section.concat("_MW");
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null) return chartResults(flexibleOptions, section, group, title, sheetName); else return null;
    }

    //-- Cost
    public Double resultsCOST(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String sheetName = "COST";
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetName);
        if (!flexibleOptions.isEmpty() && flexibleOptions.size() == 1) {
            return flexibleOptions.get(0).getCost();
        } else return null;
    }

    public ChartDataAggregators chartResults(
        List<FlexibleOption> flexibleOptions,
        String section,
        String group,
        String title,
        String sheetName
    ) {
        YAxisDTO yAxis = new YAxisDTO();
        yAxis.setTitle(section);

        XAxisDTO xAxis = new XAxisDTO();
        xAxis.setTitle(ToolsResultsChartConstants.X_TITLE_TIME);
        xAxis.setLabels(ToolsResultsChartConstants.X_LABELS_HH_MM);

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

    public T41ResultsDTO t41PrepareChartsDataSet(Map<String, List<FlexibleOption>> mapDataForSheet) {
        T41ResultsDTO results = new T41ResultsDTO();

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
}
