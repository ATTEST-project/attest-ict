package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.Transformer;
import com.univocity.parsers.annotations.Parsed;

public class TransformerAnnotated extends Transformer {

    @Parsed(index = 0)
    public Long getFbus() {
        return super.getFbus();
    }

    @Parsed(index = 0, field = "fbus")
    public void setFbus(Long fbus) {
        super.setFbus(fbus);
    }

    @Parsed(index = 1)
    public Long getTbus() {
        return super.getTbus();
    }

    @Parsed(index = 1, field = "tbus")
    public void setTbus(Long tbus) {
        super.setTbus(tbus);
    }

    @Parsed(index = 2)
    public Double getMin() {
        return super.getMin();
    }

    @Parsed(index = 2, field = "min")
    public void setMin(Double min) {
        super.setMin(min);
    }

    @Parsed(index = 3)
    public Double getMax() {
        return super.getMax();
    }

    @Parsed(index = 3, field = "max")
    public void setMax(Double max) {
        super.setMax(max);
    }

    @Parsed(index = 4)
    public Integer getTotalTaps() {
        return super.getTotalTaps();
    }

    @Parsed(index = 4, field = "total_taps")
    public void setTotalTaps(Integer totalTaps) {
        super.setTotalTaps(totalTaps);
    }

    @Parsed(index = 5)
    public Integer getTap() {
        return super.getTap();
    }

    @Parsed(index = 5, field = "tap")
    public void setTap(Integer tap) {
        super.setTap(tap);
    }
}
