package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Branch} entity.
 */
public class BranchDTO implements Serializable {

    private Long id;

    private Long fbus;

    private Long tbus;

    private Double r;

    private Double x;

    private Double b;

    private Double ratea;

    private Double rateb;

    private Double ratec;

    private Double tapRatio;

    private Double angle;

    private Integer status;

    private Integer angmin;

    private Integer angmax;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFbus() {
        return fbus;
    }

    public void setFbus(Long fbus) {
        this.fbus = fbus;
    }

    public Long getTbus() {
        return tbus;
    }

    public void setTbus(Long tbus) {
        this.tbus = tbus;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Double getRatea() {
        return ratea;
    }

    public void setRatea(Double ratea) {
        this.ratea = ratea;
    }

    public Double getRateb() {
        return rateb;
    }

    public void setRateb(Double rateb) {
        this.rateb = rateb;
    }

    public Double getRatec() {
        return ratec;
    }

    public void setRatec(Double ratec) {
        this.ratec = ratec;
    }

    public Double getTapRatio() {
        return tapRatio;
    }

    public void setTapRatio(Double tapRatio) {
        this.tapRatio = tapRatio;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAngmin() {
        return angmin;
    }

    public void setAngmin(Integer angmin) {
        this.angmin = angmin;
    }

    public Integer getAngmax() {
        return angmax;
    }

    public void setAngmax(Integer angmax) {
        this.angmax = angmax;
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
        if (!(o instanceof BranchDTO)) {
            return false;
        }

        BranchDTO branchDTO = (BranchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchDTO{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", r=" + getR() +
            ", x=" + getX() +
            ", b=" + getB() +
            ", ratea=" + getRatea() +
            ", rateb=" + getRateb() +
            ", ratec=" + getRatec() +
            ", tapRatio=" + getTapRatio() +
            ", angle=" + getAngle() +
            ", status=" + getStatus() +
            ", angmin=" + getAngmin() +
            ", angmax=" + getAngmax() +
            ", network=" + getNetwork() +
            "}";
    }
}
