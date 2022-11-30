package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GenTag.
 */
@Entity
@Table(name = "gen_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gen_tag")
    private String genTag;

    @JsonIgnoreProperties(value = { "genElVals", "generatorExtension", "genTag", "genCost", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Generator generator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenTag() {
        return this.genTag;
    }

    public GenTag genTag(String genTag) {
        this.setGenTag(genTag);
        return this;
    }

    public void setGenTag(String genTag) {
        this.genTag = genTag;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public GenTag generator(Generator generator) {
        this.setGenerator(generator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenTag)) {
            return false;
        }
        return id != null && id.equals(((GenTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenTag{" +
            "id=" + getId() +
            ", genTag='" + getGenTag() + "'" +
            "}";
    }
}
