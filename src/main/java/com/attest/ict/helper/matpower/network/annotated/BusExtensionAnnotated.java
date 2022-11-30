package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BusExtension;
import com.univocity.parsers.annotations.Parsed;

public class BusExtensionAnnotated extends BusExtension {

    @Parsed(index = 0)
    public Integer getHasGen() {
        return super.getHasGen();
    }

    @Parsed(index = 0, field = "hasGen")
    public void setHasGen(Integer hasGen) {
        super.setHasGen(hasGen);
    }

    @Parsed(index = 1)
    public Integer getIsLoad() {
        return super.getIsLoad();
    }

    @Parsed(index = 1, field = "isLoad")
    public void setIsLoad(Integer isLoad) {
        super.setIsLoad(isLoad);
    }

    @Parsed(index = 2)
    public Double getSnowMva() {
        return super.getSnomMva();
    }

    @Parsed(index = 2, field = "snowMva")
    public void setSnowMva(Double snowMva) {
        super.setSnomMva(snowMva);
    }

    @Parsed(index = 3)
    public Double getSx() {
        return super.getSx();
    }

    @Parsed(index = 3, field = "sx")
    public void setSx(Double sx) {
        super.setSx(sx);
    }

    @Parsed(index = 4)
    public Double getSy() {
        return super.getSy();
    }

    @Parsed(index = 4, field = "sy")
    public void setSy(Double sy) {
        super.setSy(sy);
    }

    @Parsed(index = 5)
    public Double getGx() {
        return super.getGx();
    }

    @Parsed(index = 5, field = "gx")
    public void setGx(Double gx) {
        super.setGx(gx);
    }

    @Parsed(index = 6)
    public Double getGy() {
        return super.getGy();
    }

    @Parsed(index = 6, field = "gy")
    public void setGy(Double gy) {
        super.setGy(gy);
    }
}
