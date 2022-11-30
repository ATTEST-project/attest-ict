package com.attest.ict.helper.matpower.network.util;

import com.attest.ict.helper.matpower.common.util.MatpowerSection;

public enum MatpowerNetworkSection implements MatpowerSection {
    VERSION("version"),
    BASE_MVA("baseMVA"),
    BUS("bus"),
    BRANCH("branch"),
    GENERATOR("gen"),
    GEN_TAGS("gen_tags"),
    BUS_NAMES("bus_names"),
    TRANSFORMER("trans"),
    BUS_COORDINATES("coordinates"),
    GEN_COST("gencost"),
    CAP_BANK_DATA("capacitor_bank_dplan"),
    V_LEVELS("V_levels");

    // LOAD_EL_VAR_P,
    // LOAD_EL_VAR_Q

    private final String label;

    MatpowerNetworkSection(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
