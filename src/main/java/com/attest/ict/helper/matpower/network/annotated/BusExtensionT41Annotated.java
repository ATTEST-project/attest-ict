package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BusExtension;
import com.univocity.parsers.annotations.Parsed;

public class BusExtensionT41Annotated extends BusExtension {

    @Parsed(index = 0)
    public Integer getStatus() {
        return super.getStatus();
    }

    @Parsed(index = 0, field = "status")
    public void setStatus(Integer status) {
        super.setStatus(status);
    }

    @Parsed(index = 1)
    public Integer getIncrementCost() {
        return super.getIncrementCost();
    }

    @Parsed(index = 1, field = "incrementCost")
    public void setIncrementCost(Integer incrementCost) {
        super.setIncrementCost(incrementCost);
    }

    @Parsed(index = 2)
    public Integer getDecrementCost() {
        return super.getDecrementCost();
    }

    @Parsed(index = 2, field = "decrementCost")
    public void setDecrementCost(Integer decrementCost) {
        super.setDecrementCost(decrementCost);
    }
}
