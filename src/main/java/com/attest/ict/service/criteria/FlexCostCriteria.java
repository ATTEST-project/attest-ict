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
 * Criteria class for the {@link com.attest.ict.domain.FlexCost} entity. This class is used
 * in {@link com.attest.ict.web.rest.FlexCostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /flex-costs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlexCostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private IntegerFilter model;

    private LongFilter nCost;

    private DoubleFilter costPr;

    private DoubleFilter costQr;

    private StringFilter costPf;

    private StringFilter costQf;

    private LongFilter flexProfileId;

    private Boolean distinct;

    public FlexCostCriteria() {}

    public FlexCostCriteria(FlexCostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.nCost = other.nCost == null ? null : other.nCost.copy();
        this.costPr = other.costPr == null ? null : other.costPr.copy();
        this.costQr = other.costQr == null ? null : other.costQr.copy();
        this.costPf = other.costPf == null ? null : other.costPf.copy();
        this.costQf = other.costQf == null ? null : other.costQf.copy();
        this.flexProfileId = other.flexProfileId == null ? null : other.flexProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FlexCostCriteria copy() {
        return new FlexCostCriteria(this);
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

    public IntegerFilter getModel() {
        return model;
    }

    public IntegerFilter model() {
        if (model == null) {
            model = new IntegerFilter();
        }
        return model;
    }

    public void setModel(IntegerFilter model) {
        this.model = model;
    }

    public LongFilter getnCost() {
        return nCost;
    }

    public LongFilter nCost() {
        if (nCost == null) {
            nCost = new LongFilter();
        }
        return nCost;
    }

    public void setnCost(LongFilter nCost) {
        this.nCost = nCost;
    }

    public DoubleFilter getCostPr() {
        return costPr;
    }

    public DoubleFilter costPr() {
        if (costPr == null) {
            costPr = new DoubleFilter();
        }
        return costPr;
    }

    public void setCostPr(DoubleFilter costPr) {
        this.costPr = costPr;
    }

    public DoubleFilter getCostQr() {
        return costQr;
    }

    public DoubleFilter costQr() {
        if (costQr == null) {
            costQr = new DoubleFilter();
        }
        return costQr;
    }

    public void setCostQr(DoubleFilter costQr) {
        this.costQr = costQr;
    }

    public StringFilter getCostPf() {
        return costPf;
    }

    public StringFilter costPf() {
        if (costPf == null) {
            costPf = new StringFilter();
        }
        return costPf;
    }

    public void setCostPf(StringFilter costPf) {
        this.costPf = costPf;
    }

    public StringFilter getCostQf() {
        return costQf;
    }

    public StringFilter costQf() {
        if (costQf == null) {
            costQf = new StringFilter();
        }
        return costQf;
    }

    public void setCostQf(StringFilter costQf) {
        this.costQf = costQf;
    }

    public LongFilter getFlexProfileId() {
        return flexProfileId;
    }

    public LongFilter flexProfileId() {
        if (flexProfileId == null) {
            flexProfileId = new LongFilter();
        }
        return flexProfileId;
    }

    public void setFlexProfileId(LongFilter flexProfileId) {
        this.flexProfileId = flexProfileId;
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
        final FlexCostCriteria that = (FlexCostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(model, that.model) &&
            Objects.equals(nCost, that.nCost) &&
            Objects.equals(costPr, that.costPr) &&
            Objects.equals(costQr, that.costQr) &&
            Objects.equals(costPf, that.costPf) &&
            Objects.equals(costQf, that.costQf) &&
            Objects.equals(flexProfileId, that.flexProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busNum, model, nCost, costPr, costQr, costPf, costQf, flexProfileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexCostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (nCost != null ? "nCost=" + nCost + ", " : "") +
            (costPr != null ? "costPr=" + costPr + ", " : "") +
            (costQr != null ? "costQr=" + costQr + ", " : "") +
            (costPf != null ? "costPf=" + costPf + ", " : "") +
            (costQf != null ? "costQf=" + costQf + ", " : "") +
            (flexProfileId != null ? "flexProfileId=" + flexProfileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
