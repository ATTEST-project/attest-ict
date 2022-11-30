package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusExtension.
 */
@Entity
@Table(name = "bus_extension")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "has_gen")
    private Integer hasGen;

    @Column(name = "is_load")
    private Integer isLoad;

    @Column(name = "snom_mva")
    private Double snomMva;

    @Column(name = "sx")
    private Double sx;

    @Column(name = "sy")
    private Double sy;

    @Column(name = "gx")
    private Double gx;

    @Column(name = "gy")
    private Double gy;

    @Column(name = "status")
    private Integer status;

    @Column(name = "increment_cost")
    private Integer incrementCost;

    @Column(name = "decrement_cost")
    private Integer decrementCost;

    @Column(name = "m_rid")
    private String mRid;

    @JsonIgnoreProperties(value = { "loadELVals", "busName", "busExtension", "busCoordinate", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Bus bus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusExtension id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHasGen() {
        return this.hasGen;
    }

    public BusExtension hasGen(Integer hasGen) {
        this.setHasGen(hasGen);
        return this;
    }

    public void setHasGen(Integer hasGen) {
        this.hasGen = hasGen;
    }

    public Integer getIsLoad() {
        return this.isLoad;
    }

    public BusExtension isLoad(Integer isLoad) {
        this.setIsLoad(isLoad);
        return this;
    }

    public void setIsLoad(Integer isLoad) {
        this.isLoad = isLoad;
    }

    public Double getSnomMva() {
        return this.snomMva;
    }

    public BusExtension snomMva(Double snomMva) {
        this.setSnomMva(snomMva);
        return this;
    }

    public void setSnomMva(Double snomMva) {
        this.snomMva = snomMva;
    }

    public Double getSx() {
        return this.sx;
    }

    public BusExtension sx(Double sx) {
        this.setSx(sx);
        return this;
    }

    public void setSx(Double sx) {
        this.sx = sx;
    }

    public Double getSy() {
        return this.sy;
    }

    public BusExtension sy(Double sy) {
        this.setSy(sy);
        return this;
    }

    public void setSy(Double sy) {
        this.sy = sy;
    }

    public Double getGx() {
        return this.gx;
    }

    public BusExtension gx(Double gx) {
        this.setGx(gx);
        return this;
    }

    public void setGx(Double gx) {
        this.gx = gx;
    }

    public Double getGy() {
        return this.gy;
    }

    public BusExtension gy(Double gy) {
        this.setGy(gy);
        return this;
    }

    public void setGy(Double gy) {
        this.gy = gy;
    }

    public Integer getStatus() {
        return this.status;
    }

    public BusExtension status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIncrementCost() {
        return this.incrementCost;
    }

    public BusExtension incrementCost(Integer incrementCost) {
        this.setIncrementCost(incrementCost);
        return this;
    }

    public void setIncrementCost(Integer incrementCost) {
        this.incrementCost = incrementCost;
    }

    public Integer getDecrementCost() {
        return this.decrementCost;
    }

    public BusExtension decrementCost(Integer decrementCost) {
        this.setDecrementCost(decrementCost);
        return this;
    }

    public void setDecrementCost(Integer decrementCost) {
        this.decrementCost = decrementCost;
    }

    public String getmRid() {
        return this.mRid;
    }

    public BusExtension mRid(String mRid) {
        this.setmRid(mRid);
        return this;
    }

    public void setmRid(String mRid) {
        this.mRid = mRid;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public BusExtension bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusExtension)) {
            return false;
        }
        return id != null && id.equals(((BusExtension) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusExtension{" +
            "id=" + getId() +
            ", hasGen=" + getHasGen() +
            ", isLoad=" + getIsLoad() +
            ", snomMva=" + getSnomMva() +
            ", sx=" + getSx() +
            ", sy=" + getSy() +
            ", gx=" + getGx() +
            ", gy=" + getGy() +
            ", status=" + getStatus() +
            ", incrementCost=" + getIncrementCost() +
            ", decrementCost=" + getDecrementCost() +
            ", mRid='" + getmRid() + "'" +
            "}";
    }
}
