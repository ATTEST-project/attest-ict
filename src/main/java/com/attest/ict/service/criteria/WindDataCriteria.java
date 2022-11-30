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
 * Criteria class for the {@link com.attest.ict.domain.WindData} entity. This class is used
 * in {@link com.attest.ict.web.rest.WindDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wind-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WindDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter windSpeed;

    private IntegerFilter hour;

    private Boolean distinct;

    public WindDataCriteria() {}

    public WindDataCriteria(WindDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.windSpeed = other.windSpeed == null ? null : other.windSpeed.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WindDataCriteria copy() {
        return new WindDataCriteria(this);
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

    public DoubleFilter getWindSpeed() {
        return windSpeed;
    }

    public DoubleFilter windSpeed() {
        if (windSpeed == null) {
            windSpeed = new DoubleFilter();
        }
        return windSpeed;
    }

    public void setWindSpeed(DoubleFilter windSpeed) {
        this.windSpeed = windSpeed;
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
        final WindDataCriteria that = (WindDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(windSpeed, that.windSpeed) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, windSpeed, hour, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WindDataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (windSpeed != null ? "windSpeed=" + windSpeed + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
