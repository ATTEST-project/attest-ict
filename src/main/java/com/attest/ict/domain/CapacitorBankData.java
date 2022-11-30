package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CapacitorBankData.
 */
@Entity
@Table(name = "capacitor_bank_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CapacitorBankData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "node_id")
    private String nodeId;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "qnom")
    private Double qnom;

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

    public CapacitorBankData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public CapacitorBankData busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public CapacitorBankData nodeId(String nodeId) {
        this.setNodeId(nodeId);
        return this;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getBankId() {
        return this.bankId;
    }

    public CapacitorBankData bankId(String bankId) {
        this.setBankId(bankId);
        return this;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Double getQnom() {
        return this.qnom;
    }

    public CapacitorBankData qnom(Double qnom) {
        this.setQnom(qnom);
        return this;
    }

    public void setQnom(Double qnom) {
        this.qnom = qnom;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public CapacitorBankData network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CapacitorBankData)) {
            return false;
        }
        return id != null && id.equals(((CapacitorBankData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CapacitorBankData{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", nodeId='" + getNodeId() + "'" +
            ", bankId='" + getBankId() + "'" +
            ", qnom=" + getQnom() +
            "}";
    }
}
