package com.attest.ict.chart.constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToolsResultsChartConstants {

    public static final List<String> X_LABELS_HH_MM = Stream
        .of(
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00",
            "05:00",
            "06:00",
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "23:00"
        )
        .collect(Collectors.toList());

    public static final String X_TITLE_TIME = "Time [hh:mm]";
    public static final String X_TITLE_TIME_HH = "Time [hh]";
}
