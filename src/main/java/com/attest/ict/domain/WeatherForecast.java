package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WeatherForecast.
 */
@Entity
@Table(name = "weather_forecast")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeatherForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "solar_profile")
    private Double solarProfile;

    @Column(name = "outside_temp")
    private Double outsideTemp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeatherForecast id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSolarProfile() {
        return this.solarProfile;
    }

    public WeatherForecast solarProfile(Double solarProfile) {
        this.setSolarProfile(solarProfile);
        return this;
    }

    public void setSolarProfile(Double solarProfile) {
        this.solarProfile = solarProfile;
    }

    public Double getOutsideTemp() {
        return this.outsideTemp;
    }

    public WeatherForecast outsideTemp(Double outsideTemp) {
        this.setOutsideTemp(outsideTemp);
        return this;
    }

    public void setOutsideTemp(Double outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherForecast)) {
            return false;
        }
        return id != null && id.equals(((WeatherForecast) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeatherForecast{" +
            "id=" + getId() +
            ", solarProfile=" + getSolarProfile() +
            ", outsideTemp=" + getOutsideTemp() +
            "}";
    }
}
