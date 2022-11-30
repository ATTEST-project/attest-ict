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
 * Criteria class for the {@link com.attest.ict.domain.Price} entity. This class is used
 * in {@link com.attest.ict.web.rest.PriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PriceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter electricityEnergy;

    private DoubleFilter gasEnergy;

    private DoubleFilter secondaryBand;

    private DoubleFilter secondaryUp;

    private DoubleFilter secondaryDown;

    private DoubleFilter secondaryRatioUp;

    private DoubleFilter secondaryRatioDown;

    private Boolean distinct;

    public PriceCriteria() {}

    public PriceCriteria(PriceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.electricityEnergy = other.electricityEnergy == null ? null : other.electricityEnergy.copy();
        this.gasEnergy = other.gasEnergy == null ? null : other.gasEnergy.copy();
        this.secondaryBand = other.secondaryBand == null ? null : other.secondaryBand.copy();
        this.secondaryUp = other.secondaryUp == null ? null : other.secondaryUp.copy();
        this.secondaryDown = other.secondaryDown == null ? null : other.secondaryDown.copy();
        this.secondaryRatioUp = other.secondaryRatioUp == null ? null : other.secondaryRatioUp.copy();
        this.secondaryRatioDown = other.secondaryRatioDown == null ? null : other.secondaryRatioDown.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PriceCriteria copy() {
        return new PriceCriteria(this);
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

    public DoubleFilter getElectricityEnergy() {
        return electricityEnergy;
    }

    public DoubleFilter electricityEnergy() {
        if (electricityEnergy == null) {
            electricityEnergy = new DoubleFilter();
        }
        return electricityEnergy;
    }

    public void setElectricityEnergy(DoubleFilter electricityEnergy) {
        this.electricityEnergy = electricityEnergy;
    }

    public DoubleFilter getGasEnergy() {
        return gasEnergy;
    }

    public DoubleFilter gasEnergy() {
        if (gasEnergy == null) {
            gasEnergy = new DoubleFilter();
        }
        return gasEnergy;
    }

    public void setGasEnergy(DoubleFilter gasEnergy) {
        this.gasEnergy = gasEnergy;
    }

    public DoubleFilter getSecondaryBand() {
        return secondaryBand;
    }

    public DoubleFilter secondaryBand() {
        if (secondaryBand == null) {
            secondaryBand = new DoubleFilter();
        }
        return secondaryBand;
    }

    public void setSecondaryBand(DoubleFilter secondaryBand) {
        this.secondaryBand = secondaryBand;
    }

    public DoubleFilter getSecondaryUp() {
        return secondaryUp;
    }

    public DoubleFilter secondaryUp() {
        if (secondaryUp == null) {
            secondaryUp = new DoubleFilter();
        }
        return secondaryUp;
    }

    public void setSecondaryUp(DoubleFilter secondaryUp) {
        this.secondaryUp = secondaryUp;
    }

    public DoubleFilter getSecondaryDown() {
        return secondaryDown;
    }

    public DoubleFilter secondaryDown() {
        if (secondaryDown == null) {
            secondaryDown = new DoubleFilter();
        }
        return secondaryDown;
    }

    public void setSecondaryDown(DoubleFilter secondaryDown) {
        this.secondaryDown = secondaryDown;
    }

    public DoubleFilter getSecondaryRatioUp() {
        return secondaryRatioUp;
    }

    public DoubleFilter secondaryRatioUp() {
        if (secondaryRatioUp == null) {
            secondaryRatioUp = new DoubleFilter();
        }
        return secondaryRatioUp;
    }

    public void setSecondaryRatioUp(DoubleFilter secondaryRatioUp) {
        this.secondaryRatioUp = secondaryRatioUp;
    }

    public DoubleFilter getSecondaryRatioDown() {
        return secondaryRatioDown;
    }

    public DoubleFilter secondaryRatioDown() {
        if (secondaryRatioDown == null) {
            secondaryRatioDown = new DoubleFilter();
        }
        return secondaryRatioDown;
    }

    public void setSecondaryRatioDown(DoubleFilter secondaryRatioDown) {
        this.secondaryRatioDown = secondaryRatioDown;
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
        final PriceCriteria that = (PriceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(electricityEnergy, that.electricityEnergy) &&
            Objects.equals(gasEnergy, that.gasEnergy) &&
            Objects.equals(secondaryBand, that.secondaryBand) &&
            Objects.equals(secondaryUp, that.secondaryUp) &&
            Objects.equals(secondaryDown, that.secondaryDown) &&
            Objects.equals(secondaryRatioUp, that.secondaryRatioUp) &&
            Objects.equals(secondaryRatioDown, that.secondaryRatioDown) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            electricityEnergy,
            gasEnergy,
            secondaryBand,
            secondaryUp,
            secondaryDown,
            secondaryRatioUp,
            secondaryRatioDown,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (electricityEnergy != null ? "electricityEnergy=" + electricityEnergy + ", " : "") +
            (gasEnergy != null ? "gasEnergy=" + gasEnergy + ", " : "") +
            (secondaryBand != null ? "secondaryBand=" + secondaryBand + ", " : "") +
            (secondaryUp != null ? "secondaryUp=" + secondaryUp + ", " : "") +
            (secondaryDown != null ? "secondaryDown=" + secondaryDown + ", " : "") +
            (secondaryRatioUp != null ? "secondaryRatioUp=" + secondaryRatioUp + ", " : "") +
            (secondaryRatioDown != null ? "secondaryRatioDown=" + secondaryRatioDown + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
