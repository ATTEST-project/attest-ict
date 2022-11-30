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
 * Criteria class for the {@link com.attest.ict.domain.VoltageLevel} entity. This class is used
 * in {@link com.attest.ict.web.rest.VoltageLevelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /voltage-levels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VoltageLevelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter v1;

    private DoubleFilter v2;

    private DoubleFilter v3;

    private LongFilter networkId;

    private Boolean distinct;

    public VoltageLevelCriteria() {}

    public VoltageLevelCriteria(VoltageLevelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.v1 = other.v1 == null ? null : other.v1.copy();
        this.v2 = other.v2 == null ? null : other.v2.copy();
        this.v3 = other.v3 == null ? null : other.v3.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VoltageLevelCriteria copy() {
        return new VoltageLevelCriteria(this);
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

    public DoubleFilter getv1() {
        return v1;
    }

    public DoubleFilter v1() {
        if (v1 == null) {
            v1 = new DoubleFilter();
        }
        return v1;
    }

    public void setv1(DoubleFilter v1) {
        this.v1 = v1;
    }

    public DoubleFilter getv2() {
        return v2;
    }

    public DoubleFilter v2() {
        if (v2 == null) {
            v2 = new DoubleFilter();
        }
        return v2;
    }

    public void setv2(DoubleFilter v2) {
        this.v2 = v2;
    }

    public DoubleFilter getv3() {
        return v3;
    }

    public DoubleFilter v3() {
        if (v3 == null) {
            v3 = new DoubleFilter();
        }
        return v3;
    }

    public void setv3(DoubleFilter v3) {
        this.v3 = v3;
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
        final VoltageLevelCriteria that = (VoltageLevelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(v1, that.v1) &&
            Objects.equals(v2, that.v2) &&
            Objects.equals(v3, that.v3) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, v1, v2, v3, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoltageLevelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (v1 != null ? "v1=" + v1 + ", " : "") +
            (v2 != null ? "v2=" + v2 + ", " : "") +
            (v3 != null ? "v3=" + v3 + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
