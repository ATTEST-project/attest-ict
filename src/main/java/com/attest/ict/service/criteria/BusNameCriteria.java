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
 * Criteria class for the {@link com.attest.ict.domain.BusName} entity. This class is used
 * in {@link com.attest.ict.web.rest.BusNameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bus-names?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusNameCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter busName;

    private LongFilter busId;

    private Boolean distinct;

    public BusNameCriteria() {}

    public BusNameCriteria(BusNameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busName = other.busName == null ? null : other.busName.copy();
        this.busId = other.busId == null ? null : other.busId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusNameCriteria copy() {
        return new BusNameCriteria(this);
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

    public StringFilter getBusName() {
        return busName;
    }

    public StringFilter busName() {
        if (busName == null) {
            busName = new StringFilter();
        }
        return busName;
    }

    public void setBusName(StringFilter busName) {
        this.busName = busName;
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
        final BusNameCriteria that = (BusNameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busName, that.busName) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busName, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusNameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busName != null ? "busName=" + busName + ", " : "") +
            (busId != null ? "busId=" + busId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
