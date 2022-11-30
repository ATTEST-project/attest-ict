package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WindData.
 */
@Entity
@Table(name = "wind_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WindData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "hour")
    private Integer hour;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WindData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWindSpeed() {
        return this.windSpeed;
    }

    public WindData windSpeed(Double windSpeed) {
        this.setWindSpeed(windSpeed);
        return this;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getHour() {
        return this.hour;
    }

    public WindData hour(Integer hour) {
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
        if (!(o instanceof WindData)) {
            return false;
        }
        return id != null && id.equals(((WindData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WindData{" +
            "id=" + getId() +
            ", windSpeed=" + getWindSpeed() +
            ", hour=" + getHour() +
            "}";
    }
}
