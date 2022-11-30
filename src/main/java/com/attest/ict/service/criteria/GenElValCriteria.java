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
 * Criteria class for the {@link com.attest.ict.domain.GenElVal} entity. This class is used
 * in {@link com.attest.ict.web.rest.GenElValResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gen-el-vals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenElValCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter hour;

    private IntegerFilter min;

    private DoubleFilter p;

    private DoubleFilter q;

    private IntegerFilter status;

    private DoubleFilter voltageMagnitude;

    private LongFilter genIdOnSubst;

    private StringFilter nominalVoltage;

    private LongFilter genProfileId;

    private LongFilter generatorId;

    private Boolean distinct;

    public GenElValCriteria() {}

    public GenElValCriteria(GenElValCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.hour = other.hour == null ? null : other.hour.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.p = other.p == null ? null : other.p.copy();
        this.q = other.q == null ? null : other.q.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.voltageMagnitude = other.voltageMagnitude == null ? null : other.voltageMagnitude.copy();
        this.genIdOnSubst = other.genIdOnSubst == null ? null : other.genIdOnSubst.copy();
        this.nominalVoltage = other.nominalVoltage == null ? null : other.nominalVoltage.copy();
        this.genProfileId = other.genProfileId == null ? null : other.genProfileId.copy();
        this.generatorId = other.generatorId == null ? null : other.generatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenElValCriteria copy() {
        return new GenElValCriteria(this);
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

    public DoubleFilter getVoltageMagnitude() {
        return voltageMagnitude;
    }

    public DoubleFilter voltageMagnitude() {
        if (voltageMagnitude == null) {
            voltageMagnitude = new DoubleFilter();
        }
        return voltageMagnitude;
    }

    public void setVoltageMagnitude(DoubleFilter voltageMagnitude) {
        this.voltageMagnitude = voltageMagnitude;
    }

    public LongFilter getGenIdOnSubst() {
        return genIdOnSubst;
    }

    public LongFilter genIdOnSubst() {
        if (genIdOnSubst == null) {
            genIdOnSubst = new LongFilter();
        }
        return genIdOnSubst;
    }

    public void setGenIdOnSubst(LongFilter genIdOnSubst) {
        this.genIdOnSubst = genIdOnSubst;
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

    public LongFilter getGenProfileId() {
        return genProfileId;
    }

    public LongFilter genProfileId() {
        if (genProfileId == null) {
            genProfileId = new LongFilter();
        }
        return genProfileId;
    }

    public void setGenProfileId(LongFilter genProfileId) {
        this.genProfileId = genProfileId;
    }

    public LongFilter getGeneratorId() {
        return generatorId;
    }

    public LongFilter generatorId() {
        if (generatorId == null) {
            generatorId = new LongFilter();
        }
        return generatorId;
    }

    public void setGeneratorId(LongFilter generatorId) {
        this.generatorId = generatorId;
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
        final GenElValCriteria that = (GenElValCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(min, that.min) &&
            Objects.equals(p, that.p) &&
            Objects.equals(q, that.q) &&
            Objects.equals(status, that.status) &&
            Objects.equals(voltageMagnitude, that.voltageMagnitude) &&
            Objects.equals(genIdOnSubst, that.genIdOnSubst) &&
            Objects.equals(nominalVoltage, that.nominalVoltage) &&
            Objects.equals(genProfileId, that.genProfileId) &&
            Objects.equals(generatorId, that.generatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            hour,
            min,
            p,
            q,
            status,
            voltageMagnitude,
            genIdOnSubst,
            nominalVoltage,
            genProfileId,
            generatorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenElValCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (hour != null ? "hour=" + hour + ", " : "") +
            (min != null ? "min=" + min + ", " : "") +
            (p != null ? "p=" + p + ", " : "") +
            (q != null ? "q=" + q + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (voltageMagnitude != null ? "voltageMagnitude=" + voltageMagnitude + ", " : "") +
            (genIdOnSubst != null ? "genIdOnSubst=" + genIdOnSubst + ", " : "") +
            (nominalVoltage != null ? "nominalVoltage=" + nominalVoltage + ", " : "") +
            (genProfileId != null ? "genProfileId=" + genProfileId + ", " : "") +
            (generatorId != null ? "generatorId=" + generatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
