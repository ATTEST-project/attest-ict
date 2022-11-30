package com.attest.ict.helper.ods.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class T41FileInputFormat extends OdsFileFormat {

    public static SortedMap<String, List<String>> scenarioGenSheetMap;

    public static SortedMap<String, List<String>> netwrokSheetMap;

    public static SortedMap<String, List<String>> flexSheetMap;

    public static List<String> networkSheetHeaders;

    public static List<String> flexSheetHeaders;

    static {
        //----- network file eg.: uk_dx_01_2020.ods, pt_dx_01_2020.ods

        //-- set sheetNames
        networkSheetHeaders = new ArrayList<String>();
        networkSheetHeaders.add("Buses");
        networkSheetHeaders.add("Lines");
        networkSheetHeaders.add("Loads");
        networkSheetHeaders.add("Gens");
        networkSheetHeaders.add("Base_MVA");
        // set columns header for each sheet
        netwrokSheetMap = new TreeMap<>();
        //Buses
        netwrokSheetMap.put(
            networkSheetHeaders.get(0),
            Arrays.asList(new String[] { "bus_i", "type", "area", "Vm", "Va", "baseKV", "zone", "Vmax", "Vmin" })
        );

        //Lines
        netwrokSheetMap.put(
            networkSheetHeaders.get(1),
            Arrays.asList(
                new String[] { "fbus", "tbus", "r", "x", "b", "rateA", "rateB", "rateC", "ratio", "angle", "status", "angmin", "angmax" }
            )
        );

        //Loads
        netwrokSheetMap.put(networkSheetHeaders.get(2), Arrays.asList(new String[] { "bus_i", "Pd", "Qd", "Gs", "Bs" }));

        //Gens
        netwrokSheetMap.put(
            networkSheetHeaders.get(3),
            Arrays.asList(
                new String[] {
                    "bus_i",
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
                }
            )
        );

        //Base_MVA
        netwrokSheetMap.put(networkSheetHeaders.get(4), Arrays.asList(new String[] { "base_MVA" }));

        //-- FLEX set sheetNames
        flexSheetHeaders = new ArrayList<String>();
        flexSheetHeaders.add("Buses_Addt");
        flexSheetHeaders.add("Buses_OLTC_Addt");
        flexSheetHeaders.add("Lines_Addt");
        flexSheetHeaders.add("Lines_OLTC_Addt");
        flexSheetHeaders.add("Loads_Addt");
        flexSheetHeaders.add("RES_Addt");
        flexSheetHeaders.add("Gens_Addt");
        flexSheetHeaders.add("Gens_cost_Addt");
        flexSheetHeaders.add("Storage_Addt");
        flexSheetHeaders.add("Storage_cost_Addt");
        flexSheetHeaders.add("pLoad_Profiles_Addt");
        flexSheetHeaders.add("qLoad_Profiles_Addt");
        flexSheetHeaders.add("pGen_Profiles_Min_Addt");
        flexSheetHeaders.add("pGen_Profiles_Max_Addt");
        flexSheetHeaders.add("qGen_Profiles_Min_Addt");
        flexSheetHeaders.add("qGen_Profiles_Max_Addt");
        flexSheetHeaders.add("pLoad_Orig(W)"); //  exists only for PT
        flexSheetHeaders.add("qLoad_Orig(W)"); // exists only for PT

        //----- file flex addional information eg:pt_dx_01_2020_flex.ods, uk_dx_01_2020_flex.ods
        flexSheetMap = new TreeMap<>();

        //Buses_addt
        flexSheetMap.put(
            flexSheetHeaders.get(0),
            Arrays.asList(new String[] { "bus_i", "hasGEN", "isLOAD", "SNOM_MVA", "SX", "SY", "GX", "GY" })
        );

        //Buses_Oltc
        flexSheetMap.put(
            flexSheetHeaders.get(1),
            Arrays.asList(
                new String[] {
                    "bus_i",
                    "type",
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
                    "GY",
                }
            )
        );

        //Lines_Addt
        flexSheetMap.put(
            flexSheetHeaders.get(2),
            Arrays.asList(
                new String[] {
                    "fbus",
                    "tbus",
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
                    "length (meter)",
                    "NormSTAT",
                    "g",
                    "minTapratio",
                    "maxTapratio",
                }
            )
        );

        //Lines_OLTC_Addt
        flexSheetMap.put(
            flexSheetHeaders.get(3),
            Arrays.asList(
                new String[] {
                    "fbus",
                    "tbus",
                    "r",
                    "x",
                    "b",
                    "rateA",
                    "rateB",
                    "rateC",
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
                    "length (meter)",
                    "NormSTAT",
                    "g",
                    "minTapratio",
                    "maxTapratio",
                }
            )
        );

        //Loads_addt
        flexSheetMap.put(flexSheetHeaders.get(4), Arrays.asList(new String[] { "bus_i", "Status", "cost_inc", "cost_dec" }));

        //RES_Addt
        flexSheetMap.put(
            flexSheetHeaders.get(5),
            Arrays.asList(
                new String[] {
                    "bus_i",
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
                }
            )
        );

        //Gens_Addt
        flexSheetMap.put(flexSheetHeaders.get(6), Arrays.asList(new String[] { "bus_i", "CurtStatus", "Dgtype" }));

        //Gen cost Addt
        flexSheetMap.put(
            flexSheetHeaders.get(7),
            Arrays.asList(new String[] { "bus_i", "model", "startUpCost", "startDownCost", "n", "c2", "c1", "c0" })
        );

        //Storage_Addt
        flexSheetMap.put(
            flexSheetHeaders.get(8),
            Arrays.asList(
                new String[] {
                    "bus_i",
                    "Ps",
                    "Qs",
                    "energy",
                    "eRat",
                    "chRat",
                    "disRat",
                    "chEff",
                    "disEff",
                    "thermalRat",
                    "qmin",
                    "qmax",
                    "r",
                    "x",
                    "ploss",
                    "qloss",
                    "status",
                    "soc_0",
                    "socMin",
                    "socMax",
                }
            )
        );

        //Storage_cost_addt
        flexSheetMap.put(flexSheetHeaders.get(9), Arrays.asList(new String[] { "bus_i", "c2", "c1", "c0" }));

        //pLoadProfiles_Addt: TO DO how to manage 24 or 96 columns
        flexSheetMap.put(flexSheetHeaders.get(10), Arrays.asList(new String[] { "bus_i", "t" }));

        // qLoadProfiles_Addt TO DO how to manage 24 or 96 columns
        flexSheetMap.put(flexSheetHeaders.get(11), Arrays.asList(new String[] { "bus_i", "t" }));

        //pGen_Profiles_Min_Addt",
        flexSheetMap.put(
            flexSheetHeaders.get(12),
            Arrays.asList(
                new String[] {
                    "Bus",
                    "t1",
                    "t2",
                    "t3",
                    "t4",
                    "t5",
                    "t6",
                    "t7",
                    "t8",
                    "t9",
                    "t10",
                    "t11",
                    "t12",
                    "t13",
                    "t14",
                    "t15",
                    "t16",
                    "t17",
                    "t18",
                    "t19",
                    "t20",
                    "t21",
                    "t22",
                    "t23",
                    "t24",
                }
            )
        );

        //"pGen_Profiles_Max_Addt",
        flexSheetMap.put(
            flexSheetHeaders.get(13),
            Arrays.asList(
                new String[] {
                    "Bus",
                    "t1",
                    "t2",
                    "t3",
                    "t4",
                    "t5",
                    "t6",
                    "t7",
                    "t8",
                    "t9",
                    "t10",
                    "t11",
                    "t12",
                    "t13",
                    "t14",
                    "t15",
                    "t16",
                    "t17",
                    "t18",
                    "t19",
                    "t20",
                    "t21",
                    "t22",
                    "t23",
                    "t24",
                }
            )
        );

        //"qGen_Profiles_Min_Addt",
        flexSheetMap.put(
            flexSheetHeaders.get(14),
            Arrays.asList(
                new String[] {
                    "Bus",
                    "t1",
                    "t2",
                    "t3",
                    "t4",
                    "t5",
                    "t6",
                    "t7",
                    "t8",
                    "t9",
                    "t10",
                    "t11",
                    "t12",
                    "t13",
                    "t14",
                    "t15",
                    "t16",
                    "t17",
                    "t18",
                    "t19",
                    "t20",
                    "t21",
                    "t22",
                    "t23",
                    "t24",
                }
            )
        );

        //"qGen_Profiles_Max_Addt",
        flexSheetMap.put(
            flexSheetHeaders.get(15),
            Arrays.asList(
                new String[] {
                    "Bus",
                    "t1",
                    "t2",
                    "t3",
                    "t4",
                    "t5",
                    "t6",
                    "t7",
                    "t8",
                    "t9",
                    "t10",
                    "t11",
                    "t12",
                    "t13",
                    "t14",
                    "t15",
                    "t16",
                    "t17",
                    "t18",
                    "t19",
                    "t20",
                    "t21",
                    "t22",
                    "t23",
                    "t24",
                }
            )
        );

        //pLoad_Orig(W)",
        flexSheetMap.put(
            flexSheetHeaders.get(16),
            Arrays.asList(
                new String[] {
                    "bus_i",
                    "t", // capire come gestire potrebbero essere 24 colonne o 96 va definito un modo autotmatico
                }
            )
        );

        // "qLoad_Orig(W)",
        flexSheetMap.put(
            flexSheetHeaders.get(17),
            Arrays.asList(
                new String[] {
                    "bus_i",
                    "t", // capire come gestire potrebbero essere 24 colonne o 96 va definito un modo autotmatico
                }
            )
        );
    }
}
