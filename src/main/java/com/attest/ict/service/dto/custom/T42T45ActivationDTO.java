package com.attest.ict.service.dto.custom;

import java.io.Serializable;

public class T42T45ActivationDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private Double activePowerUpTot;
    private Double activePowerDnTot;
    private Double reactivePowerUpTot;
    private Double reactivePowerDnTot;

    public Double getActivePowerUpTot() {
        return activePowerUpTot;
    }

    public void setActivePowerUpTot(Double activePowerUpTot) {
        this.activePowerUpTot = activePowerUpTot;
    }

    public Double getActivePowerDnTot() {
        return activePowerDnTot;
    }

    public void setActivePowerDnTot(Double activePowerDnTot) {
        this.activePowerDnTot = activePowerDnTot;
    }

    public Double getReactivePowerUpTot() {
        return reactivePowerUpTot;
    }

    public void setReactivePowerUpTot(Double reactivePowerUpTot) {
        this.reactivePowerUpTot = reactivePowerUpTot;
    }

    public Double getReactivePowerDnTot() {
        return reactivePowerDnTot;
    }

    public void setReactivePowerDnTot(Double reactivePowerDnTot) {
        this.reactivePowerDnTot = reactivePowerDnTot;
    }

    @Override
    public String toString() {
        return (
            "T42T45ActivationDTO{" +
            "activePowerUpTot=" +
            activePowerUpTot +
            ", activePowerDnTot=" +
            activePowerDnTot +
            ", reactivePowerUpTot=" +
            reactivePowerUpTot +
            ", reactivePowerDnTot=" +
            reactivePowerDnTot +
            '}'
        );
    }
}
