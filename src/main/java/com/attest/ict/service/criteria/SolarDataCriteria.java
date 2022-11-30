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
 * Criteria class for the {@link com.attest.ict.domain.SolarData} entity. This class is used
 * in {@link com.attest.ict.web.rest.SolarDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /solar-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SolarDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter p;

    private IntegerFilter hour;

    private Boolean distinct;

    public SolarDataCriteria() {}

    public SolarDataCriteria(SolarDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.p = other.p == null ? null : other.p.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SolarDataCriteria copy() {
        return new SolarDataCriteria(this);
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

    public DoubleFilter getP() {
        return p;
    }

    public DoubleFilter p() {
        if (p == null) {
            p = new DoubleFilter();
        }
        return p;
    }

    public void setP(DoubleFilter p) {
        this.p = p;
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
        final SolarDataCriteria that = (SolarDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(p, that.p) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, p, hour, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolarDataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (p != null ? "p=" + p + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
