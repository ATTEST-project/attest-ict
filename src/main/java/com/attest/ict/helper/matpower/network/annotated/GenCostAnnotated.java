package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.GenCost;
import com.univocity.parsers.annotations.Parsed;

public class GenCostAnnotated extends GenCost {

    @Parsed(index = 0)
    public Integer getModel() {
        return super.getModel();
    }

    @Parsed(index = 0, field = "model")
    public void setModel(Integer model) {
        super.setModel(model);
    }

    @Parsed(index = 1)
    public Double getStartup() {
        return super.getStartup();
    }

    @Parsed(index = 1, field = "startup")
    public void setStartup(Double startup) {
        super.setStartup(startup);
    }

    @Parsed(index = 2)
    public Double getShutdown() {
        return super.getShutdown();
    }

    @Parsed(index = 2, field = "shutdown")
    public void setShutdown(Double shutdown) {
        super.setShutdown(shutdown);
    }

    @Parsed(index = 3)
    public Long getnCost() {
        return super.getnCost();
    }

    @Parsed(index = 3, field = "nCost")
    public void setnCost(Long n) {
        super.setnCost(n);
    }

    @Parsed(index = 4)
    public String getCostPF() {
        return super.getCostPF();
    }

    @Parsed(index = 4, field = "costPF")
    public void setCostPF(String n) {
        super.setCostPF(n);
    }

    @Parsed(index = 5)
    public String getCostQF() {
        return super.getCostQF();
    }

    @Parsed(index = 5, field = "costQF")
    public void setCostQF(String costQF) {
        super.setCostQF(costQF);
    }
}
