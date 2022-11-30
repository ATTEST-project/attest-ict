package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.AssetUGCable} entity.
 */
public class AssetUGCableDTO implements Serializable {

    private Long id;

    private String sectionLabel;

    private Long circuitId;

    private Integer conductorCrossSectionalArea;

    private String sheathMaterial;

    private String designVoltage;

    private String operatingVoltage;

    private String insulationTypeSheath;

    private String conductorMaterial;

    private Integer age;

    private Integer faultHistory;

    private Integer lengthOfCableSectionMeters;

    private Integer sectionRating;

    private String type;

    private Integer numberOfCores;

    private String netPerformanceCostOfFailureEuro;

    private Integer repairTimeHour;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public Long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
    }

    public Integer getConductorCrossSectionalArea() {
        return conductorCrossSectionalArea;
    }

    public void setConductorCrossSectionalArea(Integer conductorCrossSectionalArea) {
        this.conductorCrossSectionalArea = conductorCrossSectionalArea;
    }

    public String getSheathMaterial() {
        return sheathMaterial;
    }

    public void setSheathMaterial(String sheathMaterial) {
        this.sheathMaterial = sheathMaterial;
    }

    public String getDesignVoltage() {
        return designVoltage;
    }

    public void setDesignVoltage(String designVoltage) {
        this.designVoltage = designVoltage;
    }

    public String getOperatingVoltage() {
        return operatingVoltage;
    }

    public void setOperatingVoltage(String operatingVoltage) {
        this.operatingVoltage = operatingVoltage;
    }

    public String getInsulationTypeSheath() {
        return insulationTypeSheath;
    }

    public void setInsulationTypeSheath(String insulationTypeSheath) {
        this.insulationTypeSheath = insulationTypeSheath;
    }

    public String getConductorMaterial() {
        return conductorMaterial;
    }

    public void setConductorMaterial(String conductorMaterial) {
        this.conductorMaterial = conductorMaterial;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getFaultHistory() {
        return faultHistory;
    }

    public void setFaultHistory(Integer faultHistory) {
        this.faultHistory = faultHistory;
    }

    public Integer getLengthOfCableSectionMeters() {
        return lengthOfCableSectionMeters;
    }

    public void setLengthOfCableSectionMeters(Integer lengthOfCableSectionMeters) {
        this.lengthOfCableSectionMeters = lengthOfCableSectionMeters;
    }

    public Integer getSectionRating() {
        return sectionRating;
    }

    public void setSectionRating(Integer sectionRating) {
        this.sectionRating = sectionRating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(Integer numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public String getNetPerformanceCostOfFailureEuro() {
        return netPerformanceCostOfFailureEuro;
    }

    public void setNetPerformanceCostOfFailureEuro(String netPerformanceCostOfFailureEuro) {
        this.netPerformanceCostOfFailureEuro = netPerformanceCostOfFailureEuro;
    }

    public Integer getRepairTimeHour() {
        return repairTimeHour;
    }

    public void setRepairTimeHour(Integer repairTimeHour) {
        this.repairTimeHour = repairTimeHour;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetUGCableDTO)) {
            return false;
        }

        AssetUGCableDTO assetUGCableDTO = (AssetUGCableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetUGCableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetUGCableDTO{" +
            "id=" + getId() +
            ", sectionLabel='" + getSectionLabel() + "'" +
            ", circuitId=" + getCircuitId() +
            ", conductorCrossSectionalArea=" + getConductorCrossSectionalArea() +
            ", sheathMaterial='" + getSheathMaterial() + "'" +
            ", designVoltage='" + getDesignVoltage() + "'" +
            ", operatingVoltage='" + getOperatingVoltage() + "'" +
            ", insulationTypeSheath='" + getInsulationTypeSheath() + "'" +
            ", conductorMaterial='" + getConductorMaterial() + "'" +
            ", age=" + getAge() +
            ", faultHistory=" + getFaultHistory() +
            ", lengthOfCableSectionMeters=" + getLengthOfCableSectionMeters() +
            ", sectionRating=" + getSectionRating() +
            ", type='" + getType() + "'" +
            ", numberOfCores=" + getNumberOfCores() +
            ", netPerformanceCostOfFailureEuro='" + getNetPerformanceCostOfFailureEuro() + "'" +
            ", repairTimeHour=" + getRepairTimeHour() +
            ", network=" + getNetwork() +
            "}";
    }
}
