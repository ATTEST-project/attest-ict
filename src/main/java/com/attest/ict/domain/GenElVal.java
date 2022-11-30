package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GenElVal.
 */
@Entity
@Table(name = "gen_el_val")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenElVal implements Serializable {

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

    @Column(name = "status")
    private Integer status;

    @Column(name = "voltage_magnitude")
    private Double voltageMagnitude;

    @Column(name = "gen_id_on_subst")
    private Long genIdOnSubst;

    @Column(name = "nominal_voltage")
    private String nominalVoltage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "genElVals", "network" }, allowSetters = true)
    private GenProfile genProfile;

    @ManyToOne
    @JsonIgnoreProperties(value = { "genElVals", "generatorExtension", "genTag", "genCost", "network" }, allowSetters = true)
    private Generator generator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenElVal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return this.hour;
    }

    public GenElVal hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return this.min;
    }

    public GenElVal min(Integer min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getP() {
        return this.p;
    }

    public GenElVal p(Double p) {
        this.setP(p);
        return this;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return this.q;
    }

    public GenElVal q(Double q) {
        this.setQ(q);
        return this;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Integer getStatus() {
        return this.status;
    }

    public GenElVal status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getVoltageMagnitude() {
        return this.voltageMagnitude;
    }

    public GenElVal voltageMagnitude(Double voltageMagnitude) {
        this.setVoltageMagnitude(voltageMagnitude);
        return this;
    }

    public void setVoltageMagnitude(Double voltageMagnitude) {
        this.voltageMagnitude = voltageMagnitude;
    }

    public Long getGenIdOnSubst() {
        return this.genIdOnSubst;
    }

    public GenElVal genIdOnSubst(Long genIdOnSubst) {
        this.setGenIdOnSubst(genIdOnSubst);
        return this;
    }

    public void setGenIdOnSubst(Long genIdOnSubst) {
        this.genIdOnSubst = genIdOnSubst;
    }

    public String getNominalVoltage() {
        return this.nominalVoltage;
    }

    public GenElVal nominalVoltage(String nominalVoltage) {
        this.setNominalVoltage(nominalVoltage);
        return this;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public GenProfile getGenProfile() {
        return this.genProfile;
    }

    public void setGenProfile(GenProfile genProfile) {
        this.genProfile = genProfile;
    }

    public GenElVal genProfile(GenProfile genProfile) {
        this.setGenProfile(genProfile);
        return this;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public GenElVal generator(Generator generator) {
        this.setGenerator(generator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenElVal)) {
            return false;
        }
        return id != null && id.equals(((GenElVal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenElVal{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", status=" + getStatus() +
            ", voltageMagnitude=" + getVoltageMagnitude() +
            ", genIdOnSubst=" + getGenIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            "}";
    }
}
