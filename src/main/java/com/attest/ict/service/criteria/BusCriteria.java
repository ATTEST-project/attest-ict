package com.attest.ict.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.attest.ict.domain.Bus} entity. This class is used
 * in {@link com.attest.ict.web.rest.BusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private IntegerFilter type;

    private DoubleFilter activePower;

    private DoubleFilter reactivePower;

    private DoubleFilter conductance;

    private DoubleFilter susceptance;

    private LongFilter area;

    private DoubleFilter vm;

    private DoubleFilter va;

    private DoubleFilter baseKv;

    private LongFilter zone;

    private DoubleFilter vmax;

    private DoubleFilter vmin;

    private LongFilter loadELValId;

    private LongFilter busNameId;

    private LongFilter busExtensionId;

    private LongFilter busCoordinateId;

    private LongFilter networkId;

    private Boolean distinct;

    public BusCriteria() {}

    public BusCriteria(BusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.activePower = other.activePower == null ? null : other.activePower.copy();
        this.reactivePower = other.reactivePower == null ? null : other.reactivePower.copy();
        this.conductance = other.conductance == null ? null : other.conductance.copy();
        this.susceptance = other.susceptance == null ? null : other.susceptance.copy();
        this.area = other.area == null ? null : other.area.copy();
        this.vm = other.vm == null ? null : other.vm.copy();
        this.va = other.va == null ? null : other.va.copy();
        this.baseKv = other.baseKv == null ? null : other.baseKv.copy();
        this.zone = other.zone == null ? null : other.zone.copy();
        this.vmax = other.vmax == null ? null : other.vmax.copy();
        this.vmin = other.vmin == null ? null : other.vmin.copy();
        this.loadELValId = other.loadELValId == null ? null : other.loadELValId.copy();
        this.busNameId = other.busNameId == null ? null : other.busNameId.copy();
        this.busExtensionId = other.busExtensionId == null ? null : other.busExtensionId.copy();
        this.busCoordinateId = other.busCoordinateId == null ? null : other.busCoordinateId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusCriteria copy() {
        return new BusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getBusNum() {
        return busNum;
    }

    public LongFilter busNum() {
        if (busNum == null) {
            busNum = new LongFilter();
        }
        return busNum;
    }

    public void setBusNum(LongFilter busNum) {
        this.busNum = busNum;
    }

    public IntegerFilter getType() {
        return type;
    }

    public IntegerFilter type() {
        if (type == null) {
            type = new IntegerFilter();
        }
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public DoubleFilter getActivePower() {
        return activePower;
    }

    public DoubleFilter activePower() {
        if (activePower == null) {
            activePower = new DoubleFilter();
        }
        return activePower;
    }

    public void setActivePower(DoubleFilter activePower) {
        this.activePower = activePower;
    }

    public DoubleFilter getReactivePower() {
        return reactivePower;
    }

    public DoubleFilter reactivePower() {
        if (reactivePower == null) {
            reactivePower = new DoubleFilter();
        }
        return reactivePower;
    }

    public void setReactivePower(DoubleFilter reactivePower) {
        this.reactivePower = reactivePower;
    }

    public DoubleFilter getConductance() {
        return conductance;
    }

    public DoubleFilter conductance() {
        if (conductance == null) {
            conductance = new DoubleFilter();
        }
        return conductance;
    }

    public void setConductance(DoubleFilter conductance) {
        this.conductance = conductance;
    }

    public DoubleFilter getSusceptance() {
        return susceptance;
    }

    public DoubleFilter susceptance() {
        if (susceptance == null) {
            susceptance = new DoubleFilter();
        }
        return susceptance;
    }

    public void setSusceptance(DoubleFilter susceptance) {
        this.susceptance = susceptance;
    }

    public LongFilter getArea() {
        return area;
    }

    public LongFilter area() {
        if (area == null) {
            area = new LongFilter();
        }
        return area;
    }

    public void setArea(LongFilter area) {
        this.area = area;
    }

    public DoubleFilter getVm() {
        return vm;
    }

    public DoubleFilter vm() {
        if (vm == null) {
            vm = new DoubleFilter();
        }
        return vm;
    }

    public void setVm(DoubleFilter vm) {
        this.vm = vm;
    }

    public DoubleFilter getVa() {
        return va;
    }

    public DoubleFilter va() {
        if (va == null) {
            va = new DoubleFilter();
        }
        return va;
    }

    public void setVa(DoubleFilter va) {
        this.va = va;
    }

    public DoubleFilter getBaseKv() {
        return baseKv;
    }

    public DoubleFilter baseKv() {
        if (baseKv == null) {
            baseKv = new DoubleFilter();
        }
        return baseKv;
    }

    public void setBaseKv(DoubleFilter baseKv) {
        this.baseKv = baseKv;
    }

    public LongFilter getZone() {
        return zone;
    }

    public LongFilter zone() {
        if (zone == null) {
            zone = new LongFilter();
        }
        return zone;
    }

    public void setZone(LongFilter zone) {
        this.zone = zone;
    }

    public DoubleFilter getVmax() {
        return vmax;
    }

    public DoubleFilter vmax() {
        if (vmax == null) {
            vmax = new DoubleFilter();
        }
        return vmax;
    }

    public void setVmax(DoubleFilter vmax) {
        this.vmax = vmax;
    }

    public DoubleFilter getVmin() {
        return vmin;
    }

    public DoubleFilter vmin() {
        if (vmin == null) {
            vmin = new DoubleFilter();
        }
        return vmin;
    }

    public void setVmin(DoubleFilter vmin) {
        this.vmin = vmin;
    }

    public LongFilter getLoadELValId() {
        return loadELValId;
    }

    public LongFilter loadELValId() {
        if (loadELValId == null) {
            loadELValId = new LongFilter();
        }
        return loadELValId;
    }

    public void setLoadELValId(LongFilter loadELValId) {
        this.loadELValId = loadELValId;
    }

    public LongFilter getBusNameId() {
        return busNameId;
    }

    public LongFilter busNameId() {
        if (busNameId == null) {
            busNameId = new LongFilter();
        }
        return busNameId;
    }

    public void setBusNameId(LongFilter busNameId) {
        this.busNameId = busNameId;
    }

    public LongFilter getBusExtensionId() {
        return busExtensionId;
    }

    public LongFilter busExtensionId() {
        if (busExtensionId == null) {
            busExtensionId = new LongFilter();
        }
        return busExtensionId;
    }

    public void setBusExtensionId(LongFilter busExtensionId) {
        this.busExtensionId = busExtensionId;
    }

    public LongFilter getBusCoordinateId() {
        return busCoordinateId;
    }

    public LongFilter busCoordinateId() {
        if (busCoordinateId == null) {
            busCoordinateId = new LongFilter();
        }
        return busCoordinateId;
    }

    public void setBusCoordinateId(LongFilter busCoordinateId) {
        this.busCoordinateId = busCoordinateId;
    }

    public LongFilter getNetworkId() {
        return networkId;
    }

    public LongFilter networkId() {
        if (networkId == null) {
            networkId = new LongFilter();
        }
        return networkId;
    }

    public void setNetworkId(LongFilter networkId) {
        this.networkId = networkId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BusCriteria that = (BusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(type, that.type) &&
            Objects.equals(activePower, that.activePower) &&
            Objects.equals(reactivePower, that.reactivePower) &&
            Objects.equals(conductance, that.conductance) &&
            Objects.equals(susceptance, that.susceptance) &&
            Objects.equals(area, that.area) &&
            Objects.equals(vm, that.vm) &&
            Objects.equals(va, that.va) &&
            Objects.equals(baseKv, that.baseKv) &&
            Objects.equals(zone, that.zone) &&
            Objects.equals(vmax, that.vmax) &&
            Objects.equals(vmin, that.vmin) &&
            Objects.equals(loadELValId, that.loadELValId) &&
            Objects.equals(busNameId, that.busNameId) &&
            Objects.equals(busExtensionId, that.busExtensionId) &&
            Objects.equals(busCoordinateId, that.busCoordinateId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            busNum,
            type,
            activePower,
            reactivePower,
            conductance,
            susceptance,
            area,
            vm,
            va,
            baseKv,
            zone,
            vmax,
            vmin,
            loadELValId,
            busNameId,
            busExtensionId,
            busCoordinateId,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (activePower != null ? "activePower=" + activePower + ", " : "") +
            (reactivePower != null ? "reactivePower=" + reactivePower + ", " : "") +
            (conductance != null ? "conductance=" + conductance + ", " : "") +
            (susceptance != null ? "susceptance=" + susceptance + ", " : "") +
            (area != null ? "area=" + area + ", " : "") +
            (vm != null ? "vm=" + vm + ", " : "") +
            (va != null ? "va=" + va + ", " : "") +
            (baseKv != null ? "baseKv=" + baseKv + ", " : "") +
            (zone != null ? "zone=" + zone + ", " : "") +
            (vmax != null ? "vmax=" + vmax + ", " : "") +
            (vmin != null ? "vmin=" + vmin + ", " : "") +
            (loadELValId != null ? "loadELValId=" + loadELValId + ", " : "") +
            (busNameId != null ? "busNameId=" + busNameId + ", " : "") +
            (busExtensionId != null ? "busExtensionId=" + busExtensionId + ", " : "") +
            (busCoordinateId != null ? "busCoordinateId=" + busCoordinateId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
