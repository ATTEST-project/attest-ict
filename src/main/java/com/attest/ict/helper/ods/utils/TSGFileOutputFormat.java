package com.attest.ict.helper.ods.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSGFileOutputFormat extends OdsFileFormat {

    public static List<String> scenarioGenSheets = Stream
        .of("wind_scenarios", "pv_scenarios", "probabilities")
        .collect(Collectors.toList());
}
