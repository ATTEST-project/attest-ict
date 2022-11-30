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
 * Criteria class for the {@link com.attest.ict.domain.DsoTsoConnection} entity. This class is used
 * in {@link com.attest.ict.web.rest.DsoTsoConnectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dso-tso-connections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DsoTsoConnectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tsoNetworkName;

    private LongFilter dsoBusNum;

    private LongFilter tsoBusNum;

    private LongFilter dsoNetworkId;

    private Boolean distinct;

    public DsoTsoConnectionCriteria() {}

    public DsoTsoConnectionCriteria(DsoTsoConnectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tsoNetworkName = other.tsoNetworkName == null ? null : other.tsoNetworkName.copy();
        this.dsoBusNum = other.dsoBusNum == null ? null : other.dsoBusNum.copy();
        this.tsoBusNum = other.tsoBusNum == null ? null : other.tsoBusNum.copy();
        this.dsoNetworkId = other.dsoNetworkId == null ? null : other.dsoNetworkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DsoTsoConnectionCriteria copy() {
        return new DsoTsoConnectionCriteria(this);
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

    public StringFilter getTsoNetworkName() {
        return tsoNetworkName;
    }

    public StringFilter tsoNetworkName() {
        if (tsoNetworkName == null) {
            tsoNetworkName = new StringFilter();
        }
        return tsoNetworkName;
    }

    public void setTsoNetworkName(StringFilter tsoNetworkName) {
        this.tsoNetworkName = tsoNetworkName;
    }

    public LongFilter getDsoBusNum() {
        return dsoBusNum;
    }

    public LongFilter dsoBusNum() {
        if (dsoBusNum == null) {
            dsoBusNum = new LongFilter();
        }
        return dsoBusNum;
    }

    public void setDsoBusNum(LongFilter dsoBusNum) {
        this.dsoBusNum = dsoBusNum;
    }

    public LongFilter getTsoBusNum() {
        return tsoBusNum;
    }

    public LongFilter tsoBusNum() {
        if (tsoBusNum == null) {
            tsoBusNum = new LongFilter();
        }
        return tsoBusNum;
    }

    public void setTsoBusNum(LongFilter tsoBusNum) {
        this.tsoBusNum = tsoBusNum;
    }

    public LongFilter getDsoNetworkId() {
        return dsoNetworkId;
    }

    public LongFilter dsoNetworkId() {
        if (dsoNetworkId == null) {
            dsoNetworkId = new LongFilter();
        }
        return dsoNetworkId;
    }

    public void setDsoNetworkId(LongFilter dsoNetworkId) {
        this.dsoNetworkId = dsoNetworkId;
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
        final DsoTsoConnectionCriteria that = (DsoTsoConnectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tsoNetworkName, that.tsoNetworkName) &&
            Objects.equals(dsoBusNum, that.dsoBusNum) &&
            Objects.equals(tsoBusNum, that.tsoBusNum) &&
            Objects.equals(dsoNetworkId, that.dsoNetworkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tsoNetworkName, dsoBusNum, tsoBusNum, dsoNetworkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DsoTsoConnectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tsoNetworkName != null ? "tsoNetworkName=" + tsoNetworkName + ", " : "") +
            (dsoBusNum != null ? "dsoBusNum=" + dsoBusNum + ", " : "") +
            (tsoBusNum != null ? "tsoBusNum=" + tsoBusNum + ", " : "") +
            (dsoNetworkId != null ? "dsoNetworkId=" + dsoNetworkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
