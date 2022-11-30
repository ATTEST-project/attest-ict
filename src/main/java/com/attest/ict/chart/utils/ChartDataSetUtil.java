package com.attest.ict.chart.utils;

import com.attest.ict.helper.utils.ProfileConstants;
import com.attest.ict.service.dto.custom.ChartDataDTO;
import com.attest.ict.service.dto.custom.DataDTO;
import com.attest.ict.service.dto.custom.OptionsDTO;
import com.attest.ict.service.dto.custom.RowDTO;
import com.attest.ict.service.dto.custom.XAxisDTO;
import com.attest.ict.service.dto.custom.YAxisDTO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartDataSetUtil {

    private final Logger log = LoggerFactory.getLogger(ChartDataSetUtil.class);

    public static ChartDataDTO chartDataLoad(String networkName, List<Tuple> assetValues, boolean isGroupBySeason, String chartTitle) {
        List<RowDTO> sr = assetValues
            .stream()
            .map(t ->
                new RowDTO(
                    t.get(0, String.class),
                    t.get(1, BigInteger.class),
                    t.get(2, Integer.class),
                    t.get(3, Integer.class),
                    t.get(4, Double.class),
                    t.get(5, Double.class)
                )
            )
            .collect(Collectors.toList());

        //group by season
        Map<String, List<RowDTO>> dataMap = sr.stream().collect(Collectors.groupingBy(RowDTO::getGroupedByField));

        //prepare chartData
        ChartDataDTO chartData = new ChartDataDTO();

        YAxisDTO yAxis = new YAxisDTO();
        String yTitle = dataMap.isEmpty() ? "" : "Active Power [MW]";
        yAxis.setTitle(yTitle);

        XAxisDTO xAxis = new XAxisDTO();
        String xTitle = dataMap.isEmpty() ? "" : "Time [hh:mm]";
        xAxis.setTitle(xTitle);

        List<String> xLabels = new ArrayList<String>();
        List<String> legend = new ArrayList<String>();
        List<Object> datasets = new ArrayList<Object>();

        for (String key : dataMap.keySet()) {
            List<RowDTO> listForKey = dataMap.get(key);
            DataDTO data = new DataDTO();
            if (isGroupBySeason) {
                String seasonName = (ProfileConstants.MAP_SEASON.containsKey(key)) ? ProfileConstants.MAP_SEASON.get(key) : key;
                data.setLabel(seasonName);
                legend.add(seasonName);
            } else {
                String typicalDay = (ProfileConstants.MAP_TIPICAL_DAY.containsKey(key)) ? ProfileConstants.MAP_TIPICAL_DAY.get(key) : key;
                data.setLabel(typicalDay);
                legend.add(typicalDay);
            }

            List<Double> values = new ArrayList<Double>();

            for (RowDTO row : listForKey) {
                Integer hh = row.getHh();
                Integer mm = row.getMm();
                Double p = row.getP();

                //popola xlabel
                String label = hh + ":" + mm;
                String hour = (hh < 10) ? "0" + hh : "" + hh;
                String minutes = (mm < 10) ? "0" + mm : "" + mm;
                label = hour + ":" + minutes;

                if (!xLabels.contains(label)) xLabels.add(label);
                values.add(p);
            }

            xAxis.setLabels(xLabels);
            data.setValues(values);
            datasets.add(data);
        }

        if (dataMap.isEmpty()) {
            xAxis.setLabels(xLabels);
        }

        OptionsDTO opt = new OptionsDTO();
        opt.setLegend(legend);
        opt.setTitle(chartTitle);
        opt.setXAxis(xAxis);
        opt.setYAxis(yAxis);

        chartData.setOptions(opt);
        chartData.setDatasets(datasets);

        return chartData;
    }
}
