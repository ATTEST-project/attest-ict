package com.attest.ict.helper.utils;

import com.attest.ict.domain.Generator;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorUtil {

    /**
     *
     * @param order: one bus could contain more than one generator. This value identify the insertion order that is the same as the one used in matpower file
     * @param busNum bus number where generator is installed
     * @param network's generators
     * @return gen's id installed on busNum
     */
    public static Generator getGeneratorForBus(int order, Long busNum, List<Generator> generators) {
        List<Generator> generatorsList = generators
            .stream() // convert to stream
            .filter(gen -> busNum.equals(gen.getBusNum())) // generator installed on busNum
            .collect(Collectors.toList()); //convert to list

        Generator gen = generatorsList.get(order - 1);
        return gen;
    }
}
