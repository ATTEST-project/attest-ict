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
 * Criteria class for the {@link com.attest.ict.domain.GenTag} entity. This class is used
 * in {@link com.attest.ict.web.rest.GenTagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gen-tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenTagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter genTag;

    private LongFilter generatorId;

    private Boolean distinct;

    public GenTagCriteria() {}

    public GenTagCriteria(GenTagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.genTag = other.genTag == null ? null : other.genTag.copy();
        this.generatorId = other.generatorId == null ? null : other.generatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenTagCriteria copy() {
        return new GenTagCriteria(this);
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

    public StringFilter getGenTag() {
        return genTag;
    }

    public StringFilter genTag() {
        if (genTag == null) {
            genTag = new StringFilter();
        }
        return genTag;
    }

    public void setGenTag(StringFilter genTag) {
        this.genTag = genTag;
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
        final GenTagCriteria that = (GenTagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(genTag, that.genTag) &&
            Objects.equals(generatorId, that.generatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genTag, generatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenTagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (genTag != null ? "genTag=" + genTag + ", " : "") +
            (generatorId != null ? "generatorId=" + generatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
