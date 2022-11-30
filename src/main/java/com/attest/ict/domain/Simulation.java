package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Simulation.
 */
@Entity
@Table(name = "simulation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Simulation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "uuid", length = 36, nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "config_file", nullable = false)
    private byte[] configFile;

    @NotNull
    @Column(name = "config_file_content_type", nullable = false)
    private String configFileContentType;

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

    @ManyToMany
    @JoinTable(
        name = "rel_simulation__input_file",
        joinColumns = @JoinColumn(name = "simulation_id"),
        inverseJoinColumns = @JoinColumn(name = "input_file_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "tool", "genProfile", "flexProfile", "loadProfile", "transfProfile", "branchProfile", "network", "simulations" },
        allowSetters = true
    )
    private Set<InputFile> inputFiles = new HashSet<>();

    @JsonIgnoreProperties(value = { "toolLogFile", "simulation", "tool", "user" }, allowSetters = true)
    @OneToOne(mappedBy = "simulation", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private Task task;

    @OneToMany(mappedBy = "simulation", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tool", "network", "simulation" }, allowSetters = true)
    private Set<OutputFile> outputFiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Simulation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Simulation uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return this.description;
    }

    public Simulation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getConfigFile() {
        return this.configFile;
    }

    public Simulation configFile(byte[] configFile) {
        this.setConfigFile(configFile);
        return this;
    }

    public void setConfigFile(byte[] configFile) {
        this.configFile = configFile;
    }

    public String getConfigFileContentType() {
        return this.configFileContentType;
    }

    public Simulation configFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
        return this;
    }

    public void setConfigFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Simulation network(Network network) {
        this.setNetwork(network);
        return this;
    }

    public Set<InputFile> getInputFiles() {
        return this.inputFiles;
    }

    public void setInputFiles(Set<InputFile> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public Simulation inputFiles(Set<InputFile> inputFiles) {
        this.setInputFiles(inputFiles);
        return this;
    }

    public Simulation addInputFile(InputFile inputFile) {
        this.inputFiles.add(inputFile);
        inputFile.getSimulations().add(this);
        return this;
    }

    public Simulation removeInputFile(InputFile inputFile) {
        this.inputFiles.remove(inputFile);
        inputFile.getSimulations().remove(this);
        return this;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        if (this.task != null) {
            this.task.setSimulation(null);
        }
        if (task != null) {
            task.setSimulation(this);
        }
        this.task = task;
    }

    public Simulation task(Task task) {
        this.setTask(task);
        return this;
    }

    public Set<OutputFile> getOutputFiles() {
        return this.outputFiles;
    }

    public void setOutputFiles(Set<OutputFile> outputFiles) {
        if (this.outputFiles != null) {
            this.outputFiles.forEach(i -> i.setSimulation(null));
        }
        if (outputFiles != null) {
            outputFiles.forEach(i -> i.setSimulation(this));
        }
        this.outputFiles = outputFiles;
    }

    public Simulation outputFiles(Set<OutputFile> outputFiles) {
        this.setOutputFiles(outputFiles);
        return this;
    }

    public Simulation addOutputFile(OutputFile outputFile) {
        this.outputFiles.add(outputFile);
        outputFile.setSimulation(this);
        return this;
    }

    public Simulation removeOutputFile(OutputFile outputFile) {
        this.outputFiles.remove(outputFile);
        outputFile.setSimulation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Simulation)) {
            return false;
        }
        return id != null && id.equals(((Simulation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Simulation{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", description='" + getDescription() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", configFileContentType='" + getConfigFileContentType() + "'" +
            "}";
    }
}
