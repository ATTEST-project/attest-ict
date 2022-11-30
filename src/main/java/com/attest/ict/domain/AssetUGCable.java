package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetUGCable.
 */
@Entity
@Table(name = "asset_ug_cable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetUGCable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "section_label")
    private String sectionLabel;

    @Column(name = "circuit_id")
    private Long circuitId;

    @Column(name = "conductor_cross_sectional_area")
    private Integer conductorCrossSectionalArea;

    @Column(name = "sheath_material")
    private String sheathMaterial;

    @Column(name = "design_voltage")
    private String designVoltage;

    @Column(name = "operating_voltage")
    private String operatingVoltage;

    @Column(name = "insulation_type_sheath")
    private String insulationTypeSheath;

    @Column(name = "conductor_material")
    private String conductorMaterial;

    @Column(name = "age")
    private Integer age;

    @Column(name = "fault_history")
    private Integer faultHistory;

    @Column(name = "length_of_cable_section_meters")
    private Integer lengthOfCableSectionMeters;

    @Column(name = "section_rating")
    private Integer sectionRating;

    @Column(name = "type")
    private String type;

    @Column(name = "number_of_cores")
    private Integer numberOfCores;

    @Column(name = "net_performance_cost_of_failure_euro")
    private String netPerformanceCostOfFailureEuro;

    @Column(name = "repair_time_hour")
    private Integer repairTimeHour;

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

    public AssetUGCable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionLabel() {
        return this.sectionLabel;
    }

    public AssetUGCable sectionLabel(String sectionLabel) {
        this.setSectionLabel(sectionLabel);
        return this;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public Long getCircuitId() {
        return this.circuitId;
    }

    public AssetUGCable circuitId(Long circuitId) {
        this.setCircuitId(circuitId);
        return this;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
    }

    public Integer getConductorCrossSectionalArea() {
        return this.conductorCrossSectionalArea;
    }

    public AssetUGCable conductorCrossSectionalArea(Integer conductorCrossSectionalArea) {
        this.setConductorCrossSectionalArea(conductorCrossSectionalArea);
        return this;
    }

    public void setConductorCrossSectionalArea(Integer conductorCrossSectionalArea) {
        this.conductorCrossSectionalArea = conductorCrossSectionalArea;
    }

    public String getSheathMaterial() {
        return this.sheathMaterial;
    }

    public AssetUGCable sheathMaterial(String sheathMaterial) {
        this.setSheathMaterial(sheathMaterial);
        return this;
    }

    public void setSheathMaterial(String sheathMaterial) {
        this.sheathMaterial = sheathMaterial;
    }

    public String getDesignVoltage() {
        return this.designVoltage;
    }

    public AssetUGCable designVoltage(String designVoltage) {
        this.setDesignVoltage(designVoltage);
        return this;
    }

    public void setDesignVoltage(String designVoltage) {
        this.designVoltage = designVoltage;
    }

    public String getOperatingVoltage() {
        return this.operatingVoltage;
    }

    public AssetUGCable operatingVoltage(String operatingVoltage) {
        this.setOperatingVoltage(operatingVoltage);
        return this;
    }

    public void setOperatingVoltage(String operatingVoltage) {
        this.operatingVoltage = operatingVoltage;
    }

    public String getInsulationTypeSheath() {
        return this.insulationTypeSheath;
    }

    public AssetUGCable insulationTypeSheath(String insulationTypeSheath) {
        this.setInsulationTypeSheath(insulationTypeSheath);
        return this;
    }

    public void setInsulationTypeSheath(String insulationTypeSheath) {
        this.insulationTypeSheath = insulationTypeSheath;
    }

    public String getConductorMaterial() {
        return this.conductorMaterial;
    }

    public AssetUGCable conductorMaterial(String conductorMaterial) {
        this.setConductorMaterial(conductorMaterial);
        return this;
    }

    public void setConductorMaterial(String conductorMaterial) {
        this.conductorMaterial = conductorMaterial;
    }

    public Integer getAge() {
        return this.age;
    }

    public AssetUGCable age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getFaultHistory() {
        return this.faultHistory;
    }

    public AssetUGCable faultHistory(Integer faultHistory) {
        this.setFaultHistory(faultHistory);
        return this;
    }

    public void setFaultHistory(Integer faultHistory) {
        this.faultHistory = faultHistory;
    }

    public Integer getLengthOfCableSectionMeters() {
        return this.lengthOfCableSectionMeters;
    }

    public AssetUGCable lengthOfCableSectionMeters(Integer lengthOfCableSectionMeters) {
        this.setLengthOfCableSectionMeters(lengthOfCableSectionMeters);
        return this;
    }

    public void setLengthOfCableSectionMeters(Integer lengthOfCableSectionMeters) {
        this.lengthOfCableSectionMeters = lengthOfCableSectionMeters;
    }

    public Integer getSectionRating() {
        return this.sectionRating;
    }

    public AssetUGCable sectionRating(Integer sectionRating) {
        this.setSectionRating(sectionRating);
        return this;
    }

    public void setSectionRating(Integer sectionRating) {
        this.sectionRating = sectionRating;
    }

    public String getType() {
        return this.type;
    }

    public AssetUGCable type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumberOfCores() {
        return this.numberOfCores;
    }

    public AssetUGCable numberOfCores(Integer numberOfCores) {
        this.setNumberOfCores(numberOfCores);
        return this;
    }

    public void setNumberOfCores(Integer numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public String getNetPerformanceCostOfFailureEuro() {
        return this.netPerformanceCostOfFailureEuro;
    }

    public AssetUGCable netPerformanceCostOfFailureEuro(String netPerformanceCostOfFailureEuro) {
        this.setNetPerformanceCostOfFailureEuro(netPerformanceCostOfFailureEuro);
        return this;
    }

    public void setNetPerformanceCostOfFailureEuro(String netPerformanceCostOfFailureEuro) {
        this.netPerformanceCostOfFailureEuro = netPerformanceCostOfFailureEuro;
    }

    public Integer getRepairTimeHour() {
        return this.repairTimeHour;
    }

    public AssetUGCable repairTimeHour(Integer repairTimeHour) {
        this.setRepairTimeHour(repairTimeHour);
        return this;
    }

    public void setRepairTimeHour(Integer repairTimeHour) {
        this.repairTimeHour = repairTimeHour;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public AssetUGCable network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetUGCable)) {
            return false;
        }
        return id != null && id.equals(((AssetUGCable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetUGCable{" +
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
            "}";
    }
}
