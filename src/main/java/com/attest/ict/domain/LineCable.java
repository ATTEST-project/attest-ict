package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LineCable.
 */
@Entity
@Table(name = "line_cable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LineCable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fbus")
    private Long fbus;

    @Column(name = "tbus")
    private Long tbus;

    @Column(name = "length_km")
    private Double lengthKm;

    @Column(name = "type_of_installation")
    private String typeOfInstallation;

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

    public LineCable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFbus() {
        return this.fbus;
    }

    public LineCable fbus(Long fbus) {
        this.setFbus(fbus);
        return this;
    }

    public void setFbus(Long fbus) {
        this.fbus = fbus;
    }

    public Long getTbus() {
        return this.tbus;
    }

    public LineCable tbus(Long tbus) {
        this.setTbus(tbus);
        return this;
    }

    public void setTbus(Long tbus) {
        this.tbus = tbus;
    }

    public Double getLengthKm() {
        return this.lengthKm;
    }

    public LineCable lengthKm(Double lengthKm) {
        this.setLengthKm(lengthKm);
        return this;
    }

    public void setLengthKm(Double lengthKm) {
        this.lengthKm = lengthKm;
    }

    public String getTypeOfInstallation() {
        return this.typeOfInstallation;
    }

    public LineCable typeOfInstallation(String typeOfInstallation) {
        this.setTypeOfInstallation(typeOfInstallation);
        return this;
    }

    public void setTypeOfInstallation(String typeOfInstallation) {
        this.typeOfInstallation = typeOfInstallation;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public LineCable network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineCable)) {
            return false;
        }
        return id != null && id.equals(((LineCable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineCable{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", lengthKm=" + getLengthKm() +
            ", typeOfInstallation='" + getTypeOfInstallation() + "'" +
            "}";
    }
}
