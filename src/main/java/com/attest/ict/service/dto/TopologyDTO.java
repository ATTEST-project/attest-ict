package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Topology} entity.
 */
public class TopologyDTO implements Serializable {

    private Long id;

    private String powerLineBranch;

    private String p1;

    private String p2;

    private TopologyBusDTO powerLineBranchParent;

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

    public String getp1() {
        return p1;
    }

    public void setp1(String p1) {
        this.p1 = p1;
    }

    public String getp2() {
        return p2;
    }

    public void setp2(String p2) {
        this.p2 = p2;
    }

    public TopologyBusDTO getPowerLineBranchParent() {
        return powerLineBranchParent;
    }

    public void setPowerLineBranchParent(TopologyBusDTO powerLineBranchParent) {
        this.powerLineBranchParent = powerLineBranchParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopologyDTO)) {
            return false;
        }

        TopologyDTO topologyDTO = (TopologyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, topologyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopologyDTO{" +
            "id=" + getId() +
            ", powerLineBranch='" + getPowerLineBranch() + "'" +
            ", p1='" + getp1() + "'" +
            ", p2='" + getp2() + "'" +
            ", powerLineBranchParent=" + getPowerLineBranchParent() +
            "}";
    }
}
