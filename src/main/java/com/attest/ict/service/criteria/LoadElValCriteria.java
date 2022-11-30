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
 * Criteria class for the {@link com.attest.ict.domain.LoadElVal} entity. This class is used
 * in {@link com.attest.ict.web.rest.LoadElValResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /load-el-vals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoadElValCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter hour;

    private IntegerFilter min;

    private DoubleFilter p;

    private DoubleFilter q;

    private LongFilter loadIdOnSubst;

    private StringFilter nominalVoltage;

    private LongFilter loadProfileId;

    private LongFilter busId;

    private Boolean distinct;

    public LoadElValCriteria() {}

    public LoadElValCriteria(LoadElValCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.p = other.p == null ? null : other.p.copy();
        this.q = other.q == null ? null : other.q.copy();
        this.loadIdOnSubst = other.loadIdOnSubst == null ? null : other.loadIdOnSubst.copy();
        this.nominalVoltage = other.nominalVoltage == null ? null : other.nominalVoltage.copy();
        this.loadProfileId = other.loadProfileId == null ? null : other.loadProfileId.copy();
        this.busId = other.busId == null ? null : other.busId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoadElValCriteria copy() {
        return new LoadElValCriteria(this);
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

    public LongFilter getLoadIdOnSubst() {
        return loadIdOnSubst;
    }

    public LongFilter loadIdOnSubst() {
        if (loadIdOnSubst == null) {
            loadIdOnSubst = new LongFilter();
        }
        return loadIdOnSubst;
    }

    public void setLoadIdOnSubst(LongFilter loadIdOnSubst) {
        this.loadIdOnSubst = loadIdOnSubst;
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

    public LongFilter getLoadProfileId() {
        return loadProfileId;
    }

    public LongFilter loadProfileId() {
        if (loadProfileId == null) {
            loadProfileId = new LongFilter();
        }
        return loadProfileId;
    }

    public void setLoadProfileId(LongFilter loadProfileId) {
        this.loadProfileId = loadProfileId;
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
        final LoadElValCriteria that = (LoadElValCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(min, that.min) &&
            Objects.equals(p, that.p) &&
            Objects.equals(q, that.q) &&
            Objects.equals(loadIdOnSubst, that.loadIdOnSubst) &&
            Objects.equals(nominalVoltage, that.nominalVoltage) &&
            Objects.equals(loadProfileId, that.loadProfileId) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hour, min, p, q, loadIdOnSubst, nominalVoltage, loadProfileId, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoadElValCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (p != null ? "p=" + p + ", " : "") +
            (q != null ? "q=" + q + ", " : "") +
            (loadIdOnSubst != null ? "loadIdOnSubst=" + loadIdOnSubst + ", " : "") +
            (nominalVoltage != null ? "nominalVoltage=" + nominalVoltage + ", " : "") +
            (loadProfileId != null ? "loadProfileId=" + loadProfileId + ", " : "") +
            (busId != null ? "busId=" + busId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
