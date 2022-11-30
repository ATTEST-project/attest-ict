package com.attest.ict.helper.matpower.flexibility.writer;

import com.attest.ict.custom.model.matpower.MatpowerFlexibilityModel;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.helper.matpower.common.util.structure.MatpowerFileStruct;
import com.attest.ict.helper.matpower.common.util.structure.MpcElement;
import com.attest.ict.helper.matpower.flexibility.util.MatpowerFlexibilitySection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MatpowerFlexibilityWriter {

    private MatpowerFlexibilityWriter() {}

    public static MatpowerFileStruct generateMatpowerStructure(MatpowerFlexibilityModel model) {
        MatpowerFileStruct struct = new MatpowerFileStruct(model.getCaseName());

        MpcElement flexBus = new MpcElement(MatpowerFlexibilitySection.FLEXBUS);
        flexBus.setContent(convertFlexBuses(model.getFlexBuses()));
        struct.getMpcElements().add(flexBus);

        MpcElement flexProf = new MpcElement(MatpowerFlexibilitySection.FLEXPROF);
        flexProf.setContent(convertFlexProf(model.getFlexProfile()));
        struct.getMpcElements().add(flexProf);

        MpcElement pfUp = new MpcElement(MatpowerFlexibilitySection.PF_UP);
        pfUp.setContent(convertFlexValues(model.getFlexPfUp()));
        struct.getMpcElements().add(pfUp);

        MpcElement pfDn = new MpcElement(MatpowerFlexibilitySection.PF_DN);
        pfDn.setContent(convertFlexValues(model.getFlexPfDn()));
        struct.getMpcElements().add(pfDn);

        MpcElement qfUp = new MpcElement(MatpowerFlexibilitySection.QF_UP);
        qfUp.setContent(convertFlexValues(model.getFlexQfUp()));
        struct.getMpcElements().add(qfUp);

        MpcElement qfDn = new MpcElement(MatpowerFlexibilitySection.QF_DN);
        qfDn.setContent(convertFlexValues(model.getFlexQfDn()));
        struct.getMpcElements().add(qfDn);

        return struct;
    }

    private static List<String> convertFlexBuses(List<Long> flexBuses) {
        List<String> flexBusesConverted = new ArrayList<>();
        for (Long flexBus : flexBuses) {
            flexBusesConverted.add(flexBus + ";\n");
        }
        return flexBusesConverted;
    }

    /*private static String revert(Double value) {
        String revertedValue = null;
        if (value == 60) {
            revertedValue = "1";
        } else if (value == 30) {
            revertedValue = "0.5";
        } else if (value == 15) {
            revertedValue = "0.25";
        } else {
            revertedValue = "1";
        }
        return revertedValue;
    }*/

    private static List<String> convertFlexProf(FlexProfile flexProfile) {
        // String timeIntervalReverted = revert(flexProfile.getTimeInterval());
        return Collections.singletonList(flexProfile.getMode() + "\t" + flexProfile.getTimeInterval() + ";\n");
    }

    private static List<String> convertFlexValues(List<List<Double>> flexValues) {
        List<String> flexValuesConverted = new ArrayList<>();
        for (List<Double> values : flexValues) {
            String newValues = values.stream().map(String::valueOf).collect(Collectors.joining("\t")) + ";\n";
            flexValuesConverted.add(newValues);
        }
        return flexValuesConverted;
    }
}
