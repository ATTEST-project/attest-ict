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
 * Criteria class for the {@link com.attest.ict.domain.Transformer} entity. This class is used
 * in {@link com.attest.ict.web.rest.TransformerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transformers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransformerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fbus;

    private LongFilter tbus;

    private DoubleFilter min;

    private DoubleFilter max;

    private IntegerFilter totalTaps;

    private IntegerFilter tap;

    private IntegerFilter manufactureYear;

    private IntegerFilter commissioningYear;

    private LongFilter networkId;

    private Boolean distinct;

    public TransformerCriteria() {}

    public TransformerCriteria(TransformerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fbus = other.fbus == null ? null : other.fbus.copy();
        this.tbus = other.tbus == null ? null : other.tbus.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.max = other.max == null ? null : other.max.copy();
        this.totalTaps = other.totalTaps == null ? null : other.totalTaps.copy();
        this.tap = other.tap == null ? null : other.tap.copy();
        this.manufactureYear = other.manufactureYear == null ? null : other.manufactureYear.copy();
        this.commissioningYear = other.commissioningYear == null ? null : other.commissioningYear.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransformerCriteria copy() {
        return new TransformerCriteria(this);
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

    public DoubleFilter getMin() {
        return min;
    }

    public DoubleFilter min() {
        if (min == null) {
            min = new DoubleFilter();
        }
        return min;
    }

    public void setMin(DoubleFilter min) {
        this.min = min;
    }

    public DoubleFilter getMax() {
        return max;
    }

    public DoubleFilter max() {
        if (max == null) {
            max = new DoubleFilter();
        }
        return max;
    }

    public void setMax(DoubleFilter max) {
        this.max = max;
    }

    public IntegerFilter getTotalTaps() {
        return totalTaps;
    }

    public IntegerFilter totalTaps() {
        if (totalTaps == null) {
            totalTaps = new IntegerFilter();
        }
        return totalTaps;
    }

    public void setTotalTaps(IntegerFilter totalTaps) {
        this.totalTaps = totalTaps;
    }

    public IntegerFilter getTap() {
        return tap;
    }

    public IntegerFilter tap() {
        if (tap == null) {
            tap = new IntegerFilter();
        }
        return tap;
    }

    public void setTap(IntegerFilter tap) {
        this.tap = tap;
    }

    public IntegerFilter getManufactureYear() {
        return manufactureYear;
    }

    public IntegerFilter manufactureYear() {
        if (manufactureYear == null) {
            manufactureYear = new IntegerFilter();
        }
        return manufactureYear;
    }

    public void setManufactureYear(IntegerFilter manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public IntegerFilter getCommissioningYear() {
        return commissioningYear;
    }

    public IntegerFilter commissioningYear() {
        if (commissioningYear == null) {
            commissioningYear = new IntegerFilter();
        }
        return commissioningYear;
    }

    public void setCommissioningYear(IntegerFilter commissioningYear) {
        this.commissioningYear = commissioningYear;
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
        final TransformerCriteria that = (TransformerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fbus, that.fbus) &&
            Objects.equals(tbus, that.tbus) &&
            Objects.equals(min, that.min) &&
            Objects.equals(max, that.max) &&
            Objects.equals(totalTaps, that.totalTaps) &&
            Objects.equals(tap, that.tap) &&
            Objects.equals(manufactureYear, that.manufactureYear) &&
            Objects.equals(commissioningYear, that.commissioningYear) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fbus, tbus, min, max, totalTaps, tap, manufactureYear, commissioningYear, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fbus != null ? "fbus=" + fbus + ", " : "") +
            (tbus != null ? "tbus=" + tbus + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (max != null ? "max=" + max + ", " : "") +
            (totalTaps != null ? "totalTaps=" + totalTaps + ", " : "") +
            (tap != null ? "tap=" + tap + ", " : "") +
            (manufactureYear != null ? "manufactureYear=" + manufactureYear + ", " : "") +
            (commissioningYear != null ? "commissioningYear=" + commissioningYear + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
