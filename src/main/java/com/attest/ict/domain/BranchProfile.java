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
 * A BranchProfile.
 */
@Entity
@Table(name = "branch_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BranchProfile implements Serializable {

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

    @OneToMany(mappedBy = "branchProfile", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "branch", "branchProfile" }, allowSetters = true)
    private Set<BranchElVal> branchElVals = new HashSet<>();

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

    public BranchProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeason() {
        return this.season;
    }

    public BranchProfile season(String season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTypicalDay() {
        return this.typicalDay;
    }

    public BranchProfile typicalDay(String typicalDay) {
        this.setTypicalDay(typicalDay);
        return this;
    }

    public void setTypicalDay(String typicalDay) {
        this.typicalDay = typicalDay;
    }

    public Integer getMode() {
        return this.mode;
    }

    public BranchProfile mode(Integer mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getTimeInterval() {
        return this.timeInterval;
    }

    public BranchProfile timeInterval(Double timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public void setTimeInterval(Double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Instant getUploadDateTime() {
        return this.uploadDateTime;
    }

    public BranchProfile uploadDateTime(Instant uploadDateTime) {
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

    public BranchProfile inputFile(InputFile inputFile) {
        this.setInputFile(inputFile);
        return this;
    }

    public Set<BranchElVal> getBranchElVals() {
        return this.branchElVals;
    }

    public void setBranchElVals(Set<BranchElVal> branchElVals) {
        if (this.branchElVals != null) {
            this.branchElVals.forEach(i -> i.setBranchProfile(null));
        }
        if (branchElVals != null) {
            branchElVals.forEach(i -> i.setBranchProfile(this));
        }
        this.branchElVals = branchElVals;
    }

    public BranchProfile branchElVals(Set<BranchElVal> branchElVals) {
        this.setBranchElVals(branchElVals);
        return this;
    }

    public BranchProfile addBranchElVal(BranchElVal branchElVal) {
        this.branchElVals.add(branchElVal);
        branchElVal.setBranchProfile(this);
        return this;
    }

    public BranchProfile removeBranchElVal(BranchElVal branchElVal) {
        this.branchElVals.remove(branchElVal);
        branchElVal.setBranchProfile(null);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public BranchProfile network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchProfile)) {
            return false;
        }
        return id != null && id.equals(((BranchProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchProfile{" +
            "id=" + getId() +
            ", season='" + getSeason() + "'" +
            ", typicalDay='" + getTypicalDay() + "'" +
            ", mode=" + getMode() +
            ", timeInterval=" + getTimeInterval() +
            ", uploadDateTime='" + getUploadDateTime() + "'" +
            "}";
    }
}
