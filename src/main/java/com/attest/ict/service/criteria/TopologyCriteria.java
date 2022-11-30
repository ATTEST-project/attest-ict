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
 * Criteria class for the {@link com.attest.ict.domain.Topology} entity. This class is used
 * in {@link com.attest.ict.web.rest.TopologyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /topologies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TopologyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter powerLineBranch;

    private StringFilter p1;

    private StringFilter p2;

    private LongFilter powerLineBranchParentId;

    private Boolean distinct;

    public TopologyCriteria() {}

    public TopologyCriteria(TopologyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.powerLineBranch = other.powerLineBranch == null ? null : other.powerLineBranch.copy();
        this.p1 = other.p1 == null ? null : other.p1.copy();
        this.p2 = other.p2 == null ? null : other.p2.copy();
        this.powerLineBranchParentId = other.powerLineBranchParentId == null ? null : other.powerLineBranchParentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TopologyCriteria copy() {
        return new TopologyCriteria(this);
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

    public StringFilter getPowerLineBranch() {
        return powerLineBranch;
    }

    public StringFilter powerLineBranch() {
        if (powerLineBranch == null) {
            powerLineBranch = new StringFilter();
        }
        return powerLineBranch;
    }

    public void setPowerLineBranch(StringFilter powerLineBranch) {
        this.powerLineBranch = powerLineBranch;
    }

    public StringFilter getp1() {
        return p1;
    }

    public StringFilter p1() {
        if (p1 == null) {
            p1 = new StringFilter();
        }
        return p1;
    }

    public void setp1(StringFilter p1) {
        this.p1 = p1;
    }

    public StringFilter getp2() {
        return p2;
    }

    public StringFilter p2() {
        if (p2 == null) {
            p2 = new StringFilter();
        }
        return p2;
    }

    public void setp2(StringFilter p2) {
        this.p2 = p2;
    }

    public LongFilter getPowerLineBranchParentId() {
        return powerLineBranchParentId;
    }

    public LongFilter powerLineBranchParentId() {
        if (powerLineBranchParentId == null) {
            powerLineBranchParentId = new LongFilter();
        }
        return powerLineBranchParentId;
    }

    public void setPowerLineBranchParentId(LongFilter powerLineBranchParentId) {
        this.powerLineBranchParentId = powerLineBranchParentId;
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
        final TopologyCriteria that = (TopologyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(powerLineBranch, that.powerLineBranch) &&
            Objects.equals(p1, that.p1) &&
            Objects.equals(p2, that.p2) &&
            Objects.equals(powerLineBranchParentId, that.powerLineBranchParentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, powerLineBranch, p1, p2, powerLineBranchParentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopologyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (powerLineBranch != null ? "powerLineBranch=" + powerLineBranch + ", " : "") +
            (p1 != null ? "p1=" + p1 + ", " : "") +
            (p2 != null ? "p2=" + p2 + ", " : "") +
            (powerLineBranchParentId != null ? "powerLineBranchParentId=" + powerLineBranchParentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
