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
 * Criteria class for the {@link com.attest.ict.domain.BaseMVA} entity. This class is used
 * in {@link com.attest.ict.web.rest.BaseMVAResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /base-mvas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BaseMVACriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter baseMva;

    private LongFilter networkId;

    private Boolean distinct;

    public BaseMVACriteria() {}

    public BaseMVACriteria(BaseMVACriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.baseMva = other.baseMva == null ? null : other.baseMva.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BaseMVACriteria copy() {
        return new BaseMVACriteria(this);
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

    public DoubleFilter getBaseMva() {
        return baseMva;
    }

    public DoubleFilter baseMva() {
        if (baseMva == null) {
            baseMva = new DoubleFilter();
        }
        return baseMva;
    }

    public void setBaseMva(DoubleFilter baseMva) {
        this.baseMva = baseMva;
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
        final BaseMVACriteria that = (BaseMVACriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(baseMva, that.baseMva) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseMva, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaseMVACriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (baseMva != null ? "baseMva=" + baseMva + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
