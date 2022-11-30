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
 * Criteria class for the {@link com.attest.ict.domain.Node} entity. This class is used
 * in {@link com.attest.ict.web.rest.NodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nodes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter networkId;

    private LongFilter loadId;

    private StringFilter name;

    private Boolean distinct;

    public NodeCriteria() {}

    public NodeCriteria(NodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.loadId = other.loadId == null ? null : other.loadId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NodeCriteria copy() {
        return new NodeCriteria(this);
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

    public StringFilter getNetworkId() {
        return networkId;
    }

    public StringFilter networkId() {
        if (networkId == null) {
            networkId = new StringFilter();
        }
        return networkId;
    }

    public void setNetworkId(StringFilter networkId) {
        this.networkId = networkId;
    }

    public LongFilter getLoadId() {
        return loadId;
    }

    public LongFilter loadId() {
        if (loadId == null) {
            loadId = new LongFilter();
        }
        return loadId;
    }

    public void setLoadId(LongFilter loadId) {
        this.loadId = loadId;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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
        final NodeCriteria that = (NodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(loadId, that.loadId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, networkId, loadId, name, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (loadId != null ? "loadId=" + loadId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
