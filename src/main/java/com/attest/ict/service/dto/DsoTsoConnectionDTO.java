package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.DsoTsoConnection} entity.
 */
public class DsoTsoConnectionDTO implements Serializable {

    private Long id;

    private String tsoNetworkName;

    private Long dsoBusNum;

    private Long tsoBusNum;

    private NetworkDTO dsoNetwork;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTsoNetworkName() {
        return tsoNetworkName;
    }

    public void setTsoNetworkName(String tsoNetworkName) {
        this.tsoNetworkName = tsoNetworkName;
    }

    public Long getDsoBusNum() {
        return dsoBusNum;
    }

    public void setDsoBusNum(Long dsoBusNum) {
        this.dsoBusNum = dsoBusNum;
    }

    public Long getTsoBusNum() {
        return tsoBusNum;
    }

    public void setTsoBusNum(Long tsoBusNum) {
        this.tsoBusNum = tsoBusNum;
    }

    public NetworkDTO getDsoNetwork() {
        return dsoNetwork;
    }

    public void setDsoNetwork(NetworkDTO dsoNetwork) {
        this.dsoNetwork = dsoNetwork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DsoTsoConnectionDTO)) {
            return false;
        }

        DsoTsoConnectionDTO dsoTsoConnectionDTO = (DsoTsoConnectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dsoTsoConnectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DsoTsoConnectionDTO{" +
            "id=" + getId() +
            ", tsoNetworkName='" + getTsoNetworkName() + "'" +
            ", dsoBusNum=" + getDsoBusNum() +
            ", tsoBusNum=" + getTsoBusNum() +
            ", dsoNetwork=" + getDsoNetwork() +
            "}";
    }
}
