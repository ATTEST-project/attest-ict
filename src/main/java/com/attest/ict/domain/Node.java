package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Node.
 */
@Entity
@Table(name = "node")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "network_id")
    private String networkId;

    @Column(name = "load_id")
    private Long loadId;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Node id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetworkId() {
        return this.networkId;
    }

    public Node networkId(String networkId) {
        this.setNetworkId(networkId);
        return this;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getLoadId() {
        return this.loadId;
    }

    public Node loadId(Long loadId) {
        this.setLoadId(loadId);
        return this;
    }

    public void setLoadId(Long loadId) {
        this.loadId = loadId;
    }

    public String getName() {
        return this.name;
    }

    public Node name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        return id != null && id.equals(((Node) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Node{" +
            "id=" + getId() +
            ", networkId='" + getNetworkId() + "'" +
            ", loadId=" + getLoadId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
