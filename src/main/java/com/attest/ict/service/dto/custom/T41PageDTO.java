package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class T41PageDTO extends ToolPageDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private boolean isInitialPF;

    private boolean isViolation;

    private boolean isLoadAndResInputData;

    private boolean isFlexibilityData;

    public boolean isInitialPF() {
        return isInitialPF;
    }

    public void setInitialPF(boolean initialPF) {
        isInitialPF = initialPF;
    }

    public boolean isViolation() {
        return isViolation;
    }

    public void setViolation(boolean violation) {
        isViolation = violation;
    }

    public boolean isLoadAndResInputData() {
        return isLoadAndResInputData;
    }

    public void setLoadAndResInputData(boolean loadAndResInputData) {
        isLoadAndResInputData = loadAndResInputData;
    }

    public boolean isFlexibilityData() {
        return isFlexibilityData;
    }

    public void setFlexibilityData(boolean flexibilityData) {
        isFlexibilityData = flexibilityData;
    }
}
