package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.WeatherForecast} entity.
 */
public class WeatherForecastDTO implements Serializable {

    private Long id;

    private Double solarProfile;

    private Double outsideTemp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSolarProfile() {
        return solarProfile;
    }

    public void setSolarProfile(Double solarProfile) {
        this.solarProfile = solarProfile;
    }

    public Double getOutsideTemp() {
        return outsideTemp;
    }

    public void setOutsideTemp(Double outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherForecastDTO)) {
            return false;
        }

        WeatherForecastDTO weatherForecastDTO = (WeatherForecastDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weatherForecastDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeatherForecastDTO{" +
            "id=" + getId() +
            ", solarProfile=" + getSolarProfile() +
            ", outsideTemp=" + getOutsideTemp() +
            "}";
    }
}
