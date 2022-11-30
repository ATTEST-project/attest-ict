package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ToolParameter.
 */
@Entity
@Table(name = "tool_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ToolParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "default_value", nullable = false)
    private String defaultValue;

    @NotNull
    @Column(name = "is_enabled", nullable = false, columnDefinition = "boolean default true")
    private Boolean isEnabled;

    @Column(name = "description")
    private String description;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFiles", "outputFiles", "tasks", "parameters" }, allowSetters = true)
    private Tool tool;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ToolParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ToolParameter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public ToolParameter defaultValue(String defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public ToolParameter isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getDescription() {
        return this.description;
    }

    public ToolParameter description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public ToolParameter lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Tool getTool() {
        return this.tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public ToolParameter tool(Tool tool) {
        this.setTool(tool);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolParameter)) {
            return false;
        }
        return id != null && id.equals(((ToolParameter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolParameter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", description='" + getDescription() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
