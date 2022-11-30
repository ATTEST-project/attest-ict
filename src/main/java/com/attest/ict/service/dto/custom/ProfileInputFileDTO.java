package com.attest.ict.service.dto.custom;

import java.io.Serializable;
import java.math.BigInteger;

public class ProfileInputFileDTO implements Serializable {

    private Long id;
    private String season;
    private String typicalDay;
    private Integer mode;
    private Double timeInterval;
    private String fileName;
    private String description;

    public ProfileInputFileDTO(
        BigInteger id,
        String season,
        String typicalDay,
        Integer mode,
        Double timeInterval,
        String fileName,
        String description
    ) {
        super();
        this.id = id.longValue();
        this.season = season;
        this.typicalDay = typicalDay;
        this.mode = mode;
        this.timeInterval = timeInterval;
        this.fileName = fileName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(BigInteger id) {
        this.id = id.longValue();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + ((season == null) ? 0 : season.hashCode());
        result = prime * result + ((timeInterval == null) ? 0 : timeInterval.hashCode());
        result = prime * result + ((typicalDay == null) ? 0 : typicalDay.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProfileInputFileDTO other = (ProfileInputFileDTO) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (fileName == null) {
            if (other.fileName != null) return false;
        } else if (!fileName.equals(other.fileName)) return false;
        if (mode == null) {
            if (other.mode != null) return false;
        } else if (!mode.equals(other.mode)) return false;
        if (season == null) {
            if (other.season != null) return false;
        } else if (!season.equals(other.season)) return false;
        if (timeInterval == null) {
            if (other.timeInterval != null) return false;
        } else if (!timeInterval.equals(other.timeInterval)) return false;
        if (typicalDay == null) {
            if (other.typicalDay != null) return false;
        } else if (!typicalDay.equals(other.typicalDay)) return false;
        return true;
    }

    @Override
    public String toString() {
        return (
            "ProfileRow [season=" +
            season +
            ", typicalDay=" +
            typicalDay +
            ", mode=" +
            mode +
            ", timeInterval=" +
            timeInterval +
            ", fileName=" +
            fileName +
            ", description=" +
            description +
            "]"
        );
    }
}
