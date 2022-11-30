package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GenProfile.
 */
@Entity
@Table(name = "gen_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "season")
    private String season;

    @Column(name = "typical_day")
    private String typicalDay;

    @Column(name = "mode")
    private Integer mode;

    @Column(name = "time_interval")
    private Double timeInterval;

    @Column(name = "upload_date_time")
    private Instant uploadDateTime;

    @JsonIgnoreProperties(
        value = { "tool", "genProfile", "flexProfile", "loadProfile", "transfProfile", "branchProfile", "network", "simulations" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private InputFile inputFile;

    @OneToMany(mappedBy = "genProfile", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "genProfile", "generator" }, allowSetters = true)
    private Set<GenElVal> genElVals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "buses",
            "generators",
            "branches",
            "storages",
            "transformers",
            "capacitors",
            "inputFiles",
            "assetUgCables",
            "assetTransformers",
            "billingConsumptions",
            "billingDers",
            "lineCables",
            "genProfiles",
            "loadProfiles",
            "flexProfiles",
            "transfProfiles",
            "branchProfiles",
            "topologyBuses",
            "dsoTsoConnections",
            "baseMVA",
            "voltageLevel",
            "simulations",
        },
        allowSetters = true
    )
    private Network network;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeason() {
        return this.season;
    }

    public GenProfile season(String season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTypicalDay() {
        return this.typicalDay;
    }

    public GenProfile typicalDay(String typicalDay) {
        this.setTypicalDay(typicalDay);
        return this;
    }

    public void setTypicalDay(String typicalDay) {
        this.typicalDay = typicalDay;
    }

    public Integer getMode() {
        return this.mode;
    }

    public GenProfile mode(Integer mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getTimeInterval() {
        return this.timeInterval;
    }

    public GenProfile timeInterval(Double timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public void setTimeInterval(Double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Instant getUploadDateTime() {
        return this.uploadDateTime;
    }

    public GenProfile uploadDateTime(Instant uploadDateTime) {
        this.setUploadDateTime(uploadDateTime);
        return this;
    }

    public void setUploadDateTime(Instant uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public InputFile getInputFile() {
        return this.inputFile;
    }

    public void setInputFile(InputFile inputFile) {
        this.inputFile = inputFile;
    }

    public GenProfile inputFile(InputFile inputFile) {
        this.setInputFile(inputFile);
        return this;
    }

    public Set<GenElVal> getGenElVals() {
        return this.genElVals;
    }

    public void setGenElVals(Set<GenElVal> genElVals) {
        if (this.genElVals != null) {
            this.genElVals.forEach(i -> i.setGenProfile(null));
        }
        if (genElVals != null) {
            genElVals.forEach(i -> i.setGenProfile(this));
        }
        this.genElVals = genElVals;
    }

    public GenProfile genElVals(Set<GenElVal> genElVals) {
        this.setGenElVals(genElVals);
        return this;
    }

    public GenProfile addGenElVal(GenElVal genElVal) {
        this.genElVals.add(genElVal);
        genElVal.setGenProfile(this);
        return this;
    }

    public GenProfile removeGenElVal(GenElVal genElVal) {
        this.genElVals.remove(genElVal);
        genElVal.setGenProfile(null);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public GenProfile network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenProfile)) {
            return false;
        }
        return id != null && id.equals(((GenProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenProfile{" +
            "id=" + getId() +
            ", season='" + getSeason() + "'" +
            ", typicalDay='" + getTypicalDay() + "'" +
            ", mode=" + getMode() +
            ", timeInterval=" + getTimeInterval() +
            ", uploadDateTime='" + getUploadDateTime() + "'" +
            "}";
    }
}
