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
 * Criteria class for the {@link com.attest.ict.domain.BusExtension} entity. This class is used
 * in {@link com.attest.ict.web.rest.BusExtensionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bus-extensions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusExtensionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter hasGen;

    private IntegerFilter isLoad;

    private DoubleFilter snomMva;

    private DoubleFilter sx;

    private DoubleFilter sy;

    private DoubleFilter gx;

    private DoubleFilter gy;

    private IntegerFilter status;

    private IntegerFilter incrementCost;

    private IntegerFilter decrementCost;

    private StringFilter mRid;

    private LongFilter busId;

    private Boolean distinct;

    public BusExtensionCriteria() {}

    public BusExtensionCriteria(BusExtensionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.hasGen = other.hasGen == null ? null : other.hasGen.copy();
        this.isLoad = other.isLoad == null ? null : other.isLoad.copy();
        this.snomMva = other.snomMva == null ? null : other.snomMva.copy();
        this.sx = other.sx == null ? null : other.sx.copy();
        this.sy = other.sy == null ? null : other.sy.copy();
        this.gx = other.gx == null ? null : other.gx.copy();
        this.gy = other.gy == null ? null : other.gy.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.incrementCost = other.incrementCost == null ? null : other.incrementCost.copy();
        this.decrementCost = other.decrementCost == null ? null : other.decrementCost.copy();
        this.mRid = other.mRid == null ? null : other.mRid.copy();
        this.busId = other.busId == null ? null : other.busId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusExtensionCriteria copy() {
        return new BusExtensionCriteria(this);
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

    public IntegerFilter getHasGen() {
        return hasGen;
    }

    public IntegerFilter hasGen() {
        if (hasGen == null) {
            hasGen = new IntegerFilter();
        }
        return hasGen;
    }

    public void setHasGen(IntegerFilter hasGen) {
        this.hasGen = hasGen;
    }

    public IntegerFilter getIsLoad() {
        return isLoad;
    }

    public IntegerFilter isLoad() {
        if (isLoad == null) {
            isLoad = new IntegerFilter();
        }
        return isLoad;
    }

    public void setIsLoad(IntegerFilter isLoad) {
        this.isLoad = isLoad;
    }

    public DoubleFilter getSnomMva() {
        return snomMva;
    }

    public DoubleFilter snomMva() {
        if (snomMva == null) {
            snomMva = new DoubleFilter();
        }
        return snomMva;
    }

    public void setSnomMva(DoubleFilter snomMva) {
        this.snomMva = snomMva;
    }

    public DoubleFilter getSx() {
        return sx;
    }

    public DoubleFilter sx() {
        if (sx == null) {
            sx = new DoubleFilter();
        }
        return sx;
    }

    public void setSx(DoubleFilter sx) {
        this.sx = sx;
    }

    public DoubleFilter getSy() {
        return sy;
    }

    public DoubleFilter sy() {
        if (sy == null) {
            sy = new DoubleFilter();
        }
        return sy;
    }

    public void setSy(DoubleFilter sy) {
        this.sy = sy;
    }

    public DoubleFilter getGx() {
        return gx;
    }

    public DoubleFilter gx() {
        if (gx == null) {
            gx = new DoubleFilter();
        }
        return gx;
    }

    public void setGx(DoubleFilter gx) {
        this.gx = gx;
    }

    public DoubleFilter getGy() {
        return gy;
    }

    public DoubleFilter gy() {
        if (gy == null) {
            gy = new DoubleFilter();
        }
        return gy;
    }

    public void setGy(DoubleFilter gy) {
        this.gy = gy;
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

    public IntegerFilter getIncrementCost() {
        return incrementCost;
    }

    public IntegerFilter incrementCost() {
        if (incrementCost == null) {
            incrementCost = new IntegerFilter();
        }
        return incrementCost;
    }

    public void setIncrementCost(IntegerFilter incrementCost) {
        this.incrementCost = incrementCost;
    }

    public IntegerFilter getDecrementCost() {
        return decrementCost;
    }

    public IntegerFilter decrementCost() {
        if (decrementCost == null) {
            decrementCost = new IntegerFilter();
        }
        return decrementCost;
    }

    public void setDecrementCost(IntegerFilter decrementCost) {
        this.decrementCost = decrementCost;
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

    public LongFilter getBusId() {
        return busId;
    }

    public LongFilter busId() {
        if (busId == null) {
            busId = new LongFilter();
        }
        return busId;
    }

    public void setBusId(LongFilter busId) {
        this.busId = busId;
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
        final BusExtensionCriteria that = (BusExtensionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(hasGen, that.hasGen) &&
            Objects.equals(isLoad, that.isLoad) &&
            Objects.equals(snomMva, that.snomMva) &&
            Objects.equals(sx, that.sx) &&
            Objects.equals(sy, that.sy) &&
            Objects.equals(gx, that.gx) &&
            Objects.equals(gy, that.gy) &&
            Objects.equals(status, that.status) &&
            Objects.equals(incrementCost, that.incrementCost) &&
            Objects.equals(decrementCost, that.decrementCost) &&
            Objects.equals(mRid, that.mRid) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hasGen, isLoad, snomMva, sx, sy, gx, gy, status, incrementCost, decrementCost, mRid, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusExtensionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (hasGen != null ? "hasGen=" + hasGen + ", " : "") +
            (isLoad != null ? "isLoad=" + isLoad + ", " : "") +
            (snomMva != null ? "snomMva=" + snomMva + ", " : "") +
            (sx != null ? "sx=" + sx + ", " : "") +
            (sy != null ? "sy=" + sy + ", " : "") +
            (gx != null ? "gx=" + gx + ", " : "") +
            (gy != null ? "gy=" + gy + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (incrementCost != null ? "incrementCost=" + incrementCost + ", " : "") +
            (decrementCost != null ? "decrementCost=" + decrementCost + ", " : "") +
            (mRid != null ? "mRid=" + mRid + ", " : "") +
            (busId != null ? "busId=" + busId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
