package com.attest.ict.custom.utils;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterUtils {

    public static final Logger LOG = LoggerFactory.getLogger(ConverterUtils.class);

    public static String convertPipeToTab(String string) {
        if (string == null) return "";
        return (string != null && string.contains("|")) ? string.replace("|", "\t") : string;
    }

    public static String convertTabToPipe(String string) {
        if (string == null) return "";
        return (string != null && string.contains("\t")) ? string.replace("\t", "|") : string;
    }

    public static Boolean isNullOrEmpty(String string) {
        return (string == null | string.isEmpty());
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
        // LOG.debug(entry.getKey()+" : "+entry.getValue());
        // }

        return newMap;
    }

    public static void main(String args[]) {}
}
