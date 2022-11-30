package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Topology.
 */
@Entity
@Table(name = "topology")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topology implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "power_line_branch")
    private String powerLineBranch;

    @Column(name = "p_1")
    private String p1;

    @Column(name = "p_2")
    private String p2;

    @ManyToOne
    @JsonIgnoreProperties(value = { "topologies", "network" }, allowSetters = true)
    private TopologyBus powerLineBranchParent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Topology id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowerLineBranch() {
        return this.powerLineBranch;
    }

    public Topology powerLineBranch(String powerLineBranch) {
        this.setPowerLineBranch(powerLineBranch);
        return this;
    }

    public void setPowerLineBranch(String powerLineBranch) {
        this.powerLineBranch = powerLineBranch;
    }

    public String getp1() {
        return this.p1;
    }

    public Topology p1(String p1) {
        this.setp1(p1);
        return this;
    }

    public void setp1(String p1) {
        this.p1 = p1;
    }

    public String getp2() {
        return this.p2;
    }

    public Topology p2(String p2) {
        this.setp2(p2);
        return this;
    }

    public void setp2(String p2) {
        this.p2 = p2;
    }

    public TopologyBus getPowerLineBranchParent() {
        return this.powerLineBranchParent;
    }

    public void setPowerLineBranchParent(TopologyBus topologyBus) {
        this.powerLineBranchParent = topologyBus;
    }

    public Topology powerLineBranchParent(TopologyBus topologyBus) {
        this.setPowerLineBranchParent(topologyBus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Topology)) {
            return false;
        }
        return id != null && id.equals(((Topology) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Topology{" +
            "id=" + getId() +
            ", powerLineBranch='" + getPowerLineBranch() + "'" +
            ", p1='" + getp1() + "'" +
            ", p2='" + getp2() + "'" +
            "}";
    }
}
