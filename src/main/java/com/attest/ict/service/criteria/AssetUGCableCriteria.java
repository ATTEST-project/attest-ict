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
 * Criteria class for the {@link com.attest.ict.domain.AssetUGCable} entity. This class is used
 * in {@link com.attest.ict.web.rest.AssetUGCableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-ug-cables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetUGCableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectionLabel;

    private LongFilter circuitId;

    private IntegerFilter conductorCrossSectionalArea;

    private StringFilter sheathMaterial;

    private StringFilter designVoltage;

    private StringFilter operatingVoltage;

    private StringFilter insulationTypeSheath;

    private StringFilter conductorMaterial;

    private IntegerFilter age;

    private IntegerFilter faultHistory;

    private IntegerFilter lengthOfCableSectionMeters;

    private IntegerFilter sectionRating;

    private StringFilter type;

    private IntegerFilter numberOfCores;

    private StringFilter netPerformanceCostOfFailureEuro;

    private IntegerFilter repairTimeHour;

    private LongFilter networkId;

    private Boolean distinct;

    public AssetUGCableCriteria() {}

    public AssetUGCableCriteria(AssetUGCableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sectionLabel = other.sectionLabel == null ? null : other.sectionLabel.copy();
        this.circuitId = other.circuitId == null ? null : other.circuitId.copy();
        this.conductorCrossSectionalArea = other.conductorCrossSectionalArea == null ? null : other.conductorCrossSectionalArea.copy();
        this.sheathMaterial = other.sheathMaterial == null ? null : other.sheathMaterial.copy();
        this.designVoltage = other.designVoltage == null ? null : other.designVoltage.copy();
        this.operatingVoltage = other.operatingVoltage == null ? null : other.operatingVoltage.copy();
        this.insulationTypeSheath = other.insulationTypeSheath == null ? null : other.insulationTypeSheath.copy();
        this.conductorMaterial = other.conductorMaterial == null ? null : other.conductorMaterial.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.faultHistory = other.faultHistory == null ? null : other.faultHistory.copy();
        this.lengthOfCableSectionMeters = other.lengthOfCableSectionMeters == null ? null : other.lengthOfCableSectionMeters.copy();
        this.sectionRating = other.sectionRating == null ? null : other.sectionRating.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.numberOfCores = other.numberOfCores == null ? null : other.numberOfCores.copy();
        this.netPerformanceCostOfFailureEuro =
            other.netPerformanceCostOfFailureEuro == null ? null : other.netPerformanceCostOfFailureEuro.copy();
        this.repairTimeHour = other.repairTimeHour == null ? null : other.repairTimeHour.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetUGCableCriteria copy() {
        return new AssetUGCableCriteria(this);
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

    public StringFilter getSectionLabel() {
        return sectionLabel;
    }

    public StringFilter sectionLabel() {
        if (sectionLabel == null) {
            sectionLabel = new StringFilter();
        }
        return sectionLabel;
    }

    public void setSectionLabel(StringFilter sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public LongFilter getCircuitId() {
        return circuitId;
    }

    public LongFilter circuitId() {
        if (circuitId == null) {
            circuitId = new LongFilter();
        }
        return circuitId;
    }

    public void setCircuitId(LongFilter circuitId) {
        this.circuitId = circuitId;
    }

    public IntegerFilter getConductorCrossSectionalArea() {
        return conductorCrossSectionalArea;
    }

    public IntegerFilter conductorCrossSectionalArea() {
        if (conductorCrossSectionalArea == null) {
            conductorCrossSectionalArea = new IntegerFilter();
        }
        return conductorCrossSectionalArea;
    }

    public void setConductorCrossSectionalArea(IntegerFilter conductorCrossSectionalArea) {
        this.conductorCrossSectionalArea = conductorCrossSectionalArea;
    }

    public StringFilter getSheathMaterial() {
        return sheathMaterial;
    }

    public StringFilter sheathMaterial() {
        if (sheathMaterial == null) {
            sheathMaterial = new StringFilter();
        }
        return sheathMaterial;
    }

    public void setSheathMaterial(StringFilter sheathMaterial) {
        this.sheathMaterial = sheathMaterial;
    }

    public StringFilter getDesignVoltage() {
        return designVoltage;
    }

    public StringFilter designVoltage() {
        if (designVoltage == null) {
            designVoltage = new StringFilter();
        }
        return designVoltage;
    }

    public void setDesignVoltage(StringFilter designVoltage) {
        this.designVoltage = designVoltage;
    }

    public StringFilter getOperatingVoltage() {
        return operatingVoltage;
    }

    public StringFilter operatingVoltage() {
        if (operatingVoltage == null) {
            operatingVoltage = new StringFilter();
        }
        return operatingVoltage;
    }

    public void setOperatingVoltage(StringFilter operatingVoltage) {
        this.operatingVoltage = operatingVoltage;
    }

    public StringFilter getInsulationTypeSheath() {
        return insulationTypeSheath;
    }

    public StringFilter insulationTypeSheath() {
        if (insulationTypeSheath == null) {
            insulationTypeSheath = new StringFilter();
        }
        return insulationTypeSheath;
    }

    public void setInsulationTypeSheath(StringFilter insulationTypeSheath) {
        this.insulationTypeSheath = insulationTypeSheath;
    }

    public StringFilter getConductorMaterial() {
        return conductorMaterial;
    }

    public StringFilter conductorMaterial() {
        if (conductorMaterial == null) {
            conductorMaterial = new StringFilter();
        }
        return conductorMaterial;
    }

    public void setConductorMaterial(StringFilter conductorMaterial) {
        this.conductorMaterial = conductorMaterial;
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

    public IntegerFilter getFaultHistory() {
        return faultHistory;
    }

    public IntegerFilter faultHistory() {
        if (faultHistory == null) {
            faultHistory = new IntegerFilter();
        }
        return faultHistory;
    }

    public void setFaultHistory(IntegerFilter faultHistory) {
        this.faultHistory = faultHistory;
    }

    public IntegerFilter getLengthOfCableSectionMeters() {
        return lengthOfCableSectionMeters;
    }

    public IntegerFilter lengthOfCableSectionMeters() {
        if (lengthOfCableSectionMeters == null) {
            lengthOfCableSectionMeters = new IntegerFilter();
        }
        return lengthOfCableSectionMeters;
    }

    public void setLengthOfCableSectionMeters(IntegerFilter lengthOfCableSectionMeters) {
        this.lengthOfCableSectionMeters = lengthOfCableSectionMeters;
    }

    public IntegerFilter getSectionRating() {
        return sectionRating;
    }

    public IntegerFilter sectionRating() {
        if (sectionRating == null) {
            sectionRating = new IntegerFilter();
        }
        return sectionRating;
    }

    public void setSectionRating(IntegerFilter sectionRating) {
        this.sectionRating = sectionRating;
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

    public IntegerFilter getNumberOfCores() {
        return numberOfCores;
    }

    public IntegerFilter numberOfCores() {
        if (numberOfCores == null) {
            numberOfCores = new IntegerFilter();
        }
        return numberOfCores;
    }

    public void setNumberOfCores(IntegerFilter numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public StringFilter getNetPerformanceCostOfFailureEuro() {
        return netPerformanceCostOfFailureEuro;
    }

    public StringFilter netPerformanceCostOfFailureEuro() {
        if (netPerformanceCostOfFailureEuro == null) {
            netPerformanceCostOfFailureEuro = new StringFilter();
        }
        return netPerformanceCostOfFailureEuro;
    }

    public void setNetPerformanceCostOfFailureEuro(StringFilter netPerformanceCostOfFailureEuro) {
        this.netPerformanceCostOfFailureEuro = netPerformanceCostOfFailureEuro;
    }

    public IntegerFilter getRepairTimeHour() {
        return repairTimeHour;
    }

    public IntegerFilter repairTimeHour() {
        if (repairTimeHour == null) {
            repairTimeHour = new IntegerFilter();
        }
        return repairTimeHour;
    }

    public void setRepairTimeHour(IntegerFilter repairTimeHour) {
        this.repairTimeHour = repairTimeHour;
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
        final AssetUGCableCriteria that = (AssetUGCableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sectionLabel, that.sectionLabel) &&
            Objects.equals(circuitId, that.circuitId) &&
            Objects.equals(conductorCrossSectionalArea, that.conductorCrossSectionalArea) &&
            Objects.equals(sheathMaterial, that.sheathMaterial) &&
            Objects.equals(designVoltage, that.designVoltage) &&
            Objects.equals(operatingVoltage, that.operatingVoltage) &&
            Objects.equals(insulationTypeSheath, that.insulationTypeSheath) &&
            Objects.equals(conductorMaterial, that.conductorMaterial) &&
            Objects.equals(age, that.age) &&
            Objects.equals(faultHistory, that.faultHistory) &&
            Objects.equals(lengthOfCableSectionMeters, that.lengthOfCableSectionMeters) &&
            Objects.equals(sectionRating, that.sectionRating) &&
            Objects.equals(type, that.type) &&
            Objects.equals(numberOfCores, that.numberOfCores) &&
            Objects.equals(netPerformanceCostOfFailureEuro, that.netPerformanceCostOfFailureEuro) &&
            Objects.equals(repairTimeHour, that.repairTimeHour) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sectionLabel,
            circuitId,
            conductorCrossSectionalArea,
            sheathMaterial,
            designVoltage,
            operatingVoltage,
            insulationTypeSheath,
            conductorMaterial,
            age,
            faultHistory,
            lengthOfCableSectionMeters,
            sectionRating,
            type,
            numberOfCores,
            netPerformanceCostOfFailureEuro,
            repairTimeHour,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetUGCableCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sectionLabel != null ? "sectionLabel=" + sectionLabel + ", " : "") +
            (circuitId != null ? "circuitId=" + circuitId + ", " : "") +
            (conductorCrossSectionalArea != null ? "conductorCrossSectionalArea=" + conductorCrossSectionalArea + ", " : "") +
            (sheathMaterial != null ? "sheathMaterial=" + sheathMaterial + ", " : "") +
            (designVoltage != null ? "designVoltage=" + designVoltage + ", " : "") +
            (operatingVoltage != null ? "operatingVoltage=" + operatingVoltage + ", " : "") +
            (insulationTypeSheath != null ? "insulationTypeSheath=" + insulationTypeSheath + ", " : "") +
            (conductorMaterial != null ? "conductorMaterial=" + conductorMaterial + ", " : "") +
            (age != null ? "age=" + age + ", " : "") +
            (faultHistory != null ? "faultHistory=" + faultHistory + ", " : "") +
            (lengthOfCableSectionMeters != null ? "lengthOfCableSectionMeters=" + lengthOfCableSectionMeters + ", " : "") +
            (sectionRating != null ? "sectionRating=" + sectionRating + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (numberOfCores != null ? "numberOfCores=" + numberOfCores + ", " : "") +
            (netPerformanceCostOfFailureEuro != null ? "netPerformanceCostOfFailureEuro=" + netPerformanceCostOfFailureEuro + ", " : "") +
            (repairTimeHour != null ? "repairTimeHour=" + repairTimeHour + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
