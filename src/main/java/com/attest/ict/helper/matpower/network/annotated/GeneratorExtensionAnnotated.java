package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.GeneratorExtension;
import com.univocity.parsers.annotations.Parsed;

public class GeneratorExtensionAnnotated extends GeneratorExtension {

    @Parsed(index = 0)
    public Integer getIdGen() {
        return super.getIdGen();
    }

    @Parsed(index = 0, field = "ID")
    public void setIdGen(Integer id) {
        super.setIdGen(id);
    }
}
