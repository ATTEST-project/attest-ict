package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.GeneratorExtension} entity.
 */
public class GeneratorExtensionDTO implements Serializable {

    private Long id;

    private Integer idGen;

    private Integer statusCurt;

    private Integer dgType;

    private GeneratorDTO generator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdGen() {
        return idGen;
    }

    public void setIdGen(Integer idGen) {
        this.idGen = idGen;
    }

    public Integer getStatusCurt() {
        return statusCurt;
    }

    public void setStatusCurt(Integer statusCurt) {
        this.statusCurt = statusCurt;
    }

    public Integer getDgType() {
        return dgType;
    }

    public void setDgType(Integer dgType) {
        this.dgType = dgType;
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
        if (!(o instanceof GeneratorExtensionDTO)) {
            return false;
        }

        GeneratorExtensionDTO generatorExtensionDTO = (GeneratorExtensionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, generatorExtensionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneratorExtensionDTO{" +
            "id=" + getId() +
            ", idGen=" + getIdGen() +
            ", statusCurt=" + getStatusCurt() +
            ", dgType=" + getDgType() +
            ", generator=" + getGenerator() +
            "}";
    }
}
