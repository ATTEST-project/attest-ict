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
 * Criteria class for the {@link com.attest.ict.domain.Task} entity. This class is used
 * in {@link com.attest.ict.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter taskStatus;

    private StringFilter info;

    private InstantFilter dateTimeStart;

    private InstantFilter dateTimeEnd;

    private LongFilter toolLogFileId;

    private LongFilter simulationId;

    private LongFilter toolId;

    private StringFilter userId;

    private Boolean distinct;

    private StringFilter toolNum;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskStatus = other.taskStatus == null ? null : other.taskStatus.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.dateTimeStart = other.dateTimeStart == null ? null : other.dateTimeStart.copy();
        this.dateTimeEnd = other.dateTimeEnd == null ? null : other.dateTimeEnd.copy();
        this.toolLogFileId = other.toolLogFileId == null ? null : other.toolLogFileId.copy();
        this.simulationId = other.simulationId == null ? null : other.simulationId.copy();
        this.toolId = other.toolId == null ? null : other.toolId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.toolNum = other.toolNum == null ? null : other.toolNum.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public StringFilter getTaskStatus() {
        return taskStatus;
    }

    public StringFilter taskStatus() {
        if (taskStatus == null) {
            taskStatus = new StringFilter();
        }
        return taskStatus;
    }

    public void setTaskStatus(StringFilter taskStatus) {
        this.taskStatus = taskStatus;
    }

    public StringFilter getInfo() {
        return info;
    }

    public StringFilter info() {
        if (info == null) {
            info = new StringFilter();
        }
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public InstantFilter getDateTimeStart() {
        return dateTimeStart;
    }

    public InstantFilter dateTimeStart() {
        if (dateTimeStart == null) {
            dateTimeStart = new InstantFilter();
        }
        return dateTimeStart;
    }

    public void setDateTimeStart(InstantFilter dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public InstantFilter getDateTimeEnd() {
        return dateTimeEnd;
    }

    public InstantFilter dateTimeEnd() {
        if (dateTimeEnd == null) {
            dateTimeEnd = new InstantFilter();
        }
        return dateTimeEnd;
    }

    public void setDateTimeEnd(InstantFilter dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public LongFilter getToolLogFileId() {
        return toolLogFileId;
    }

    public LongFilter toolLogFileId() {
        if (toolLogFileId == null) {
            toolLogFileId = new LongFilter();
        }
        return toolLogFileId;
    }

    public void setToolLogFileId(LongFilter toolLogFileId) {
        this.toolLogFileId = toolLogFileId;
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

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    //20231003 added new criteria for sorting by toolNum in task page
    public StringFilter getToolNum() {
        return toolNum;
    }

    public StringFilter toolNum() {
        if (toolNum == null) {
            toolNum = new StringFilter();
        }
        return toolNum;
    }

    public void setToolNum(StringFilter toolNum) {
        this.toolNum = toolNum;
    }

    //20231003 modify

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
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(taskStatus, that.taskStatus) &&
            Objects.equals(info, that.info) &&
            Objects.equals(dateTimeStart, that.dateTimeStart) &&
            Objects.equals(dateTimeEnd, that.dateTimeEnd) &&
            Objects.equals(toolLogFileId, that.toolLogFileId) &&
            Objects.equals(simulationId, that.simulationId) &&
            Objects.equals(toolId, that.toolId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskStatus, info, dateTimeStart, dateTimeEnd, toolLogFileId, simulationId, toolId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (taskStatus != null ? "taskStatus=" + taskStatus + ", " : "") +
            (info != null ? "info=" + info + ", " : "") +
            (dateTimeStart != null ? "dateTimeStart=" + dateTimeStart + ", " : "") +
            (dateTimeEnd != null ? "dateTimeEnd=" + dateTimeEnd + ", " : "") +
            (toolLogFileId != null ? "toolLogFileId=" + toolLogFileId + ", " : "") +
            (simulationId != null ? "simulationId=" + simulationId + ", " : "") +
            (toolId != null ? "toolId=" + toolId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
