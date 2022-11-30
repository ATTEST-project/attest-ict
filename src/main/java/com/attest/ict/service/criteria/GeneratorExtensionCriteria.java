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
 * Criteria class for the {@link com.attest.ict.domain.GeneratorExtension} entity. This class is used
 * in {@link com.attest.ict.web.rest.GeneratorExtensionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /generator-extensions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeneratorExtensionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter idGen;

    private IntegerFilter statusCurt;

    private IntegerFilter dgType;

    private LongFilter generatorId;

    private Boolean distinct;

    public GeneratorExtensionCriteria() {}

    public GeneratorExtensionCriteria(GeneratorExtensionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idGen = other.idGen == null ? null : other.idGen.copy();
        this.statusCurt = other.statusCurt == null ? null : other.statusCurt.copy();
        this.dgType = other.dgType == null ? null : other.dgType.copy();
        this.generatorId = other.generatorId == null ? null : other.generatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GeneratorExtensionCriteria copy() {
        return new GeneratorExtensionCriteria(this);
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

    public IntegerFilter getIdGen() {
        return idGen;
    }

    public IntegerFilter idGen() {
        if (idGen == null) {
            idGen = new IntegerFilter();
        }
        return idGen;
    }

    public void setIdGen(IntegerFilter idGen) {
        this.idGen = idGen;
    }

    public IntegerFilter getStatusCurt() {
        return statusCurt;
    }

    public IntegerFilter statusCurt() {
        if (statusCurt == null) {
            statusCurt = new IntegerFilter();
        }
        return statusCurt;
    }

    public void setStatusCurt(IntegerFilter statusCurt) {
        this.statusCurt = statusCurt;
    }

    public IntegerFilter getDgType() {
        return dgType;
    }

    public IntegerFilter dgType() {
        if (dgType == null) {
            dgType = new IntegerFilter();
        }
        return dgType;
    }

    public void setDgType(IntegerFilter dgType) {
        this.dgType = dgType;
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
        final GeneratorExtensionCriteria that = (GeneratorExtensionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idGen, that.idGen) &&
            Objects.equals(statusCurt, that.statusCurt) &&
            Objects.equals(dgType, that.dgType) &&
            Objects.equals(generatorId, that.generatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idGen, statusCurt, dgType, generatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneratorExtensionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idGen != null ? "idGen=" + idGen + ", " : "") +
            (statusCurt != null ? "statusCurt=" + statusCurt + ", " : "") +
            (dgType != null ? "dgType=" + dgType + ", " : "") +
            (generatorId != null ? "generatorId=" + generatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
