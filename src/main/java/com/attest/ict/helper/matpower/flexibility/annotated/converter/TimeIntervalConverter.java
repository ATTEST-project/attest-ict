package com.attest.ict.helper.matpower.flexibility.annotated.converter;

import com.univocity.parsers.conversions.Conversion;

public class TimeIntervalConverter implements Conversion<String, Integer> {

    public TimeIntervalConverter() {}

    @Override
    public Integer execute(String s) {
        int convertedValue;
        switch (s) {
            case "1":
                convertedValue = 60;
                break;
            case "0.5":
                convertedValue = 30;
                break;
            case "0.25":
                convertedValue = 15;
                break;
            default:
                convertedValue = 60;
        }
        return convertedValue;
    }

    @Override
    public String revert(Integer value) {
        String revertedValue = null;
        switch (value) {
            case 60:
                revertedValue = "1";
                break;
            case 30:
                revertedValue = "0.5";
                break;
            case 15:
                revertedValue = "0.25";
                break;
            default:
                revertedValue = "1";
        }
        return revertedValue;
    }
}
