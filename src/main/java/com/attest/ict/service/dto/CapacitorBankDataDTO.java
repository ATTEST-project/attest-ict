package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.CapacitorBankData} entity.
 */
public class CapacitorBankDataDTO implements Serializable {

    private Long id;

    private Long busNum;

    private String nodeId;

    private String bankId;

    private Double qnom;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return busNum;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Double getQnom() {
        return qnom;
    }

    public void setQnom(Double qnom) {
        this.qnom = qnom;
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
        if (!(o instanceof CapacitorBankDataDTO)) {
            return false;
        }

        CapacitorBankDataDTO capacitorBankDataDTO = (CapacitorBankDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, capacitorBankDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CapacitorBankDataDTO{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", nodeId='" + getNodeId() + "'" +
            ", bankId='" + getBankId() + "'" +
            ", qnom=" + getQnom() +
            ", network=" + getNetwork() +
            "}";
    }
}
