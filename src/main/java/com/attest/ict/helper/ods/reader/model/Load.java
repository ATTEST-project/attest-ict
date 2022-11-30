package com.attest.ict.helper.ods.reader.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Load {

    public final Logger log = LoggerFactory.getLogger(Load.class);

    private Long busNum;

    private Double pd;

    private Double qd;

    private Double gs;

    private Double bs;

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long bus_i) {
        this.busNum = bus_i;
    }

    public Double getPd() {
        return pd;
    }

    public void setPd(Double pd) {
        this.pd = pd;
    }

    public Double getQd() {
        return qd;
    }

    public void setQd(Double qd) {
        this.qd = qd;
    }

    public Double getGs() {
        return gs;
    }

    public void setGs(Double gs) {
        this.gs = gs;
    }

    public Double getBs() {
        return bs;
    }

    public void setBs(Double bs) {
        this.bs = bs;
    }

    @Override
    public String toString() {
        return "Load [busI=" + busNum + ", pd=" + pd + ", qd=" + qd + ", gs=" + gs + ", bs=" + bs + "]";
    }
}
