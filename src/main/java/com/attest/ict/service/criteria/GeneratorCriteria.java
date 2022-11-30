package com.attest.ict.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.attest.ict.domain.Generator} entity. This class is used
 * in {@link com.attest.ict.web.rest.GeneratorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /generators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeneratorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private DoubleFilter pg;

    private DoubleFilter qg;

    private DoubleFilter qmax;

    private DoubleFilter qmin;

    private DoubleFilter vg;

    private DoubleFilter mBase;

    private IntegerFilter status;

    private DoubleFilter pmax;

    private DoubleFilter pmin;

    private DoubleFilter pc1;

    private DoubleFilter pc2;

    private DoubleFilter qc1min;

    private DoubleFilter qc1max;

    private DoubleFilter qc2min;

    private DoubleFilter qc2max;

    private DoubleFilter rampAgc;

    private DoubleFilter ramp10;

    private DoubleFilter ramp30;

    private DoubleFilter rampQ;

    private LongFilter apf;

    private LongFilter genElValId;

    private LongFilter generatorExtensionId;

    private LongFilter genTagId;

    private LongFilter genCostId;

    private LongFilter networkId;

    private Boolean distinct;

    public GeneratorCriteria() {}

    public GeneratorCriteria(GeneratorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.pg = other.pg == null ? null : other.pg.copy();
        this.qg = other.qg == null ? null : other.qg.copy();
        this.qmax = other.qmax == null ? null : other.qmax.copy();
        this.qmin = other.qmin == null ? null : other.qmin.copy();
        this.vg = other.vg == null ? null : other.vg.copy();
        this.mBase = other.mBase == null ? null : other.mBase.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.pmax = other.pmax == null ? null : other.pmax.copy();
        this.pmin = other.pmin == null ? null : other.pmin.copy();
        this.pc1 = other.pc1 == null ? null : other.pc1.copy();
        this.pc2 = other.pc2 == null ? null : other.pc2.copy();
        this.qc1min = other.qc1min == null ? null : other.qc1min.copy();
        this.qc1max = other.qc1max == null ? null : other.qc1max.copy();
        this.qc2min = other.qc2min == null ? null : other.qc2min.copy();
        this.qc2max = other.qc2max == null ? null : other.qc2max.copy();
        this.rampAgc = other.rampAgc == null ? null : other.rampAgc.copy();
        this.ramp10 = other.ramp10 == null ? null : other.ramp10.copy();
        this.ramp30 = other.ramp30 == null ? null : other.ramp30.copy();
        this.rampQ = other.rampQ == null ? null : other.rampQ.copy();
        this.apf = other.apf == null ? null : other.apf.copy();
        this.genElValId = other.genElValId == null ? null : other.genElValId.copy();
        this.generatorExtensionId = other.generatorExtensionId == null ? null : other.generatorExtensionId.copy();
        this.genTagId = other.genTagId == null ? null : other.genTagId.copy();
        this.genCostId = other.genCostId == null ? null : other.genCostId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GeneratorCriteria copy() {
        return new GeneratorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getBusNum() {
        return busNum;
    }

    public LongFilter busNum() {
        if (busNum == null) {
            busNum = new LongFilter();
        }
        return busNum;
    }

    public void setBusNum(LongFilter busNum) {
        this.busNum = busNum;
    }

    public DoubleFilter getPg() {
        return pg;
    }

    public DoubleFilter pg() {
        if (pg == null) {
            pg = new DoubleFilter();
        }
        return pg;
    }

    public void setPg(DoubleFilter pg) {
        this.pg = pg;
    }

    public DoubleFilter getQg() {
        return qg;
    }

    public DoubleFilter qg() {
        if (qg == null) {
            qg = new DoubleFilter();
        }
        return qg;
    }

    public void setQg(DoubleFilter qg) {
        this.qg = qg;
    }

    public DoubleFilter getQmax() {
        return qmax;
    }

    public DoubleFilter qmax() {
        if (qmax == null) {
            qmax = new DoubleFilter();
        }
        return qmax;
    }

    public void setQmax(DoubleFilter qmax) {
        this.qmax = qmax;
    }

    public DoubleFilter getQmin() {
        return qmin;
    }

    public DoubleFilter qmin() {
        if (qmin == null) {
            qmin = new DoubleFilter();
        }
        return qmin;
    }

    public void setQmin(DoubleFilter qmin) {
        this.qmin = qmin;
    }

    public DoubleFilter getVg() {
        return vg;
    }

    public DoubleFilter vg() {
        if (vg == null) {
            vg = new DoubleFilter();
        }
        return vg;
    }

    public void setVg(DoubleFilter vg) {
        this.vg = vg;
    }

    public DoubleFilter getmBase() {
        return mBase;
    }

    public DoubleFilter mBase() {
        if (mBase == null) {
            mBase = new DoubleFilter();
        }
        return mBase;
    }

    public void setmBase(DoubleFilter mBase) {
        this.mBase = mBase;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public DoubleFilter getPmax() {
        return pmax;
    }

    public DoubleFilter pmax() {
        if (pmax == null) {
            pmax = new DoubleFilter();
        }
        return pmax;
    }

    public void setPmax(DoubleFilter pmax) {
        this.pmax = pmax;
    }

    public DoubleFilter getPmin() {
        return pmin;
    }

    public DoubleFilter pmin() {
        if (pmin == null) {
            pmin = new DoubleFilter();
        }
        return pmin;
    }

    public void setPmin(DoubleFilter pmin) {
        this.pmin = pmin;
    }

    public DoubleFilter getPc1() {
        return pc1;
    }

    public DoubleFilter pc1() {
        if (pc1 == null) {
            pc1 = new DoubleFilter();
        }
        return pc1;
    }

    public void setPc1(DoubleFilter pc1) {
        this.pc1 = pc1;
    }

    public DoubleFilter getPc2() {
        return pc2;
    }

    public DoubleFilter pc2() {
        if (pc2 == null) {
            pc2 = new DoubleFilter();
        }
        return pc2;
    }

    public void setPc2(DoubleFilter pc2) {
        this.pc2 = pc2;
    }

    public DoubleFilter getQc1min() {
        return qc1min;
    }

    public DoubleFilter qc1min() {
        if (qc1min == null) {
            qc1min = new DoubleFilter();
        }
        return qc1min;
    }

    public void setQc1min(DoubleFilter qc1min) {
        this.qc1min = qc1min;
    }

    public DoubleFilter getQc1max() {
        return qc1max;
    }

    public DoubleFilter qc1max() {
        if (qc1max == null) {
            qc1max = new DoubleFilter();
        }
        return qc1max;
    }

    public void setQc1max(DoubleFilter qc1max) {
        this.qc1max = qc1max;
    }

    public DoubleFilter getQc2min() {
        return qc2min;
    }

    public DoubleFilter qc2min() {
        if (qc2min == null) {
            qc2min = new DoubleFilter();
        }
        return qc2min;
    }

    public void setQc2min(DoubleFilter qc2min) {
        this.qc2min = qc2min;
    }

    public DoubleFilter getQc2max() {
        return qc2max;
    }

    public DoubleFilter qc2max() {
        if (qc2max == null) {
            qc2max = new DoubleFilter();
        }
        return qc2max;
    }

    public void setQc2max(DoubleFilter qc2max) {
        this.qc2max = qc2max;
    }

    public DoubleFilter getRampAgc() {
        return rampAgc;
    }

    public DoubleFilter rampAgc() {
        if (rampAgc == null) {
            rampAgc = new DoubleFilter();
        }
        return rampAgc;
    }

    public void setRampAgc(DoubleFilter rampAgc) {
        this.rampAgc = rampAgc;
    }

    public DoubleFilter getRamp10() {
        return ramp10;
    }

    public DoubleFilter ramp10() {
        if (ramp10 == null) {
            ramp10 = new DoubleFilter();
        }
        return ramp10;
    }

    public void setRamp10(DoubleFilter ramp10) {
        this.ramp10 = ramp10;
    }

    public DoubleFilter getRamp30() {
        return ramp30;
    }

    public DoubleFilter ramp30() {
        if (ramp30 == null) {
            ramp30 = new DoubleFilter();
        }
        return ramp30;
    }

    public void setRamp30(DoubleFilter ramp30) {
        this.ramp30 = ramp30;
    }

    public DoubleFilter getRampQ() {
        return rampQ;
    }

    public DoubleFilter rampQ() {
        if (rampQ == null) {
            rampQ = new DoubleFilter();
        }
        return rampQ;
    }

    public void setRampQ(DoubleFilter rampQ) {
        this.rampQ = rampQ;
    }

    public LongFilter getApf() {
        return apf;
    }

    public LongFilter apf() {
        if (apf == null) {
            apf = new LongFilter();
        }
        return apf;
    }

    public void setApf(LongFilter apf) {
        this.apf = apf;
    }

    public LongFilter getGenElValId() {
        return genElValId;
    }

    public LongFilter genElValId() {
        if (genElValId == null) {
            genElValId = new LongFilter();
        }
        return genElValId;
    }

    public void setGenElValId(LongFilter genElValId) {
        this.genElValId = genElValId;
    }

    public LongFilter getGeneratorExtensionId() {
        return generatorExtensionId;
    }

    public LongFilter generatorExtensionId() {
        if (generatorExtensionId == null) {
            generatorExtensionId = new LongFilter();
        }
        return generatorExtensionId;
    }

    public void setGeneratorExtensionId(LongFilter generatorExtensionId) {
        this.generatorExtensionId = generatorExtensionId;
    }

    public LongFilter getGenTagId() {
        return genTagId;
    }

    public LongFilter genTagId() {
        if (genTagId == null) {
            genTagId = new LongFilter();
        }
        return genTagId;
    }

    public void setGenTagId(LongFilter genTagId) {
        this.genTagId = genTagId;
    }

    public LongFilter getGenCostId() {
        return genCostId;
    }

    public LongFilter genCostId() {
        if (genCostId == null) {
            genCostId = new LongFilter();
        }
        return genCostId;
    }

    public void setGenCostId(LongFilter genCostId) {
        this.genCostId = genCostId;
    }

    public LongFilter getNetworkId() {
        return networkId;
    }

    public LongFilter networkId() {
        if (networkId == null) {
            networkId = new LongFilter();
        }
        return networkId;
    }

    public void setNetworkId(LongFilter networkId) {
        this.networkId = networkId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeneratorCriteria that = (GeneratorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(pg, that.pg) &&
            Objects.equals(qg, that.qg) &&
            Objects.equals(qmax, that.qmax) &&
            Objects.equals(qmin, that.qmin) &&
            Objects.equals(vg, that.vg) &&
            Objects.equals(mBase, that.mBase) &&
            Objects.equals(status, that.status) &&
            Objects.equals(pmax, that.pmax) &&
            Objects.equals(pmin, that.pmin) &&
            Objects.equals(pc1, that.pc1) &&
            Objects.equals(pc2, that.pc2) &&
            Objects.equals(qc1min, that.qc1min) &&
            Objects.equals(qc1max, that.qc1max) &&
            Objects.equals(qc2min, that.qc2min) &&
            Objects.equals(qc2max, that.qc2max) &&
            Objects.equals(rampAgc, that.rampAgc) &&
            Objects.equals(ramp10, that.ramp10) &&
            Objects.equals(ramp30, that.ramp30) &&
            Objects.equals(rampQ, that.rampQ) &&
            Objects.equals(apf, that.apf) &&
            Objects.equals(genElValId, that.genElValId) &&
            Objects.equals(generatorExtensionId, that.generatorExtensionId) &&
            Objects.equals(genTagId, that.genTagId) &&
            Objects.equals(genCostId, that.genCostId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            busNum,
            pg,
            qg,
            qmax,
            qmin,
            vg,
            mBase,
            status,
            pmax,
            pmin,
            pc1,
            pc2,
            qc1min,
            qc1max,
            qc2min,
            qc2max,
            rampAgc,
            ramp10,
            ramp30,
            rampQ,
            apf,
            genElValId,
            generatorExtensionId,
            genTagId,
            genCostId,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneratorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (pg != null ? "pg=" + pg + ", " : "") +
            (qg != null ? "qg=" + qg + ", " : "") +
            (qmax != null ? "qmax=" + qmax + ", " : "") +
            (qmin != null ? "qmin=" + qmin + ", " : "") +
            (vg != null ? "vg=" + vg + ", " : "") +
            (mBase != null ? "mBase=" + mBase + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (pmax != null ? "pmax=" + pmax + ", " : "") +
            (pmin != null ? "pmin=" + pmin + ", " : "") +
            (pc1 != null ? "pc1=" + pc1 + ", " : "") +
            (pc2 != null ? "pc2=" + pc2 + ", " : "") +
            (qc1min != null ? "qc1min=" + qc1min + ", " : "") +
            (qc1max != null ? "qc1max=" + qc1max + ", " : "") +
            (qc2min != null ? "qc2min=" + qc2min + ", " : "") +
            (qc2max != null ? "qc2max=" + qc2max + ", " : "") +
            (rampAgc != null ? "rampAgc=" + rampAgc + ", " : "") +
            (ramp10 != null ? "ramp10=" + ramp10 + ", " : "") +
            (ramp30 != null ? "ramp30=" + ramp30 + ", " : "") +
            (rampQ != null ? "rampQ=" + rampQ + ", " : "") +
            (apf != null ? "apf=" + apf + ", " : "") +
            (genElValId != null ? "genElValId=" + genElValId + ", " : "") +
            (generatorExtensionId != null ? "generatorExtensionId=" + generatorExtensionId + ", " : "") +
            (genTagId != null ? "genTagId=" + genTagId + ", " : "") +
            (genCostId != null ? "genCostId=" + genCostId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
