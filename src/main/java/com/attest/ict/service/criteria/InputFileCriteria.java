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
 * Criteria class for the {@link com.attest.ict.domain.InputFile} entity. This class is used
 * in {@link com.attest.ict.web.rest.InputFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /input-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InputFileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter description;

    private InstantFilter uploadTime;

    private LongFilter toolId;

    private LongFilter genProfileId;

    private LongFilter flexProfileId;

    private LongFilter loadProfileId;

    private LongFilter transfProfileId;

    private LongFilter branchProfileId;

    private LongFilter networkId;

    private LongFilter simulationId;

    private Boolean distinct;

    public InputFileCriteria() {}

    public InputFileCriteria(InputFileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.uploadTime = other.uploadTime == null ? null : other.uploadTime.copy();
        this.toolId = other.toolId == null ? null : other.toolId.copy();
        this.genProfileId = other.genProfileId == null ? null : other.genProfileId.copy();
        this.flexProfileId = other.flexProfileId == null ? null : other.flexProfileId.copy();
        this.loadProfileId = other.loadProfileId == null ? null : other.loadProfileId.copy();
        this.transfProfileId = other.transfProfileId == null ? null : other.transfProfileId.copy();
        this.branchProfileId = other.branchProfileId == null ? null : other.branchProfileId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.simulationId = other.simulationId == null ? null : other.simulationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InputFileCriteria copy() {
        return new InputFileCriteria(this);
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

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
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

    public InstantFilter getUploadTime() {
        return uploadTime;
    }

    public InstantFilter uploadTime() {
        if (uploadTime == null) {
            uploadTime = new InstantFilter();
        }
        return uploadTime;
    }

    public void setUploadTime(InstantFilter uploadTime) {
        this.uploadTime = uploadTime;
    }

    public LongFilter getToolId() {
        return toolId;
    }

    public LongFilter toolId() {
        if (toolId == null) {
            toolId = new LongFilter();
        }
        return toolId;
    }

    public void setToolId(LongFilter toolId) {
        this.toolId = toolId;
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
        final InputFileCriteria that = (InputFileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(uploadTime, that.uploadTime) &&
            Objects.equals(toolId, that.toolId) &&
            Objects.equals(genProfileId, that.genProfileId) &&
            Objects.equals(flexProfileId, that.flexProfileId) &&
            Objects.equals(loadProfileId, that.loadProfileId) &&
            Objects.equals(transfProfileId, that.transfProfileId) &&
            Objects.equals(branchProfileId, that.branchProfileId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(simulationId, that.simulationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fileName,
            description,
            uploadTime,
            toolId,
            genProfileId,
            flexProfileId,
            loadProfileId,
            transfProfileId,
            branchProfileId,
            networkId,
            simulationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputFileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (uploadTime != null ? "uploadTime=" + uploadTime + ", " : "") +
            (toolId != null ? "toolId=" + toolId + ", " : "") +
            (genProfileId != null ? "genProfileId=" + genProfileId + ", " : "") +
            (flexProfileId != null ? "flexProfileId=" + flexProfileId + ", " : "") +
            (loadProfileId != null ? "loadProfileId=" + loadProfileId + ", " : "") +
            (transfProfileId != null ? "transfProfileId=" + transfProfileId + ", " : "") +
            (branchProfileId != null ? "branchProfileId=" + branchProfileId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (simulationId != null ? "simulationId=" + simulationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
