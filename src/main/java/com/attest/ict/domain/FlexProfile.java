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
 * A FlexProfile.
 */
@Entity
@Table(name = "flex_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlexProfile implements Serializable {

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

    @OneToMany(mappedBy = "flexProfile", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flexProfile" }, allowSetters = true)
    private Set<FlexElVal> flexElVals = new HashSet<>();

    @OneToMany(mappedBy = "flexProfile", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flexProfile" }, allowSetters = true)
    private Set<FlexCost> flexCosts = new HashSet<>();

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

    public FlexProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeason() {
        return this.season;
    }

    public FlexProfile season(String season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTypicalDay() {
        return this.typicalDay;
    }

    public FlexProfile typicalDay(String typicalDay) {
        this.setTypicalDay(typicalDay);
        return this;
    }

    public void setTypicalDay(String typicalDay) {
        this.typicalDay = typicalDay;
    }

    public Integer getMode() {
        return this.mode;
    }

    public FlexProfile mode(Integer mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getTimeInterval() {
        return this.timeInterval;
    }

    public FlexProfile timeInterval(Double timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public void setTimeInterval(Double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Instant getUploadDateTime() {
        return this.uploadDateTime;
    }

    public FlexProfile uploadDateTime(Instant uploadDateTime) {
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

    public FlexProfile inputFile(InputFile inputFile) {
        this.setInputFile(inputFile);
        return this;
    }

    public Set<FlexElVal> getFlexElVals() {
        return this.flexElVals;
    }

    public void setFlexElVals(Set<FlexElVal> flexElVals) {
        if (this.flexElVals != null) {
            this.flexElVals.forEach(i -> i.setFlexProfile(null));
        }
        if (flexElVals != null) {
            flexElVals.forEach(i -> i.setFlexProfile(this));
        }
        this.flexElVals = flexElVals;
    }

    public FlexProfile flexElVals(Set<FlexElVal> flexElVals) {
        this.setFlexElVals(flexElVals);
        return this;
    }

    public FlexProfile addFlexElVal(FlexElVal flexElVal) {
        this.flexElVals.add(flexElVal);
        flexElVal.setFlexProfile(this);
        return this;
    }

    public FlexProfile removeFlexElVal(FlexElVal flexElVal) {
        this.flexElVals.remove(flexElVal);
        flexElVal.setFlexProfile(null);
        return this;
    }

    public Set<FlexCost> getFlexCosts() {
        return this.flexCosts;
    }

    public void setFlexCosts(Set<FlexCost> flexCosts) {
        if (this.flexCosts != null) {
            this.flexCosts.forEach(i -> i.setFlexProfile(null));
        }
        if (flexCosts != null) {
            flexCosts.forEach(i -> i.setFlexProfile(this));
        }
        this.flexCosts = flexCosts;
    }

    public FlexProfile flexCosts(Set<FlexCost> flexCosts) {
        this.setFlexCosts(flexCosts);
        return this;
    }

    public FlexProfile addFlexCost(FlexCost flexCost) {
        this.flexCosts.add(flexCost);
        flexCost.setFlexProfile(this);
        return this;
    }

    public FlexProfile removeFlexCost(FlexCost flexCost) {
        this.flexCosts.remove(flexCost);
        flexCost.setFlexProfile(null);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public FlexProfile network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexProfile)) {
            return false;
        }
        return id != null && id.equals(((FlexProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexProfile{" +
            "id=" + getId() +
            ", season='" + getSeason() + "'" +
            ", typicalDay='" + getTypicalDay() + "'" +
            ", mode=" + getMode() +
            ", timeInterval=" + getTimeInterval() +
            ", uploadDateTime='" + getUploadDateTime() + "'" +
            "}";
    }
}
