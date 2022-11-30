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
 * Criteria class for the {@link com.attest.ict.domain.Storage} entity. This class is used
 * in {@link com.attest.ict.web.rest.StorageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /storages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StorageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private DoubleFilter ps;

    private DoubleFilter qs;

    private DoubleFilter energy;

    private DoubleFilter eRating;

    private DoubleFilter chargeRating;

    private DoubleFilter dischargeRating;

    private DoubleFilter chargeEfficiency;

    private DoubleFilter thermalRating;

    private DoubleFilter qmin;

    private DoubleFilter qmax;

    private DoubleFilter r;

    private DoubleFilter x;

    private DoubleFilter pLoss;

    private DoubleFilter qLoss;

    private IntegerFilter status;

    private DoubleFilter socInitial;

    private DoubleFilter socMin;

    private DoubleFilter socMax;

    private LongFilter networkId;

    private Boolean distinct;

    public StorageCriteria() {}

    public StorageCriteria(StorageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.ps = other.ps == null ? null : other.ps.copy();
        this.qs = other.qs == null ? null : other.qs.copy();
        this.energy = other.energy == null ? null : other.energy.copy();
        this.eRating = other.eRating == null ? null : other.eRating.copy();
        this.chargeRating = other.chargeRating == null ? null : other.chargeRating.copy();
        this.dischargeRating = other.dischargeRating == null ? null : other.dischargeRating.copy();
        this.chargeEfficiency = other.chargeEfficiency == null ? null : other.chargeEfficiency.copy();
        this.thermalRating = other.thermalRating == null ? null : other.thermalRating.copy();
        this.qmin = other.qmin == null ? null : other.qmin.copy();
        this.qmax = other.qmax == null ? null : other.qmax.copy();
        this.r = other.r == null ? null : other.r.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.pLoss = other.pLoss == null ? null : other.pLoss.copy();
        this.qLoss = other.qLoss == null ? null : other.qLoss.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.socInitial = other.socInitial == null ? null : other.socInitial.copy();
        this.socMin = other.socMin == null ? null : other.socMin.copy();
        this.socMax = other.socMax == null ? null : other.socMax.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StorageCriteria copy() {
        return new StorageCriteria(this);
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

    public DoubleFilter getPs() {
        return ps;
    }

    public DoubleFilter ps() {
        if (ps == null) {
            ps = new DoubleFilter();
        }
        return ps;
    }

    public void setPs(DoubleFilter ps) {
        this.ps = ps;
    }

    public DoubleFilter getQs() {
        return qs;
    }

    public DoubleFilter qs() {
        if (qs == null) {
            qs = new DoubleFilter();
        }
        return qs;
    }

    public void setQs(DoubleFilter qs) {
        this.qs = qs;
    }

    public DoubleFilter getEnergy() {
        return energy;
    }

    public DoubleFilter energy() {
        if (energy == null) {
            energy = new DoubleFilter();
        }
        return energy;
    }

    public void setEnergy(DoubleFilter energy) {
        this.energy = energy;
    }

    public DoubleFilter geteRating() {
        return eRating;
    }

    public DoubleFilter eRating() {
        if (eRating == null) {
            eRating = new DoubleFilter();
        }
        return eRating;
    }

    public void seteRating(DoubleFilter eRating) {
        this.eRating = eRating;
    }

    public DoubleFilter getChargeRating() {
        return chargeRating;
    }

    public DoubleFilter chargeRating() {
        if (chargeRating == null) {
            chargeRating = new DoubleFilter();
        }
        return chargeRating;
    }

    public void setChargeRating(DoubleFilter chargeRating) {
        this.chargeRating = chargeRating;
    }

    public DoubleFilter getDischargeRating() {
        return dischargeRating;
    }

    public DoubleFilter dischargeRating() {
        if (dischargeRating == null) {
            dischargeRating = new DoubleFilter();
        }
        return dischargeRating;
    }

    public void setDischargeRating(DoubleFilter dischargeRating) {
        this.dischargeRating = dischargeRating;
    }

    public DoubleFilter getChargeEfficiency() {
        return chargeEfficiency;
    }

    public DoubleFilter chargeEfficiency() {
        if (chargeEfficiency == null) {
            chargeEfficiency = new DoubleFilter();
        }
        return chargeEfficiency;
    }

    public void setChargeEfficiency(DoubleFilter chargeEfficiency) {
        this.chargeEfficiency = chargeEfficiency;
    }

    public DoubleFilter getThermalRating() {
        return thermalRating;
    }

    public DoubleFilter thermalRating() {
        if (thermalRating == null) {
            thermalRating = new DoubleFilter();
        }
        return thermalRating;
    }

    public void setThermalRating(DoubleFilter thermalRating) {
        this.thermalRating = thermalRating;
    }

    public DoubleFilter getQmin() {
        return qmin;
    }

    public DoubleFilter qmin() {
        if (qmin == null) {
            qmin = new DoubleFilter();
        }
        return qmin;
    }

    public void setQmin(DoubleFilter qmin) {
        this.qmin = qmin;
    }

    public DoubleFilter getQmax() {
        return qmax;
    }

    public DoubleFilter qmax() {
        if (qmax == null) {
            qmax = new DoubleFilter();
        }
        return qmax;
    }

    public void setQmax(DoubleFilter qmax) {
        this.qmax = qmax;
    }

    public DoubleFilter getR() {
        return r;
    }

    public DoubleFilter r() {
        if (r == null) {
            r = new DoubleFilter();
        }
        return r;
    }

    public void setR(DoubleFilter r) {
        this.r = r;
    }

    public DoubleFilter getX() {
        return x;
    }

    public DoubleFilter x() {
        if (x == null) {
            x = new DoubleFilter();
        }
        return x;
    }

    public void setX(DoubleFilter x) {
        this.x = x;
    }

    public DoubleFilter getpLoss() {
        return pLoss;
    }

    public DoubleFilter pLoss() {
        if (pLoss == null) {
            pLoss = new DoubleFilter();
        }
        return pLoss;
    }

    public void setpLoss(DoubleFilter pLoss) {
        this.pLoss = pLoss;
    }

    public DoubleFilter getqLoss() {
        return qLoss;
    }

    public DoubleFilter qLoss() {
        if (qLoss == null) {
            qLoss = new DoubleFilter();
        }
        return qLoss;
    }

    public void setqLoss(DoubleFilter qLoss) {
        this.qLoss = qLoss;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public DoubleFilter getSocInitial() {
        return socInitial;
    }

    public DoubleFilter socInitial() {
        if (socInitial == null) {
            socInitial = new DoubleFilter();
        }
        return socInitial;
    }

    public void setSocInitial(DoubleFilter socInitial) {
        this.socInitial = socInitial;
    }

    public DoubleFilter getSocMin() {
        return socMin;
    }

    public DoubleFilter socMin() {
        if (socMin == null) {
            socMin = new DoubleFilter();
        }
        return socMin;
    }

    public void setSocMin(DoubleFilter socMin) {
        this.socMin = socMin;
    }

    public DoubleFilter getSocMax() {
        return socMax;
    }

    public DoubleFilter socMax() {
        if (socMax == null) {
            socMax = new DoubleFilter();
        }
        return socMax;
    }

    public void setSocMax(DoubleFilter socMax) {
        this.socMax = socMax;
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
        final StorageCriteria that = (StorageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(ps, that.ps) &&
            Objects.equals(qs, that.qs) &&
            Objects.equals(energy, that.energy) &&
            Objects.equals(eRating, that.eRating) &&
            Objects.equals(chargeRating, that.chargeRating) &&
            Objects.equals(dischargeRating, that.dischargeRating) &&
            Objects.equals(chargeEfficiency, that.chargeEfficiency) &&
            Objects.equals(thermalRating, that.thermalRating) &&
            Objects.equals(qmin, that.qmin) &&
            Objects.equals(qmax, that.qmax) &&
            Objects.equals(r, that.r) &&
            Objects.equals(x, that.x) &&
            Objects.equals(pLoss, that.pLoss) &&
            Objects.equals(qLoss, that.qLoss) &&
            Objects.equals(status, that.status) &&
            Objects.equals(socInitial, that.socInitial) &&
            Objects.equals(socMin, that.socMin) &&
            Objects.equals(socMax, that.socMax) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            busNum,
            ps,
            qs,
            energy,
            eRating,
            chargeRating,
            dischargeRating,
            chargeEfficiency,
            thermalRating,
            qmin,
            qmax,
            r,
            x,
            pLoss,
            qLoss,
            status,
            socInitial,
            socMin,
            socMax,
            networkId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (ps != null ? "ps=" + ps + ", " : "") +
            (qs != null ? "qs=" + qs + ", " : "") +
            (energy != null ? "energy=" + energy + ", " : "") +
            (eRating != null ? "eRating=" + eRating + ", " : "") +
            (chargeRating != null ? "chargeRating=" + chargeRating + ", " : "") +
            (dischargeRating != null ? "dischargeRating=" + dischargeRating + ", " : "") +
            (chargeEfficiency != null ? "chargeEfficiency=" + chargeEfficiency + ", " : "") +
            (thermalRating != null ? "thermalRating=" + thermalRating + ", " : "") +
            (qmin != null ? "qmin=" + qmin + ", " : "") +
            (qmax != null ? "qmax=" + qmax + ", " : "") +
            (r != null ? "r=" + r + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (pLoss != null ? "pLoss=" + pLoss + ", " : "") +
            (qLoss != null ? "qLoss=" + qLoss + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (socInitial != null ? "socInitial=" + socInitial + ", " : "") +
            (socMin != null ? "socMin=" + socMin + ", " : "") +
            (socMax != null ? "socMax=" + socMax + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
