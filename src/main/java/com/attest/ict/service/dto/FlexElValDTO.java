package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.FlexElVal} entity.
 */
public class FlexElValDTO implements Serializable {

    private Long id;

    private Long busNum;

    private Integer hour;

    private Integer min;

    private Double pfmaxUp;

    private Double pfmaxDn;

    private Double qfmaxUp;

    private Double qfmaxDn;

    private FlexProfileDTO flexProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
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

    public Double getPfmaxUp() {
        return pfmaxUp;
    }

    public void setPfmaxUp(Double pfmaxUp) {
        this.pfmaxUp = pfmaxUp;
    }

    public Double getPfmaxDn() {
        return pfmaxDn;
    }

    public void setPfmaxDn(Double pfmaxDn) {
        this.pfmaxDn = pfmaxDn;
    }

    public Double getQfmaxUp() {
        return qfmaxUp;
    }

    public void setQfmaxUp(Double qfmaxUp) {
        this.qfmaxUp = qfmaxUp;
    }

    public Double getQfmaxDn() {
        return qfmaxDn;
    }

    public void setQfmaxDn(Double qfmaxDn) {
        this.qfmaxDn = qfmaxDn;
    }

    public FlexProfileDTO getFlexProfile() {
        return flexProfile;
    }

    public void setFlexProfile(FlexProfileDTO flexProfile) {
        this.flexProfile = flexProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexElValDTO)) {
            return false;
        }

        FlexElValDTO flexElValDTO = (FlexElValDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flexElValDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexElValDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", pfmaxUp=" + getPfmaxUp() +
            ", pfmaxDn=" + getPfmaxDn() +
            ", qfmaxUp=" + getQfmaxUp() +
            ", qfmaxDn=" + getQfmaxDn() +
            ", flexProfile=" + getFlexProfile() +
            "}";
    }
}
