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
 * Criteria class for the {@link com.attest.ict.domain.Branch} entity. This class is used
 * in {@link com.attest.ict.web.rest.BranchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /branches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BranchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fbus;

    private LongFilter tbus;

    private DoubleFilter r;

    private DoubleFilter x;

    private DoubleFilter b;

    private DoubleFilter ratea;

    private DoubleFilter rateb;

    private DoubleFilter ratec;

    private DoubleFilter tapRatio;

    private DoubleFilter angle;

    private IntegerFilter status;

    private IntegerFilter angmin;

    private IntegerFilter angmax;

    private LongFilter transfElValId;

    private LongFilter branchElValId;

    private LongFilter branchExtensionId;

    private LongFilter networkId;

    private Boolean distinct;

    public BranchCriteria() {}

    public BranchCriteria(BranchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fbus = other.fbus == null ? null : other.fbus.copy();
        this.tbus = other.tbus == null ? null : other.tbus.copy();
        this.r = other.r == null ? null : other.r.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.b = other.b == null ? null : other.b.copy();
        this.ratea = other.ratea == null ? null : other.ratea.copy();
        this.rateb = other.rateb == null ? null : other.rateb.copy();
        this.ratec = other.ratec == null ? null : other.ratec.copy();
        this.tapRatio = other.tapRatio == null ? null : other.tapRatio.copy();
        this.angle = other.angle == null ? null : other.angle.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.angmin = other.angmin == null ? null : other.angmin.copy();
        this.angmax = other.angmax == null ? null : other.angmax.copy();
        this.transfElValId = other.transfElValId == null ? null : other.transfElValId.copy();
        this.branchElValId = other.branchElValId == null ? null : other.branchElValId.copy();
        this.branchExtensionId = other.branchExtensionId == null ? null : other.branchExtensionId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BranchCriteria copy() {
        return new BranchCriteria(this);
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

    public LongFilter getFbus() {
        return fbus;
    }

    public LongFilter fbus() {
        if (fbus == null) {
            fbus = new LongFilter();
        }
        return fbus;
    }

    public void setFbus(LongFilter fbus) {
        this.fbus = fbus;
    }

    public LongFilter getTbus() {
        return tbus;
    }

    public LongFilter tbus() {
        if (tbus == null) {
            tbus = new LongFilter();
        }
        return tbus;
    }

    public void setTbus(LongFilter tbus) {
        this.tbus = tbus;
    }

    public DoubleFilter getR() {
        return r;
    }

    public DoubleFilter r() {
        if (r == null) {
            r = new DoubleFilter();
        }
        return r;
    }

    public void setR(DoubleFilter r) {
        this.r = r;
    }

    public DoubleFilter getX() {
        return x;
    }

    public DoubleFilter x() {
        if (x == null) {
            x = new DoubleFilter();
        }
        return x;
    }

    public void setX(DoubleFilter x) {
        this.x = x;
    }

    public DoubleFilter getB() {
        return b;
    }

    public DoubleFilter b() {
        if (b == null) {
            b = new DoubleFilter();
        }
        return b;
    }

    public void setB(DoubleFilter b) {
        this.b = b;
    }

    public DoubleFilter getRatea() {
        return ratea;
    }

    public DoubleFilter ratea() {
        if (ratea == null) {
            ratea = new DoubleFilter();
        }
        return ratea;
    }

    public void setRatea(DoubleFilter ratea) {
        this.ratea = ratea;
    }

    public DoubleFilter getRateb() {
        return rateb;
    }

    public DoubleFilter rateb() {
        if (rateb == null) {
            rateb = new DoubleFilter();
        }
        return rateb;
    }

    public void setRateb(DoubleFilter rateb) {
        this.rateb = rateb;
    }

    public DoubleFilter getRatec() {
        return ratec;
    }

    public DoubleFilter ratec() {
        if (ratec == null) {
            ratec = new DoubleFilter();
        }
        return ratec;
    }

    public void setRatec(DoubleFilter ratec) {
        this.ratec = ratec;
    }

    public DoubleFilter getTapRatio() {
        return tapRatio;
    }

    public DoubleFilter tapRatio() {
        if (tapRatio == null) {
            tapRatio = new DoubleFilter();
        }
        return tapRatio;
    }

    public void setTapRatio(DoubleFilter tapRatio) {
        this.tapRatio = tapRatio;
    }

    public DoubleFilter getAngle() {
        return angle;
    }

    public DoubleFilter angle() {
        if (angle == null) {
            angle = new DoubleFilter();
        }
        return angle;
    }

    public void setAngle(DoubleFilter angle) {
        this.angle = angle;
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

    public IntegerFilter getAngmin() {
        return angmin;
    }

    public IntegerFilter angmin() {
        if (angmin == null) {
            angmin = new IntegerFilter();
        }
        return angmin;
    }

    public void setAngmin(IntegerFilter angmin) {
        this.angmin = angmin;
    }

    public IntegerFilter getAngmax() {
        return angmax;
    }

    public IntegerFilter angmax() {
        if (angmax == null) {
            angmax = new IntegerFilter();
        }
        return angmax;
    }

    public void setAngmax(IntegerFilter angmax) {
        this.angmax = angmax;
    }

    public LongFilter getTransfElValId() {
        return transfElValId;
    }

    public LongFilter transfElValId() {
        if (transfElValId == null) {
            transfElValId = new LongFilter();
        }
        return transfElValId;
    }

    public void setTransfElValId(LongFilter transfElValId) {
        this.transfElValId = transfElValId;
    }

    public LongFilter getBranchElValId() {
        return branchElValId;
    }

    public LongFilter branchElValId() {
        if (branchElValId == null) {
            branchElValId = new LongFilter();
        }
        return branchElValId;
    }

    public void setBranchElValId(LongFilter branchElValId) {
        this.branchElValId = branchElValId;
    }

    public LongFilter getBranchExtensionId() {
        return branchExtensionId;
    }

    public LongFilter branchExtensionId() {
        if (branchExtensionId == null) {
            branchExtensionId = new LongFilter();
        }
        return branchExtensionId;
    }

    public void setBranchExtensionId(LongFilter branchExtensionId) {
        this.branchExtensionId = branchExtensionId;
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
        final BranchCriteria that = (BranchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fbus, that.fbus) &&
            Objects.equals(tbus, that.tbus) &&
            Objects.equals(r, that.r) &&
            Objects.equals(x, that.x) &&
            Objects.equals(b, that.b) &&
            Objects.equals(ratea, that.ratea) &&
            Objects.equals(rateb, that.rateb) &&
            Objects.equals(ratec, that.ratec) &&
            Objects.equals(tapRatio, that.tapRatio) &&
            Objects.equals(angle, that.angle) &&
            Objects.equals(status, that.status) &&
            Objects.equals(angmin, that.angmin) &&
            Objects.equals(angmax, that.angmax) &&
            Objects.equals(transfElValId, that.transfElValId) &&
            Objects.equals(branchElValId, that.branchElValId) &&
            Objects.equals(branchExtensionId, that.branchExtensionId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fbus,
            tbus,
            r,
            x,
            b,
            ratea,
            rateb,
            ratec,
            tapRatio,
            angle,
            status,
            angmin,
            angmax,
            transfElValId,
            branchElValId,
            branchExtensionId,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fbus != null ? "fbus=" + fbus + ", " : "") +
            (tbus != null ? "tbus=" + tbus + ", " : "") +
            (r != null ? "r=" + r + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (b != null ? "b=" + b + ", " : "") +
            (ratea != null ? "ratea=" + ratea + ", " : "") +
            (rateb != null ? "rateb=" + rateb + ", " : "") +
            (ratec != null ? "ratec=" + ratec + ", " : "") +
            (tapRatio != null ? "tapRatio=" + tapRatio + ", " : "") +
            (angle != null ? "angle=" + angle + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (angmin != null ? "angmin=" + angmin + ", " : "") +
            (angmax != null ? "angmax=" + angmax + ", " : "") +
            (transfElValId != null ? "transfElValId=" + transfElValId + ", " : "") +
            (branchElValId != null ? "branchElValId=" + branchElValId + ", " : "") +
            (branchExtensionId != null ? "branchExtensionId=" + branchExtensionId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
