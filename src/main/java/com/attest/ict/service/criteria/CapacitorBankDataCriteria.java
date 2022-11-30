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
 * Criteria class for the {@link com.attest.ict.domain.CapacitorBankData} entity. This class is used
 * in {@link com.attest.ict.web.rest.CapacitorBankDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /capacitor-bank-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CapacitorBankDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter busNum;

    private StringFilter nodeId;

    private StringFilter bankId;

    private DoubleFilter qnom;

    private LongFilter networkId;

    private Boolean distinct;

    public CapacitorBankDataCriteria() {}

    public CapacitorBankDataCriteria(CapacitorBankDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.busNum = other.busNum == null ? null : other.busNum.copy();
        this.nodeId = other.nodeId == null ? null : other.nodeId.copy();
        this.bankId = other.bankId == null ? null : other.bankId.copy();
        this.qnom = other.qnom == null ? null : other.qnom.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CapacitorBankDataCriteria copy() {
        return new CapacitorBankDataCriteria(this);
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

    public StringFilter getNodeId() {
        return nodeId;
    }

    public StringFilter nodeId() {
        if (nodeId == null) {
            nodeId = new StringFilter();
        }
        return nodeId;
    }

    public void setNodeId(StringFilter nodeId) {
        this.nodeId = nodeId;
    }

    public StringFilter getBankId() {
        return bankId;
    }

    public StringFilter bankId() {
        if (bankId == null) {
            bankId = new StringFilter();
        }
        return bankId;
    }

    public void setBankId(StringFilter bankId) {
        this.bankId = bankId;
    }

    public DoubleFilter getQnom() {
        return qnom;
    }

    public DoubleFilter qnom() {
        if (qnom == null) {
            qnom = new DoubleFilter();
        }
        return qnom;
    }

    public void setQnom(DoubleFilter qnom) {
        this.qnom = qnom;
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
        final CapacitorBankDataCriteria that = (CapacitorBankDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(busNum, that.busNum) &&
            Objects.equals(nodeId, that.nodeId) &&
            Objects.equals(bankId, that.bankId) &&
            Objects.equals(qnom, that.qnom) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busNum, nodeId, bankId, qnom, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CapacitorBankDataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (busNum != null ? "busNum=" + busNum + ", " : "") +
            (nodeId != null ? "nodeId=" + nodeId + ", " : "") +
            (bankId != null ? "bankId=" + bankId + ", " : "") +
            (qnom != null ? "qnom=" + qnom + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
