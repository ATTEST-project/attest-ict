package com.attest.ict.helper.utils;

import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import java.text.SimpleDateFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileUtil {

    public static final Logger log = LoggerFactory.getLogger(ProfileUtil.class);

    /**
     * @param data:           list row parsed from csv file
     * @param numSkipColumns: num coloums that don't contain time series values
     * @return
     *         24 = 1 hour
     *         48 = 30 mins
     *         96 = 15 mins
     */
    public static int getMaxNumCol(List<String[]> data, int numSkipColumns) {
        int max = 0;
        // non tutte le righe contengono i valori completi delle time series,
        // in alcuni casi la prima riga ne contiene di meno quindi conviene individuare
        // la riga con + valori
        for (String[] values : data) {
            if (values.length > max) {
                max = values.length;
            }
        }
        // first and second colums are not time serie's values
        int numColMax = (max - numSkipColumns);
        log.debug("Return numColMax: {} " + numColMax);
        return numColMax;
    }

    /**
     * @param data:  list row parsed from excel file
     * @return
     *         24 = 1 hour
     *         48 = 30 mins
     *         96 = 15 mins
     */
    public static int getMaxNumCol(List<LoadGeneratorPower> data) {
        int max = 0;
        // non tutte le righe contengono i valori completi delle time series,
        // in alcuni casi la prima riga ne contiene di meno quindi conviene individuare
        // la riga con + valori
        for (LoadGeneratorPower demand : data) {
            int numValues = demand.getValues().size();
            if (numValues > max) {
                max = numValues;
            }
        }
        log.debug("Return numColMax: {} " + max);
        return max;
    }

    /**
     * @param data
     * @return
     *         1.0 = 1 hour
     *         0.5 = 30 mins
     *         0.25 = 15 mins
     */
    public static double getTimeInterval(int numColMax) {
        double timeInterval = 1.0 / (numColMax / 24);
        log.debug("Return timeInterval: {} " + timeInterval);
        return timeInterval;
    }

    // Return Load Profile Mode
    public static int getMode(String season, String typicalDay) {
        // 2 = a representative business day for a season
        log.debug("season: {}, typicalDay: {}", season, typicalDay);
        if (season != null && typicalDay != null && typicalDay.equals("Bd")) {
            log.debug("Return mode: {} " + 2);
            return 2;
        }

        // 3 = a representative business day for a month
        if (season == null && typicalDay != null && typicalDay.equals("Bd")) {
            log.debug("Return mode: {} " + 3);
            return 3;
        }

        // 4 = a representative weekend for a season
        if (season != null && typicalDay != null && !typicalDay.equals("Bd")) {
            log.debug("Return mode: {} " + 4);
            return 4;
        }

        // 5 = a representative weekend for a month */
        if (season == null && typicalDay != null && !typicalDay.equals("Bd")) {
            log.debug("Return mode: {} " + 5);
            return 5;
        }

        // 1 = full time-series for a year
        log.debug("Return mode: {} " + 1);
        return 1;
    }

    public static String preformat(String file) {
        String nameToParse = file.replaceAll("\\s", "");
        nameToParse = nameToParse.replace("_GEN", "");
        nameToParse = nameToParse.replace("_Flex", "");
        nameToParse = nameToParse.replace("-Flex", "");
        return nameToParse.trim();
    }

    public static String[] getProfile(String fileNameToParse) {
        String file = preformat(fileNameToParse);
        int pos = file.lastIndexOf(".");
        // Exclude file extension
        String fileName = file.substring(0, pos);
        return fileName.split("_");
    }

    public static String getNetWorkName(String[] loadProfile) {
        // Network1_W_Su_2030.xlsx, Network1_Su_Sa.csv, KPC_35_A_Bd.csv
        int dim = loadProfile.length;
        dim = (getYear(loadProfile[dim - 1]) != null) ? dim - 3 : dim - 2;
        String name = "";
        for (int i = 0; i < dim; i++) {
            name = (i > 0) ? name + "_" + loadProfile[i] : loadProfile[i];
        }
        log.debug(" return network's name: {} ", name);
        return name;
    }

    public static String getSeason(String[] loadProfile) {
        int dim = loadProfile.length;
        // Network1_W_Su_2030.xlsx, Network1_Su_Sa.csv, KPC_35_A_Bd.csv
        return (getYear(loadProfile[dim - 1]) != null) ? loadProfile[dim - 3] : loadProfile[dim - 2];
    }

    public static String getTypicalDay(String[] loadProfile) {
        int dim = loadProfile.length;
        // Network1_W_Su_2030.xlsx, Network1_Su_Sa.csv, KPC_35_A_Bd.csv
        return (getYear(loadProfile[dim - 1]) != null) ? loadProfile[dim - 2] : loadProfile[dim - 1];
    }

    public static String getYear(String year) {
        SimpleDateFormat formatter = new SimpleDateFormat("y");
        try {
            formatter.parse(year);
            return year;
        } catch (java.text.ParseException e) {
            log.debug("Network Name does not contain Year, return null ");
            return null;
        }
    }

    public static boolean validateMode(Integer mode) {
        return (mode.intValue() >= ProfileConstants.MODE_MIN && mode.intValue() <= ProfileConstants.MODE_MAX);
    }

    public static boolean validateSeason(String season) {
        return (ProfileConstants.SEASON_SET.contains(season));
    }

    public static boolean validateTypicalDay(String typicalDay) {
        return (ProfileConstants.DAY_SET.contains(typicalDay));
    }

    public static void main(String args[]) {
        //String flexFile = "Network5_Su_Bd - Flex.xlsx";

        //String flexFile = "Network1_A_Bd_2030.xlsx";

        String flexFile = "Network1_Su_Bd_2030 - Flex.xlsx";

        //String flexFile = "Network5_Su_Su - Flex.xlsx";
        //    	String loadFile1 = "Network5_A_Bd.xlsx";
        //    	String loadFile2 = "Network1_A_Bd_2030.xlsx";
        //    	String genFile1 =  "Network5_A_Bd_GEN.xlsx";
        String genFile2 = "KPC_10_A_Bd.csv";

        String[] prof = ProfileUtil.getProfile(flexFile);
        String netName = ProfileUtil.getNetWorkName(prof);
        String season = ProfileUtil.getSeason(prof);
        String day = ProfileUtil.getTypicalDay(prof);
        int mode = ProfileUtil.getMode(season, day);
    }
}
