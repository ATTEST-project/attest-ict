package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BranchExtension;
import com.univocity.parsers.annotations.Parsed;

public class BranchExtension1Annotated extends BranchExtension {

    @Parsed(index = 0)
    public Double getStepSize() {
        return super.getStepSize();
    }

    @Parsed(index = 0, field = "stepSize")
    public void setStepSize(Double stepSize) {
        super.setStepSize(stepSize);
    }

    @Parsed(index = 1)
    public Double getActTap() {
        return super.getActTap();
    }

    @Parsed(index = 1, field = "actTap")
    public void setActTap(Double actTap) {
        super.setActTap(actTap);
    }

    @Parsed(index = 2)
    public Double getMinTap() {
        return super.getMinTap();
    }

    @Parsed(index = 2, field = "minTap")
    public void setMinTap(Double minTap) {
        super.setMinTap(minTap);
    }

    @Parsed(index = 3)
    public Double getMaxTap() {
        return super.getMaxTap();
    }

    @Parsed(index = 3, field = "maxTap")
    public void setMaxTap(Double maxTap) {
        super.setMaxTap(maxTap);
    }

    @Parsed(index = 4)
    public Double getNormalTap() {
        return super.getNormalTap();
    }

    @Parsed(index = 4, field = "normalTap")
    public void setNormalTap(Double normalTap) {
        super.setNormalTap(normalTap);
    }

    @Parsed(index = 5)
    public Double getLength() {
        return super.getLength();
    }

    @Parsed(index = 5, field = "length")
    public void setLength(Double length) {
        super.setLength(length);
    }
}
