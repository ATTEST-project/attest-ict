package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.LoadElVal} entity.
 */
public class LoadElValDTO implements Serializable {

    private Long id;

    private Integer hour;

    private Integer min;

    private Double p;

    private Double q;

    private Long loadIdOnSubst;

    private String nominalVoltage;

    private LoadProfileDTO loadProfile;

    private BusDTO bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Long getLoadIdOnSubst() {
        return loadIdOnSubst;
    }

    public void setLoadIdOnSubst(Long loadIdOnSubst) {
        this.loadIdOnSubst = loadIdOnSubst;
    }

    public String getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public LoadProfileDTO getLoadProfile() {
        return loadProfile;
    }

    public void setLoadProfile(LoadProfileDTO loadProfile) {
        this.loadProfile = loadProfile;
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
        if (!(o instanceof LoadElValDTO)) {
            return false;
        }

        LoadElValDTO loadElValDTO = (LoadElValDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loadElValDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoadElValDTO{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", loadIdOnSubst=" + getLoadIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            ", loadProfile=" + getLoadProfile() +
            ", bus=" + getBus() +
            "}";
    }
}
