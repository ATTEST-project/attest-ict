package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.WindData} entity.
 */
public class WindDataDTO implements Serializable {

    private Long id;

    private Double windSpeed;

    private Integer hour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WindDataDTO)) {
            return false;
        }

        WindDataDTO windDataDTO = (WindDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, windDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WindDataDTO{" +
            "id=" + getId() +
            ", windSpeed=" + getWindSpeed() +
            ", hour=" + getHour() +
            "}";
    }
}
