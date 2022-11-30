package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetTransformer.
 */
@Entity
@Table(name = "asset_transformer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetTransformer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "voltage_ratio")
    private String voltageRatio;

    @Column(name = "insulation_medium")
    private String insulationMedium;

    @Column(name = "type")
    private String type;

    @Column(name = "indoor_outdoor")
    private String indoorOutdoor;

    @Column(name = "annual_max_load_kva")
    private Integer annualMaxLoadKva;

    @Column(name = "age")
    private Integer age;

    @Column(name = "external_condition")
    private String externalCondition;

    @Column(name = "rating_kva")
    private Integer ratingKva;

    @Column(name = "num_connected_customers")
    private Integer numConnectedCustomers;

    @Column(name = "num_sensitive_customers")
    private Integer numSensitiveCustomers;

    @Column(name = "backup_supply")
    private String backupSupply;

    @Column(name = "cost_of_failure_euro")
    private Long costOfFailureEuro;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "buses",
            "generators",
            "branches",
            "storages",
            "transformers",
            "capacitors",
            "inputFiles",
            "assetUgCables",
            "assetTransformers",
            "billingConsumptions",
            "billingDers",
            "lineCables",
            "genProfiles",
            "loadProfiles",
            "flexProfiles",
            "transfProfiles",
            "branchProfiles",
            "topologyBuses",
            "dsoTsoConnections",
            "baseMVA",
            "voltageLevel",
            "simulations",
        },
        allowSetters = true
    )
    private Network network;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetTransformer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public AssetTransformer busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getVoltageRatio() {
        return this.voltageRatio;
    }

    public AssetTransformer voltageRatio(String voltageRatio) {
        this.setVoltageRatio(voltageRatio);
        return this;
    }

    public void setVoltageRatio(String voltageRatio) {
        this.voltageRatio = voltageRatio;
    }

    public String getInsulationMedium() {
        return this.insulationMedium;
    }

    public AssetTransformer insulationMedium(String insulationMedium) {
        this.setInsulationMedium(insulationMedium);
        return this;
    }

    public void setInsulationMedium(String insulationMedium) {
        this.insulationMedium = insulationMedium;
    }

    public String getType() {
        return this.type;
    }

    public AssetTransformer type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndoorOutdoor() {
        return this.indoorOutdoor;
    }

    public AssetTransformer indoorOutdoor(String indoorOutdoor) {
        this.setIndoorOutdoor(indoorOutdoor);
        return this;
    }

    public void setIndoorOutdoor(String indoorOutdoor) {
        this.indoorOutdoor = indoorOutdoor;
    }

    public Integer getAnnualMaxLoadKva() {
        return this.annualMaxLoadKva;
    }

    public AssetTransformer annualMaxLoadKva(Integer annualMaxLoadKva) {
        this.setAnnualMaxLoadKva(annualMaxLoadKva);
        return this;
    }

    public void setAnnualMaxLoadKva(Integer annualMaxLoadKva) {
        this.annualMaxLoadKva = annualMaxLoadKva;
    }

    public Integer getAge() {
        return this.age;
    }

    public AssetTransformer age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getExternalCondition() {
        return this.externalCondition;
    }

    public AssetTransformer externalCondition(String externalCondition) {
        this.setExternalCondition(externalCondition);
        return this;
    }

    public void setExternalCondition(String externalCondition) {
        this.externalCondition = externalCondition;
    }

    public Integer getRatingKva() {
        return this.ratingKva;
    }

    public AssetTransformer ratingKva(Integer ratingKva) {
        this.setRatingKva(ratingKva);
        return this;
    }

    public void setRatingKva(Integer ratingKva) {
        this.ratingKva = ratingKva;
    }

    public Integer getNumConnectedCustomers() {
        return this.numConnectedCustomers;
    }

    public AssetTransformer numConnectedCustomers(Integer numConnectedCustomers) {
        this.setNumConnectedCustomers(numConnectedCustomers);
        return this;
    }

    public void setNumConnectedCustomers(Integer numConnectedCustomers) {
        this.numConnectedCustomers = numConnectedCustomers;
    }

    public Integer getNumSensitiveCustomers() {
        return this.numSensitiveCustomers;
    }

    public AssetTransformer numSensitiveCustomers(Integer numSensitiveCustomers) {
        this.setNumSensitiveCustomers(numSensitiveCustomers);
        return this;
    }

    public void setNumSensitiveCustomers(Integer numSensitiveCustomers) {
        this.numSensitiveCustomers = numSensitiveCustomers;
    }

    public String getBackupSupply() {
        return this.backupSupply;
    }

    public AssetTransformer backupSupply(String backupSupply) {
        this.setBackupSupply(backupSupply);
        return this;
    }

    public void setBackupSupply(String backupSupply) {
        this.backupSupply = backupSupply;
    }

    public Long getCostOfFailureEuro() {
        return this.costOfFailureEuro;
    }

    public AssetTransformer costOfFailureEuro(Long costOfFailureEuro) {
        this.setCostOfFailureEuro(costOfFailureEuro);
        return this;
    }

    public void setCostOfFailureEuro(Long costOfFailureEuro) {
        this.costOfFailureEuro = costOfFailureEuro;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public AssetTransformer network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetTransformer)) {
            return false;
        }
        return id != null && id.equals(((AssetTransformer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetTransformer{" +
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
            "}";
    }
}
