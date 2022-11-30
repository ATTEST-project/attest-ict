package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GeneratorExtension.
 */
@Entity
@Table(name = "generator_extension")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GeneratorExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_gen")
    private Integer idGen;

    @Column(name = "status_curt")
    private Integer statusCurt;

    @Column(name = "dg_type")
    private Integer dgType;

    @JsonIgnoreProperties(value = { "genElVals", "generatorExtension", "genTag", "genCost", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Generator generator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GeneratorExtension id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdGen() {
        return this.idGen;
    }

    public GeneratorExtension idGen(Integer idGen) {
        this.setIdGen(idGen);
        return this;
    }

    public void setIdGen(Integer idGen) {
        this.idGen = idGen;
    }

    public Integer getStatusCurt() {
        return this.statusCurt;
    }

    public GeneratorExtension statusCurt(Integer statusCurt) {
        this.setStatusCurt(statusCurt);
        return this;
    }

    public void setStatusCurt(Integer statusCurt) {
        this.statusCurt = statusCurt;
    }

    public Integer getDgType() {
        return this.dgType;
    }

    public GeneratorExtension dgType(Integer dgType) {
        this.setDgType(dgType);
        return this;
    }

    public void setDgType(Integer dgType) {
        this.dgType = dgType;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public GeneratorExtension generator(Generator generator) {
        this.setGenerator(generator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeneratorExtension)) {
            return false;
        }
        return id != null && id.equals(((GeneratorExtension) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneratorExtension{" +
            "id=" + getId() +
            ", idGen=" + getIdGen() +
            ", statusCurt=" + getStatusCurt() +
            ", dgType=" + getDgType() +
            "}";
    }
}
