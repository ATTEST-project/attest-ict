package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.attest.ict.domain.Tool} entity.
 */
public class ToolDTO implements Serializable {

    private Long id;

    @NotNull
    private String workPackage;

    @NotNull
    private String num;

    @NotNull
    private String name;

    private String path;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkPackage() {
        return workPackage;
    }

    public void setWorkPackage(String workPackage) {
        this.workPackage = workPackage;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolDTO)) {
            return false;
        }

        ToolDTO toolDTO = (ToolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolDTO{" +
            "id=" + getId() +
            ", workPackage='" + getWorkPackage() + "'" +
            ", num='" + getNum() + "'" +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
