package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoltageLevel.
 */
@Entity
@Table(name = "voltage_level")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VoltageLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "v_1")
    private Double v1;

    @Column(name = "v_2")
    private Double v2;

    @Column(name = "v_3")
    private Double v3;

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
    @OneToOne
    @JoinColumn(unique = true)
    private Network network;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoltageLevel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getv1() {
        return this.v1;
    }

    public VoltageLevel v1(Double v1) {
        this.setv1(v1);
        return this;
    }

    public void setv1(Double v1) {
        this.v1 = v1;
    }

    public Double getv2() {
        return this.v2;
    }

    public VoltageLevel v2(Double v2) {
        this.setv2(v2);
        return this;
    }

    public void setv2(Double v2) {
        this.v2 = v2;
    }

    public Double getv3() {
        return this.v3;
    }

    public VoltageLevel v3(Double v3) {
        this.setv3(v3);
        return this;
    }

    public void setv3(Double v3) {
        this.v3 = v3;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public VoltageLevel network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoltageLevel)) {
            return false;
        }
        return id != null && id.equals(((VoltageLevel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoltageLevel{" +
            "id=" + getId() +
            ", v1=" + getv1() +
            ", v2=" + getv2() +
            ", v3=" + getv3() +
            "}";
    }
}
