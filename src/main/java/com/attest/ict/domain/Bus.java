package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bus.
 */
@Entity
@Table(name = "bus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Bus Number
    @Column(name = "bus_num")
    private Long busNum;

    //Bus type 1=PQ, 2=PV  3 = ref 4 =isolated
    @Column(name = "type")
    private Integer type;

    //real power demand on bus (MW)
    @Column(name = "active_power")
    private Double activePower;

    //reactive power demand on bus (MVAr)
    @Column(name = "reactive_power")
    private Double reactivePower;

    //GS shunt conductance MW
    @Column(name = "conductance")
    private Double conductance;

    //BS shunt susceptance MVAr
    @Column(name = "susceptance")
    private Double susceptance;

    // Area Number
    @Column(name = "area")
    private Long area;

    //Voltage magnitude
    @Column(name = "vm")
    private Double vm;

    //Voltage angle
    @Column(name = "va")
    private Double va;

    //Base voltage (Kv)
    @Column(name = "base_kv")
    private Double baseKv;

    //loos zone
    @Column(name = "zone")
    private Long zone;

    //Maximum Voltage magnitude
    @Column(name = "vmax")
    private Double vmax;

    //Minimum Voltage magnitude
    @Column(name = "vmin")
    private Double vmin;

    @OneToMany(mappedBy = "bus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "loadProfile", "bus" }, allowSetters = true)
    private Set<LoadElVal> loadELVals = new HashSet<>();

    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    @OneToOne(mappedBy = "bus", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private BusName busName;

    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    @OneToOne(mappedBy = "bus", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private BusExtension busExtension;

    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    @OneToOne(mappedBy = "bus", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private BusCoordinate busCoordinate;

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

    public Bus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public Bus busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Integer getType() {
        return this.type;
    }

    public Bus type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getActivePower() {
        return this.activePower;
    }

    public Bus activePower(Double activePower) {
        this.setActivePower(activePower);
        return this;
    }

    public void setActivePower(Double activePower) {
        this.activePower = activePower;
    }

    public Double getReactivePower() {
        return this.reactivePower;
    }

    public Bus reactivePower(Double reactivePower) {
        this.setReactivePower(reactivePower);
        return this;
    }

    public void setReactivePower(Double reactivePower) {
        this.reactivePower = reactivePower;
    }

    public Double getConductance() {
        return this.conductance;
    }

    public Bus conductance(Double conductance) {
        this.setConductance(conductance);
        return this;
    }

    public void setConductance(Double conductance) {
        this.conductance = conductance;
    }

    public Double getSusceptance() {
        return this.susceptance;
    }

    public Bus susceptance(Double susceptance) {
        this.setSusceptance(susceptance);
        return this;
    }

    public void setSusceptance(Double susceptance) {
        this.susceptance = susceptance;
    }

    public Long getArea() {
        return this.area;
    }

    public Bus area(Long area) {
        this.setArea(area);
        return this;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Double getVm() {
        return this.vm;
    }

    public Bus vm(Double vm) {
        this.setVm(vm);
        return this;
    }

    public void setVm(Double vm) {
        this.vm = vm;
    }

    public Double getVa() {
        return this.va;
    }

    public Bus va(Double va) {
        this.setVa(va);
        return this;
    }

    public void setVa(Double va) {
        this.va = va;
    }

    public Double getBaseKv() {
        return this.baseKv;
    }

    public Bus baseKv(Double baseKv) {
        this.setBaseKv(baseKv);
        return this;
    }

    public void setBaseKv(Double baseKv) {
        this.baseKv = baseKv;
    }

    public Long getZone() {
        return this.zone;
    }

    public Bus zone(Long zone) {
        this.setZone(zone);
        return this;
    }

    public void setZone(Long zone) {
        this.zone = zone;
    }

    public Double getVmax() {
        return this.vmax;
    }

    public Bus vmax(Double vmax) {
        this.setVmax(vmax);
        return this;
    }

    public void setVmax(Double vmax) {
        this.vmax = vmax;
    }

    public Double getVmin() {
        return this.vmin;
    }

    public Bus vmin(Double vmin) {
        this.setVmin(vmin);
        return this;
    }

    public void setVmin(Double vmin) {
        this.vmin = vmin;
    }

    public Set<LoadElVal> getLoadELVals() {
        return this.loadELVals;
    }

    public void setLoadELVals(Set<LoadElVal> loadElVals) {
        if (this.loadELVals != null) {
            this.loadELVals.forEach(i -> i.setBus(null));
        }
        if (loadElVals != null) {
            loadElVals.forEach(i -> i.setBus(this));
        }
        this.loadELVals = loadElVals;
    }

    public Bus loadELVals(Set<LoadElVal> loadElVals) {
        this.setLoadELVals(loadElVals);
        return this;
    }

    public Bus addLoadELVal(LoadElVal loadElVal) {
        this.loadELVals.add(loadElVal);
        loadElVal.setBus(this);
        return this;
    }

    public Bus removeLoadELVal(LoadElVal loadElVal) {
        this.loadELVals.remove(loadElVal);
        loadElVal.setBus(null);
        return this;
    }

    public BusName getBusName() {
        return this.busName;
    }

    public void setBusName(BusName busName) {
        if (this.busName != null) {
            this.busName.setBus(null);
        }
        if (busName != null) {
            busName.setBus(this);
        }
        this.busName = busName;
    }

    public Bus busName(BusName busName) {
        this.setBusName(busName);
        return this;
    }

    public BusExtension getBusExtension() {
        return this.busExtension;
    }

    public void setBusExtension(BusExtension busExtension) {
        if (this.busExtension != null) {
            this.busExtension.setBus(null);
        }
        if (busExtension != null) {
            busExtension.setBus(this);
        }
        this.busExtension = busExtension;
    }

    public Bus busExtension(BusExtension busExtension) {
        this.setBusExtension(busExtension);
        return this;
    }

    public BusCoordinate getBusCoordinate() {
        return this.busCoordinate;
    }

    public void setBusCoordinate(BusCoordinate busCoordinate) {
        if (this.busCoordinate != null) {
            this.busCoordinate.setBus(null);
        }
        if (busCoordinate != null) {
            busCoordinate.setBus(this);
        }
        this.busCoordinate = busCoordinate;
    }

    public Bus busCoordinate(BusCoordinate busCoordinate) {
        this.setBusCoordinate(busCoordinate);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Bus network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bus)) {
            return false;
        }
        return id != null && id.equals(((Bus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bus{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", type=" + getType() +
            ", activePower=" + getActivePower() +
            ", reactivePower=" + getReactivePower() +
            ", conductance=" + getConductance() +
            ", susceptance=" + getSusceptance() +
            ", area=" + getArea() +
            ", vm=" + getVm() +
            ", va=" + getVa() +
            ", baseKv=" + getBaseKv() +
            ", zone=" + getZone() +
            ", vmax=" + getVmax() +
            ", vmin=" + getVmin() +
            "}";
    }
}
