package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BusName;
import com.univocity.parsers.annotations.Parsed;

public class BusNameAnnotated extends BusName {

    @Parsed(index = 0)
    public String getBusName() {
        return super.getBusName();
    }

    @Parsed(index = 0, field = "gen_tag")
    public void setBusName(String busName) {
        super.setBusName(busName);
    }
}
