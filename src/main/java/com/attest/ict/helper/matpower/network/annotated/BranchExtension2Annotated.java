package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BranchExtension;
import com.univocity.parsers.annotations.Parsed;

public class BranchExtension2Annotated extends BranchExtension {

    @Parsed(index = 0)
    public Double getG() {
        return super.getG();
    }

    @Parsed(index = 0, field = "g")
    public void setG(Double g) {
        super.setG(g);
    }

    @Parsed(index = 1)
    public Double getMinTap() {
        return super.getMinTap();
    }

    @Parsed(index = 1, field = "minTap")
    public void setMinTap(Double minTap) {
        super.setMinTap(minTap);
    }

    @Parsed(index = 2)
    public Double getMaxTap() {
        return super.getMaxTap();
    }

    @Parsed(index = 2, field = "maxTap")
    public void setMaxTap(Double maxTap) {
        super.setMaxTap(maxTap);
    }
}
