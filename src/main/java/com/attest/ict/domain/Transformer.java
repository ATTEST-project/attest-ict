package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transformer.
 */
@Entity
@Table(name = "transformer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transformer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fbus")
    private Long fbus;

    @Column(name = "tbus")
    private Long tbus;

    @Column(name = "min")
    private Double min;

    @Column(name = "max")
    private Double max;

    @Column(name = "total_taps")
    private Integer totalTaps;

    @Column(name = "tap")
    private Integer tap;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    @Column(name = "commissioning_year")
    private Integer commissioningYear;

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

    public Transformer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFbus() {
        return this.fbus;
    }

    public Transformer fbus(Long fbus) {
        this.setFbus(fbus);
        return this;
    }

    public void setFbus(Long fbus) {
        this.fbus = fbus;
    }

    public Long getTbus() {
        return this.tbus;
    }

    public Transformer tbus(Long tbus) {
        this.setTbus(tbus);
        return this;
    }

    public void setTbus(Long tbus) {
        this.tbus = tbus;
    }

    public Double getMin() {
        return this.min;
    }

    public Transformer min(Double min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return this.max;
    }

    public Transformer max(Double max) {
        this.setMax(max);
        return this;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Integer getTotalTaps() {
        return this.totalTaps;
    }

    public Transformer totalTaps(Integer totalTaps) {
        this.setTotalTaps(totalTaps);
        return this;
    }

    public void setTotalTaps(Integer totalTaps) {
        this.totalTaps = totalTaps;
    }

    public Integer getTap() {
        return this.tap;
    }

    public Transformer tap(Integer tap) {
        this.setTap(tap);
        return this;
    }

    public void setTap(Integer tap) {
        this.tap = tap;
    }

    public Integer getManufactureYear() {
        return this.manufactureYear;
    }

    public Transformer manufactureYear(Integer manufactureYear) {
        this.setManufactureYear(manufactureYear);
        return this;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Integer getCommissioningYear() {
        return this.commissioningYear;
    }

    public Transformer commissioningYear(Integer commissioningYear) {
        this.setCommissioningYear(commissioningYear);
        return this;
    }

    public void setCommissioningYear(Integer commissioningYear) {
        this.commissioningYear = commissioningYear;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Transformer network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transformer)) {
            return false;
        }
        return id != null && id.equals(((Transformer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transformer{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", min=" + getMin() +
            ", max=" + getMax() +
            ", totalTaps=" + getTotalTaps() +
            ", tap=" + getTap() +
            ", manufactureYear=" + getManufactureYear() +
            ", commissioningYear=" + getCommissioningYear() +
            "}";
    }
}
