package com.attest.ict.custom.utils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TestUtils {

    private void test1() {
        String inputDirPath = "ATSIM/WP5/T52/UUID/dataset";
        String path =
            "C:/Users/Davide Longo/Documents/software_factory_acotel/wp5/Assest-WP-5.2-Tools-master/dataset/tool51_Spain_supports_EI.csv";
        //String path = "tool51_Spain_supports_EI.csv";
        int pos = path.lastIndexOf("/");

        String inputFileName = (pos == 0) ? path.substring(pos, path.length()) : path.substring(pos + 1, path.length());
        String newPath = inputDirPath.concat(File.separator).concat(inputFileName);

        List<String> letters = new ArrayList<String>();

        letters.add("a");
        letters.add("b");
        letters.add("c");

        for (int i = 0; i < letters.size(); i++) {
            letters.set(i, "D");
        }

        for (int i = 0; i < letters.size(); i++) {
            System.out.println(letters.get(i));
        }

        List<Map<String, String>> mapListTest = new ArrayList<Map<String, String>>();
        Map<String, String> sec1 = new HashMap<String, String>();
        Map<String, String> sec2 = new HashMap<String, String>();
        Map<String, String> sec3 = new HashMap<String, String>();

        sec1.put("sec_req_path", "sec_req.csv");
        sec2.put("gen_bid_qnt_path", "gen_bid_qnt.csv");
        sec3.put("gen_bid_prices_path", "gen_bid_prices.csv");

        mapListTest.add(sec1);
        mapListTest.add(sec2);
        mapListTest.add(sec3);

        mapListTest.stream().filter(m -> m.keySet().contains("gen_bid_qnt_path")).findFirst();
    }

    private List<String> test2() {
        List<String> line = Arrays.asList("2", "0", "0", "2", "10", "0", "0");
        int index = 4;
        List<String> row = line.subList(0, 4);
        List<String> costs = line.subList(4, line.size());
        String cost = costs.stream().map(Object::toString).collect(Collectors.joining("|"));

        List<String> newRow = new ArrayList<>();
        newRow.addAll(row);
        newRow.add(cost);
        return newRow;
    }

    public static void main(String[] args) {
        TestUtils test = new TestUtils();
        System.out.println(" Test concat string " + test.test2().toString());
    }
}
