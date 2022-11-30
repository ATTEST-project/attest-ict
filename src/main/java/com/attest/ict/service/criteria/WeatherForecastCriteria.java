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
 * Criteria class for the {@link com.attest.ict.domain.WeatherForecast} entity. This class is used
 * in {@link com.attest.ict.web.rest.WeatherForecastResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /weather-forecasts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WeatherForecastCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter solarProfile;

    private DoubleFilter outsideTemp;

    private Boolean distinct;

    public WeatherForecastCriteria() {}

    public WeatherForecastCriteria(WeatherForecastCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.solarProfile = other.solarProfile == null ? null : other.solarProfile.copy();
        this.outsideTemp = other.outsideTemp == null ? null : other.outsideTemp.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WeatherForecastCriteria copy() {
        return new WeatherForecastCriteria(this);
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

    public DoubleFilter getSolarProfile() {
        return solarProfile;
    }

    public DoubleFilter solarProfile() {
        if (solarProfile == null) {
            solarProfile = new DoubleFilter();
        }
        return solarProfile;
    }

    public void setSolarProfile(DoubleFilter solarProfile) {
        this.solarProfile = solarProfile;
    }

    public DoubleFilter getOutsideTemp() {
        return outsideTemp;
    }

    public DoubleFilter outsideTemp() {
        if (outsideTemp == null) {
            outsideTemp = new DoubleFilter();
        }
        return outsideTemp;
    }

    public void setOutsideTemp(DoubleFilter outsideTemp) {
        this.outsideTemp = outsideTemp;
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
        final WeatherForecastCriteria that = (WeatherForecastCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(solarProfile, that.solarProfile) &&
            Objects.equals(outsideTemp, that.outsideTemp) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, solarProfile, outsideTemp, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeatherForecastCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (solarProfile != null ? "solarProfile=" + solarProfile + ", " : "") +
            (outsideTemp != null ? "outsideTemp=" + outsideTemp + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
