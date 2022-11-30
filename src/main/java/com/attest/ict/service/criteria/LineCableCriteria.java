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
 * Criteria class for the {@link com.attest.ict.domain.LineCable} entity. This class is used
 * in {@link com.attest.ict.web.rest.LineCableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /line-cables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LineCableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fbus;

    private LongFilter tbus;

    private DoubleFilter lengthKm;

    private StringFilter typeOfInstallation;

    private LongFilter networkId;

    private Boolean distinct;

    public LineCableCriteria() {}

    public LineCableCriteria(LineCableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fbus = other.fbus == null ? null : other.fbus.copy();
        this.tbus = other.tbus == null ? null : other.tbus.copy();
        this.lengthKm = other.lengthKm == null ? null : other.lengthKm.copy();
        this.typeOfInstallation = other.typeOfInstallation == null ? null : other.typeOfInstallation.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LineCableCriteria copy() {
        return new LineCableCriteria(this);
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

    public DoubleFilter getLengthKm() {
        return lengthKm;
    }

    public DoubleFilter lengthKm() {
        if (lengthKm == null) {
            lengthKm = new DoubleFilter();
        }
        return lengthKm;
    }

    public void setLengthKm(DoubleFilter lengthKm) {
        this.lengthKm = lengthKm;
    }

    public StringFilter getTypeOfInstallation() {
        return typeOfInstallation;
    }

    public StringFilter typeOfInstallation() {
        if (typeOfInstallation == null) {
            typeOfInstallation = new StringFilter();
        }
        return typeOfInstallation;
    }

    public void setTypeOfInstallation(StringFilter typeOfInstallation) {
        this.typeOfInstallation = typeOfInstallation;
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
        final LineCableCriteria that = (LineCableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fbus, that.fbus) &&
            Objects.equals(tbus, that.tbus) &&
            Objects.equals(lengthKm, that.lengthKm) &&
            Objects.equals(typeOfInstallation, that.typeOfInstallation) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fbus, tbus, lengthKm, typeOfInstallation, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineCableCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fbus != null ? "fbus=" + fbus + ", " : "") +
            (tbus != null ? "tbus=" + tbus + ", " : "") +
            (lengthKm != null ? "lengthKm=" + lengthKm + ", " : "") +
            (typeOfInstallation != null ? "typeOfInstallation=" + typeOfInstallation + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
