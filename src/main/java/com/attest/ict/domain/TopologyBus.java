package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TopologyBus.
 */
@Entity
@Table(name = "topology_bus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TopologyBus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "power_line_branch")
    private String powerLineBranch;

    @Column(name = "bus_name_1")
    private String busName1;

    @Column(name = "bus_name_2")
    private String busName2;

    @OneToMany(mappedBy = "powerLineBranchParent", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "powerLineBranchParent" }, allowSetters = true)
    private Set<Topology> topologies = new HashSet<>();

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

    public TopologyBus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowerLineBranch() {
        return this.powerLineBranch;
    }

    public TopologyBus powerLineBranch(String powerLineBranch) {
        this.setPowerLineBranch(powerLineBranch);
        return this;
    }

    public void setPowerLineBranch(String powerLineBranch) {
        this.powerLineBranch = powerLineBranch;
    }

    public String getBusName1() {
        return this.busName1;
    }

    public TopologyBus busName1(String busName1) {
        this.setBusName1(busName1);
        return this;
    }

    public void setBusName1(String busName1) {
        this.busName1 = busName1;
    }

    public String getBusName2() {
        return this.busName2;
    }

    public TopologyBus busName2(String busName2) {
        this.setBusName2(busName2);
        return this;
    }

    public void setBusName2(String busName2) {
        this.busName2 = busName2;
    }

    public Set<Topology> getTopologies() {
        return this.topologies;
    }

    public void setTopologies(Set<Topology> topologies) {
        if (this.topologies != null) {
            this.topologies.forEach(i -> i.setPowerLineBranchParent(null));
        }
        if (topologies != null) {
            topologies.forEach(i -> i.setPowerLineBranchParent(this));
        }
        this.topologies = topologies;
    }

    public TopologyBus topologies(Set<Topology> topologies) {
        this.setTopologies(topologies);
        return this;
    }

    public TopologyBus addTopology(Topology topology) {
        this.topologies.add(topology);
        topology.setPowerLineBranchParent(this);
        return this;
    }

    public TopologyBus removeTopology(Topology topology) {
        this.topologies.remove(topology);
        topology.setPowerLineBranchParent(null);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public TopologyBus network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopologyBus)) {
            return false;
        }
        return id != null && id.equals(((TopologyBus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopologyBus{" +
            "id=" + getId() +
            ", powerLineBranch='" + getPowerLineBranch() + "'" +
            ", busName1='" + getBusName1() + "'" +
            ", busName2='" + getBusName2() + "'" +
            "}";
    }
}
