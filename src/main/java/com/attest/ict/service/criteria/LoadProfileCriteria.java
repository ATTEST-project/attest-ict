package com.attest.ict.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.attest.ict.domain.LoadProfile} entity. This class is used
 * in {@link com.attest.ict.web.rest.LoadProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /load-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoadProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter season;

    private StringFilter typicalDay;

    private IntegerFilter mode;

    private DoubleFilter timeInterval;

    private InstantFilter uploadDateTime;

    private LongFilter inputFileId;

    private LongFilter loadElValId;

    private LongFilter networkId;

    private Boolean distinct;

    public LoadProfileCriteria() {}

    public LoadProfileCriteria(LoadProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.season = other.season == null ? null : other.season.copy();
        this.typicalDay = other.typicalDay == null ? null : other.typicalDay.copy();
        this.mode = other.mode == null ? null : other.mode.copy();
        this.timeInterval = other.timeInterval == null ? null : other.timeInterval.copy();
        this.uploadDateTime = other.uploadDateTime == null ? null : other.uploadDateTime.copy();
        this.inputFileId = other.inputFileId == null ? null : other.inputFileId.copy();
        this.loadElValId = other.loadElValId == null ? null : other.loadElValId.copy();
        this.networkId = other.networkId == null ? null : other.networkId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoadProfileCriteria copy() {
        return new LoadProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSeason() {
        return season;
    }

    public StringFilter season() {
        if (season == null) {
            season = new StringFilter();
        }
        return season;
    }

    public void setSeason(StringFilter season) {
        this.season = season;
    }

    public StringFilter getTypicalDay() {
        return typicalDay;
    }

    public StringFilter typicalDay() {
        if (typicalDay == null) {
            typicalDay = new StringFilter();
        }
        return typicalDay;
    }

    public void setTypicalDay(StringFilter typicalDay) {
        this.typicalDay = typicalDay;
    }

    public IntegerFilter getMode() {
        return mode;
    }

    public IntegerFilter mode() {
        if (mode == null) {
            mode = new IntegerFilter();
        }
        return mode;
    }

    public void setMode(IntegerFilter mode) {
        this.mode = mode;
    }

    public DoubleFilter getTimeInterval() {
        return timeInterval;
    }

    public DoubleFilter timeInterval() {
        if (timeInterval == null) {
            timeInterval = new DoubleFilter();
        }
        return timeInterval;
    }

    public void setTimeInterval(DoubleFilter timeInterval) {
        this.timeInterval = timeInterval;
    }

    public InstantFilter getUploadDateTime() {
        return uploadDateTime;
    }

    public InstantFilter uploadDateTime() {
        if (uploadDateTime == null) {
            uploadDateTime = new InstantFilter();
        }
        return uploadDateTime;
    }

    public void setUploadDateTime(InstantFilter uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public LongFilter getInputFileId() {
        return inputFileId;
    }

    public LongFilter inputFileId() {
        if (inputFileId == null) {
            inputFileId = new LongFilter();
        }
        return inputFileId;
    }

    public void setInputFileId(LongFilter inputFileId) {
        this.inputFileId = inputFileId;
    }

    public LongFilter getLoadElValId() {
        return loadElValId;
    }

    public LongFilter loadElValId() {
        if (loadElValId == null) {
            loadElValId = new LongFilter();
        }
        return loadElValId;
    }

    public void setLoadElValId(LongFilter loadElValId) {
        this.loadElValId = loadElValId;
    }

    public LongFilter getNetworkId() {
        return networkId;
    }

    public LongFilter networkId() {
        if (networkId == null) {
            networkId = new LongFilter();
        }
        return networkId;
    }

    public void setNetworkId(LongFilter networkId) {
        this.networkId = networkId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoadProfileCriteria that = (LoadProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(season, that.season) &&
            Objects.equals(typicalDay, that.typicalDay) &&
            Objects.equals(mode, that.mode) &&
            Objects.equals(timeInterval, that.timeInterval) &&
            Objects.equals(uploadDateTime, that.uploadDateTime) &&
            Objects.equals(inputFileId, that.inputFileId) &&
            Objects.equals(loadElValId, that.loadElValId) &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, season, typicalDay, mode, timeInterval, uploadDateTime, inputFileId, loadElValId, networkId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoadProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (season != null ? "season=" + season + ", " : "") +
            (typicalDay != null ? "typicalDay=" + typicalDay + ", " : "") +
            (mode != null ? "mode=" + mode + ", " : "") +
            (timeInterval != null ? "timeInterval=" + timeInterval + ", " : "") +
            (uploadDateTime != null ? "uploadDateTime=" + uploadDateTime + ", " : "") +
            (inputFileId != null ? "inputFileId=" + inputFileId + ", " : "") +
            (loadElValId != null ? "loadElValId=" + loadElValId + ", " : "") +
            (networkId != null ? "networkId=" + networkId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
