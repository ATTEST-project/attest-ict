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
 * Criteria class for the {@link com.attest.ict.domain.FlexElVal} entity. This class is used
 * in {@link com.attest.ict.web.rest.FlexElValResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /flex-el-vals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlexElValCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private IntegerFilter hour;

    private IntegerFilter min;

    private DoubleFilter pfmaxUp;

    private DoubleFilter pfmaxDn;

    private DoubleFilter qfmaxUp;

    private DoubleFilter qfmaxDn;

    private LongFilter flexProfileId;

    private Boolean distinct;

    public FlexElValCriteria() {}

    public FlexElValCriteria(FlexElValCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.pfmaxUp = other.pfmaxUp == null ? null : other.pfmaxUp.copy();
        this.pfmaxDn = other.pfmaxDn == null ? null : other.pfmaxDn.copy();
        this.qfmaxUp = other.qfmaxUp == null ? null : other.qfmaxUp.copy();
        this.qfmaxDn = other.qfmaxDn == null ? null : other.qfmaxDn.copy();
        this.flexProfileId = other.flexProfileId == null ? null : other.flexProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FlexElValCriteria copy() {
        return new FlexElValCriteria(this);
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

    public IntegerFilter getHour() {
        return hour;
    }

    public IntegerFilter hour() {
        if (hour == null) {
            hour = new IntegerFilter();
        }
        return hour;
    }

    public void setHour(IntegerFilter hour) {
        this.hour = hour;
    }

    public IntegerFilter getMin() {
        return min;
    }

    public IntegerFilter min() {
        if (min == null) {
            min = new IntegerFilter();
        }
        return min;
    }

    public void setMin(IntegerFilter min) {
        this.min = min;
    }

    public DoubleFilter getPfmaxUp() {
        return pfmaxUp;
    }

    public DoubleFilter pfmaxUp() {
        if (pfmaxUp == null) {
            pfmaxUp = new DoubleFilter();
        }
        return pfmaxUp;
    }

    public void setPfmaxUp(DoubleFilter pfmaxUp) {
        this.pfmaxUp = pfmaxUp;
    }

    public DoubleFilter getPfmaxDn() {
        return pfmaxDn;
    }

    public DoubleFilter pfmaxDn() {
        if (pfmaxDn == null) {
            pfmaxDn = new DoubleFilter();
        }
        return pfmaxDn;
    }

    public void setPfmaxDn(DoubleFilter pfmaxDn) {
        this.pfmaxDn = pfmaxDn;
    }

    public DoubleFilter getQfmaxUp() {
        return qfmaxUp;
    }

    public DoubleFilter qfmaxUp() {
        if (qfmaxUp == null) {
            qfmaxUp = new DoubleFilter();
        }
        return qfmaxUp;
    }

    public void setQfmaxUp(DoubleFilter qfmaxUp) {
        this.qfmaxUp = qfmaxUp;
    }

    public DoubleFilter getQfmaxDn() {
        return qfmaxDn;
    }

    public DoubleFilter qfmaxDn() {
        if (qfmaxDn == null) {
            qfmaxDn = new DoubleFilter();
        }
        return qfmaxDn;
    }

    public void setQfmaxDn(DoubleFilter qfmaxDn) {
        this.qfmaxDn = qfmaxDn;
    }

    public LongFilter getFlexProfileId() {
        return flexProfileId;
    }

    public LongFilter flexProfileId() {
        if (flexProfileId == null) {
            flexProfileId = new LongFilter();
        }
        return flexProfileId;
    }

    public void setFlexProfileId(LongFilter flexProfileId) {
        this.flexProfileId = flexProfileId;
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
        final FlexElValCriteria that = (FlexElValCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(min, that.min) &&
            Objects.equals(pfmaxUp, that.pfmaxUp) &&
            Objects.equals(pfmaxDn, that.pfmaxDn) &&
            Objects.equals(qfmaxUp, that.qfmaxUp) &&
            Objects.equals(qfmaxDn, that.qfmaxDn) &&
            Objects.equals(flexProfileId, that.flexProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busNum, hour, min, pfmaxUp, pfmaxDn, qfmaxUp, qfmaxDn, flexProfileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexElValCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (pfmaxUp != null ? "pfmaxUp=" + pfmaxUp + ", " : "") +
            (pfmaxDn != null ? "pfmaxDn=" + pfmaxDn + ", " : "") +
            (qfmaxUp != null ? "qfmaxUp=" + qfmaxUp + ", " : "") +
            (qfmaxDn != null ? "qfmaxDn=" + qfmaxDn + ", " : "") +
            (flexProfileId != null ? "flexProfileId=" + flexProfileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
