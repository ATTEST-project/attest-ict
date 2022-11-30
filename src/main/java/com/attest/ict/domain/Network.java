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
 * A Network.
 */
@Entity
@Table(name = "network")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Network implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //20220228 add unique constraint
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "mpc_name")
    private String mpcName;

    //20220707 add length =4
    @Column(name = "country", nullable = false, length = 4)
    private String country;

    @Column(name = "type", nullable = false, length = 3)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(name = "network_date")
    private Instant networkDate;

    @Column(name = "version")
    private Integer version;

    @Column(name = "creation_date_time")
    private Instant creationDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "loadELVals", "busName", "busExtension", "busCoordinate", "network" }, allowSetters = true)
    private Set<Bus> buses = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "genElVals", "generatorExtension", "genTag", "genCost", "network" }, allowSetters = true)
    private Set<Generator> generators = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transfElVals", "branchElVals", "branchExtension", "network" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<Storage> storages = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<Transformer> transformers = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<CapacitorBankData> capacitors = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "tool", "genProfile", "flexProfile", "loadProfile", "transfProfile", "branchProfile", "network", "simulations" },
        allowSetters = true
    )
    private Set<InputFile> inputFiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<AssetUGCable> assetUgCables = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<AssetTransformer> assetTransformers = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<BillingConsumption> billingConsumptions = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<BillingDer> billingDers = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    private Set<LineCable> lineCables = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFile", "genElVals", "network" }, allowSetters = true)
    private Set<GenProfile> genProfiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFile", "loadElVals", "network" }, allowSetters = true)
    private Set<LoadProfile> loadProfiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFile", "flexElVals", "flexCosts", "network" }, allowSetters = true)
    private Set<FlexProfile> flexProfiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFile", "transfElVals", "network" }, allowSetters = true)
    private Set<TransfProfile> transfProfiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inputFile", "branchElVals", "network" }, allowSetters = true)
    private Set<BranchProfile> branchProfiles = new HashSet<>();

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "topologies", "network" }, allowSetters = true)
    private Set<TopologyBus> topologyBuses = new HashSet<>();

    @OneToMany(mappedBy = "dsoNetwork", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dsoNetwork" }, allowSetters = true)
    private Set<DsoTsoConnection> dsoTsoConnections = new HashSet<>();

    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    @OneToOne(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private BaseMVA baseMVA;

    @JsonIgnoreProperties(value = { "network" }, allowSetters = true)
    @OneToOne(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private VoltageLevel voltageLevel;

    @OneToMany(mappedBy = "network", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "network", "inputFiles", "task", "outputFiles" }, allowSetters = true)
    private Set<Simulation> simulations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Network id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Network name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMpcName() {
        return this.mpcName;
    }

    public Network mpcName(String mpcName) {
        this.setMpcName(mpcName);
        return this;
    }

    public void setMpcName(String mpcName) {
        this.mpcName = mpcName;
    }

    public String getCountry() {
        return this.country;
    }

    public Network country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return this.type;
    }

    public Network type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Network description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Network isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getNetworkDate() {
        return this.networkDate;
    }

    public Network networkDate(Instant networkDate) {
        this.setNetworkDate(networkDate);
        return this;
    }

    public void setNetworkDate(Instant networkDate) {
        this.networkDate = networkDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Network version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreationDateTime() {
        return this.creationDateTime;
    }

    public Network creationDateTime(Instant creationDateTime) {
        this.setCreationDateTime(creationDateTime);
        return this;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Instant getUpdateDateTime() {
        return this.updateDateTime;
    }

    public Network updateDateTime(Instant updateDateTime) {
        this.setUpdateDateTime(updateDateTime);
        return this;
    }

    public void setUpdateDateTime(Instant updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Set<Bus> getBuses() {
        return this.buses;
    }

    public void setBuses(Set<Bus> buses) {
        if (this.buses != null) {
            this.buses.forEach(i -> i.setNetwork(null));
        }
        if (buses != null) {
            buses.forEach(i -> i.setNetwork(this));
        }
        this.buses = buses;
    }

    public Network buses(Set<Bus> buses) {
        this.setBuses(buses);
        return this;
    }

    public Network addBus(Bus bus) {
        this.buses.add(bus);
        bus.setNetwork(this);
        return this;
    }

    public Network removeBus(Bus bus) {
        this.buses.remove(bus);
        bus.setNetwork(null);
        return this;
    }

    public Set<Generator> getGenerators() {
        return this.generators;
    }

    public void setGenerators(Set<Generator> generators) {
        if (this.generators != null) {
            this.generators.forEach(i -> i.setNetwork(null));
        }
        if (generators != null) {
            generators.forEach(i -> i.setNetwork(this));
        }
        this.generators = generators;
    }

    public Network generators(Set<Generator> generators) {
        this.setGenerators(generators);
        return this;
    }

    public Network addGenerator(Generator generator) {
        this.generators.add(generator);
        generator.setNetwork(this);
        return this;
    }

    public Network removeGenerator(Generator generator) {
        this.generators.remove(generator);
        generator.setNetwork(null);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        if (this.branches != null) {
            this.branches.forEach(i -> i.setNetwork(null));
        }
        if (branches != null) {
            branches.forEach(i -> i.setNetwork(this));
        }
        this.branches = branches;
    }

    public Network branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public Network addBranch(Branch branch) {
        this.branches.add(branch);
        branch.setNetwork(this);
        return this;
    }

    public Network removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.setNetwork(null);
        return this;
    }

    public Set<Storage> getStorages() {
        return this.storages;
    }

    public void setStorages(Set<Storage> storages) {
        if (this.storages != null) {
            this.storages.forEach(i -> i.setNetwork(null));
        }
        if (storages != null) {
            storages.forEach(i -> i.setNetwork(this));
        }
        this.storages = storages;
    }

    public Network storages(Set<Storage> storages) {
        this.setStorages(storages);
        return this;
    }

    public Network addStorage(Storage storage) {
        this.storages.add(storage);
        storage.setNetwork(this);
        return this;
    }

    public Network removeStorage(Storage storage) {
        this.storages.remove(storage);
        storage.setNetwork(null);
        return this;
    }

    public Set<Transformer> getTransformers() {
        return this.transformers;
    }

    public void setTransformers(Set<Transformer> transformers) {
        if (this.transformers != null) {
            this.transformers.forEach(i -> i.setNetwork(null));
        }
        if (transformers != null) {
            transformers.forEach(i -> i.setNetwork(this));
        }
        this.transformers = transformers;
    }

    public Network transformers(Set<Transformer> transformers) {
        this.setTransformers(transformers);
        return this;
    }

    public Network addTransformer(Transformer transformer) {
        this.transformers.add(transformer);
        transformer.setNetwork(this);
        return this;
    }

    public Network removeTransformer(Transformer transformer) {
        this.transformers.remove(transformer);
        transformer.setNetwork(null);
        return this;
    }

    public Set<CapacitorBankData> getCapacitors() {
        return this.capacitors;
    }

    public void setCapacitors(Set<CapacitorBankData> capacitorBankData) {
        if (this.capacitors != null) {
            this.capacitors.forEach(i -> i.setNetwork(null));
        }
        if (capacitorBankData != null) {
            capacitorBankData.forEach(i -> i.setNetwork(this));
        }
        this.capacitors = capacitorBankData;
    }

    public Network capacitors(Set<CapacitorBankData> capacitorBankData) {
        this.setCapacitors(capacitorBankData);
        return this;
    }

    public Network addCapacitor(CapacitorBankData capacitorBankData) {
        this.capacitors.add(capacitorBankData);
        capacitorBankData.setNetwork(this);
        return this;
    }

    public Network removeCapacitor(CapacitorBankData capacitorBankData) {
        this.capacitors.remove(capacitorBankData);
        capacitorBankData.setNetwork(null);
        return this;
    }

    public Set<InputFile> getInputFiles() {
        return this.inputFiles;
    }

    public void setInputFiles(Set<InputFile> inputFiles) {
        if (this.inputFiles != null) {
            this.inputFiles.forEach(i -> i.setNetwork(null));
        }
        if (inputFiles != null) {
            inputFiles.forEach(i -> i.setNetwork(this));
        }
        this.inputFiles = inputFiles;
    }

    public Network inputFiles(Set<InputFile> inputFiles) {
        this.setInputFiles(inputFiles);
        return this;
    }

    public Network addInputFile(InputFile inputFile) {
        this.inputFiles.add(inputFile);
        inputFile.setNetwork(this);
        return this;
    }

    public Network removeInputFile(InputFile inputFile) {
        this.inputFiles.remove(inputFile);
        inputFile.setNetwork(null);
        return this;
    }

    public Set<AssetUGCable> getAssetUgCables() {
        return this.assetUgCables;
    }

    public void setAssetUgCables(Set<AssetUGCable> assetUGCables) {
        if (this.assetUgCables != null) {
            this.assetUgCables.forEach(i -> i.setNetwork(null));
        }
        if (assetUGCables != null) {
            assetUGCables.forEach(i -> i.setNetwork(this));
        }
        this.assetUgCables = assetUGCables;
    }

    public Network assetUgCables(Set<AssetUGCable> assetUGCables) {
        this.setAssetUgCables(assetUGCables);
        return this;
    }

    public Network addAssetUgCable(AssetUGCable assetUGCable) {
        this.assetUgCables.add(assetUGCable);
        assetUGCable.setNetwork(this);
        return this;
    }

    public Network removeAssetUgCable(AssetUGCable assetUGCable) {
        this.assetUgCables.remove(assetUGCable);
        assetUGCable.setNetwork(null);
        return this;
    }

    public Set<AssetTransformer> getAssetTransformers() {
        return this.assetTransformers;
    }

    public void setAssetTransformers(Set<AssetTransformer> assetTransformers) {
        if (this.assetTransformers != null) {
            this.assetTransformers.forEach(i -> i.setNetwork(null));
        }
        if (assetTransformers != null) {
            assetTransformers.forEach(i -> i.setNetwork(this));
        }
        this.assetTransformers = assetTransformers;
    }

    public Network assetTransformers(Set<AssetTransformer> assetTransformers) {
        this.setAssetTransformers(assetTransformers);
        return this;
    }

    public Network addAssetTransformer(AssetTransformer assetTransformer) {
        this.assetTransformers.add(assetTransformer);
        assetTransformer.setNetwork(this);
        return this;
    }

    public Network removeAssetTransformer(AssetTransformer assetTransformer) {
        this.assetTransformers.remove(assetTransformer);
        assetTransformer.setNetwork(null);
        return this;
    }

    public Set<BillingConsumption> getBillingConsumptions() {
        return this.billingConsumptions;
    }

    public void setBillingConsumptions(Set<BillingConsumption> billingConsumptions) {
        if (this.billingConsumptions != null) {
            this.billingConsumptions.forEach(i -> i.setNetwork(null));
        }
        if (billingConsumptions != null) {
            billingConsumptions.forEach(i -> i.setNetwork(this));
        }
        this.billingConsumptions = billingConsumptions;
    }

    public Network billingConsumptions(Set<BillingConsumption> billingConsumptions) {
        this.setBillingConsumptions(billingConsumptions);
        return this;
    }

    public Network addBillingConsumption(BillingConsumption billingConsumption) {
        this.billingConsumptions.add(billingConsumption);
        billingConsumption.setNetwork(this);
        return this;
    }

    public Network removeBillingConsumption(BillingConsumption billingConsumption) {
        this.billingConsumptions.remove(billingConsumption);
        billingConsumption.setNetwork(null);
        return this;
    }

    public Set<BillingDer> getBillingDers() {
        return this.billingDers;
    }

    public void setBillingDers(Set<BillingDer> billingDers) {
        if (this.billingDers != null) {
            this.billingDers.forEach(i -> i.setNetwork(null));
        }
        if (billingDers != null) {
            billingDers.forEach(i -> i.setNetwork(this));
        }
        this.billingDers = billingDers;
    }

    public Network billingDers(Set<BillingDer> billingDers) {
        this.setBillingDers(billingDers);
        return this;
    }

    public Network addBillingDer(BillingDer billingDer) {
        this.billingDers.add(billingDer);
        billingDer.setNetwork(this);
        return this;
    }

    public Network removeBillingDer(BillingDer billingDer) {
        this.billingDers.remove(billingDer);
        billingDer.setNetwork(null);
        return this;
    }

    public Set<LineCable> getLineCables() {
        return this.lineCables;
    }

    public void setLineCables(Set<LineCable> lineCables) {
        if (this.lineCables != null) {
            this.lineCables.forEach(i -> i.setNetwork(null));
        }
        if (lineCables != null) {
            lineCables.forEach(i -> i.setNetwork(this));
        }
        this.lineCables = lineCables;
    }

    public Network lineCables(Set<LineCable> lineCables) {
        this.setLineCables(lineCables);
        return this;
    }

    public Network addLineCable(LineCable lineCable) {
        this.lineCables.add(lineCable);
        lineCable.setNetwork(this);
        return this;
    }

    public Network removeLineCable(LineCable lineCable) {
        this.lineCables.remove(lineCable);
        lineCable.setNetwork(null);
        return this;
    }

    public Set<GenProfile> getGenProfiles() {
        return this.genProfiles;
    }

    public void setGenProfiles(Set<GenProfile> genProfiles) {
        if (this.genProfiles != null) {
            this.genProfiles.forEach(i -> i.setNetwork(null));
        }
        if (genProfiles != null) {
            genProfiles.forEach(i -> i.setNetwork(this));
        }
        this.genProfiles = genProfiles;
    }

    public Network genProfiles(Set<GenProfile> genProfiles) {
        this.setGenProfiles(genProfiles);
        return this;
    }

    public Network addGenProfile(GenProfile genProfile) {
        this.genProfiles.add(genProfile);
        genProfile.setNetwork(this);
        return this;
    }

    public Network removeGenProfile(GenProfile genProfile) {
        this.genProfiles.remove(genProfile);
        genProfile.setNetwork(null);
        return this;
    }

    public Set<LoadProfile> getLoadProfiles() {
        return this.loadProfiles;
    }

    public void setLoadProfiles(Set<LoadProfile> loadProfiles) {
        if (this.loadProfiles != null) {
            this.loadProfiles.forEach(i -> i.setNetwork(null));
        }
        if (loadProfiles != null) {
            loadProfiles.forEach(i -> i.setNetwork(this));
        }
        this.loadProfiles = loadProfiles;
    }

    public Network loadProfiles(Set<LoadProfile> loadProfiles) {
        this.setLoadProfiles(loadProfiles);
        return this;
    }

    public Network addLoadProfile(LoadProfile loadProfile) {
        this.loadProfiles.add(loadProfile);
        loadProfile.setNetwork(this);
        return this;
    }

    public Network removeLoadProfile(LoadProfile loadProfile) {
        this.loadProfiles.remove(loadProfile);
        loadProfile.setNetwork(null);
        return this;
    }

    public Set<FlexProfile> getFlexProfiles() {
        return this.flexProfiles;
    }

    public void setFlexProfiles(Set<FlexProfile> flexProfiles) {
        if (this.flexProfiles != null) {
            this.flexProfiles.forEach(i -> i.setNetwork(null));
        }
        if (flexProfiles != null) {
            flexProfiles.forEach(i -> i.setNetwork(this));
        }
        this.flexProfiles = flexProfiles;
    }

    public Network flexProfiles(Set<FlexProfile> flexProfiles) {
        this.setFlexProfiles(flexProfiles);
        return this;
    }

    public Network addFlexProfile(FlexProfile flexProfile) {
        this.flexProfiles.add(flexProfile);
        flexProfile.setNetwork(this);
        return this;
    }

    public Network removeFlexProfile(FlexProfile flexProfile) {
        this.flexProfiles.remove(flexProfile);
        flexProfile.setNetwork(null);
        return this;
    }

    public Set<TransfProfile> getTransfProfiles() {
        return this.transfProfiles;
    }

    public void setTransfProfiles(Set<TransfProfile> transfProfiles) {
        if (this.transfProfiles != null) {
            this.transfProfiles.forEach(i -> i.setNetwork(null));
        }
        if (transfProfiles != null) {
            transfProfiles.forEach(i -> i.setNetwork(this));
        }
        this.transfProfiles = transfProfiles;
    }

    public Network transfProfiles(Set<TransfProfile> transfProfiles) {
        this.setTransfProfiles(transfProfiles);
        return this;
    }

    public Network addTransfProfile(TransfProfile transfProfile) {
        this.transfProfiles.add(transfProfile);
        transfProfile.setNetwork(this);
        return this;
    }

    public Network removeTransfProfile(TransfProfile transfProfile) {
        this.transfProfiles.remove(transfProfile);
        transfProfile.setNetwork(null);
        return this;
    }

    public Set<BranchProfile> getBranchProfiles() {
        return this.branchProfiles;
    }

    public void setBranchProfiles(Set<BranchProfile> branchProfiles) {
        if (this.branchProfiles != null) {
            this.branchProfiles.forEach(i -> i.setNetwork(null));
        }
        if (branchProfiles != null) {
            branchProfiles.forEach(i -> i.setNetwork(this));
        }
        this.branchProfiles = branchProfiles;
    }

    public Network branchProfiles(Set<BranchProfile> branchProfiles) {
        this.setBranchProfiles(branchProfiles);
        return this;
    }

    public Network addBranchProfile(BranchProfile branchProfile) {
        this.branchProfiles.add(branchProfile);
        branchProfile.setNetwork(this);
        return this;
    }

    public Network removeBranchProfile(BranchProfile branchProfile) {
        this.branchProfiles.remove(branchProfile);
        branchProfile.setNetwork(null);
        return this;
    }

    public Set<TopologyBus> getTopologyBuses() {
        return this.topologyBuses;
    }

    public void setTopologyBuses(Set<TopologyBus> topologyBuses) {
        if (this.topologyBuses != null) {
            this.topologyBuses.forEach(i -> i.setNetwork(null));
        }
        if (topologyBuses != null) {
            topologyBuses.forEach(i -> i.setNetwork(this));
        }
        this.topologyBuses = topologyBuses;
    }

    public Network topologyBuses(Set<TopologyBus> topologyBuses) {
        this.setTopologyBuses(topologyBuses);
        return this;
    }

    public Network addTopologyBus(TopologyBus topologyBus) {
        this.topologyBuses.add(topologyBus);
        topologyBus.setNetwork(this);
        return this;
    }

    public Network removeTopologyBus(TopologyBus topologyBus) {
        this.topologyBuses.remove(topologyBus);
        topologyBus.setNetwork(null);
        return this;
    }

    public Set<DsoTsoConnection> getDsoTsoConnections() {
        return this.dsoTsoConnections;
    }

    public void setDsoTsoConnections(Set<DsoTsoConnection> dsoTsoConnections) {
        if (this.dsoTsoConnections != null) {
            this.dsoTsoConnections.forEach(i -> i.setDsoNetwork(null));
        }
        if (dsoTsoConnections != null) {
            dsoTsoConnections.forEach(i -> i.setDsoNetwork(this));
        }
        this.dsoTsoConnections = dsoTsoConnections;
    }

    public Network dsoTsoConnections(Set<DsoTsoConnection> dsoTsoConnections) {
        this.setDsoTsoConnections(dsoTsoConnections);
        return this;
    }

    public Network addDsoTsoConnection(DsoTsoConnection dsoTsoConnection) {
        this.dsoTsoConnections.add(dsoTsoConnection);
        dsoTsoConnection.setDsoNetwork(this);
        return this;
    }

    public Network removeDsoTsoConnection(DsoTsoConnection dsoTsoConnection) {
        this.dsoTsoConnections.remove(dsoTsoConnection);
        dsoTsoConnection.setDsoNetwork(null);
        return this;
    }

    public BaseMVA getBaseMVA() {
        return this.baseMVA;
    }

    public void setBaseMVA(BaseMVA baseMVA) {
        if (this.baseMVA != null) {
            this.baseMVA.setNetwork(null);
        }
        if (baseMVA != null) {
            baseMVA.setNetwork(this);
        }
        this.baseMVA = baseMVA;
    }

    public Network baseMVA(BaseMVA baseMVA) {
        this.setBaseMVA(baseMVA);
        return this;
    }

    public VoltageLevel getVoltageLevel() {
        return this.voltageLevel;
    }

    public void setVoltageLevel(VoltageLevel voltageLevel) {
        if (this.voltageLevel != null) {
            this.voltageLevel.setNetwork(null);
        }
        if (voltageLevel != null) {
            voltageLevel.setNetwork(this);
        }
        this.voltageLevel = voltageLevel;
    }

    public Network voltageLevel(VoltageLevel voltageLevel) {
        this.setVoltageLevel(voltageLevel);
        return this;
    }

    public Set<Simulation> getSimulations() {
        return this.simulations;
    }

    public void setSimulations(Set<Simulation> simulations) {
        if (this.simulations != null) {
            this.simulations.forEach(i -> i.setNetwork(null));
        }
        if (simulations != null) {
            simulations.forEach(i -> i.setNetwork(this));
        }
        this.simulations = simulations;
    }

    public Network simulations(Set<Simulation> simulations) {
        this.setSimulations(simulations);
        return this;
    }

    public Network addSimulation(Simulation simulation) {
        this.simulations.add(simulation);
        simulation.setNetwork(this);
        return this;
    }

    public Network removeSimulation(Simulation simulation) {
        this.simulations.remove(simulation);
        simulation.setNetwork(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Network)) {
            return false;
        }
        return id != null && id.equals(((Network) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Network{" +
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
