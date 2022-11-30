package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.AssetTransformer} entity.
 */
public class AssetTransformerDTO implements Serializable {

    private Long id;

    private Long busNum;

    private String voltageRatio;

    private String insulationMedium;

    private String type;

    private String indoorOutdoor;

    private Integer annualMaxLoadKva;

    private Integer age;

    private String externalCondition;

    private Integer ratingKva;

    private Integer numConnectedCustomers;

    private Integer numSensitiveCustomers;

    private String backupSupply;

    private Long costOfFailureEuro;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getVoltageRatio() {
        return voltageRatio;
    }

    public void setVoltageRatio(String voltageRatio) {
        this.voltageRatio = voltageRatio;
    }

    public String getInsulationMedium() {
        return insulationMedium;
    }

    public void setInsulationMedium(String insulationMedium) {
        this.insulationMedium = insulationMedium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndoorOutdoor() {
        return indoorOutdoor;
    }

    public void setIndoorOutdoor(String indoorOutdoor) {
        this.indoorOutdoor = indoorOutdoor;
    }

    public Integer getAnnualMaxLoadKva() {
        return annualMaxLoadKva;
    }

    public void setAnnualMaxLoadKva(Integer annualMaxLoadKva) {
        this.annualMaxLoadKva = annualMaxLoadKva;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getExternalCondition() {
        return externalCondition;
    }

    public void setExternalCondition(String externalCondition) {
        this.externalCondition = externalCondition;
    }

    public Integer getRatingKva() {
        return ratingKva;
    }

    public void setRatingKva(Integer ratingKva) {
        this.ratingKva = ratingKva;
    }

    public Integer getNumConnectedCustomers() {
        return numConnectedCustomers;
    }

    public void setNumConnectedCustomers(Integer numConnectedCustomers) {
        this.numConnectedCustomers = numConnectedCustomers;
    }

    public Integer getNumSensitiveCustomers() {
        return numSensitiveCustomers;
    }

    public void setNumSensitiveCustomers(Integer numSensitiveCustomers) {
        this.numSensitiveCustomers = numSensitiveCustomers;
    }

    public String getBackupSupply() {
        return backupSupply;
    }

    public void setBackupSupply(String backupSupply) {
        this.backupSupply = backupSupply;
    }

    public Long getCostOfFailureEuro() {
        return costOfFailureEuro;
    }

    public void setCostOfFailureEuro(Long costOfFailureEuro) {
        this.costOfFailureEuro = costOfFailureEuro;
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
        if (!(o instanceof AssetTransformerDTO)) {
            return false;
        }

        AssetTransformerDTO assetTransformerDTO = (AssetTransformerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetTransformerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetTransformerDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", voltageRatio='" + getVoltageRatio() + "'" +
            ", insulationMedium='" + getInsulationMedium() + "'" +
            ", type='" + getType() + "'" +
            ", indoorOutdoor='" + getIndoorOutdoor() + "'" +
            ", annualMaxLoadKva=" + getAnnualMaxLoadKva() +
            ", age=" + getAge() +
            ", externalCondition='" + getExternalCondition() + "'" +
            ", ratingKva=" + getRatingKva() +
            ", numConnectedCustomers=" + getNumConnectedCustomers() +
            ", numSensitiveCustomers=" + getNumSensitiveCustomers() +
            ", backupSupply='" + getBackupSupply() + "'" +
            ", costOfFailureEuro=" + getCostOfFailureEuro() +
            ", network=" + getNetwork() +
            "}";
    }
}
