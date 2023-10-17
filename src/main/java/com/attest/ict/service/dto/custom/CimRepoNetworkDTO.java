package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class CimRepoNetworkDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @NotNull
    private Long networkId;

    @NotNull
    private String networkName;

    private String type;

    private String country;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CimRepoNetworkDTO that = (CimRepoNetworkDTO) o;
        return (
            networkId.equals(that.networkId) &&
            networkName.equals(that.networkName) &&
            type.equals(that.type) &&
            country.equals(that.country)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, networkName, type, country);
    }

    @Override
    public String toString() {
        return (
            "CimNetworkRepoDTO{" +
            "id=" +
            networkId +
            ", name='" +
            networkName +
            '\'' +
            ", type='" +
            type +
            '\'' +
            ", country='" +
            country +
            '\'' +
            '}'
        );
    }
}
