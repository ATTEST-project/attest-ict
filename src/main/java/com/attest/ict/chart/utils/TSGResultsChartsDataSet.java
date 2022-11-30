package com.attest.ict.chart.utils;

import com.attest.ict.chart.constants.ToolsResultsChartConstants;
import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.attest.ict.service.dto.custom.ChartDataAggregators;
import com.attest.ict.service.dto.custom.DataDTO;
import com.attest.ict.service.dto.custom.OptionsDTO;
import com.attest.ict.service.dto.custom.TSGResultsDTO;
import com.attest.ict.service.dto.custom.XAxisDTO;
import com.attest.ict.service.dto.custom.YAxisDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSGResultsChartsDataSet {

    public final Logger log = LoggerFactory.getLogger(TSGResultsChartsDataSet.class);

    // -- Wind for scenarios
    public ChartDataAggregators windScenario(Map<String, List<ScenarioValues>> mapSheet) {
        String section = "WIND";
        String title = "Wind Scenarios";
        String group = title;
        String sheetName = TSGFileOutputFormat.scenarioGenSheets.get(0);
        List<ScenarioValues> scenariosValues = mapSheet.get(sheetName);
        if (scenariosValues.isEmpty()) return null;

        return chartWindPVResults(scenariosValues, section, title, group, sheetName);
    }

    // -- PV for scenarios
    public ChartDataAggregators pvScenario(Map<String, List<ScenarioValues>> mapSheet) {
        String section = "PV";
        String title = "PV Scenarios";
        String group = title;
        String sheetName = TSGFileOutputFormat.scenarioGenSheets.get(1);
        List<ScenarioValues> scenariosValues = mapSheet.get(sheetName);
        if (scenariosValues.isEmpty()) return null;
        return chartWindPVResults(scenariosValues, section, title, group, sheetName);
    }

    // -- Probabilities
    public ChartDataAggregators probabilitiesScenario(Map<String, List<ScenarioValues>> mapSheet) {
        String section = "PROB";
        String title = "Probabilities";
        String group = title;
        String sheetName = TSGFileOutputFormat.scenarioGenSheets.get(2);

        List<ScenarioValues> scenariosValues = mapSheet.get(sheetName);
        if (scenariosValues.isEmpty()) return null;

        return chartProbResults(scenariosValues, section, title, group, sheetName);
    }

    private ChartDataAggregators chartWindPVResults(
        List<ScenarioValues> scenarioValues,
        String section,
        String title,
        String group,
        String sheetName
    ) {
        ChartDataAggregators results = new ChartDataAggregators();
        results.setSection(section);
        results.setGroup(group);

        YAxisDTO yAxis = new YAxisDTO();
        yAxis.setTitle("Power (MW)");

        XAxisDTO xAxis = new XAxisDTO();
        xAxis.setTitle(ToolsResultsChartConstants.X_TITLE_TIME);
        xAxis.setLabels(ToolsResultsChartConstants.X_LABELS_HH_MM);

        List<String> legend = new ArrayList<String>();
        List<Object> datasets = new ArrayList<Object>();

        for (ScenarioValues scnVals : scenarioValues) {
            String scNum = scnVals.getScNum();
            legend.add(scNum);
            // -- prepare time series
            DataDTO data = new DataDTO();
            data.setLabel(scNum);
            data.setValues(scnVals.getValues());
            datasets.add(data);
        }

        OptionsDTO opt = new OptionsDTO();
        opt.setTitle(title);
        opt.setXAxis(xAxis);
        opt.setYAxis(yAxis);
        opt.setLegend(legend);

        results.setGroup(group);
        results.setSection(section);
        results.setOptions(opt);
        results.setDatasets(datasets);
        return results;
    }

    private ChartDataAggregators chartProbResults(
        List<ScenarioValues> scenarioValues,
        String section,
        String title,
        String group,
        String sheetName
    ) {
        ChartDataAggregators results = new ChartDataAggregators();

        List<String> xlabels = new ArrayList<String>();
        List<String> legend = new ArrayList<String>();

        YAxisDTO yAxis = new YAxisDTO();
        yAxis.setTitle("Probabilities");

        XAxisDTO xAxis = new XAxisDTO();
        xAxis.setTitle("Scenarios");
        legend.add("Scenarios");

        List<Object> datasets = new ArrayList<Object>();
        List<Double> values = new ArrayList<Double>();
        DataDTO data = new DataDTO();
        data.setLabel("Scenarios");
        for (ScenarioValues scnVals : scenarioValues) {
            // -- prepare time series
            String scNum = scnVals.getScNum();
            xlabels.add(scNum);
            values.add(scnVals.getValues().get(0));
        }

        xAxis.setLabels(xlabels);
        data.setValues(values);
        datasets.add(data);

        OptionsDTO opt = new OptionsDTO();
        opt.setTitle(title);
        opt.setXAxis(xAxis);
        opt.setYAxis(yAxis);
        opt.setLegend(legend);

        results.setGroup(group);
        results.setSection(section);
        results.setOptions(opt);
        results.setDatasets(datasets);
        return results;
    }

    public TSGResultsDTO tsgPrepareChartsDataSet(Map<String, List<ScenarioValues>> mapSheet) {
        TSGResultsDTO results = new TSGResultsDTO();

        List<ChartDataAggregators> charts = new ArrayList<ChartDataAggregators>();

        if (mapSheet.isEmpty()) {
            return results;
        }

        if (mapSheet.containsKey(TSGFileOutputFormat.scenarioGenSheets.get(0))) {
            charts.add(this.windScenario(mapSheet));
        }

        if (mapSheet.containsKey(TSGFileOutputFormat.scenarioGenSheets.get(1))) {
            charts.add(this.pvScenario(mapSheet));
        }

        if (mapSheet.containsKey(TSGFileOutputFormat.scenarioGenSheets.get(2))) {
            charts.add(this.probabilitiesScenario(mapSheet));
        }

        results.setCharts(charts);

        return results;
    }
}
