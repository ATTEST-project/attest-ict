package com.attest.ict.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.attest.ict.domain.Network} entity. This class is used
 * in {@link com.attest.ict.web.rest.NetworkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /networks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NetworkCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter mpcName;

    private StringFilter country;

    private StringFilter type;

    private StringFilter description;

    private BooleanFilter isDeleted;

    private InstantFilter networkDate;

    private IntegerFilter version;

    private InstantFilter creationDateTime;

    private InstantFilter updateDateTime;

    private LongFilter busId;

    private LongFilter generatorId;

    private LongFilter branchId;

    private LongFilter storageId;

    private LongFilter transformerId;

    private LongFilter capacitorId;

    private LongFilter inputFileId;

    private LongFilter assetUgCableId;

    private LongFilter assetTransformerId;

    private LongFilter billingConsumptionId;

    private LongFilter billingDerId;

    private LongFilter lineCableId;

    private LongFilter genProfileId;

    private LongFilter loadProfileId;

    private LongFilter flexProfileId;

    private LongFilter transfProfileId;

    private LongFilter branchProfileId;

    private LongFilter topologyBusId;

    private LongFilter dsoTsoConnectionId;

    private LongFilter baseMVAId;

    private LongFilter voltageLevelId;

    private LongFilter simulationId;

    private Boolean distinct;

    public NetworkCriteria() {}

    public NetworkCriteria(NetworkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.mpcName = other.mpcName == null ? null : other.mpcName.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.networkDate = other.networkDate == null ? null : other.networkDate.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.creationDateTime = other.creationDateTime == null ? null : other.creationDateTime.copy();
        this.updateDateTime = other.updateDateTime == null ? null : other.updateDateTime.copy();
        this.busId = other.busId == null ? null : other.busId.copy();
        this.generatorId = other.generatorId == null ? null : other.generatorId.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.storageId = other.storageId == null ? null : other.storageId.copy();
        this.transformerId = other.transformerId == null ? null : other.transformerId.copy();
        this.capacitorId = other.capacitorId == null ? null : other.capacitorId.copy();
        this.inputFileId = other.inputFileId == null ? null : other.inputFileId.copy();
        this.assetUgCableId = other.assetUgCableId == null ? null : other.assetUgCableId.copy();
        this.assetTransformerId = other.assetTransformerId == null ? null : other.assetTransformerId.copy();
        this.billingConsumptionId = other.billingConsumptionId == null ? null : other.billingConsumptionId.copy();
        this.billingDerId = other.billingDerId == null ? null : other.billingDerId.copy();
        this.lineCableId = other.lineCableId == null ? null : other.lineCableId.copy();
        this.genProfileId = other.genProfileId == null ? null : other.genProfileId.copy();
        this.loadProfileId = other.loadProfileId == null ? null : other.loadProfileId.copy();
        this.flexProfileId = other.flexProfileId == null ? null : other.flexProfileId.copy();
        this.transfProfileId = other.transfProfileId == null ? null : other.transfProfileId.copy();
        this.branchProfileId = other.branchProfileId == null ? null : other.branchProfileId.copy();
        this.topologyBusId = other.topologyBusId == null ? null : other.topologyBusId.copy();
        this.dsoTsoConnectionId = other.dsoTsoConnectionId == null ? null : other.dsoTsoConnectionId.copy();
        this.baseMVAId = other.baseMVAId == null ? null : other.baseMVAId.copy();
        this.voltageLevelId = other.voltageLevelId == null ? null : other.voltageLevelId.copy();
        this.simulationId = other.simulationId == null ? null : other.simulationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NetworkCriteria copy() {
        return new NetworkCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getMpcName() {
        return mpcName;
    }

    public StringFilter mpcName() {
        if (mpcName == null) {
            mpcName = new StringFilter();
        }
        return mpcName;
    }

    public void setMpcName(StringFilter mpcName) {
        this.mpcName = mpcName;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public InstantFilter getNetworkDate() {
        return networkDate;
    }

    public InstantFilter networkDate() {
        if (networkDate == null) {
            networkDate = new InstantFilter();
        }
        return networkDate;
    }

    public void setNetworkDate(InstantFilter networkDate) {
        this.networkDate = networkDate;
    }

    public IntegerFilter getVersion() {
        return version;
    }

    public IntegerFilter version() {
        if (version == null) {
            version = new IntegerFilter();
        }
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
    }

    public InstantFilter getCreationDateTime() {
        return creationDateTime;
    }

    public InstantFilter creationDateTime() {
        if (creationDateTime == null) {
            creationDateTime = new InstantFilter();
        }
        return creationDateTime;
    }

    public void setCreationDateTime(InstantFilter creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public InstantFilter getUpdateDateTime() {
        return updateDateTime;
    }

    public InstantFilter updateDateTime() {
        if (updateDateTime == null) {
            updateDateTime = new InstantFilter();
        }
        return updateDateTime;
    }

    public void setUpdateDateTime(InstantFilter updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public LongFilter getBusId() {
        return busId;
    }

    public LongFilter busId() {
        if (busId == null) {
            busId = new LongFilter();
        }
        return busId;
    }

    public void setBusId(LongFilter busId) {
        this.busId = busId;
    }

    public LongFilter getGeneratorId() {
        return generatorId;
    }

    public LongFilter generatorId() {
        if (generatorId == null) {
            generatorId = new LongFilter();
        }
        return generatorId;
    }

    public void setGeneratorId(LongFilter generatorId) {
        this.generatorId = generatorId;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public LongFilter branchId() {
        if (branchId == null) {
            branchId = new LongFilter();
        }
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
    }

    public LongFilter getStorageId() {
        return storageId;
    }

    public LongFilter storageId() {
        if (storageId == null) {
            storageId = new LongFilter();
        }
        return storageId;
    }

    public void setStorageId(LongFilter storageId) {
        this.storageId = storageId;
    }

    public LongFilter getTransformerId() {
        return transformerId;
    }

    public LongFilter transformerId() {
        if (transformerId == null) {
            transformerId = new LongFilter();
        }
        return transformerId;
    }

    public void setTransformerId(LongFilter transformerId) {
        this.transformerId = transformerId;
    }

    public LongFilter getCapacitorId() {
        return capacitorId;
    }

    public LongFilter capacitorId() {
        if (capacitorId == null) {
            capacitorId = new LongFilter();
        }
        return capacitorId;
    }

    public void setCapacitorId(LongFilter capacitorId) {
        this.capacitorId = capacitorId;
    }

    public LongFilter getInputFileId() {
        return inputFileId;
    }

    public LongFilter inputFileId() {
        if (inputFileId == null) {
            inputFileId = new LongFilter();
        }
        return inputFileId;
    }

    public void setInputFileId(LongFilter inputFileId) {
        this.inputFileId = inputFileId;
    }

    public LongFilter getAssetUgCableId() {
        return assetUgCableId;
    }

    public LongFilter assetUgCableId() {
        if (assetUgCableId == null) {
            assetUgCableId = new LongFilter();
        }
        return assetUgCableId;
    }

    public void setAssetUgCableId(LongFilter assetUgCableId) {
        this.assetUgCableId = assetUgCableId;
    }

    public LongFilter getAssetTransformerId() {
        return assetTransformerId;
    }

    public LongFilter assetTransformerId() {
        if (assetTransformerId == null) {
            assetTransformerId = new LongFilter();
        }
        return assetTransformerId;
    }

    public void setAssetTransformerId(LongFilter assetTransformerId) {
        this.assetTransformerId = assetTransformerId;
    }

    public LongFilter getBillingConsumptionId() {
        return billingConsumptionId;
    }

    public LongFilter billingConsumptionId() {
        if (billingConsumptionId == null) {
            billingConsumptionId = new LongFilter();
        }
        return billingConsumptionId;
    }

    public void setBillingConsumptionId(LongFilter billingConsumptionId) {
        this.billingConsumptionId = billingConsumptionId;
    }

    public LongFilter getBillingDerId() {
        return billingDerId;
    }

    public LongFilter billingDerId() {
        if (billingDerId == null) {
            billingDerId = new LongFilter();
        }
        return billingDerId;
    }

    public void setBillingDerId(LongFilter billingDerId) {
        this.billingDerId = billingDerId;
    }

    public LongFilter getLineCableId() {
        return lineCableId;
    }

    public LongFilter lineCableId() {
        if (lineCableId == null) {
            lineCableId = new LongFilter();
        }
        return lineCableId;
    }

    public void setLineCableId(LongFilter lineCableId) {
        this.lineCableId = lineCableId;
    }

    public LongFilter getGenProfileId() {
        return genProfileId;
    }

    public LongFilter genProfileId() {
        if (genProfileId == null) {
            genProfileId = new LongFilter();
        }
        return genProfileId;
    }

    public void setGenProfileId(LongFilter genProfileId) {
        this.genProfileId = genProfileId;
    }

    public LongFilter getLoadProfileId() {
        return loadProfileId;
    }

    public LongFilter loadProfileId() {
        if (loadProfileId == null) {
            loadProfileId = new LongFilter();
        }
        return loadProfileId;
    }

    public void setLoadProfileId(LongFilter loadProfileId) {
        this.loadProfileId = loadProfileId;
    }

    public LongFilter getFlexProfileId() {
        return flexProfileId;
    }

    public LongFilter flexProfileId() {
        if (flexProfileId == null) {
            flexProfileId = new LongFilter();
        }
        return flexProfileId;
    }

    public void setFlexProfileId(LongFilter flexProfileId) {
        this.flexProfileId = flexProfileId;
    }

    public LongFilter getTransfProfileId() {
        return transfProfileId;
    }

    public LongFilter transfProfileId() {
        if (transfProfileId == null) {
            transfProfileId = new LongFilter();
        }
        return transfProfileId;
    }

    public void setTransfProfileId(LongFilter transfProfileId) {
        this.transfProfileId = transfProfileId;
    }

    public LongFilter getBranchProfileId() {
        return branchProfileId;
    }

    public LongFilter branchProfileId() {
        if (branchProfileId == null) {
            branchProfileId = new LongFilter();
        }
        return branchProfileId;
    }

    public void setBranchProfileId(LongFilter branchProfileId) {
        this.branchProfileId = branchProfileId;
    }

    public LongFilter getTopologyBusId() {
        return topologyBusId;
    }

    public LongFilter topologyBusId() {
        if (topologyBusId == null) {
            topologyBusId = new LongFilter();
        }
        return topologyBusId;
    }

    public void setTopologyBusId(LongFilter topologyBusId) {
        this.topologyBusId = topologyBusId;
    }

    public LongFilter getDsoTsoConnectionId() {
        return dsoTsoConnectionId;
    }

    public LongFilter dsoTsoConnectionId() {
        if (dsoTsoConnectionId == null) {
            dsoTsoConnectionId = new LongFilter();
        }
        return dsoTsoConnectionId;
    }

    public void setDsoTsoConnectionId(LongFilter dsoTsoConnectionId) {
        this.dsoTsoConnectionId = dsoTsoConnectionId;
    }

    public LongFilter getBaseMVAId() {
        return baseMVAId;
    }

    public LongFilter baseMVAId() {
        if (baseMVAId == null) {
            baseMVAId = new LongFilter();
        }
        return baseMVAId;
    }

    public void setBaseMVAId(LongFilter baseMVAId) {
        this.baseMVAId = baseMVAId;
    }

    public LongFilter getVoltageLevelId() {
        return voltageLevelId;
    }

    public LongFilter voltageLevelId() {
        if (voltageLevelId == null) {
            voltageLevelId = new LongFilter();
        }
        return voltageLevelId;
    }

    public void setVoltageLevelId(LongFilter voltageLevelId) {
        this.voltageLevelId = voltageLevelId;
    }

    public LongFilter getSimulationId() {
        return simulationId;
    }

    public LongFilter simulationId() {
        if (simulationId == null) {
            simulationId = new LongFilter();
        }
        return simulationId;
    }

    public void setSimulationId(LongFilter simulationId) {
        this.simulationId = simulationId;
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
        final NetworkCriteria that = (NetworkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mpcName, that.mpcName) &&
            Objects.equals(country, that.country) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(networkDate, that.networkDate) &&
            Objects.equals(version, that.version) &&
            Objects.equals(creationDateTime, that.creationDateTime) &&
            Objects.equals(updateDateTime, that.updateDateTime) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(generatorId, that.generatorId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(storageId, that.storageId) &&
            Objects.equals(transformerId, that.transformerId) &&
            Objects.equals(capacitorId, that.capacitorId) &&
            Objects.equals(inputFileId, that.inputFileId) &&
            Objects.equals(assetUgCableId, that.assetUgCableId) &&
            Objects.equals(assetTransformerId, that.assetTransformerId) &&
            Objects.equals(billingConsumptionId, that.billingConsumptionId) &&
            Objects.equals(billingDerId, that.billingDerId) &&
            Objects.equals(lineCableId, that.lineCableId) &&
            Objects.equals(genProfileId, that.genProfileId) &&
            Objects.equals(loadProfileId, that.loadProfileId) &&
            Objects.equals(flexProfileId, that.flexProfileId) &&
            Objects.equals(transfProfileId, that.transfProfileId) &&
            Objects.equals(branchProfileId, that.branchProfileId) &&
            Objects.equals(topologyBusId, that.topologyBusId) &&
            Objects.equals(dsoTsoConnectionId, that.dsoTsoConnectionId) &&
            Objects.equals(baseMVAId, that.baseMVAId) &&
            Objects.equals(voltageLevelId, that.voltageLevelId) &&
            Objects.equals(simulationId, that.simulationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            mpcName,
            country,
            type,
            description,
            isDeleted,
            networkDate,
            version,
            creationDateTime,
            updateDateTime,
            busId,
            generatorId,
            branchId,
            storageId,
            transformerId,
            capacitorId,
            inputFileId,
            assetUgCableId,
            assetTransformerId,
            billingConsumptionId,
            billingDerId,
            lineCableId,
            genProfileId,
            loadProfileId,
            flexProfileId,
            transfProfileId,
            branchProfileId,
            topologyBusId,
            dsoTsoConnectionId,
            baseMVAId,
            voltageLevelId,
            simulationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetworkCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (mpcName != null ? "mpcName=" + mpcName + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (networkDate != null ? "networkDate=" + networkDate + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (creationDateTime != null ? "creationDateTime=" + creationDateTime + ", " : "") +
            (updateDateTime != null ? "updateDateTime=" + updateDateTime + ", " : "") +
            (busId != null ? "busId=" + busId + ", " : "") +
            (generatorId != null ? "generatorId=" + generatorId + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (storageId != null ? "storageId=" + storageId + ", " : "") +
            (transformerId != null ? "transformerId=" + transformerId + ", " : "") +
            (capacitorId != null ? "capacitorId=" + capacitorId + ", " : "") +
            (inputFileId != null ? "inputFileId=" + inputFileId + ", " : "") +
            (assetUgCableId != null ? "assetUgCableId=" + assetUgCableId + ", " : "") +
            (assetTransformerId != null ? "assetTransformerId=" + assetTransformerId + ", " : "") +
            (billingConsumptionId != null ? "billingConsumptionId=" + billingConsumptionId + ", " : "") +
            (billingDerId != null ? "billingDerId=" + billingDerId + ", " : "") +
            (lineCableId != null ? "lineCableId=" + lineCableId + ", " : "") +
            (genProfileId != null ? "genProfileId=" + genProfileId + ", " : "") +
            (loadProfileId != null ? "loadProfileId=" + loadProfileId + ", " : "") +
            (flexProfileId != null ? "flexProfileId=" + flexProfileId + ", " : "") +
            (transfProfileId != null ? "transfProfileId=" + transfProfileId + ", " : "") +
            (branchProfileId != null ? "branchProfileId=" + branchProfileId + ", " : "") +
            (topologyBusId != null ? "topologyBusId=" + topologyBusId + ", " : "") +
            (dsoTsoConnectionId != null ? "dsoTsoConnectionId=" + dsoTsoConnectionId + ", " : "") +
            (baseMVAId != null ? "baseMVAId=" + baseMVAId + ", " : "") +
            (voltageLevelId != null ? "voltageLevelId=" + voltageLevelId + ", " : "") +
            (simulationId != null ? "simulationId=" + simulationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
