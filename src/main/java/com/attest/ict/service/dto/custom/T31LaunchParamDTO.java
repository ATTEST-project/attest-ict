package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class T31LaunchParamDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private T31ParamsDTO parameters;

    public T31ParamsDTO getParameters() {
        return parameters;
    }

    public void setParameters(T31ParamsDTO parameters) {
        this.parameters = parameters;
    }
}
