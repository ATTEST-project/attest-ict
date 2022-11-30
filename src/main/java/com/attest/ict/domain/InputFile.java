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
 * A InputFile.
 */
@Entity
@Table(name = "input_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InputFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Column(name = "upload_time")
    private Instant uploadTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFiles", "outputFiles", "tasks", "parameters" }, allowSetters = true)
    private Tool tool;

    @JsonIgnoreProperties(value = { "inputFile", "genElVals", "network" }, allowSetters = true)
    @OneToOne(mappedBy = "inputFile")
    private GenProfile genProfile;

    @JsonIgnoreProperties(value = { "inputFile", "flexElVals", "flexCosts", "network" }, allowSetters = true)
    @OneToOne(mappedBy = "inputFile")
    private FlexProfile flexProfile;

    @JsonIgnoreProperties(value = { "inputFile", "loadElVals", "network" }, allowSetters = true)
    @OneToOne(mappedBy = "inputFile")
    private LoadProfile loadProfile;

    @JsonIgnoreProperties(value = { "inputFile", "transfElVals", "network" }, allowSetters = true)
    @OneToOne(mappedBy = "inputFile")
    private TransfProfile transfProfile;

    @JsonIgnoreProperties(value = { "inputFile", "branchElVals", "network" }, allowSetters = true)
    @OneToOne(mappedBy = "inputFile")
    private BranchProfile branchProfile;

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

    @ManyToMany(mappedBy = "inputFiles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFiles", "task", "outputFiles" }, allowSetters = true)
    private Set<Simulation> simulations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InputFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public InputFile fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return this.description;
    }

    public InputFile description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getData() {
        return this.data;
    }

    public InputFile data(byte[] data) {
        this.setData(data);
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return this.dataContentType;
    }

    public InputFile dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Instant getUploadTime() {
        return this.uploadTime;
    }

    public InputFile uploadTime(Instant uploadTime) {
        this.setUploadTime(uploadTime);
        return this;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Tool getTool() {
        return this.tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public InputFile tool(Tool tool) {
        this.setTool(tool);
        return this;
    }

    public GenProfile getGenProfile() {
        return this.genProfile;
    }

    public void setGenProfile(GenProfile genProfile) {
        if (this.genProfile != null) {
            this.genProfile.setInputFile(null);
        }
        if (genProfile != null) {
            genProfile.setInputFile(this);
        }
        this.genProfile = genProfile;
    }

    public InputFile genProfile(GenProfile genProfile) {
        this.setGenProfile(genProfile);
        return this;
    }

    public FlexProfile getFlexProfile() {
        return this.flexProfile;
    }

    public void setFlexProfile(FlexProfile flexProfile) {
        if (this.flexProfile != null) {
            this.flexProfile.setInputFile(null);
        }
        if (flexProfile != null) {
            flexProfile.setInputFile(this);
        }
        this.flexProfile = flexProfile;
    }

    public InputFile flexProfile(FlexProfile flexProfile) {
        this.setFlexProfile(flexProfile);
        return this;
    }

    public LoadProfile getLoadProfile() {
        return this.loadProfile;
    }

    public void setLoadProfile(LoadProfile loadProfile) {
        if (this.loadProfile != null) {
            this.loadProfile.setInputFile(null);
        }
        if (loadProfile != null) {
            loadProfile.setInputFile(this);
        }
        this.loadProfile = loadProfile;
    }

    public InputFile loadProfile(LoadProfile loadProfile) {
        this.setLoadProfile(loadProfile);
        return this;
    }

    public TransfProfile getTransfProfile() {
        return this.transfProfile;
    }

    public void setTransfProfile(TransfProfile transfProfile) {
        if (this.transfProfile != null) {
            this.transfProfile.setInputFile(null);
        }
        if (transfProfile != null) {
            transfProfile.setInputFile(this);
        }
        this.transfProfile = transfProfile;
    }

    public InputFile transfProfile(TransfProfile transfProfile) {
        this.setTransfProfile(transfProfile);
        return this;
    }

    public BranchProfile getBranchProfile() {
        return this.branchProfile;
    }

    public void setBranchProfile(BranchProfile branchProfile) {
        if (this.branchProfile != null) {
            this.branchProfile.setInputFile(null);
        }
        if (branchProfile != null) {
            branchProfile.setInputFile(this);
        }
        this.branchProfile = branchProfile;
    }

    public InputFile branchProfile(BranchProfile branchProfile) {
        this.setBranchProfile(branchProfile);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public InputFile network(Network network) {
        this.setNetwork(network);
        return this;
    }

    public Set<Simulation> getSimulations() {
        return this.simulations;
    }

    public void setSimulations(Set<Simulation> simulations) {
        if (this.simulations != null) {
            this.simulations.forEach(i -> i.removeInputFile(this));
        }
        if (simulations != null) {
            simulations.forEach(i -> i.addInputFile(this));
        }
        this.simulations = simulations;
    }

    public InputFile simulations(Set<Simulation> simulations) {
        this.setSimulations(simulations);
        return this;
    }

    public InputFile addSimulation(Simulation simulation) {
        this.simulations.add(simulation);
        simulation.getInputFiles().add(this);
        return this;
    }

    public InputFile removeSimulation(Simulation simulation) {
        this.simulations.remove(simulation);
        simulation.getInputFiles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InputFile)) {
            return false;
        }
        return id != null && id.equals(((InputFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", description='" + getDescription() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            ", uploadTime='" + getUploadTime() + "'" +
            "}";
    }
}
