package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillingDer.
 */
@Entity
@Table(name = "billing_der")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillingDer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "max_power_kw")
    private Long maxPowerKw;

    @Column(name = "type")
    private String type;

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

    public BillingDer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public BillingDer busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Long getMaxPowerKw() {
        return this.maxPowerKw;
    }

    public BillingDer maxPowerKw(Long maxPowerKw) {
        this.setMaxPowerKw(maxPowerKw);
        return this;
    }

    public void setMaxPowerKw(Long maxPowerKw) {
        this.maxPowerKw = maxPowerKw;
    }

    public String getType() {
        return this.type;
    }

    public BillingDer type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public BillingDer network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingDer)) {
            return false;
        }
        return id != null && id.equals(((BillingDer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingDer{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", maxPowerKw=" + getMaxPowerKw() +
            ", type='" + getType() + "'" +
            "}";
    }
}
