package com.attest.ict.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.attest.ict.domain.ToolParameter} entity.
 */
public class ToolParameterDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String defaultValue;

    @NotNull
    private Boolean isEnabled;

    private String description;

    private Instant lastUpdate;

    private ToolDTO tool;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public void setTool(ToolDTO tool) {
        this.tool = tool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolParameterDTO)) {
            return false;
        }

        ToolParameterDTO toolParameterDTO = (ToolParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toolParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolParameterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", description='" + getDescription() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", tool=" + getTool() +
            "}";
    }
}
