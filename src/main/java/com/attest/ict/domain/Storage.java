package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Storage.
 */
@Entity
@Table(name = "storage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "ps")
    private Double ps;

    @Column(name = "qs")
    private Double qs;

    @Column(name = "energy")
    private Double energy;

    @Column(name = "e_rating")
    private Double eRating;

    @Column(name = "charge_rating")
    private Double chargeRating;

    @Column(name = "discharge_rating")
    private Double dischargeRating;

    @Column(name = "charge_efficiency")
    private Double chargeEfficiency;

    @Column(name = "thermal_rating")
    private Double thermalRating;

    @Column(name = "qmin")
    private Double qmin;

    @Column(name = "qmax")
    private Double qmax;

    @Column(name = "r")
    private Double r;

    @Column(name = "x")
    private Double x;

    @Column(name = "p_loss")
    private Double pLoss;

    @Column(name = "q_loss")
    private Double qLoss;

    @Column(name = "status")
    private Integer status;

    @Column(name = "soc_initial")
    private Double socInitial;

    @Column(name = "soc_min")
    private Double socMin;

    @Column(name = "soc_max")
    private Double socMax;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "buses",
            "generators",
            "branches",
            "storages",
            "transformers",
            "capacitors",
            "inputFiles",
            "assetUgCables",
            "assetTransformers",
            "billingConsumptions",
            "billingDers",
            "lineCables",
            "genProfiles",
            "loadProfiles",
            "flexProfiles",
            "transfProfiles",
            "branchProfiles",
            "topologyBuses",
            "dsoTsoConnections",
            "baseMVA",
            "voltageLevel",
            "simulations",
        },
        allowSetters = true
    )
    private Network network;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Storage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public Storage busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Double getPs() {
        return this.ps;
    }

    public Storage ps(Double ps) {
        this.setPs(ps);
        return this;
    }

    public void setPs(Double ps) {
        this.ps = ps;
    }

    public Double getQs() {
        return this.qs;
    }

    public Storage qs(Double qs) {
        this.setQs(qs);
        return this;
    }

    public void setQs(Double qs) {
        this.qs = qs;
    }

    public Double getEnergy() {
        return this.energy;
    }

    public Storage energy(Double energy) {
        this.setEnergy(energy);
        return this;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double geteRating() {
        return this.eRating;
    }

    public Storage eRating(Double eRating) {
        this.seteRating(eRating);
        return this;
    }

    public void seteRating(Double eRating) {
        this.eRating = eRating;
    }

    public Double getChargeRating() {
        return this.chargeRating;
    }

    public Storage chargeRating(Double chargeRating) {
        this.setChargeRating(chargeRating);
        return this;
    }

    public void setChargeRating(Double chargeRating) {
        this.chargeRating = chargeRating;
    }

    public Double getDischargeRating() {
        return this.dischargeRating;
    }

    public Storage dischargeRating(Double dischargeRating) {
        this.setDischargeRating(dischargeRating);
        return this;
    }

    public void setDischargeRating(Double dischargeRating) {
        this.dischargeRating = dischargeRating;
    }

    public Double getChargeEfficiency() {
        return this.chargeEfficiency;
    }

    public Storage chargeEfficiency(Double chargeEfficiency) {
        this.setChargeEfficiency(chargeEfficiency);
        return this;
    }

    public void setChargeEfficiency(Double chargeEfficiency) {
        this.chargeEfficiency = chargeEfficiency;
    }

    public Double getThermalRating() {
        return this.thermalRating;
    }

    public Storage thermalRating(Double thermalRating) {
        this.setThermalRating(thermalRating);
        return this;
    }

    public void setThermalRating(Double thermalRating) {
        this.thermalRating = thermalRating;
    }

    public Double getQmin() {
        return this.qmin;
    }

    public Storage qmin(Double qmin) {
        this.setQmin(qmin);
        return this;
    }

    public void setQmin(Double qmin) {
        this.qmin = qmin;
    }

    public Double getQmax() {
        return this.qmax;
    }

    public Storage qmax(Double qmax) {
        this.setQmax(qmax);
        return this;
    }

    public void setQmax(Double qmax) {
        this.qmax = qmax;
    }

    public Double getR() {
        return this.r;
    }

    public Storage r(Double r) {
        this.setR(r);
        return this;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getX() {
        return this.x;
    }

    public Storage x(Double x) {
        this.setX(x);
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getpLoss() {
        return this.pLoss;
    }

    public Storage pLoss(Double pLoss) {
        this.setpLoss(pLoss);
        return this;
    }

    public void setpLoss(Double pLoss) {
        this.pLoss = pLoss;
    }

    public Double getqLoss() {
        return this.qLoss;
    }

    public Storage qLoss(Double qLoss) {
        this.setqLoss(qLoss);
        return this;
    }

    public void setqLoss(Double qLoss) {
        this.qLoss = qLoss;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Storage status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getSocInitial() {
        return this.socInitial;
    }

    public Storage socInitial(Double socInitial) {
        this.setSocInitial(socInitial);
        return this;
    }

    public void setSocInitial(Double socInitial) {
        this.socInitial = socInitial;
    }

    public Double getSocMin() {
        return this.socMin;
    }

    public Storage socMin(Double socMin) {
        this.setSocMin(socMin);
        return this;
    }

    public void setSocMin(Double socMin) {
        this.socMin = socMin;
    }

    public Double getSocMax() {
        return this.socMax;
    }

    public Storage socMax(Double socMax) {
        this.setSocMax(socMax);
        return this;
    }

    public void setSocMax(Double socMax) {
        this.socMax = socMax;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Storage network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Storage)) {
            return false;
        }
        return id != null && id.equals(((Storage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Storage{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", ps=" + getPs() +
            ", qs=" + getQs() +
            ", energy=" + getEnergy() +
            ", eRating=" + geteRating() +
            ", chargeRating=" + getChargeRating() +
            ", dischargeRating=" + getDischargeRating() +
            ", chargeEfficiency=" + getChargeEfficiency() +
            ", thermalRating=" + getThermalRating() +
            ", qmin=" + getQmin() +
            ", qmax=" + getQmax() +
            ", r=" + getR() +
            ", x=" + getX() +
            ", pLoss=" + getpLoss() +
            ", qLoss=" + getqLoss() +
            ", status=" + getStatus() +
            ", socInitial=" + getSocInitial() +
            ", socMin=" + getSocMin() +
            ", socMax=" + getSocMax() +
            "}";
    }
}
