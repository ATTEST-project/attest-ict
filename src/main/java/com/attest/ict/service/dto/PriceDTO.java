package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.Price} entity.
 */
public class PriceDTO implements Serializable {

    private Long id;

    private Double electricityEnergy;

    private Double gasEnergy;

    private Double secondaryBand;

    private Double secondaryUp;

    private Double secondaryDown;

    private Double secondaryRatioUp;

    private Double secondaryRatioDown;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getElectricityEnergy() {
        return electricityEnergy;
    }

    public void setElectricityEnergy(Double electricityEnergy) {
        this.electricityEnergy = electricityEnergy;
    }

    public Double getGasEnergy() {
        return gasEnergy;
    }

    public void setGasEnergy(Double gasEnergy) {
        this.gasEnergy = gasEnergy;
    }

    public Double getSecondaryBand() {
        return secondaryBand;
    }

    public void setSecondaryBand(Double secondaryBand) {
        this.secondaryBand = secondaryBand;
    }

    public Double getSecondaryUp() {
        return secondaryUp;
    }

    public void setSecondaryUp(Double secondaryUp) {
        this.secondaryUp = secondaryUp;
    }

    public Double getSecondaryDown() {
        return secondaryDown;
    }

    public void setSecondaryDown(Double secondaryDown) {
        this.secondaryDown = secondaryDown;
    }

    public Double getSecondaryRatioUp() {
        return secondaryRatioUp;
    }

    public void setSecondaryRatioUp(Double secondaryRatioUp) {
        this.secondaryRatioUp = secondaryRatioUp;
    }

    public Double getSecondaryRatioDown() {
        return secondaryRatioDown;
    }

    public void setSecondaryRatioDown(Double secondaryRatioDown) {
        this.secondaryRatioDown = secondaryRatioDown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceDTO)) {
            return false;
        }

        PriceDTO priceDTO = (PriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceDTO{" +
            "id=" + getId() +
            ", electricityEnergy=" + getElectricityEnergy() +
            ", gasEnergy=" + getGasEnergy() +
            ", secondaryBand=" + getSecondaryBand() +
            ", secondaryUp=" + getSecondaryUp() +
            ", secondaryDown=" + getSecondaryDown() +
            ", secondaryRatioUp=" + getSecondaryRatioUp() +
            ", secondaryRatioDown=" + getSecondaryRatioDown() +
            "}";
    }
}
