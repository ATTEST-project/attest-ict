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
 * Criteria class for the {@link com.attest.ict.domain.BusCoordinate} entity. This class is used
 * in {@link com.attest.ict.web.rest.BusCoordinateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bus-coordinates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusCoordinateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter x;

    private DoubleFilter y;

    private LongFilter busId;

    private Boolean distinct;

    public BusCoordinateCriteria() {}

    public BusCoordinateCriteria(BusCoordinateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.busId = other.busId == null ? null : other.busId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusCoordinateCriteria copy() {
        return new BusCoordinateCriteria(this);
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

    public DoubleFilter getY() {
        return y;
    }

    public DoubleFilter y() {
        if (y == null) {
            y = new DoubleFilter();
        }
        return y;
    }

    public void setY(DoubleFilter y) {
        this.y = y;
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
        final BusCoordinateCriteria that = (BusCoordinateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(x, that.x) &&
            Objects.equals(y, that.y) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusCoordinateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (y != null ? "y=" + y + ", " : "") +
            (busId != null ? "busId=" + busId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
