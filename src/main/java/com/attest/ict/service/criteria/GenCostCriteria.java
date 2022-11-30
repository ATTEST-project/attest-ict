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
 * Criteria class for the {@link com.attest.ict.domain.GenCost} entity. This class is used
 * in {@link com.attest.ict.web.rest.GenCostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gen-costs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenCostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter model;

    private DoubleFilter startup;

    private DoubleFilter shutdown;

    private LongFilter nCost;

    private StringFilter costPF;

    private StringFilter costQF;

    private LongFilter generatorId;

    private Boolean distinct;

    public GenCostCriteria() {}

    public GenCostCriteria(GenCostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.startup = other.startup == null ? null : other.startup.copy();
        this.shutdown = other.shutdown == null ? null : other.shutdown.copy();
        this.nCost = other.nCost == null ? null : other.nCost.copy();
        this.costPF = other.costPF == null ? null : other.costPF.copy();
        this.costQF = other.costQF == null ? null : other.costQF.copy();
        this.generatorId = other.generatorId == null ? null : other.generatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenCostCriteria copy() {
        return new GenCostCriteria(this);
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

    public DoubleFilter getStartup() {
        return startup;
    }

    public DoubleFilter startup() {
        if (startup == null) {
            startup = new DoubleFilter();
        }
        return startup;
    }

    public void setStartup(DoubleFilter startup) {
        this.startup = startup;
    }

    public DoubleFilter getShutdown() {
        return shutdown;
    }

    public DoubleFilter shutdown() {
        if (shutdown == null) {
            shutdown = new DoubleFilter();
        }
        return shutdown;
    }

    public void setShutdown(DoubleFilter shutdown) {
        this.shutdown = shutdown;
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

    public StringFilter getCostPF() {
        return costPF;
    }

    public StringFilter costPF() {
        if (costPF == null) {
            costPF = new StringFilter();
        }
        return costPF;
    }

    public void setCostPF(StringFilter costPF) {
        this.costPF = costPF;
    }

    public StringFilter getCostQF() {
        return costQF;
    }

    public StringFilter costQF() {
        if (costQF == null) {
            costQF = new StringFilter();
        }
        return costQF;
    }

    public void setCostQF(StringFilter costQF) {
        this.costQF = costQF;
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
        final GenCostCriteria that = (GenCostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(model, that.model) &&
            Objects.equals(startup, that.startup) &&
            Objects.equals(shutdown, that.shutdown) &&
            Objects.equals(nCost, that.nCost) &&
            Objects.equals(costPF, that.costPF) &&
            Objects.equals(costQF, that.costQF) &&
            Objects.equals(generatorId, that.generatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, startup, shutdown, nCost, costPF, costQF, generatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenCostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (startup != null ? "startup=" + startup + ", " : "") +
            (shutdown != null ? "shutdown=" + shutdown + ", " : "") +
            (nCost != null ? "nCost=" + nCost + ", " : "") +
            (costPF != null ? "costPF=" + costPF + ", " : "") +
            (costQF != null ? "costQF=" + costQF + ", " : "") +
            (generatorId != null ? "generatorId=" + generatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
