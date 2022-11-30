package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.GenCost} entity.
 */
public class GenCostDTO implements Serializable {

    private Long id;

    private Integer model;

    private Double startup;

    private Double shutdown;

    private Long nCost;

    private String costPF;

    private String costQF;

    private GeneratorDTO generator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Double getStartup() {
        return startup;
    }

    public void setStartup(Double startup) {
        this.startup = startup;
    }

    public Double getShutdown() {
        return shutdown;
    }

    public void setShutdown(Double shutdown) {
        this.shutdown = shutdown;
    }

    public Long getnCost() {
        return nCost;
    }

    public void setnCost(Long nCost) {
        this.nCost = nCost;
    }

    public String getCostPF() {
        return costPF;
    }

    public void setCostPF(String costPF) {
        this.costPF = costPF;
    }

    public String getCostQF() {
        return costQF;
    }

    public void setCostQF(String costQF) {
        this.costQF = costQF;
    }

    public GeneratorDTO getGenerator() {
        return generator;
    }

    public void setGenerator(GeneratorDTO generator) {
        this.generator = generator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenCostDTO)) {
            return false;
        }

        GenCostDTO genCostDTO = (GenCostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genCostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenCostDTO{" +
            "id=" + getId() +
            ", model=" + getModel() +
            ", startup=" + getStartup() +
            ", shutdown=" + getShutdown() +
            ", nCost=" + getnCost() +
            ", costPF='" + getCostPF() + "'" +
            ", costQF='" + getCostQF() + "'" +
            ", generator=" + getGenerator() +
            "}";
    }
}
