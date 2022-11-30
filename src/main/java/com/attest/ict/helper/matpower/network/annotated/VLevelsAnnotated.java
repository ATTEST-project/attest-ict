package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.VoltageLevel;
import com.univocity.parsers.annotations.Parsed;

public class VLevelsAnnotated extends VoltageLevel {

    @Parsed(index = 0)
    public Double getV1() {
        return super.getv1();
    }

    @Parsed(index = 0, field = "v1")
    public void setV1(Double v1) {
        super.setv1(v1);
    }

    @Parsed(index = 1)
    public Double getV2() {
        return super.getv2();
    }

    @Parsed(index = 1, field = "v2")
    public void setV2(Double v2) {
        super.setv2(v2);
    }

    @Parsed(index = 2)
    public Double getV3() {
        return super.getv3();
    }

    @Parsed(index = 2, field = "v3")
    public void setV3(Double v3) {
        super.setv3(v3);
    }
}
