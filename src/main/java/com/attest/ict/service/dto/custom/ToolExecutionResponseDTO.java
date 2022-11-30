package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class ToolExecutionResponseDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    String status;

    String uuid;

    public ToolExecutionResponseDTO(String status, String uuid) {
        super();
        this.status = status;
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
