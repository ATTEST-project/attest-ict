package com.attest.ict.custom.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class testUtils {

    public static void main(String[] args) {
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
    }
}
