package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Node} entity.
 */
public class NodeDTO implements Serializable {

    private Long id;

    private String networkId;

    private Long loadId;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getLoadId() {
        return loadId;
    }

    public void setLoadId(Long loadId) {
        this.loadId = loadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NodeDTO)) {
            return false;
        }

        NodeDTO nodeDTO = (NodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NodeDTO{" +
            "id=" + getId() +
            ", networkId='" + getNetworkId() + "'" +
            ", loadId=" + getLoadId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
