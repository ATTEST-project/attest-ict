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
 * Criteria class for the {@link com.attest.ict.domain.BranchElVal} entity. This class is used
 * in {@link com.attest.ict.web.rest.BranchElValResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /branch-el-vals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BranchElValCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter hour;

    private IntegerFilter min;

    private DoubleFilter p;

    private DoubleFilter q;

    private IntegerFilter status;

    private LongFilter branchIdOnSubst;

    private StringFilter nominalVoltage;

    private LongFilter branchId;

    private LongFilter branchProfileId;

    private Boolean distinct;

    public BranchElValCriteria() {}

    public BranchElValCriteria(BranchElValCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.p = other.p == null ? null : other.p.copy();
        this.q = other.q == null ? null : other.q.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.branchIdOnSubst = other.branchIdOnSubst == null ? null : other.branchIdOnSubst.copy();
        this.nominalVoltage = other.nominalVoltage == null ? null : other.nominalVoltage.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.branchProfileId = other.branchProfileId == null ? null : other.branchProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BranchElValCriteria copy() {
        return new BranchElValCriteria(this);
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

    public IntegerFilter getMin() {
        return min;
    }

    public IntegerFilter min() {
        if (min == null) {
            min = new IntegerFilter();
        }
        return min;
    }

    public void setMin(IntegerFilter min) {
        this.min = min;
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

    public DoubleFilter getQ() {
        return q;
    }

    public DoubleFilter q() {
        if (q == null) {
            q = new DoubleFilter();
        }
        return q;
    }

    public void setQ(DoubleFilter q) {
        this.q = q;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public LongFilter getBranchIdOnSubst() {
        return branchIdOnSubst;
    }

    public LongFilter branchIdOnSubst() {
        if (branchIdOnSubst == null) {
            branchIdOnSubst = new LongFilter();
        }
        return branchIdOnSubst;
    }

    public void setBranchIdOnSubst(LongFilter branchIdOnSubst) {
        this.branchIdOnSubst = branchIdOnSubst;
    }

    public StringFilter getNominalVoltage() {
        return nominalVoltage;
    }

    public StringFilter nominalVoltage() {
        if (nominalVoltage == null) {
            nominalVoltage = new StringFilter();
        }
        return nominalVoltage;
    }

    public void setNominalVoltage(StringFilter nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public LongFilter branchId() {
        if (branchId == null) {
            branchId = new LongFilter();
        }
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
    }

    public LongFilter getBranchProfileId() {
        return branchProfileId;
    }

    public LongFilter branchProfileId() {
        if (branchProfileId == null) {
            branchProfileId = new LongFilter();
        }
        return branchProfileId;
    }

    public void setBranchProfileId(LongFilter branchProfileId) {
        this.branchProfileId = branchProfileId;
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
        final BranchElValCriteria that = (BranchElValCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(min, that.min) &&
            Objects.equals(p, that.p) &&
            Objects.equals(q, that.q) &&
            Objects.equals(status, that.status) &&
            Objects.equals(branchIdOnSubst, that.branchIdOnSubst) &&
            Objects.equals(nominalVoltage, that.nominalVoltage) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(branchProfileId, that.branchProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hour, min, p, q, status, branchIdOnSubst, nominalVoltage, branchId, branchProfileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchElValCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (p != null ? "p=" + p + ", " : "") +
            (q != null ? "q=" + q + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (branchIdOnSubst != null ? "branchIdOnSubst=" + branchIdOnSubst + ", " : "") +
            (nominalVoltage != null ? "nominalVoltage=" + nominalVoltage + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (branchProfileId != null ? "branchProfileId=" + branchProfileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
