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
 * Criteria class for the {@link com.attest.ict.domain.Tool} entity. This class is used
 * in {@link com.attest.ict.web.rest.ToolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tools?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ToolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter workPackage;

    private StringFilter num;

    private StringFilter name;

    private StringFilter path;

    private StringFilter description;

    private LongFilter inputFileId;

    private LongFilter outputFileId;

    private LongFilter taskId;

    private LongFilter parameterId;

    private Boolean distinct;

    public ToolCriteria() {}

    public ToolCriteria(ToolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.workPackage = other.workPackage == null ? null : other.workPackage.copy();
        this.num = other.num == null ? null : other.num.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.inputFileId = other.inputFileId == null ? null : other.inputFileId.copy();
        this.outputFileId = other.outputFileId == null ? null : other.outputFileId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.parameterId = other.parameterId == null ? null : other.parameterId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ToolCriteria copy() {
        return new ToolCriteria(this);
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

    public StringFilter getWorkPackage() {
        return workPackage;
    }

    public StringFilter workPackage() {
        if (workPackage == null) {
            workPackage = new StringFilter();
        }
        return workPackage;
    }

    public void setWorkPackage(StringFilter workPackage) {
        this.workPackage = workPackage;
    }

    public StringFilter getNum() {
        return num;
    }

    public StringFilter num() {
        if (num == null) {
            num = new StringFilter();
        }
        return num;
    }

    public void setNum(StringFilter num) {
        this.num = num;
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

    public StringFilter getPath() {
        return path;
    }

    public StringFilter path() {
        if (path == null) {
            path = new StringFilter();
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
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

    public LongFilter getParameterId() {
        return parameterId;
    }

    public LongFilter parameterId() {
        if (parameterId == null) {
            parameterId = new LongFilter();
        }
        return parameterId;
    }

    public void setParameterId(LongFilter parameterId) {
        this.parameterId = parameterId;
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
        final ToolCriteria that = (ToolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(workPackage, that.workPackage) &&
            Objects.equals(num, that.num) &&
            Objects.equals(name, that.name) &&
            Objects.equals(path, that.path) &&
            Objects.equals(description, that.description) &&
            Objects.equals(inputFileId, that.inputFileId) &&
            Objects.equals(outputFileId, that.outputFileId) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(parameterId, that.parameterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workPackage, num, name, path, description, inputFileId, outputFileId, taskId, parameterId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (workPackage != null ? "workPackage=" + workPackage + ", " : "") +
            (num != null ? "num=" + num + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (path != null ? "path=" + path + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (inputFileId != null ? "inputFileId=" + inputFileId + ", " : "") +
            (outputFileId != null ? "outputFileId=" + outputFileId + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            (parameterId != null ? "parameterId=" + parameterId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
