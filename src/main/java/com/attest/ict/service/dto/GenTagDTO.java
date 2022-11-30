package com.attest.ict.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.GenTag} entity.
 */
public class GenTagDTO implements Serializable {

    private Long id;

    private String genTag;

    private GeneratorDTO generator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenTag() {
        return genTag;
    }

    public void setGenTag(String genTag) {
        this.genTag = genTag;
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
        if (!(o instanceof GenTagDTO)) {
            return false;
        }

        GenTagDTO genTagDTO = (GenTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenTagDTO{" +
            "id=" + getId() +
            ", genTag='" + getGenTag() + "'" +
            ", generator=" + getGenerator() +
            "}";
    }
}
