package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.BusName} entity.
 */
public class BusNameDTO implements Serializable {

    private Long id;

    private String busName;

    private BusDTO bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public BusDTO getBus() {
        return bus;
    }

    public void setBus(BusDTO bus) {
        this.bus = bus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusNameDTO)) {
            return false;
        }

        BusNameDTO busNameDTO = (BusNameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, busNameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusNameDTO{" +
            "id=" + getId() +
            ", busName='" + getBusName() + "'" +
            ", bus=" + getBus() +
            "}";
    }
}
