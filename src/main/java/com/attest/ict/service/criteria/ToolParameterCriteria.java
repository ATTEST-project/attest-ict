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
 * Criteria class for the {@link com.attest.ict.domain.ToolParameter} entity. This class is used
 * in {@link com.attest.ict.web.rest.ToolParameterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tool-parameters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ToolParameterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter defaultValue;

    private BooleanFilter isEnabled;

    private StringFilter description;

    private InstantFilter lastUpdate;

    private LongFilter toolId;

    private Boolean distinct;

    public ToolParameterCriteria() {}

    public ToolParameterCriteria(ToolParameterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.defaultValue = other.defaultValue == null ? null : other.defaultValue.copy();
        this.isEnabled = other.isEnabled == null ? null : other.isEnabled.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.lastUpdate = other.lastUpdate == null ? null : other.lastUpdate.copy();
        this.toolId = other.toolId == null ? null : other.toolId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ToolParameterCriteria copy() {
        return new ToolParameterCriteria(this);
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

    public StringFilter getDefaultValue() {
        return defaultValue;
    }

    public StringFilter defaultValue() {
        if (defaultValue == null) {
            defaultValue = new StringFilter();
        }
        return defaultValue;
    }

    public void setDefaultValue(StringFilter defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public BooleanFilter isEnabled() {
        if (isEnabled == null) {
            isEnabled = new BooleanFilter();
        }
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
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

    public InstantFilter getLastUpdate() {
        return lastUpdate;
    }

    public InstantFilter lastUpdate() {
        if (lastUpdate == null) {
            lastUpdate = new InstantFilter();
        }
        return lastUpdate;
    }

    public void setLastUpdate(InstantFilter lastUpdate) {
        this.lastUpdate = lastUpdate;
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
        final ToolParameterCriteria that = (ToolParameterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(defaultValue, that.defaultValue) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(description, that.description) &&
            Objects.equals(lastUpdate, that.lastUpdate) &&
            Objects.equals(toolId, that.toolId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, defaultValue, isEnabled, description, lastUpdate, toolId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolParameterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (defaultValue != null ? "defaultValue=" + defaultValue + ", " : "") +
            (isEnabled != null ? "isEnabled=" + isEnabled + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (lastUpdate != null ? "lastUpdate=" + lastUpdate + ", " : "") +
            (toolId != null ? "toolId=" + toolId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
