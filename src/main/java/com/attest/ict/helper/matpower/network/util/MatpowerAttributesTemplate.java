package com.attest.ict.helper.matpower.network.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatpowerAttributesTemplate {

    public static final String ATTRIBUTE_LENGTH = "length";
    public static final String ATTRIBUTE_LENGTH_KM = "length (km)";

    public static final List<String> BUS_STANDARD = Arrays.asList(
        "bus_i",
        "type",
        "pd",
        "qd",
        "gs",
        "bs",
        "area",
        "vm",
        "va",
        "basekv",
        "zone",
        "vmax",
        "vmin"
    );

    public static final List<String> BUS_EXTENSION_1 = Stream
        .concat(BUS_STANDARD.stream(), Stream.of("hasgen", "isload", "snom_mva", "sx", "sy", "sx", "gy"))
        .collect(Collectors.toList());

    /*
        public static final List<String> BUS_EXTENSION_2 = Stream
        .concat(BUS_STANDARD.stream(), Stream.of("status", "increment_cost", "decrement_cost"))
        .collect(Collectors.toList());
   */

    public static final List<String> BUS_TOTAL = Arrays.asList(
        "bus_i",
        "type",
        "Pd",
        "Qd",
        "Gs",
        "Bs",
        "area",
        "Vm",
        "Va",
        "baseKV",
        "zone",
        "Vmax",
        "Vmin",
        "hasGEN",
        "isLOAD",
        "SNOM_MVA",
        "SX",
        "SY",
        "GX",
        "GY"
        // fix 2023/03 apparently  used from T41 first version of the tool
        //"Status",
        //"increment_cost",
        //"decrement_cost"
    );

    public static final List<String> GENERATOR_STANDARD = Arrays.asList(
        "bus",
        "pg",
        "qg",
        "qmax",
        "qmin",
        "vg",
        "mbase",
        "status",
        "pmax",
        "pmin",
        "pc1",
        "pc2",
        "qc1min",
        "qc1max",
        "qc2min",
        "qc2max",
        "ramp_agc",
        "ramp_10",
        "ramp_30",
        "ramp_q",
        "apf"
    );

    public static final List<String> GENERATOR_EXTENSION_1 = Stream
        //.concat(GENERATOR_STANDARD.stream(), Stream.of("id"))
        .concat(GENERATOR_STANDARD.stream(), Stream.of("ID"))
        .collect(Collectors.toList());

    /**   fix 2023/03 apparently  used from T41 first version of the tool
    public static final List<String> GENERATOR_EXTENSION_2 = Stream
        .concat(GENERATOR_STANDARD.stream(), Stream.of("status_curt", "dg_type"))
        .collect(Collectors.toList());

    public static final List<String> GENERATOR_TOTAL = Arrays.asList(
        "bus",
        "Pg",
        "Qg",
        "Qmax",
        "Qmin",
        "Vg",
        "mBase",
        "status",
        "Pmax",
        "Pmin",
        "Pc1",
        "Pc2",
        "Qc1min",
        "Qc1max",
        "Qc2min",
        "Qc2max",
        "ramp_agc",
        "ramp_10",
        "ramp_30",
        "ramp_q",
        "apf",
        "ID"  //2023/03 Bug Fix writer
      //  "id",
      //  "status_curt",
      //  "dg_type"
    ); */

    public static final List<String> BRANCH_STANDARD = Arrays.asList(
        "fbus",
        "tbus",
        "r",
        "x",
        "b",
        "ratea",
        "rateb",
        "ratec",
        "ratio",
        "angle",
        "status",
        "angmin",
        "angmax"
    );

    public static final List<String> BRANCH_EXTENSION_1 = Stream
        .concat(BRANCH_STANDARD.stream(), Stream.of("step_size", "acttap", "mintap", "maxtap", "normaltap", "length"))
        .collect(Collectors.toList());

    public static final List<String> BRANCH_EXTENSION_2 = Stream
        .concat(
            BRANCH_STANDARD.stream(),
            Stream.of(
                "step_size",
                "acttap",
                "mintap",
                "maxtap",
                "normaltap",
                "nominalratio",
                "r_ip",
                "r_n",
                "r0",
                "x0",
                "b0",
                "length",
                "normstat"
            )
        )
        .collect(Collectors.toList());
    /*
    public static final List<String> BRANCH_EXTENSION_3 = Stream
        .concat(BRANCH_STANDARD.stream(), Stream.of("g", "mintap", "maxtap"))
        .collect(Collectors.toList());

    public static final List<String> BRANCH_TOTAL = Arrays.asList(
        "fbus",
        "tbus",
        "r",
        "x",
        "b",
        "rateA (typical)",
        "rateB (summer)",
        "rateC (winter)",
        "ratio",
        "angle",
        "status",
        "angmin",
        "angmax",
        "step_size",
        "actTap",
        "minTap",
        "maxTap",
        "normalTap",
        "nominalRatio",
        "r_ip",
        "r_n",
        "r0",
        "x0",
        "b0",
        "length (km)",  // 2023/03 length in DB table are stored in KM
        "NormSTAT"
     //   "g"
    ); */
}
