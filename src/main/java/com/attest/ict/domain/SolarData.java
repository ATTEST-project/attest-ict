package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SolarData.
 */
@Entity
@Table(name = "solar_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SolarData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "p")
    private Double p;

    @Column(name = "hour")
    private Integer hour;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SolarData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getP() {
        return this.p;
    }

    public SolarData p(Double p) {
        this.setP(p);
        return this;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Integer getHour() {
        return this.hour;
    }

    public SolarData hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolarData)) {
            return false;
        }
        return id != null && id.equals(((SolarData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolarData{" +
            "id=" + getId() +
            ", p=" + getP() +
            ", hour=" + getHour() +
            "}";
    }
}
