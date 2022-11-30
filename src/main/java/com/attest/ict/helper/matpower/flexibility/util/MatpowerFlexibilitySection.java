package com.attest.ict.helper.matpower.flexibility.util;

import com.attest.ict.helper.matpower.common.util.MatpowerSection;

public enum MatpowerFlexibilitySection implements MatpowerSection {
    FLEXBUS("flexbus"),
    FLEXPROF("flexprof"),
    PF_UP("pf_up"),
    PF_DN("pf_dn"),
    QF_UP("qf_up"),
    QF_DN("qf_dn");

    // FLEXCOST("flexcost");

    private final String label;

    MatpowerFlexibilitySection(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
