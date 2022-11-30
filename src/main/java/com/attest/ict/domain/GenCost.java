package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GenCost.
 */
@Entity
@Table(name = "gen_cost")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "model")
    private Integer model;

    @Column(name = "startup")
    private Double startup;

    @Column(name = "shutdown")
    private Double shutdown;

    @Column(name = "n_cost")
    private Long nCost;

    @Column(name = "cost_pf")
    private String costPF;

    @Column(name = "cost_qf")
    private String costQF;

    @JsonIgnoreProperties(value = { "genElVals", "generatorExtension", "genTag", "genCost", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Generator generator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenCost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModel() {
        return this.model;
    }

    public GenCost model(Integer model) {
        this.setModel(model);
        return this;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Double getStartup() {
        return this.startup;
    }

    public GenCost startup(Double startup) {
        this.setStartup(startup);
        return this;
    }

    public void setStartup(Double startup) {
        this.startup = startup;
    }

    public Double getShutdown() {
        return this.shutdown;
    }

    public GenCost shutdown(Double shutdown) {
        this.setShutdown(shutdown);
        return this;
    }

    public void setShutdown(Double shutdown) {
        this.shutdown = shutdown;
    }

    public Long getnCost() {
        return this.nCost;
    }

    public GenCost nCost(Long nCost) {
        this.setnCost(nCost);
        return this;
    }

    public void setnCost(Long nCost) {
        this.nCost = nCost;
    }

    public String getCostPF() {
        return this.costPF;
    }

    public GenCost costPF(String costPF) {
        this.setCostPF(costPF);
        return this;
    }

    public void setCostPF(String costPF) {
        this.costPF = costPF;
    }

    public String getCostQF() {
        return this.costQF;
    }

    public GenCost costQF(String costQF) {
        this.setCostQF(costQF);
        return this;
    }

    public void setCostQF(String costQF) {
        this.costQF = costQF;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public GenCost generator(Generator generator) {
        this.setGenerator(generator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenCost)) {
            return false;
        }
        return id != null && id.equals(((GenCost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenCost{" +
            "id=" + getId() +
            ", model=" + getModel() +
            ", startup=" + getStartup() +
            ", shutdown=" + getShutdown() +
            ", nCost=" + getnCost() +
            ", costPF='" + getCostPF() + "'" +
            ", costQF='" + getCostQF() + "'" +
            "}";
    }
}
