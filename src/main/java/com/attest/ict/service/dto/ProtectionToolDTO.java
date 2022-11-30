package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.ProtectionTool} entity.
 */
public class ProtectionToolDTO implements Serializable {

    private Long id;

    private String type;

    private BranchDTO branch;

    private BusDTO bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }

    public BusDTO getBus() {
        return bus;
    }

    public void setBus(BusDTO bus) {
        this.bus = bus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtectionToolDTO)) {
            return false;
        }

        ProtectionToolDTO protectionToolDTO = (ProtectionToolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, protectionToolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProtectionToolDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", branch=" + getBranch() +
            ", bus=" + getBus() +
            "}";
    }
}
