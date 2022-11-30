package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BranchExtension;
import com.univocity.parsers.annotations.Parsed;

public class BranchExtensionAnnotated extends BranchExtension {

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
    public Double getNominalRatio() {
        return super.getNominalRatio();
    }

    @Parsed(index = 5, field = "nominalRatio")
    public void setNominalRatio(Double nominalRatio) {
        super.setNominalRatio(nominalRatio);
    }

    @Parsed(index = 6)
    public Double getrIp() {
        return super.getrIp();
    }

    @Parsed(index = 6, field = "rIp")
    public void setrIp(Double rIp) {
        super.setrIp(rIp);
    }

    @Parsed(index = 7)
    public Double getrN() {
        return super.getrN();
    }

    @Parsed(index = 7, field = "rN")
    public void setrN(Double rN) {
        super.setrN(rN);
    }

    @Parsed(index = 8)
    public Double getR0() {
        return super.getr0();
    }

    @Parsed(index = 8, field = "r0")
    public void setR0(Double r0) {
        super.setr0(r0);
    }

    @Parsed(index = 9)
    public Double getX0() {
        return super.getx0();
    }

    @Parsed(index = 9, field = "x0")
    public void setX0(Double x0) {
        super.setx0(x0);
    }

    @Parsed(index = 10)
    public Double getB0() {
        return super.getb0();
    }

    @Parsed(index = 10, field = "b0")
    public void setB0(Double b0) {
        super.setb0(b0);
    }

    @Parsed(index = 11)
    public Double getLength() {
        return super.getLength();
    }

    @Parsed(index = 11, field = "length")
    public void setLength(Double length) {
        super.setLength(length);
    }

    @Parsed(index = 12)
    public Integer getNormStat() {
        return super.getNormStat();
    }

    @Parsed(index = 12, field = "normStat")
    public void setNormStat(Integer normStat) {
        super.setNormStat(normStat);
    }
}
