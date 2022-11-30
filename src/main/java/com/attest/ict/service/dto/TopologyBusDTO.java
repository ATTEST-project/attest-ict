package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.TopologyBus} entity.
 */
public class TopologyBusDTO implements Serializable {

    private Long id;

    private String powerLineBranch;

    private String busName1;

    private String busName2;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowerLineBranch() {
        return powerLineBranch;
    }

    public void setPowerLineBranch(String powerLineBranch) {
        this.powerLineBranch = powerLineBranch;
    }

    public String getBusName1() {
        return busName1;
    }

    public void setBusName1(String busName1) {
        this.busName1 = busName1;
    }

    public String getBusName2() {
        return busName2;
    }

    public void setBusName2(String busName2) {
        this.busName2 = busName2;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopologyBusDTO)) {
            return false;
        }

        TopologyBusDTO topologyBusDTO = (TopologyBusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, topologyBusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopologyBusDTO{" +
            "id=" + getId() +
            ", powerLineBranch='" + getPowerLineBranch() + "'" +
            ", busName1='" + getBusName1() + "'" +
            ", busName2='" + getBusName2() + "'" +
            ", network=" + getNetwork() +
            "}";
    }
}
