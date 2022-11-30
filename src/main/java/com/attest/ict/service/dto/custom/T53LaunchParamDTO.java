package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class T53LaunchParamDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private T53FirstConfigDTO firstConfig;

    private T53AllConfigDTO t52Config;

    private T53AllConfigDTO actionsConfig;

    public T53FirstConfigDTO getFirstConfig() {
        return firstConfig;
    }

    public void setFirstConfig(T53FirstConfigDTO firstConfig) {
        this.firstConfig = firstConfig;
    }

    public T53AllConfigDTO getT52Config() {
        return t52Config;
    }

    public void setT52Config(T53AllConfigDTO t52Config) {
        this.t52Config = t52Config;
    }

    public T53AllConfigDTO getActionsConfig() {
        return actionsConfig;
    }

    public void setActionsConfig(T53AllConfigDTO actionsConfig) {
        this.actionsConfig = actionsConfig;
    }

    @Override
    public String toString() {
        return "T53LaunchParamDTO [firstConfig=" + firstConfig + ", t52Config=" + t52Config + ", actionsConfig=" + actionsConfig + "]";
    }
}
