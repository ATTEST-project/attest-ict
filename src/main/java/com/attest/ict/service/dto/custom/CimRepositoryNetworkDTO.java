package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.Objects;

public class CimRepositoryNetworkDTO implements Serializable {

    private Long networkId;
    private String networkName;

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CimRepositoryNetworkDTO)) {
            return false;
        }

        CimRepositoryNetworkDTO networkDTO = (CimRepositoryNetworkDTO) o;
        if (this.networkId == null) {
            return false;
        }
        return Objects.equals(this.networkId, networkDTO.networkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.networkId);
    }

    @Override
    public String toString() {
        return "CimRepositoryNetworkDTO{" + "networkId=" + getNetworkId() + ", networkName=" + getNetworkName() + "}";
    }
}
