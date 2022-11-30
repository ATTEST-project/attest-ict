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
 * Criteria class for the {@link com.attest.ict.domain.BranchExtension} entity. This class is used
 * in {@link com.attest.ict.web.rest.BranchExtensionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /branch-extensions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BranchExtensionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter stepSize;

    private DoubleFilter actTap;

    private DoubleFilter minTap;

    private DoubleFilter maxTap;

    private DoubleFilter normalTap;

    private DoubleFilter nominalRatio;

    private DoubleFilter rIp;

    private DoubleFilter rN;

    private DoubleFilter r0;

    private DoubleFilter x0;

    private DoubleFilter b0;

    private DoubleFilter length;

    private IntegerFilter normStat;

    private DoubleFilter g;

    private StringFilter mRid;

    private LongFilter branchId;

    private Boolean distinct;

    public BranchExtensionCriteria() {}

    public BranchExtensionCriteria(BranchExtensionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stepSize = other.stepSize == null ? null : other.stepSize.copy();
        this.actTap = other.actTap == null ? null : other.actTap.copy();
        this.minTap = other.minTap == null ? null : other.minTap.copy();
        this.maxTap = other.maxTap == null ? null : other.maxTap.copy();
        this.normalTap = other.normalTap == null ? null : other.normalTap.copy();
        this.nominalRatio = other.nominalRatio == null ? null : other.nominalRatio.copy();
        this.rIp = other.rIp == null ? null : other.rIp.copy();
        this.rN = other.rN == null ? null : other.rN.copy();
        this.r0 = other.r0 == null ? null : other.r0.copy();
        this.x0 = other.x0 == null ? null : other.x0.copy();
        this.b0 = other.b0 == null ? null : other.b0.copy();
        this.length = other.length == null ? null : other.length.copy();
        this.normStat = other.normStat == null ? null : other.normStat.copy();
        this.g = other.g == null ? null : other.g.copy();
        this.mRid = other.mRid == null ? null : other.mRid.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BranchExtensionCriteria copy() {
        return new BranchExtensionCriteria(this);
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

    public DoubleFilter getStepSize() {
        return stepSize;
    }

    public DoubleFilter stepSize() {
        if (stepSize == null) {
            stepSize = new DoubleFilter();
        }
        return stepSize;
    }

    public void setStepSize(DoubleFilter stepSize) {
        this.stepSize = stepSize;
    }

    public DoubleFilter getActTap() {
        return actTap;
    }

    public DoubleFilter actTap() {
        if (actTap == null) {
            actTap = new DoubleFilter();
        }
        return actTap;
    }

    public void setActTap(DoubleFilter actTap) {
        this.actTap = actTap;
    }

    public DoubleFilter getMinTap() {
        return minTap;
    }

    public DoubleFilter minTap() {
        if (minTap == null) {
            minTap = new DoubleFilter();
        }
        return minTap;
    }

    public void setMinTap(DoubleFilter minTap) {
        this.minTap = minTap;
    }

    public DoubleFilter getMaxTap() {
        return maxTap;
    }

    public DoubleFilter maxTap() {
        if (maxTap == null) {
            maxTap = new DoubleFilter();
        }
        return maxTap;
    }

    public void setMaxTap(DoubleFilter maxTap) {
        this.maxTap = maxTap;
    }

    public DoubleFilter getNormalTap() {
        return normalTap;
    }

    public DoubleFilter normalTap() {
        if (normalTap == null) {
            normalTap = new DoubleFilter();
        }
        return normalTap;
    }

    public void setNormalTap(DoubleFilter normalTap) {
        this.normalTap = normalTap;
    }

    public DoubleFilter getNominalRatio() {
        return nominalRatio;
    }

    public DoubleFilter nominalRatio() {
        if (nominalRatio == null) {
            nominalRatio = new DoubleFilter();
        }
        return nominalRatio;
    }

    public void setNominalRatio(DoubleFilter nominalRatio) {
        this.nominalRatio = nominalRatio;
    }

    public DoubleFilter getrIp() {
        return rIp;
    }

    public DoubleFilter rIp() {
        if (rIp == null) {
            rIp = new DoubleFilter();
        }
        return rIp;
    }

    public void setrIp(DoubleFilter rIp) {
        this.rIp = rIp;
    }

    public DoubleFilter getrN() {
        return rN;
    }

    public DoubleFilter rN() {
        if (rN == null) {
            rN = new DoubleFilter();
        }
        return rN;
    }

    public void setrN(DoubleFilter rN) {
        this.rN = rN;
    }

    public DoubleFilter getr0() {
        return r0;
    }

    public DoubleFilter r0() {
        if (r0 == null) {
            r0 = new DoubleFilter();
        }
        return r0;
    }

    public void setr0(DoubleFilter r0) {
        this.r0 = r0;
    }

    public DoubleFilter getx0() {
        return x0;
    }

    public DoubleFilter x0() {
        if (x0 == null) {
            x0 = new DoubleFilter();
        }
        return x0;
    }

    public void setx0(DoubleFilter x0) {
        this.x0 = x0;
    }

    public DoubleFilter getb0() {
        return b0;
    }

    public DoubleFilter b0() {
        if (b0 == null) {
            b0 = new DoubleFilter();
        }
        return b0;
    }

    public void setb0(DoubleFilter b0) {
        this.b0 = b0;
    }

    public DoubleFilter getLength() {
        return length;
    }

    public DoubleFilter length() {
        if (length == null) {
            length = new DoubleFilter();
        }
        return length;
    }

    public void setLength(DoubleFilter length) {
        this.length = length;
    }

    public IntegerFilter getNormStat() {
        return normStat;
    }

    public IntegerFilter normStat() {
        if (normStat == null) {
            normStat = new IntegerFilter();
        }
        return normStat;
    }

    public void setNormStat(IntegerFilter normStat) {
        this.normStat = normStat;
    }

    public DoubleFilter getG() {
        return g;
    }

    public DoubleFilter g() {
        if (g == null) {
            g = new DoubleFilter();
        }
        return g;
    }

    public void setG(DoubleFilter g) {
        this.g = g;
    }

    public StringFilter getmRid() {
        return mRid;
    }

    public StringFilter mRid() {
        if (mRid == null) {
            mRid = new StringFilter();
        }
        return mRid;
    }

    public void setmRid(StringFilter mRid) {
        this.mRid = mRid;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public LongFilter branchId() {
        if (branchId == null) {
            branchId = new LongFilter();
        }
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
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
        final BranchExtensionCriteria that = (BranchExtensionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stepSize, that.stepSize) &&
            Objects.equals(actTap, that.actTap) &&
            Objects.equals(minTap, that.minTap) &&
            Objects.equals(maxTap, that.maxTap) &&
            Objects.equals(normalTap, that.normalTap) &&
            Objects.equals(nominalRatio, that.nominalRatio) &&
            Objects.equals(rIp, that.rIp) &&
            Objects.equals(rN, that.rN) &&
            Objects.equals(r0, that.r0) &&
            Objects.equals(x0, that.x0) &&
            Objects.equals(b0, that.b0) &&
            Objects.equals(length, that.length) &&
            Objects.equals(normStat, that.normStat) &&
            Objects.equals(g, that.g) &&
            Objects.equals(mRid, that.mRid) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stepSize,
            actTap,
            minTap,
            maxTap,
            normalTap,
            nominalRatio,
            rIp,
            rN,
            r0,
            x0,
            b0,
            length,
            normStat,
            g,
            mRid,
            branchId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchExtensionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (stepSize != null ? "stepSize=" + stepSize + ", " : "") +
            (actTap != null ? "actTap=" + actTap + ", " : "") +
            (minTap != null ? "minTap=" + minTap + ", " : "") +
            (maxTap != null ? "maxTap=" + maxTap + ", " : "") +
            (normalTap != null ? "normalTap=" + normalTap + ", " : "") +
            (nominalRatio != null ? "nominalRatio=" + nominalRatio + ", " : "") +
            (rIp != null ? "rIp=" + rIp + ", " : "") +
            (rN != null ? "rN=" + rN + ", " : "") +
            (r0 != null ? "r0=" + r0 + ", " : "") +
            (x0 != null ? "x0=" + x0 + ", " : "") +
            (b0 != null ? "b0=" + b0 + ", " : "") +
            (length != null ? "length=" + length + ", " : "") +
            (normStat != null ? "normStat=" + normStat + ", " : "") +
            (g != null ? "g=" + g + ", " : "") +
            (mRid != null ? "mRid=" + mRid + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
