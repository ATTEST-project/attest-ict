package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.GenElVal} entity.
 */
public class GenElValDTO implements Serializable {

    private Long id;

    private Integer hour;

    private Integer min;

    private Double p;

    private Double q;

    private Integer status;

    private Double voltageMagnitude;

    private Long genIdOnSubst;

    private String nominalVoltage;

    private GenProfileDTO genProfile;

    private GeneratorDTO generator;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getVoltageMagnitude() {
        return voltageMagnitude;
    }

    public void setVoltageMagnitude(Double voltageMagnitude) {
        this.voltageMagnitude = voltageMagnitude;
    }

    public Long getGenIdOnSubst() {
        return genIdOnSubst;
    }

    public void setGenIdOnSubst(Long genIdOnSubst) {
        this.genIdOnSubst = genIdOnSubst;
    }

    public String getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public GenProfileDTO getGenProfile() {
        return genProfile;
    }

    public void setGenProfile(GenProfileDTO genProfile) {
        this.genProfile = genProfile;
    }

    public GeneratorDTO getGenerator() {
        return generator;
    }

    public void setGenerator(GeneratorDTO generator) {
        this.generator = generator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenElValDTO)) {
            return false;
        }

        GenElValDTO genElValDTO = (GenElValDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genElValDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenElValDTO{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", status=" + getStatus() +
            ", voltageMagnitude=" + getVoltageMagnitude() +
            ", genIdOnSubst=" + getGenIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            ", genProfile=" + getGenProfile() +
            ", generator=" + getGenerator() +
            "}";
    }
}
