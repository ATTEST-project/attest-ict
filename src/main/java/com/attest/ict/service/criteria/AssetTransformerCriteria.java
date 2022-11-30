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
 * Criteria class for the {@link com.attest.ict.domain.AssetTransformer} entity. This class is used
 * in {@link com.attest.ict.web.rest.AssetTransformerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-transformers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetTransformerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private StringFilter voltageRatio;

    private StringFilter insulationMedium;

    private StringFilter type;

    private StringFilter indoorOutdoor;

    private IntegerFilter annualMaxLoadKva;

    private IntegerFilter age;

    private StringFilter externalCondition;

    private IntegerFilter ratingKva;

    private IntegerFilter numConnectedCustomers;

    private IntegerFilter numSensitiveCustomers;

    private StringFilter backupSupply;

    private LongFilter costOfFailureEuro;

    private LongFilter networkId;

    private Boolean distinct;

    public AssetTransformerCriteria() {}

    public AssetTransformerCriteria(AssetTransformerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.voltageRatio = other.voltageRatio == null ? null : other.voltageRatio.copy();
        this.insulationMedium = other.insulationMedium == null ? null : other.insulationMedium.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.indoorOutdoor = other.indoorOutdoor == null ? null : other.indoorOutdoor.copy();
        this.annualMaxLoadKva = other.annualMaxLoadKva == null ? null : other.annualMaxLoadKva.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.externalCondition = other.externalCondition == null ? null : other.externalCondition.copy();
        this.ratingKva = other.ratingKva == null ? null : other.ratingKva.copy();
        this.numConnectedCustomers = other.numConnectedCustomers == null ? null : other.numConnectedCustomers.copy();
        this.numSensitiveCustomers = other.numSensitiveCustomers == null ? null : other.numSensitiveCustomers.copy();
        this.backupSupply = other.backupSupply == null ? null : other.backupSupply.copy();
        this.costOfFailureEuro = other.costOfFailureEuro == null ? null : other.costOfFailureEuro.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetTransformerCriteria copy() {
        return new AssetTransformerCriteria(this);
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

    public StringFilter getVoltageRatio() {
        return voltageRatio;
    }

    public StringFilter voltageRatio() {
        if (voltageRatio == null) {
            voltageRatio = new StringFilter();
        }
        return voltageRatio;
    }

    public void setVoltageRatio(StringFilter voltageRatio) {
        this.voltageRatio = voltageRatio;
    }

    public StringFilter getInsulationMedium() {
        return insulationMedium;
    }

    public StringFilter insulationMedium() {
        if (insulationMedium == null) {
            insulationMedium = new StringFilter();
        }
        return insulationMedium;
    }

    public void setInsulationMedium(StringFilter insulationMedium) {
        this.insulationMedium = insulationMedium;
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

    public StringFilter getIndoorOutdoor() {
        return indoorOutdoor;
    }

    public StringFilter indoorOutdoor() {
        if (indoorOutdoor == null) {
            indoorOutdoor = new StringFilter();
        }
        return indoorOutdoor;
    }

    public void setIndoorOutdoor(StringFilter indoorOutdoor) {
        this.indoorOutdoor = indoorOutdoor;
    }

    public IntegerFilter getAnnualMaxLoadKva() {
        return annualMaxLoadKva;
    }

    public IntegerFilter annualMaxLoadKva() {
        if (annualMaxLoadKva == null) {
            annualMaxLoadKva = new IntegerFilter();
        }
        return annualMaxLoadKva;
    }

    public void setAnnualMaxLoadKva(IntegerFilter annualMaxLoadKva) {
        this.annualMaxLoadKva = annualMaxLoadKva;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public IntegerFilter age() {
        if (age == null) {
            age = new IntegerFilter();
        }
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public StringFilter getExternalCondition() {
        return externalCondition;
    }

    public StringFilter externalCondition() {
        if (externalCondition == null) {
            externalCondition = new StringFilter();
        }
        return externalCondition;
    }

    public void setExternalCondition(StringFilter externalCondition) {
        this.externalCondition = externalCondition;
    }

    public IntegerFilter getRatingKva() {
        return ratingKva;
    }

    public IntegerFilter ratingKva() {
        if (ratingKva == null) {
            ratingKva = new IntegerFilter();
        }
        return ratingKva;
    }

    public void setRatingKva(IntegerFilter ratingKva) {
        this.ratingKva = ratingKva;
    }

    public IntegerFilter getNumConnectedCustomers() {
        return numConnectedCustomers;
    }

    public IntegerFilter numConnectedCustomers() {
        if (numConnectedCustomers == null) {
            numConnectedCustomers = new IntegerFilter();
        }
        return numConnectedCustomers;
    }

    public void setNumConnectedCustomers(IntegerFilter numConnectedCustomers) {
        this.numConnectedCustomers = numConnectedCustomers;
    }

    public IntegerFilter getNumSensitiveCustomers() {
        return numSensitiveCustomers;
    }

    public IntegerFilter numSensitiveCustomers() {
        if (numSensitiveCustomers == null) {
            numSensitiveCustomers = new IntegerFilter();
        }
        return numSensitiveCustomers;
    }

    public void setNumSensitiveCustomers(IntegerFilter numSensitiveCustomers) {
        this.numSensitiveCustomers = numSensitiveCustomers;
    }

    public StringFilter getBackupSupply() {
        return backupSupply;
    }

    public StringFilter backupSupply() {
        if (backupSupply == null) {
            backupSupply = new StringFilter();
        }
        return backupSupply;
    }

    public void setBackupSupply(StringFilter backupSupply) {
        this.backupSupply = backupSupply;
    }

    public LongFilter getCostOfFailureEuro() {
        return costOfFailureEuro;
    }

    public LongFilter costOfFailureEuro() {
        if (costOfFailureEuro == null) {
            costOfFailureEuro = new LongFilter();
        }
        return costOfFailureEuro;
    }

    public void setCostOfFailureEuro(LongFilter costOfFailureEuro) {
        this.costOfFailureEuro = costOfFailureEuro;
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
        final AssetTransformerCriteria that = (AssetTransformerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(voltageRatio, that.voltageRatio) &&
            Objects.equals(insulationMedium, that.insulationMedium) &&
            Objects.equals(type, that.type) &&
            Objects.equals(indoorOutdoor, that.indoorOutdoor) &&
            Objects.equals(annualMaxLoadKva, that.annualMaxLoadKva) &&
            Objects.equals(age, that.age) &&
            Objects.equals(externalCondition, that.externalCondition) &&
            Objects.equals(ratingKva, that.ratingKva) &&
            Objects.equals(numConnectedCustomers, that.numConnectedCustomers) &&
            Objects.equals(numSensitiveCustomers, that.numSensitiveCustomers) &&
            Objects.equals(backupSupply, that.backupSupply) &&
            Objects.equals(costOfFailureEuro, that.costOfFailureEuro) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            busNum,
            voltageRatio,
            insulationMedium,
            type,
            indoorOutdoor,
            annualMaxLoadKva,
            age,
            externalCondition,
            ratingKva,
            numConnectedCustomers,
            numSensitiveCustomers,
            backupSupply,
            costOfFailureEuro,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetTransformerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (voltageRatio != null ? "voltageRatio=" + voltageRatio + ", " : "") +
            (insulationMedium != null ? "insulationMedium=" + insulationMedium + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (indoorOutdoor != null ? "indoorOutdoor=" + indoorOutdoor + ", " : "") +
            (annualMaxLoadKva != null ? "annualMaxLoadKva=" + annualMaxLoadKva + ", " : "") +
            (age != null ? "age=" + age + ", " : "") +
            (externalCondition != null ? "externalCondition=" + externalCondition + ", " : "") +
            (ratingKva != null ? "ratingKva=" + ratingKva + ", " : "") +
            (numConnectedCustomers != null ? "numConnectedCustomers=" + numConnectedCustomers + ", " : "") +
            (numSensitiveCustomers != null ? "numSensitiveCustomers=" + numSensitiveCustomers + ", " : "") +
            (backupSupply != null ? "backupSupply=" + backupSupply + ", " : "") +
            (costOfFailureEuro != null ? "costOfFailureEuro=" + costOfFailureEuro + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
