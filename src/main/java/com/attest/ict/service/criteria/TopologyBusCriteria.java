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
 * Criteria class for the {@link com.attest.ict.domain.TopologyBus} entity. This class is used
 * in {@link com.attest.ict.web.rest.TopologyBusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /topology-buses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TopologyBusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter powerLineBranch;

    private StringFilter busName1;

    private StringFilter busName2;

    private LongFilter topologyId;

    private LongFilter networkId;

    private Boolean distinct;

    public TopologyBusCriteria() {}

    public TopologyBusCriteria(TopologyBusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.powerLineBranch = other.powerLineBranch == null ? null : other.powerLineBranch.copy();
        this.busName1 = other.busName1 == null ? null : other.busName1.copy();
        this.busName2 = other.busName2 == null ? null : other.busName2.copy();
        this.topologyId = other.topologyId == null ? null : other.topologyId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TopologyBusCriteria copy() {
        return new TopologyBusCriteria(this);
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

    public StringFilter getBusName1() {
        return busName1;
    }

    public StringFilter busName1() {
        if (busName1 == null) {
            busName1 = new StringFilter();
        }
        return busName1;
    }

    public void setBusName1(StringFilter busName1) {
        this.busName1 = busName1;
    }

    public StringFilter getBusName2() {
        return busName2;
    }

    public StringFilter busName2() {
        if (busName2 == null) {
            busName2 = new StringFilter();
        }
        return busName2;
    }

    public void setBusName2(StringFilter busName2) {
        this.busName2 = busName2;
    }

    public LongFilter getTopologyId() {
        return topologyId;
    }

    public LongFilter topologyId() {
        if (topologyId == null) {
            topologyId = new LongFilter();
        }
        return topologyId;
    }

    public void setTopologyId(LongFilter topologyId) {
        this.topologyId = topologyId;
    }

    public LongFilter getNetworkId() {
        return networkId;
    }

    public LongFilter networkId() {
        if (networkId == null) {
            networkId = new LongFilter();
        }
        return networkId;
    }

    public void setNetworkId(LongFilter networkId) {
        this.networkId = networkId;
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
        final TopologyBusCriteria that = (TopologyBusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(powerLineBranch, that.powerLineBranch) &&
            Objects.equals(busName1, that.busName1) &&
            Objects.equals(busName2, that.busName2) &&
            Objects.equals(topologyId, that.topologyId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, powerLineBranch, busName1, busName2, topologyId, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopologyBusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (powerLineBranch != null ? "powerLineBranch=" + powerLineBranch + ", " : "") +
            (busName1 != null ? "busName1=" + busName1 + ", " : "") +
            (busName2 != null ? "busName2=" + busName2 + ", " : "") +
            (topologyId != null ? "topologyId=" + topologyId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
