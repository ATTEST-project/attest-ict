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
 * Criteria class for the {@link com.attest.ict.domain.TransfElVal} entity. This class is used
 * in {@link com.attest.ict.web.rest.TransfElValResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transf-el-vals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransfElValCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter hour;

    private IntegerFilter min;

    private DoubleFilter tapRatio;

    private IntegerFilter status;

    private LongFilter trasfIdOnSubst;

    private StringFilter nominalVoltage;

    private LongFilter transfProfileId;

    private LongFilter branchId;

    private Boolean distinct;

    public TransfElValCriteria() {}

    public TransfElValCriteria(TransfElValCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.tapRatio = other.tapRatio == null ? null : other.tapRatio.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.trasfIdOnSubst = other.trasfIdOnSubst == null ? null : other.trasfIdOnSubst.copy();
        this.nominalVoltage = other.nominalVoltage == null ? null : other.nominalVoltage.copy();
        this.transfProfileId = other.transfProfileId == null ? null : other.transfProfileId.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransfElValCriteria copy() {
        return new TransfElValCriteria(this);
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

    public DoubleFilter getTapRatio() {
        return tapRatio;
    }

    public DoubleFilter tapRatio() {
        if (tapRatio == null) {
            tapRatio = new DoubleFilter();
        }
        return tapRatio;
    }

    public void setTapRatio(DoubleFilter tapRatio) {
        this.tapRatio = tapRatio;
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

    public LongFilter getTrasfIdOnSubst() {
        return trasfIdOnSubst;
    }

    public LongFilter trasfIdOnSubst() {
        if (trasfIdOnSubst == null) {
            trasfIdOnSubst = new LongFilter();
        }
        return trasfIdOnSubst;
    }

    public void setTrasfIdOnSubst(LongFilter trasfIdOnSubst) {
        this.trasfIdOnSubst = trasfIdOnSubst;
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

    public LongFilter getTransfProfileId() {
        return transfProfileId;
    }

    public LongFilter transfProfileId() {
        if (transfProfileId == null) {
            transfProfileId = new LongFilter();
        }
        return transfProfileId;
    }

    public void setTransfProfileId(LongFilter transfProfileId) {
        this.transfProfileId = transfProfileId;
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
        final TransfElValCriteria that = (TransfElValCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(min, that.min) &&
            Objects.equals(tapRatio, that.tapRatio) &&
            Objects.equals(status, that.status) &&
            Objects.equals(trasfIdOnSubst, that.trasfIdOnSubst) &&
            Objects.equals(nominalVoltage, that.nominalVoltage) &&
            Objects.equals(transfProfileId, that.transfProfileId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hour, min, tapRatio, status, trasfIdOnSubst, nominalVoltage, transfProfileId, branchId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfElValCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (tapRatio != null ? "tapRatio=" + tapRatio + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (trasfIdOnSubst != null ? "trasfIdOnSubst=" + trasfIdOnSubst + ", " : "") +
            (nominalVoltage != null ? "nominalVoltage=" + nominalVoltage + ", " : "") +
            (transfProfileId != null ? "transfProfileId=" + transfProfileId + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
