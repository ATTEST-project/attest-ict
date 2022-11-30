package com.attest.ict.custom.model.matpower;

import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.FlexProfile;
import java.util.ArrayList;
import java.util.List;

public class MatpowerFlexibilityModel {

    private String caseName;

    private final List<Long> flexBuses = new ArrayList<>();

    private FlexProfile flexProfile;

    private final List<FlexElVal> flexElVals = new ArrayList<>();

    private final List<List<Double>> flexPfUp = new ArrayList<>();

    private final List<List<Double>> flexPfDn = new ArrayList<>();

    private final List<List<Double>> flexQfUp = new ArrayList<>();

    private final List<List<Double>> flexQfDn = new ArrayList<>();

    public MatpowerFlexibilityModel(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public List<Long> getFlexBuses() {
        return flexBuses;
    }

    public FlexProfile getFlexProfile() {
        return flexProfile;
    }

    public void setFlexProfile(FlexProfile flexProfile) {
        this.flexProfile = flexProfile;
    }

    public List<FlexElVal> getFlexElVals() {
        return flexElVals;
    }

    public List<List<Double>> getFlexPfUp() {
        return flexPfUp;
    }

    public List<List<Double>> getFlexPfDn() {
        return flexPfDn;
    }

    public List<List<Double>> getFlexQfUp() {
        return flexQfUp;
    }

    public List<List<Double>> getFlexQfDn() {
        return flexQfDn;
    }
}
