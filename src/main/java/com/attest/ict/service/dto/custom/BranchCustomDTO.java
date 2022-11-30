package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.math.BigInteger;

public class BranchCustomDTO implements Serializable {

    private Long branchExtensionId;

    private Long branchId;

    private Long fromBus;

    private Long toBus;

    private Double kmLength;

    public BranchCustomDTO(BigInteger branchExtensionId, BigInteger branchId, BigInteger fromBus, BigInteger toBus, Double kmLength) {
        super();
        this.branchExtensionId = branchExtensionId.longValue();
        this.branchId = branchId.longValue();
        this.fromBus = fromBus.longValue();
        this.toBus = toBus.longValue();
        this.kmLength = kmLength;
    }

    public Long getBranchExtensionId() {
        return branchExtensionId;
    }

    public void setBranchExtensionId(Long branchExtensionId) {
        this.branchExtensionId = branchExtensionId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getFromBus() {
        return fromBus;
    }

    public void setFromBus(Long fromBus) {
        this.fromBus = fromBus;
    }

    public Long getToBus() {
        return toBus;
    }

    public void setToBus(Long toBus) {
        this.toBus = toBus;
    }

    public Double getKmLength() {
        return kmLength;
    }

    public void setKmLength(Double kmLength) {
        this.kmLength = kmLength;
    }

    @Override
    public String toString() {
        return (
            "BranchCustomDTO [branchExtensionId=" +
            branchExtensionId +
            ", branchId=" +
            branchId +
            ", fromBus=" +
            fromBus +
            ", toBus=" +
            toBus +
            ", kmLength=" +
            kmLength +
            "]"
        );
    }
}
