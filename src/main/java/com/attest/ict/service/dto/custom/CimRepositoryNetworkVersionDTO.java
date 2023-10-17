package com.attest.ict.service.dto.custom;

import java.time.Instant;
import java.util.Objects;

public class CimRepositoryNetworkVersionDTO {

    private Long networkId;
    private Long networkVersionId;
    private String networkVersion;
    private Instant networkVersionDate;

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public Long getNetworkVersionId() {
        return networkVersionId;
    }

    public void setNetworkVersionId(Long networkVersionId) {
        this.networkVersionId = networkVersionId;
    }

    public String getNetworkVersion() {
        return networkVersion;
    }

    public void setNetworkVersion(String networkVersion) {
        this.networkVersion = networkVersion;
    }

    public Instant getNetworkVersionDate() {
        return networkVersionDate;
    }

    public void setNetworkVersionDate(Instant networkVersionDate) {
        this.networkVersionDate = networkVersionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CimRepositoryNetworkVersionDTO)) {
            return false;
        }

        CimRepositoryNetworkVersionDTO networkDTO = (CimRepositoryNetworkVersionDTO) o;
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
        return (
            "CimRepositoryNetworkVersionDTO{" +
            "networkId=" +
            getNetworkId() +
            ", networkVersionId=" +
            getNetworkVersionId() +
            ", networkVersion=" +
            getNetworkVersion() +
            ", networkVersionDate=" +
            getNetworkVersionDate() +
            "}"
        );
    }
}
