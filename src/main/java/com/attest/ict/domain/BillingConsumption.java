package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillingConsumption.
 */
@Entity
@Table(name = "billing_consumption")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillingConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "type")
    private String type;

    @Column(name = "total_energy_consumption")
    private Long totalEnergyConsumption;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

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

    public BillingConsumption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public BillingConsumption busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getType() {
        return this.type;
    }

    public BillingConsumption type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalEnergyConsumption() {
        return this.totalEnergyConsumption;
    }

    public BillingConsumption totalEnergyConsumption(Long totalEnergyConsumption) {
        this.setTotalEnergyConsumption(totalEnergyConsumption);
        return this;
    }

    public void setTotalEnergyConsumption(Long totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public BillingConsumption unitOfMeasure(String unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public BillingConsumption network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingConsumption)) {
            return false;
        }
        return id != null && id.equals(((BillingConsumption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingConsumption{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", type='" + getType() + "'" +
            ", totalEnergyConsumption=" + getTotalEnergyConsumption() +
            ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
            "}";
    }
}
