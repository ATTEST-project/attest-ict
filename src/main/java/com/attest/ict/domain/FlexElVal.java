package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlexElVal.
 */
@Entity
@Table(name = "flex_el_val")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlexElVal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "min")
    private Integer min;

    @Column(name = "pfmax_up")
    private Double pfmaxUp;

    @Column(name = "pfmax_dn")
    private Double pfmaxDn;

    @Column(name = "qfmax_up")
    private Double qfmaxUp;

    @Column(name = "qfmax_dn")
    private Double qfmaxDn;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "flexElVals", "flexCosts", "network" }, allowSetters = true)
    private FlexProfile flexProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlexElVal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public FlexElVal busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Integer getHour() {
        return this.hour;
    }

    public FlexElVal hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return this.min;
    }

    public FlexElVal min(Integer min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getPfmaxUp() {
        return this.pfmaxUp;
    }

    public FlexElVal pfmaxUp(Double pfmaxUp) {
        this.setPfmaxUp(pfmaxUp);
        return this;
    }

    public void setPfmaxUp(Double pfmaxUp) {
        this.pfmaxUp = pfmaxUp;
    }

    public Double getPfmaxDn() {
        return this.pfmaxDn;
    }

    public FlexElVal pfmaxDn(Double pfmaxDn) {
        this.setPfmaxDn(pfmaxDn);
        return this;
    }

    public void setPfmaxDn(Double pfmaxDn) {
        this.pfmaxDn = pfmaxDn;
    }

    public Double getQfmaxUp() {
        return this.qfmaxUp;
    }

    public FlexElVal qfmaxUp(Double qfmaxUp) {
        this.setQfmaxUp(qfmaxUp);
        return this;
    }

    public void setQfmaxUp(Double qfmaxUp) {
        this.qfmaxUp = qfmaxUp;
    }

    public Double getQfmaxDn() {
        return this.qfmaxDn;
    }

    public FlexElVal qfmaxDn(Double qfmaxDn) {
        this.setQfmaxDn(qfmaxDn);
        return this;
    }

    public void setQfmaxDn(Double qfmaxDn) {
        this.qfmaxDn = qfmaxDn;
    }

    public FlexProfile getFlexProfile() {
        return this.flexProfile;
    }

    public void setFlexProfile(FlexProfile flexProfile) {
        this.flexProfile = flexProfile;
    }

    public FlexElVal flexProfile(FlexProfile flexProfile) {
        this.setFlexProfile(flexProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexElVal)) {
            return false;
        }
        return id != null && id.equals(((FlexElVal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexElVal{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", pfmaxUp=" + getPfmaxUp() +
            ", pfmaxDn=" + getPfmaxDn() +
            ", qfmaxUp=" + getQfmaxUp() +
            ", qfmaxDn=" + getQfmaxDn() +
            "}";
    }
}
