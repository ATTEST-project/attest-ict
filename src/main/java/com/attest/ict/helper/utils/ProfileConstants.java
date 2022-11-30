package com.attest.ict.helper.utils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfileConstants {

    public static final Set<String> SEASON_SET = Stream.of("A", "S", "Su", "W").collect(Collectors.toUnmodifiableSet());

    public static final Set<String> DAY_SET = Stream.of("Bd", "Sa", "Su").collect(Collectors.toUnmodifiableSet());

    public static final Integer MODE_MIN = 1;

    public static final Integer MODE_MAX = 5;

    public static final Integer MINS_FOR_HOUR = 60;

    public static final Map<String, String> MAP_SEASON;

    public static final Map<String, String> MAP_TIPICAL_DAY;

    static {
        MAP_SEASON = new TreeMap<String, String>();
        MAP_SEASON.put("A", "Autumn");
        MAP_SEASON.put("S", "Spring");
        MAP_SEASON.put("Su", "Summer");
        MAP_SEASON.put("W", "Winter");

        MAP_TIPICAL_DAY = new TreeMap<String, String>();
        MAP_TIPICAL_DAY.put("Bd", "Business Day");
        MAP_TIPICAL_DAY.put("Su", "Sunday");
        MAP_TIPICAL_DAY.put("Sa", "Saturday");
    }
}
