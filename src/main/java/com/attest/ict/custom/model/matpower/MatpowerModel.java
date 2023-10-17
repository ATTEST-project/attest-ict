package com.attest.ict.custom.model.matpower;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.domain.BusName;
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.GenTag;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.Transformer;
import com.attest.ict.domain.VoltageLevel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatpowerModel {

    @JsonProperty("caseName")
    private String caseName;

    private Double baseMva;
    private String version;

    private final List<Bus> buses = new ArrayList<>();

    private final List<BusExtension> busExtensions = new ArrayList<>();

    private final List<Generator> generators = new ArrayList<>();

    private final List<GeneratorExtension> generatorExtensions = new ArrayList<>();

    private final List<Branch> branches = new ArrayList<>();

    private final List<BranchExtension> branchExtensions = new ArrayList<>();

    private final List<GenTag> genTags = new ArrayList<>();

    private final List<BusName> busNames = new ArrayList<>();

    private final List<Transformer> transformers = new ArrayList<>();

    private final List<BusCoordinate> busCoordinates = new ArrayList<>();

    private final List<GenCost> genCosts = new ArrayList<>();

    private final List<CapacitorBankData> caps = new ArrayList<>();

    private VoltageLevel vLevels;

    private final List<LoadElVal> loadElPVals = new ArrayList<>();

    private final List<LoadElVal> loadElQVals = new ArrayList<>();

    @JsonCreator
    public MatpowerModel(@JsonProperty("caseName") String caseName) {
        this.caseName = Objects.requireNonNull(caseName);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCaseName() {
        return caseName;
    }

    public Double getBaseMva() {
        return baseMva;
    }

    public void setBaseMva(Double baseMva) {
        this.baseMva = baseMva;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public List<BusExtension> getBusExtensions() {
        return busExtensions;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public List<GeneratorExtension> getGeneratorExtensions() {
        return generatorExtensions;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public List<BranchExtension> getBranchExtensions() {
        return branchExtensions;
    }

    public List<GenTag> getGenTags() {
        return genTags;
    }

    public List<BusName> getBusNames() {
        return busNames;
    }

    public List<Transformer> getTransformers() {
        return transformers;
    }

    public List<BusCoordinate> getBusCoordinates() {
        return this.busCoordinates;
    }

    public List<GenCost> getGenCosts() {
        return genCosts;
    }

    public List<CapacitorBankData> getCaps() {
        return caps;
    }

    public VoltageLevel getvLevels() {
        return vLevels;
    }

    public void setvLevels(VoltageLevel vLevels) {
        this.vLevels = vLevels;
    }

    public List<LoadElVal> getLoadElPVals() {
        return loadElPVals;
    }

    public List<LoadElVal> getLoadElQVals() {
        return loadElQVals;
    }

    @Override
    public String toString() {
        return (
            "MatpowerModel{" +
            "caseName='" +
            caseName +
            '\'' +
            ", baseMva=" +
            baseMva +
            ", version='" +
            version +
            '\'' +
            ", buses=" +
            buses.size() +
            ", busExtensions=" +
            busExtensions.size() +
            ", generators=" +
            generators.size() +
            ", generatorExtensions=" +
            generatorExtensions.size() +
            ", branches=" +
            branches.size() +
            ", branchExtensions=" +
            branchExtensions.size() +
            ", genTags=" +
            genTags.size() +
            ", genCosts=" +
            genCosts.size() +
            ", busNames=" +
            busNames.size() +
            ", transformers=" +
            transformers.size() +
            ", busCoordinates=" +
            busCoordinates.size() +
            ", caps=" +
            caps.size() +
            ", vLevels=" +
            vLevels +
            '}'
        );
    }
}
