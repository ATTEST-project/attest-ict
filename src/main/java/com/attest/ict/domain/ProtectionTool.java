package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProtectionTool.
 */
@Entity
@Table(name = "protection_tool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProtectionTool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @JsonIgnoreProperties(value = { "transfElVals", "branchElVals", "branchExtension", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Branch branch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "loadELVals", "busName", "busExtension", "busCoordinate", "network" }, allowSetters = true)
    private Bus bus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProtectionTool id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ProtectionTool type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public ProtectionTool branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public ProtectionTool bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtectionTool)) {
            return false;
        }
        return id != null && id.equals(((ProtectionTool) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProtectionTool{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
