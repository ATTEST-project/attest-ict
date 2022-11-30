package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlexCost.
 */
@Entity
@Table(name = "flex_cost")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlexCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "model")
    private Integer model;

    @Column(name = "n_cost")
    private Long nCost;

    @Column(name = "cost_pr")
    private Double costPr;

    @Column(name = "cost_qr")
    private Double costQr;

    @Column(name = "cost_pf")
    private String costPf;

    @Column(name = "cost_qf")
    private String costQf;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "flexElVals", "flexCosts", "network" }, allowSetters = true)
    private FlexProfile flexProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlexCost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public FlexCost busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Integer getModel() {
        return this.model;
    }

    public FlexCost model(Integer model) {
        this.setModel(model);
        return this;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Long getnCost() {
        return this.nCost;
    }

    public FlexCost nCost(Long nCost) {
        this.setnCost(nCost);
        return this;
    }

    public void setnCost(Long nCost) {
        this.nCost = nCost;
    }

    public Double getCostPr() {
        return this.costPr;
    }

    public FlexCost costPr(Double costPr) {
        this.setCostPr(costPr);
        return this;
    }

    public void setCostPr(Double costPr) {
        this.costPr = costPr;
    }

    public Double getCostQr() {
        return this.costQr;
    }

    public FlexCost costQr(Double costQr) {
        this.setCostQr(costQr);
        return this;
    }

    public void setCostQr(Double costQr) {
        this.costQr = costQr;
    }

    public String getCostPf() {
        return this.costPf;
    }

    public FlexCost costPf(String costPf) {
        this.setCostPf(costPf);
        return this;
    }

    public void setCostPf(String costPf) {
        this.costPf = costPf;
    }

    public String getCostQf() {
        return this.costQf;
    }

    public FlexCost costQf(String costQf) {
        this.setCostQf(costQf);
        return this;
    }

    public void setCostQf(String costQf) {
        this.costQf = costQf;
    }

    public FlexProfile getFlexProfile() {
        return this.flexProfile;
    }

    public void setFlexProfile(FlexProfile flexProfile) {
        this.flexProfile = flexProfile;
    }

    public FlexCost flexProfile(FlexProfile flexProfile) {
        this.setFlexProfile(flexProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexCost)) {
            return false;
        }
        return id != null && id.equals(((FlexCost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexCost{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", model=" + getModel() +
            ", nCost=" + getnCost() +
            ", costPr=" + getCostPr() +
            ", costQr=" + getCostQr() +
            ", costPf='" + getCostPf() + "'" +
            ", costQf='" + getCostQf() + "'" +
            "}";
    }
}
