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
 * A TransfProfile.
 */
@Entity
@Table(name = "transf_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransfProfile implements Serializable {

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

    @OneToMany(mappedBy = "transfProfile", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transfProfile", "branch" }, allowSetters = true)
    private Set<TransfElVal> transfElVals = new HashSet<>();

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

    public TransfProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeason() {
        return this.season;
    }

    public TransfProfile season(String season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTypicalDay() {
        return this.typicalDay;
    }

    public TransfProfile typicalDay(String typicalDay) {
        this.setTypicalDay(typicalDay);
        return this;
    }

    public void setTypicalDay(String typicalDay) {
        this.typicalDay = typicalDay;
    }

    public Integer getMode() {
        return this.mode;
    }

    public TransfProfile mode(Integer mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getTimeInterval() {
        return this.timeInterval;
    }

    public TransfProfile timeInterval(Double timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public void setTimeInterval(Double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Instant getUploadDateTime() {
        return this.uploadDateTime;
    }

    public TransfProfile uploadDateTime(Instant uploadDateTime) {
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

    public TransfProfile inputFile(InputFile inputFile) {
        this.setInputFile(inputFile);
        return this;
    }

    public Set<TransfElVal> getTransfElVals() {
        return this.transfElVals;
    }

    public void setTransfElVals(Set<TransfElVal> transfElVals) {
        if (this.transfElVals != null) {
            this.transfElVals.forEach(i -> i.setTransfProfile(null));
        }
        if (transfElVals != null) {
            transfElVals.forEach(i -> i.setTransfProfile(this));
        }
        this.transfElVals = transfElVals;
    }

    public TransfProfile transfElVals(Set<TransfElVal> transfElVals) {
        this.setTransfElVals(transfElVals);
        return this;
    }

    public TransfProfile addTransfElVal(TransfElVal transfElVal) {
        this.transfElVals.add(transfElVal);
        transfElVal.setTransfProfile(this);
        return this;
    }

    public TransfProfile removeTransfElVal(TransfElVal transfElVal) {
        this.transfElVals.remove(transfElVal);
        transfElVal.setTransfProfile(null);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public TransfProfile network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfProfile)) {
            return false;
        }
        return id != null && id.equals(((TransfProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfProfile{" +
            "id=" + getId() +
            ", season='" + getSeason() + "'" +
            ", typicalDay='" + getTypicalDay() + "'" +
            ", mode=" + getMode() +
            ", timeInterval=" + getTimeInterval() +
            ", uploadDateTime='" + getUploadDateTime() + "'" +
            "}";
    }
}
