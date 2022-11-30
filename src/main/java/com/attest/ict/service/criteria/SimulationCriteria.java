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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.attest.ict.domain.Simulation} entity. This class is used
 * in {@link com.attest.ict.web.rest.SimulationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /simulations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SimulationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter description;

    private LongFilter networkId;

    private LongFilter inputFileId;

    private LongFilter taskId;

    private LongFilter outputFileId;

    private Boolean distinct;

    public SimulationCriteria() {}

    public SimulationCriteria(SimulationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.inputFileId = other.inputFileId == null ? null : other.inputFileId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.outputFileId = other.outputFileId == null ? null : other.outputFileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SimulationCriteria copy() {
        return new SimulationCriteria(this);
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

    public UUIDFilter getUuid() {
        return uuid;
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            uuid = new UUIDFilter();
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
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

    public LongFilter getTaskId() {
        return taskId;
    }

    public LongFilter taskId() {
        if (taskId == null) {
            taskId = new LongFilter();
        }
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getOutputFileId() {
        return outputFileId;
    }

    public LongFilter outputFileId() {
        if (outputFileId == null) {
            outputFileId = new LongFilter();
        }
        return outputFileId;
    }

    public void setOutputFileId(LongFilter outputFileId) {
        this.outputFileId = outputFileId;
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
        final SimulationCriteria that = (SimulationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(description, that.description) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(inputFileId, that.inputFileId) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(outputFileId, that.outputFileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, description, networkId, inputFileId, taskId, outputFileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimulationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (inputFileId != null ? "inputFileId=" + inputFileId + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            (outputFileId != null ? "outputFileId=" + outputFileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
