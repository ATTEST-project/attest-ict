package com.attest.ict.custom.utils;

import com.attest.ict.constants.AttestConstants;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterUtils {

    public static final Logger log = LoggerFactory.getLogger(ConverterUtils.class);

    public static String convertPipeToTab(String string) {
        if (string == null) return "";
        return (string.contains(AttestConstants.GEN_COST_DELIMITER)) ? string.replace(AttestConstants.GEN_COST_DELIMITER, "\t") : string;
    }

    public static String convertTabToPipe(String string) {
        if (string == null) return "";
        return (string.contains("\t")) ? string.replace("\t", AttestConstants.GEN_COST_DELIMITER) : string;
    }

    public static Map<String, Integer> mapReplaceKeyChar(
        Map<String, Integer> map,
        String charToReplace,
        String newChar,
        boolean isLowerCase
    ) {
        Map<String, Integer> newMap = new Hashtable<String, Integer>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String newKey = key.toLowerCase().replaceAll(charToReplace, newChar);
            if (isLowerCase) newMap.put(newKey.toLowerCase(), map.get(key)); else newMap.put(newKey, map.get(key));
        }

        // for (Map.Entry<String, Integer> entry : newMap.entrySet()) {
        // log.debug(entry.getKey()+" : "+entry.getValue());
        // }

        return newMap;
    }

    /**
     * @param val String representation of the number to be formatted
     * @return value  the rounded number with two number of decimal places, in case of error parsing the input string, return the input string unchanged
     */
    public static String roundValue(String val) {
        if (val.isEmpty()) {
            return val;
        }
        int pos = val.indexOf("%");
        String value = (pos > 0) ? val.substring(0, pos) : val;
        try {
            double valRet = BigDecimal
                .valueOf(Double.parseDouble(value))
                .setScale(AttestConstants.SCALE, RoundingMode.HALF_DOWN)
                .doubleValue();
            return (pos > 0) ? String.valueOf(valRet) + "%" : String.valueOf(valRet);
        } catch (Exception ex) {
            log.error("[RoundValue] Error formatting decimal places of value: {}. Error: {}  ", value, ex.getMessage());
            // ex.printStackTrace();
            return value;
        }
    }

    /**
     * @param value String representation of the number to be formatted
     * @return Double: the rounded number with two number of decimal places, if error accours, return the input string unchanged
     */
    public static Double roundStringValue(String value) {
        if (value.isEmpty()) {
            return null;
        }

        try {
            double valRet = BigDecimal
                .valueOf(Double.parseDouble(value))
                .setScale(AttestConstants.SCALE, RoundingMode.HALF_DOWN)
                .doubleValue();
            //log.debug ("roundValue return: {} ", valRet);
            return valRet;
        } catch (Exception ex) {
            log.error("[RoundStringValue] Error formatting decimal places of value: {}. Error: {}  ", value, ex.getMessage());
            // ex.printStackTrace();
            return Double.valueOf(value);
        }
    }

    public static String removeDotZero(String value) {
        if (value.isEmpty() || value.equals("-")) {
            return value;
        }
        int pos = value.lastIndexOf(".0");
        return (pos > 0) ? value.substring(0, pos) : value;
    }

    /**
     * @param strVal String
     * @return Integer: integer rapresentation of the strign, if error accours, return null
     */
    public static Integer convertStringToInteger(String strVal) {
        if (!isNumeric(strVal)) {
            return null;
        }
        Double value = Double.valueOf(strVal);
        long longVal = Math.round(value);
        return Long.valueOf(longVal).intValue();
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    public static String replaceAllSpaceWithUnderscore(String stringToConvert) {
        return stringToConvert.replaceAll("\\s+", "_");
    }

    public static void main(String[] args) {
        String percentVal = "1.345678%";
        String roundVal = ConverterUtils.roundValue(percentVal);
        System.out.println("Original: " + percentVal + " RoundVal: " + roundVal);

        String val = "1.34567899";
        roundVal = ConverterUtils.roundValue(val);
        System.out.println("Original: " + val + " RoundVal: " + roundVal);

        String case1 = "pippo pluto    paperino";
        System.out.println(ConverterUtils.replaceAllSpaceWithUnderscore(case1));
    }
}
