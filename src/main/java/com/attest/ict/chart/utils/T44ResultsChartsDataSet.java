package com.attest.ict.chart.utils;

import com.attest.ict.chart.constants.ToolsResultsChartConstants;
import com.attest.ict.helper.excel.model.FlexibleCost;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.model.FlexibleOptionWithContin;
import com.attest.ict.helper.excel.reader.T44FileOutputFormat;
import com.attest.ict.service.dto.custom.ChartDataAggregators;
import com.attest.ict.service.dto.custom.DataDTO;
import com.attest.ict.service.dto.custom.OptionsDTO;
import com.attest.ict.service.dto.custom.T44ResultsDTO;
import com.attest.ict.service.dto.custom.XAxisDTO;
import com.attest.ict.service.dto.custom.YAxisDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T44ResultsChartsDataSet {

    public final Logger LOGGER = LoggerFactory.getLogger(T44ResultsChartsDataSet.class);

    public ChartDataAggregators resultsActivePower(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "ARP";
        String group = "Active and Reactive Power";
        String title = "Active Power";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(0); // active_power
        LOGGER.debug(" --- Prepare Active Power data set from sheet {}", sheetName);

        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Gen_";

        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsReactivePower(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "ARP";
        String group = "Active And Reactive Power";
        String title = "Reactive Power";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(1); // reactive_power
        String yTitle = "Reactive Power,[MVar]";
        String legendPrefix = "Gen_";

        LOGGER.debug(" --- Prepare Reactive Power data set from sheet {}", sheetName);

        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- Flexible loads
    public ChartDataAggregators resultsFLInc(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "FL";
        String group = "Flexible Loads (FLs)";
        String title = "Flexible Loads Inc";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(2); // Fl inc
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_";

        LOGGER.debug(" --- Prepare Flexible Loads Inc data set from sheet {}", sheetName);

        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsFLDec(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "FL";
        String group = "Flexible Loads (FLs)";
        String title = "Flexible Loads Dec";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(3); // FL dec
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_";
        LOGGER.debug(" --- Prepare Flexible Loads data set from sheet {}", sheetName);
        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- STR
    public ChartDataAggregators resultsSTR(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "STR";
        String group = "STR";
        String title = "Storage";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(4);
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "STR_";
        LOGGER.debug(" --- Prepare Storage data set from sheet {}", sheetName);
        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- Load curtailment
    public ChartDataAggregators resultsLoadCurt(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "CURT";
        String group = "Load and RES";
        String title = "Load Curtailment";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(5);
        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Load_";
        LOGGER.debug(" --- Prepare Load Curtailment data set from sheet {}", sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- Res curtailment
    public ChartDataAggregators resultsResCurt(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String section = "CURT";
        String group = "Load and RES";
        String title = "RES Curtailment";
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(6);
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "RES_";
        LOGGER.debug(" --- Prepare RES Curtailment data set from sheet {}", sheetName);
        List<FlexibleOptionWithContin> flexibleOptions = mapSheetExcel.get(sheetName);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetName, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- Cost
    public List<FlexibleCost> resultsCOST(Map<String, List<FlexibleOptionWithContin>> mapSheetExcel) {
        String sheetName = T44FileOutputFormat.SHEETS_NAME.get(7);
        List<FlexibleOptionWithContin> flexibleOptions = new ArrayList<FlexibleOptionWithContin>();
        flexibleOptions = mapSheetExcel.get(sheetName);
        LOGGER.debug("--- Prepare COST data set from sheet {}", sheetName);
        if (flexibleOptions == null || flexibleOptions.isEmpty()) {
            // in case of post contingencies cost info are stored in Sheet1 (file// _cost.xlsx)
            sheetName = T44FileOutputFormat.SHEETS_NAME_COST.get(0).toLowerCase(); // Sheet1
            flexibleOptions = mapSheetExcel.get(sheetName);
        } else if (!flexibleOptions.isEmpty() && flexibleOptions.size() == 1) {
            return flexibleOptions.get(0).getFlexCosts();
        }
        return null;
    }

    public ChartDataAggregators chartResults(
        List<FlexibleOptionWithContin> flexibleOptions,
        String section,
        String group,
        String title,
        String sheetName,
        String yTitle,
        String legendPrefix
    ) {
        YAxisDTO yAxis = new YAxisDTO();
        yAxis.setTitle(yTitle);

        XAxisDTO xAxis = new XAxisDTO();
        xAxis.setTitle(ToolsResultsChartConstants.X_TITLE_TIME_HH);

        xAxis.setLabels(getXAxisLabel(flexibleOptions.get(0)));

        OptionsDTO opt = new OptionsDTO();
        opt.setTitle(title);
        opt.setYAxis(yAxis);
        opt.setXAxis(xAxis);

        List<String> legend = new ArrayList<String>();
        List<Object> datasets = new ArrayList<Object>();

        ChartDataAggregators results = new ChartDataAggregators();
        results.setSection(section);
        results.setGroup(group);

        for (FlexibleOption flexOpt : flexibleOptions) {
            Long busNum = flexOpt.getBusNum();
            String busNumStr = (busNum != null) ? String.valueOf(busNum) : "NA";
            String bus = legendPrefix.concat(busNumStr);
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

    private List<String> getXAxisLabelHourMin(FlexibleOption flexOpt) {
        List<String> xlabels = new ArrayList<String>();
        List<Double> values = flexOpt.getValues();
        String label;
        if (values != null) {
            int size = values.size();
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
        String label;
        if (values != null) {
            int size = values.size();
            for (int i = 0; i < size; i++) {
                label = "" + i;
                xlabels.add(label);
            }
        }
        return xlabels;
    }

    public T44ResultsDTO prepareChartsDataSet(Map<String, List<FlexibleOptionWithContin>> mapDataForSheet) {
        List<ChartDataAggregators> resultsCharts = new ArrayList<ChartDataAggregators>();
        T44ResultsDTO results = new T44ResultsDTO();

        List<FlexibleCost> costs = this.resultsCOST(mapDataForSheet);
        if (costs != null) results.setFlexCosts(costs);

        ChartDataAggregators resultsActivePower = this.resultsActivePower(mapDataForSheet);
        if (resultsActivePower != null) resultsCharts.add(resultsActivePower);

        ChartDataAggregators resultsReactivePower = this.resultsReactivePower(mapDataForSheet);
        if (resultsReactivePower != null) resultsCharts.add(resultsReactivePower);

        ChartDataAggregators resultFLInc = this.resultsFLInc(mapDataForSheet);
        if (resultFLInc != null) resultsCharts.add(resultFLInc);

        ChartDataAggregators resultFLDec = this.resultsFLDec(mapDataForSheet);
        if (resultFLDec != null) resultsCharts.add(resultFLDec);

        ChartDataAggregators resultsSTR = this.resultsSTR(mapDataForSheet);
        if (resultsSTR != null) resultsCharts.add(resultsSTR);

        ChartDataAggregators resultsLoadCurt = this.resultsLoadCurt(mapDataForSheet);
        if (resultsLoadCurt != null) resultsCharts.add(resultsLoadCurt);

        ChartDataAggregators resultsRESCurt = this.resultsResCurt(mapDataForSheet);
        if (resultsRESCurt != null) resultsCharts.add(resultsRESCurt);
        results.setCharts(resultsCharts);

        return results;
    }
}
