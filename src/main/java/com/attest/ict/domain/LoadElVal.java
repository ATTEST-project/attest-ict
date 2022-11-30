package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LoadElVal.
 */
@Entity
@Table(name = "load_el_val")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LoadElVal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "min")
    private Integer min;

    @Column(name = "p")
    private Double p;

    @Column(name = "q")
    private Double q;

    @Column(name = "load_id_on_subst")
    private Long loadIdOnSubst;

    @Column(name = "nominal_voltage")
    private String nominalVoltage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "loadElVals", "network" }, allowSetters = true)
    private LoadProfile loadProfile;

    @ManyToOne
    @JsonIgnoreProperties(value = { "loadELVals", "busName", "busExtension", "busCoordinate", "network" }, allowSetters = true)
    private Bus bus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoadElVal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return this.hour;
    }

    public LoadElVal hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return this.min;
    }

    public LoadElVal min(Integer min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getP() {
        return this.p;
    }

    public LoadElVal p(Double p) {
        this.setP(p);
        return this;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return this.q;
    }

    public LoadElVal q(Double q) {
        this.setQ(q);
        return this;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Long getLoadIdOnSubst() {
        return this.loadIdOnSubst;
    }

    public LoadElVal loadIdOnSubst(Long loadIdOnSubst) {
        this.setLoadIdOnSubst(loadIdOnSubst);
        return this;
    }

    public void setLoadIdOnSubst(Long loadIdOnSubst) {
        this.loadIdOnSubst = loadIdOnSubst;
    }

    public String getNominalVoltage() {
        return this.nominalVoltage;
    }

    public LoadElVal nominalVoltage(String nominalVoltage) {
        this.setNominalVoltage(nominalVoltage);
        return this;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public LoadProfile getLoadProfile() {
        return this.loadProfile;
    }

    public void setLoadProfile(LoadProfile loadProfile) {
        this.loadProfile = loadProfile;
    }

    public LoadElVal loadProfile(LoadProfile loadProfile) {
        this.setLoadProfile(loadProfile);
        return this;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public LoadElVal bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadElVal)) {
            return false;
        }
        return id != null && id.equals(((LoadElVal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoadElVal{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", loadIdOnSubst=" + getLoadIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            "}";
    }
}
