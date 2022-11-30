package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.GeneratorExtension;
import com.univocity.parsers.annotations.Parsed;

public class GeneratorExtension1Annotated extends GeneratorExtension {

    @Parsed(index = 0)
    public Integer getStatusCurt() {
        return super.getStatusCurt();
    }

    @Parsed(index = 0, field = "statusCurt")
    public void setStatusCurt(Integer statusCurt) {
        super.setStatusCurt(statusCurt);
    }

    @Parsed(index = 1)
    public Integer getDgType() {
        return super.getDgType();
    }

    @Parsed(index = 1, field = "dgType")
    public void setDgType(Integer dgType) {
        super.setDgType(dgType);
    }
}
