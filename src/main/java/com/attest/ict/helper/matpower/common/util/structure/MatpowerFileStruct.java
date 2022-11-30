package com.attest.ict.helper.matpower.common.util.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatpowerFileStruct {

    private String functionName;
    private List<MpcBaseElement> mpcElements = new ArrayList<>();

    public MatpowerFileStruct(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public List<MpcBaseElement> getMpcElements() {
        return mpcElements;
    }

    public void setMpcElements(List<MpcBaseElement> mpcElements) {
        this.mpcElements = mpcElements;
    }

    private String getMpcElementsFormatted() {
        return mpcElements.stream().map(MpcBaseElement::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return "function = " + functionName + "\n\n" + getMpcElementsFormatted();
    }
}
