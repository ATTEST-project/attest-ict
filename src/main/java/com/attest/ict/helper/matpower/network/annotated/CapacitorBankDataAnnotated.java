package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.CapacitorBankData;
import com.univocity.parsers.annotations.Parsed;

public class CapacitorBankDataAnnotated extends CapacitorBankData {

    @Parsed(index = 0)
    public Long getBus_i() {
        return super.getBusNum();
    }

    @Parsed(index = 0, field = "bus_i")
    public void setBus_i(Long bus_i) {
        super.setBusNum(bus_i);
    }

    @Parsed(index = 1)
    public String getNode_id() {
        return super.getNodeId();
    }

    @Parsed(index = 1, field = "node_id")
    public void setNode_id(String node_id) {
        super.setNodeId(node_id);
    }

    @Parsed(index = 2)
    public String getBank_id() {
        return super.getBankId();
    }

    @Parsed(index = 2, field = "bank_id")
    public void setBank_id(String bank_id) {
        super.setBankId(bank_id);
    }

    @Parsed(index = 3)
    public Double getQnom() {
        return super.getQnom();
    }

    @Parsed(index = 3, field = "qnom")
    public void setQnom(Double qnom) {
        super.setQnom(qnom);
    }
}
