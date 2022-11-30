package com.attest.ict.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.attest.ict.domain.TransfProfile} entity.
 */
public class TransfProfileDTO implements Serializable {

    private Long id;

    private String season;

    private String typicalDay;

    private Integer mode;

    private Double timeInterval;

    private Instant uploadDateTime;

    private InputFileDTO inputFile;

    private NetworkDTO network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTypicalDay() {
        return typicalDay;
    }

    public void setTypicalDay(String typicalDay) {
        this.typicalDay = typicalDay;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Instant getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(Instant uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public InputFileDTO getInputFile() {
        return inputFile;
    }

    public void setInputFile(InputFileDTO inputFile) {
        this.inputFile = inputFile;
    }

    public NetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDTO network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfProfileDTO)) {
            return false;
        }

        TransfProfileDTO transfProfileDTO = (TransfProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transfProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfProfileDTO{" +
            "id=" + getId() +
            ", season='" + getSeason() + "'" +
            ", typicalDay='" + getTypicalDay() + "'" +
            ", mode=" + getMode() +
            ", timeInterval=" + getTimeInterval() +
            ", uploadDateTime='" + getUploadDateTime() + "'" +
            ", inputFile=" + getInputFile() +
            ", network=" + getNetwork() +
            "}";
    }
}
