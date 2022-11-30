package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DsoTsoConnection.
 */
@Entity
@Table(name = "dso_tso_connection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DsoTsoConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tso_network_name")
    private String tsoNetworkName;

    @Column(name = "dso_bus_num")
    private Long dsoBusNum;

    @Column(name = "tso_bus_num")
    private Long tsoBusNum;

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
    private Network dsoNetwork;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DsoTsoConnection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTsoNetworkName() {
        return this.tsoNetworkName;
    }

    public DsoTsoConnection tsoNetworkName(String tsoNetworkName) {
        this.setTsoNetworkName(tsoNetworkName);
        return this;
    }

    public void setTsoNetworkName(String tsoNetworkName) {
        this.tsoNetworkName = tsoNetworkName;
    }

    public Long getDsoBusNum() {
        return this.dsoBusNum;
    }

    public DsoTsoConnection dsoBusNum(Long dsoBusNum) {
        this.setDsoBusNum(dsoBusNum);
        return this;
    }

    public void setDsoBusNum(Long dsoBusNum) {
        this.dsoBusNum = dsoBusNum;
    }

    public Long getTsoBusNum() {
        return this.tsoBusNum;
    }

    public DsoTsoConnection tsoBusNum(Long tsoBusNum) {
        this.setTsoBusNum(tsoBusNum);
        return this;
    }

    public void setTsoBusNum(Long tsoBusNum) {
        this.tsoBusNum = tsoBusNum;
    }

    public Network getDsoNetwork() {
        return this.dsoNetwork;
    }

    public void setDsoNetwork(Network network) {
        this.dsoNetwork = network;
    }

    public DsoTsoConnection dsoNetwork(Network network) {
        this.setDsoNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DsoTsoConnection)) {
            return false;
        }
        return id != null && id.equals(((DsoTsoConnection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DsoTsoConnection{" +
            "id=" + getId() +
            ", tsoNetworkName='" + getTsoNetworkName() + "'" +
            ", dsoBusNum=" + getDsoBusNum() +
            ", tsoBusNum=" + getTsoBusNum() +
            "}";
    }
}
