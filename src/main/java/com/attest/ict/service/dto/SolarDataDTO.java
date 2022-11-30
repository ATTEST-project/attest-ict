package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.SolarData} entity.
 */
public class SolarDataDTO implements Serializable {

    private Long id;

    private Double p;

    private Integer hour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
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
        if (!(o instanceof SolarDataDTO)) {
            return false;
        }

        SolarDataDTO solarDataDTO = (SolarDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, solarDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolarDataDTO{" +
            "id=" + getId() +
            ", p=" + getP() +
            ", hour=" + getHour() +
            "}";
    }
}
