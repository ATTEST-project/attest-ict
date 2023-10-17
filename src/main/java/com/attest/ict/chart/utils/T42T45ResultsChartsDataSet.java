package com.attest.ict.chart.utils;

import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.reader.T42T45FileOutputFormat;
import com.attest.ict.service.dto.custom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T42T45ResultsChartsDataSet {

    public final Logger LOGGER = LoggerFactory.getLogger(T42T45ResultsChartsDataSet.class);

    private String timePeriod;

    public T42T45ResultsChartsDataSet(String timePeriod) {
        LOGGER.debug("T42T45ResultsChartsDataSet() - TimePeriod: {}", timePeriod);
        this.timePeriod = timePeriod;
    }

    public ChartDataAggregators resultsActivePower(Map<String, List<FlexibleOption>> mapSheetAndColumnExcel) {
        String section = "APC";
        String group = "Active Power";
        String title = "Flexible Generator Active Power Curtailment";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("APC_MW");
        LOGGER.debug(" --- Prepare Active Power data set from sheet;column {}", sheetNameAndColumn);
        List<FlexibleOption> flexibleOptions = mapSheetAndColumnExcel.get(sheetNameAndColumn);
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_gen_";

        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- Flexible EV loads
    public ChartDataAggregators resultsFlexibleEvUp(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_OD"; // section is used  be the front end don't change...
        String group = "Flexible EV Up and Dn Active Power";
        String title = "Flexible EV Up Active Power";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("FL_OD_MW"); // Fl up
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_load_";

        LOGGER.debug("resultsFlexibleEvUp() - Prepare Flex_Load_up data set from sheet {}", sheetNameAndColumn);

        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsFlexibleEvDn(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "FL_UD";
        String group = "Flexible EV Up and Dn Active Power";
        String title = "Flexible EV Dn Active Power";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("FL_UD_MW"); // Fl dn
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_load_";
        LOGGER.debug("resultsFlexibleEvDn() - Prepare Prepare Flex_Load_dn set from sheet {}", sheetNameAndColumn);

        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    // -- STR
    public ChartDataAggregators resultsStoragePUp(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_P_CH";
        String group = "Storage Active Reactive Power Up and Dn";
        String title = "Storage Active Power Up";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("EES_P_CH_MW");
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_str_";
        LOGGER.debug("resultsStoragePUp() - Prepare  STR data set from sheet {}", sheetNameAndColumn);
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsStoragePDn(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_P_DCH";
        String group = "Storage Active Power Up and Dn";
        String title = "Storage Active Power Dn";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("EES_P_DCH_MW");
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_str_";
        LOGGER.debug("resultsStoragePDn() - Prepare  Flex_storage dn data set from sheet {}", sheetNameAndColumn);
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsStorageQUp(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_Q_CH";
        String group = "Storage Reactive Power Up and Dn";
        String title = "Storage Reactive Power Up";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("EES_Q_CH_MW");
        String yTitle = "Active Power,[MW]";
        String legendPrefix = "Flex_str_";
        LOGGER.debug("resultsStorageQUp() - Prepare Flex_storage Q Up data set from sheet {}", sheetNameAndColumn);
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators resultsStorageQDn(Map<String, List<FlexibleOption>> mapSheetExcel) {
        String section = "EES_Q_DCH";
        String group = "Storage Reactive Power Up Dn";
        String title = "Storage Reactive Power Dn";
        String sheetNameAndColumn = T42T45FileOutputFormat.MAP_FLEX_SHEETS.get("EES_Q_DCH_MW");
        String yTitle = "Reactive Power,[MW]";
        String legendPrefix = "Flex_str_";
        LOGGER.debug("resultsStorageQDn() - Prepare  Flex_storage Q Dn data set from sheet {}", sheetNameAndColumn);
        List<FlexibleOption> flexibleOptions = mapSheetExcel.get(sheetNameAndColumn);
        if (flexibleOptions != null && !flexibleOptions.isEmpty()) {
            return chartResults(flexibleOptions, section, group, title, sheetNameAndColumn, yTitle, legendPrefix);
        } else {
            return null;
        }
    }

    public ChartDataAggregators chartResults(
        List<FlexibleOption> flexibleOptions,
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
        xAxis.setTitle("Current Time Period");
        xAxis.setLabels(Stream.of(this.timePeriod).collect(Collectors.toList()));

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
            String bus = legendPrefix.concat(String.valueOf(busNum));
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

    public T42T45FlexResultsDTO prepareFlexChartsDataSet(Map<String, List<FlexibleOption>> mapDataForSheet) {
        List<ChartDataAggregators> resultsCharts = new ArrayList<ChartDataAggregators>();
        T42T45FlexResultsDTO results = new T42T45FlexResultsDTO();

        ChartDataAggregators resultsActivePower = this.resultsActivePower(mapDataForSheet);
        if (resultsActivePower != null) resultsCharts.add(resultsActivePower);

        ChartDataAggregators resultFlEvUp = this.resultsFlexibleEvUp(mapDataForSheet);
        if (resultFlEvUp != null) resultsCharts.add(resultFlEvUp);

        ChartDataAggregators resultFlEvDown = this.resultsFlexibleEvDn(mapDataForSheet);
        if (resultFlEvDown != null) resultsCharts.add(resultFlEvDown);

        ChartDataAggregators resultsStoragePUp = this.resultsStoragePUp(mapDataForSheet);
        if (resultsStoragePUp != null) resultsCharts.add(resultsStoragePUp);

        ChartDataAggregators resultsStoragePDn = this.resultsStoragePDn(mapDataForSheet);
        if (resultsStoragePDn != null) resultsCharts.add(resultsStoragePDn);

        ChartDataAggregators resultsStorageQUp = this.resultsStorageQUp(mapDataForSheet);
        if (resultsStorageQUp != null) resultsCharts.add(resultsStorageQUp);

        ChartDataAggregators resultsStorageQDn = this.resultsStorageQDn(mapDataForSheet);
        if (resultsStorageQDn != null) resultsCharts.add(resultsStorageQDn);

        results.setCharts(resultsCharts);
        return results;
    }
}
