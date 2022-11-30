package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "electricity_energy")
    private Double electricityEnergy;

    @Column(name = "gas_energy")
    private Double gasEnergy;

    @Column(name = "secondary_band")
    private Double secondaryBand;

    @Column(name = "secondary_up")
    private Double secondaryUp;

    @Column(name = "secondary_down")
    private Double secondaryDown;

    @Column(name = "secondary_ratio_up")
    private Double secondaryRatioUp;

    @Column(name = "secondary_ratio_down")
    private Double secondaryRatioDown;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Price id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getElectricityEnergy() {
        return this.electricityEnergy;
    }

    public Price electricityEnergy(Double electricityEnergy) {
        this.setElectricityEnergy(electricityEnergy);
        return this;
    }

    public void setElectricityEnergy(Double electricityEnergy) {
        this.electricityEnergy = electricityEnergy;
    }

    public Double getGasEnergy() {
        return this.gasEnergy;
    }

    public Price gasEnergy(Double gasEnergy) {
        this.setGasEnergy(gasEnergy);
        return this;
    }

    public void setGasEnergy(Double gasEnergy) {
        this.gasEnergy = gasEnergy;
    }

    public Double getSecondaryBand() {
        return this.secondaryBand;
    }

    public Price secondaryBand(Double secondaryBand) {
        this.setSecondaryBand(secondaryBand);
        return this;
    }

    public void setSecondaryBand(Double secondaryBand) {
        this.secondaryBand = secondaryBand;
    }

    public Double getSecondaryUp() {
        return this.secondaryUp;
    }

    public Price secondaryUp(Double secondaryUp) {
        this.setSecondaryUp(secondaryUp);
        return this;
    }

    public void setSecondaryUp(Double secondaryUp) {
        this.secondaryUp = secondaryUp;
    }

    public Double getSecondaryDown() {
        return this.secondaryDown;
    }

    public Price secondaryDown(Double secondaryDown) {
        this.setSecondaryDown(secondaryDown);
        return this;
    }

    public void setSecondaryDown(Double secondaryDown) {
        this.secondaryDown = secondaryDown;
    }

    public Double getSecondaryRatioUp() {
        return this.secondaryRatioUp;
    }

    public Price secondaryRatioUp(Double secondaryRatioUp) {
        this.setSecondaryRatioUp(secondaryRatioUp);
        return this;
    }

    public void setSecondaryRatioUp(Double secondaryRatioUp) {
        this.secondaryRatioUp = secondaryRatioUp;
    }

    public Double getSecondaryRatioDown() {
        return this.secondaryRatioDown;
    }

    public Price secondaryRatioDown(Double secondaryRatioDown) {
        this.setSecondaryRatioDown(secondaryRatioDown);
        return this;
    }

    public void setSecondaryRatioDown(Double secondaryRatioDown) {
        this.secondaryRatioDown = secondaryRatioDown;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        return id != null && id.equals(((Price) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Price{" +
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
