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
 * Criteria class for the {@link com.attest.ict.domain.OutputFile} entity. This class is used
 * in {@link com.attest.ict.web.rest.OutputFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /output-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OutputFileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter description;

    private InstantFilter uploadTime;

    private LongFilter toolId;

    private LongFilter networkId;

    private LongFilter simulationId;

    private Boolean distinct;

    public OutputFileCriteria() {}

    public OutputFileCriteria(OutputFileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.uploadTime = other.uploadTime == null ? null : other.uploadTime.copy();
        this.toolId = other.toolId == null ? null : other.toolId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.simulationId = other.simulationId == null ? null : other.simulationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OutputFileCriteria copy() {
        return new OutputFileCriteria(this);
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
        final OutputFileCriteria that = (OutputFileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(uploadTime, that.uploadTime) &&
            Objects.equals(toolId, that.toolId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(simulationId, that.simulationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, description, uploadTime, toolId, networkId, simulationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputFileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (uploadTime != null ? "uploadTime=" + uploadTime + ", " : "") +
            (toolId != null ? "toolId=" + toolId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (simulationId != null ? "simulationId=" + simulationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
