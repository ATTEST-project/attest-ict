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
 * Criteria class for the {@link com.attest.ict.domain.BillingConsumption} entity. This class is used
 * in {@link com.attest.ict.web.rest.BillingConsumptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /billing-consumptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillingConsumptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private StringFilter type;

    private LongFilter totalEnergyConsumption;

    private StringFilter unitOfMeasure;

    private LongFilter networkId;

    private Boolean distinct;

    public BillingConsumptionCriteria() {}

    public BillingConsumptionCriteria(BillingConsumptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.totalEnergyConsumption = other.totalEnergyConsumption == null ? null : other.totalEnergyConsumption.copy();
        this.unitOfMeasure = other.unitOfMeasure == null ? null : other.unitOfMeasure.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BillingConsumptionCriteria copy() {
        return new BillingConsumptionCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public LongFilter totalEnergyConsumption() {
        if (totalEnergyConsumption == null) {
            totalEnergyConsumption = new LongFilter();
        }
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption(LongFilter totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public StringFilter getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public StringFilter unitOfMeasure() {
        if (unitOfMeasure == null) {
            unitOfMeasure = new StringFilter();
        }
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(StringFilter unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
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
        final BillingConsumptionCriteria that = (BillingConsumptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(type, that.type) &&
            Objects.equals(totalEnergyConsumption, that.totalEnergyConsumption) &&
            Objects.equals(unitOfMeasure, that.unitOfMeasure) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busNum, type, totalEnergyConsumption, unitOfMeasure, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingConsumptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (totalEnergyConsumption != null ? "totalEnergyConsumption=" + totalEnergyConsumption + ", " : "") +
            (unitOfMeasure != null ? "unitOfMeasure=" + unitOfMeasure + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
