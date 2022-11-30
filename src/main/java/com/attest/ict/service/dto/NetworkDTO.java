package com.attest.ict.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.attest.ict.domain.Network} entity.
 */
public class NetworkDTO implements Serializable {

    private Long id;

    private String name;

    private String mpcName;

    private String country;

    private String type;

    private String description;

    @NotNull
    private Boolean isDeleted;

    private Instant networkDate;

    private Integer version;

    private Instant creationDateTime;

    private Instant updateDateTime;

    //LP18/11/2022 test
    private InputFileDTO inputFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMpcName() {
        return mpcName;
    }

    public void setMpcName(String mpcName) {
        this.mpcName = mpcName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getNetworkDate() {
        return networkDate;
    }

    public void setNetworkDate(Instant networkDate) {
        this.networkDate = networkDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Instant getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Instant updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkDTO)) {
            return false;
        }

        NetworkDTO networkDTO = (NetworkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, networkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetworkDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mpcName='" + getMpcName() + "'" +
            ", country='" + getCountry() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", networkDate='" + getNetworkDate() + "'" +
            ", version=" + getVersion() +
            ", creationDateTime='" + getCreationDateTime() + "'" +
            ", updateDateTime='" + getUpdateDateTime() + "'" +
            "}";
    }
}
