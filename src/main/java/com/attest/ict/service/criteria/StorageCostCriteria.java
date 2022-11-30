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
 * Criteria class for the {@link com.attest.ict.domain.StorageCost} entity. This class is used
 * in {@link com.attest.ict.web.rest.StorageCostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /storage-costs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StorageCostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private DoubleFilter costA;

    private DoubleFilter costB;

    private DoubleFilter costC;

    private Boolean distinct;

    public StorageCostCriteria() {}

    public StorageCostCriteria(StorageCostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.costA = other.costA == null ? null : other.costA.copy();
        this.costB = other.costB == null ? null : other.costB.copy();
        this.costC = other.costC == null ? null : other.costC.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StorageCostCriteria copy() {
        return new StorageCostCriteria(this);
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

    public LongFilter getBusNum() {
        return busNum;
    }

    public LongFilter busNum() {
        if (busNum == null) {
            busNum = new LongFilter();
        }
        return busNum;
    }

    public void setBusNum(LongFilter busNum) {
        this.busNum = busNum;
    }

    public DoubleFilter getCostA() {
        return costA;
    }

    public DoubleFilter costA() {
        if (costA == null) {
            costA = new DoubleFilter();
        }
        return costA;
    }

    public void setCostA(DoubleFilter costA) {
        this.costA = costA;
    }

    public DoubleFilter getCostB() {
        return costB;
    }

    public DoubleFilter costB() {
        if (costB == null) {
            costB = new DoubleFilter();
        }
        return costB;
    }

    public void setCostB(DoubleFilter costB) {
        this.costB = costB;
    }

    public DoubleFilter getCostC() {
        return costC;
    }

    public DoubleFilter costC() {
        if (costC == null) {
            costC = new DoubleFilter();
        }
        return costC;
    }

    public void setCostC(DoubleFilter costC) {
        this.costC = costC;
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
        final StorageCostCriteria that = (StorageCostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(costA, that.costA) &&
            Objects.equals(costB, that.costB) &&
            Objects.equals(costC, that.costC) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busNum, costA, costB, costC, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageCostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (costA != null ? "costA=" + costA + ", " : "") +
            (costB != null ? "costB=" + costB + ", " : "") +
            (costC != null ? "costC=" + costC + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
